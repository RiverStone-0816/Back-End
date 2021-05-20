package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.exception.DuplicateKeyException;
import kr.co.eicn.ippbx.exception.EntityNotFoundException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.PdsIvr;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.PdsIvrRecord;
import kr.co.eicn.ippbx.model.PDSIvrComposite;
import kr.co.eicn.ippbx.model.dto.eicn.PDSIvrDetailResponse;
import kr.co.eicn.ippbx.model.enums.Button;
import kr.co.eicn.ippbx.model.enums.IvrTreeType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionSubType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.model.form.PDSIvrButtonMappingFormRequest;
import kr.co.eicn.ippbx.model.form.PDSIvrFormRequest;
import kr.co.eicn.ippbx.model.form.PDSIvrFormUpdateRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import lombok.Getter;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PdsIvr.PDS_IVR;
import static org.apache.commons.lang3.StringUtils.*;

@Getter
@Repository
public class PDSIvrRepository extends EicnBaseRepository<PdsIvr, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr, Integer> {
    private final Logger logger = LoggerFactory.getLogger(PDSIvrRepository.class);
    private final CacheService cacheService;
    private final PBXServerInterface pbxServerInterface;
    private final WebSecureHistoryRepository webSecureHistoryRepository;
    private final DecimalFormat decimalFormat = new DecimalFormat("0000");

    public PDSIvrRepository(CacheService cacheService, PBXServerInterface pbxServerInterface, WebSecureHistoryRepository webSecureHistoryRepository) {
        super(PDS_IVR, PDS_IVR.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr.class);
        this.cacheService = cacheService;
        this.pbxServerInterface = pbxServerInterface;
        this.webSecureHistoryRepository = webSecureHistoryRepository;

        orderByFields.add(PDS_IVR.TREE_NAME.asc());
    }

    public List<PDSIvrComposite> getIvrLists() {
        final List<PDSIvrComposite> all = findAll(DSL.noCondition(), PDS_IVR.TREE_NAME.asc())
                .stream()
                .map(e -> modelMapper.map(e, PDSIvrComposite.class))
                .collect(Collectors.toList());

        final Predicate<PDSIvrComposite> rootPredicate = e -> e.getType() == 0 && e.getLevel() == 0;

        final List<PDSIvrComposite> rootNodes = all.stream()
                .filter(rootPredicate)
                .collect(Collectors.toList());

        final List<PDSIvrComposite> childNodes = all.stream()
                .filter(e -> !rootPredicate.test(e))
                .collect(Collectors.toList());

        // call by reference
        rootNodes.forEach(e -> recursiveTree(e, childNodes));

        return rootNodes;
    }

    public PDSIvrDetailResponse getIvr(final Integer seq) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr entity = findOneIfNullThrow(seq);
        final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr> buttonMappings =
                findAll(PDS_IVR.CODE.eq(entity.getCode()).and(PDS_IVR.TYPE.ne((byte) 0)), Arrays.asList(PDS_IVR.TREE_NAME.asc(), PDS_IVR.BUTTON.asc()));

        final PDSIvrDetailResponse detailResponse = new PDSIvrDetailResponse();

        detailResponse.setName(entity.getName());
        detailResponse.setSoundCode(entity.getSoundCode());
        detailResponse.setCode(entity.getCode());
        detailResponse.setSeq(entity.getSeq());
        detailResponse.setButtonMappingList(buttonMappings);

        return detailResponse;
    }

    private void recursiveTree(PDSIvrComposite parentNode, List<PDSIvrComposite> childNodes) {
        List<PDSIvrComposite> subTrees = childNodes.stream().filter(e -> e.getTreeName().startsWith(parentNode.getTreeName())).collect(Collectors.toList());

        final Integer minLevel = subTrees.stream().min(comparingInt(value -> countMatches(value.getTreeName(), '_'))).map(e -> countMatches(e.getTreeName(), '_')).orElse(0);

        List<PDSIvrComposite> collect = subTrees.stream()
                .filter(e -> countMatches(e.getTreeName(), "_") == minLevel)
                .collect(Collectors.toList());

        parentNode.setNodes(collect);

        subTrees.removeAll(parentNode.getNodes());

        collect.forEach(e -> recursiveTree(e, subTrees));
    }

    public void insertAllPbxServers(PDSIvrFormRequest form) {
        final Optional<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr> duplicatedName = findAll(PDS_IVR.NAME.eq(form.getName()).and(PDS_IVR.TYPE.eq((byte) 0))).stream().findAny();
        if (duplicatedName.isPresent())
            throw new DuplicateKeyException("중복된 IVR명이 있습니다.");

        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr();

        final Integer code = nextSequence();
        record.setName(form.getName());
        record.setCode(code);
        record.setRoot(code);
        record.setParent(0);
        record.setLevel(0);
        record.setTreeName(new DecimalFormat("0000").format(code));
        record.setType((byte) 0);
        record.setButton(EMPTY);
        record.setTypeData(EMPTY);
        record.setSoundCode(defaultString(form.getSoundCode()));
        record.setCompanyId(getCompanyId());

        super.insert(record);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                        this.insert(pbxDsl, record);
                    }
                });

        webSecureHistoryRepository.insert(WebSecureActionType.PDS_IVR, WebSecureActionSubType.ADD, form.getName());
    }

    public void updateByKeyAllPbxServers(PDSIvrFormUpdateRequest form, Integer code) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr entity = findOneIfNullThrow(PDS_IVR.CODE.eq(code).and(PDS_IVR.TYPE.eq((byte) 0)));
        if (!IvrTreeType.PARENT_TREE.getCode().equals(entity.getType()))
            throw new IllegalArgumentException();

        final Optional<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr> duplicatedName = findAll(PDS_IVR.CODE.ne(code).and(PDS_IVR.NAME.eq(form.getName())).and(PDS_IVR.TYPE.eq((byte) 0)))
                .stream().findAny();
        if (duplicatedName.isPresent())
            throw new DuplicateKeyException("중복된 IVR명이 있습니다.");

        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr();

        record.setName(form.getName());
        record.setSoundCode(defaultString(form.getSoundCode()));

        updateOrInsertByDslContext(dsl, form, entity, record, code);

        cacheService.pbxServerList(getCompanyId())
                .forEach(server -> {
                    try (DSLContext pbxDsl = pbxServerInterface.using(server.getHost())) {
                        updateOrInsertByDslContext(pbxDsl, form, entity, record, code);
                    }
                });

        webSecureHistoryRepository.insert(WebSecureActionType.PDS_IVR, WebSecureActionSubType.MOD, form.getName());
    }

    public void updateOrInsertByDslContext(DSLContext dslContext, PDSIvrFormUpdateRequest form, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr entity,
                                           kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr record, Integer code) {


        dslContext.update(PDS_IVR)
                .set(PDS_IVR.NAME, record.getName())
                .set(PDS_IVR.SOUND_CODE, record.getSoundCode())
                .where(compareCompanyId())
                .and(PDS_IVR.SEQ.eq(entity.getSeq()))
                .execute();

        final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr> ivrTrees = findAll(dslContext, PDS_IVR.CODE.eq(code));

        final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr> deletePdsTrees = ivrTrees.stream()
                .filter(e -> e.getType() != 0)
                .filter(e -> form.getButtonMappingFormRequests().stream().noneMatch(data -> e.getSeq().equals(data.getSeq())))
                .collect(Collectors.toList());

        for (kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr deletePdsTree : deletePdsTrees) {
            dslContext.deleteFrom(PDS_IVR)
                    .where(PDS_IVR.SEQ.eq(deletePdsTree.getSeq()))
                    .and(PDS_IVR.COMPANY_ID.eq(getCompanyId()))
                    .execute();
        }

        for (int i = 0, length = form.getButtonMappingFormRequests().size(); i < length; i++) {
            final PDSIvrButtonMappingFormRequest mappingForm = form.getButtonMappingFormRequests().get(i);
            final Optional<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr> any = ivrTrees.stream().filter(e -> e.getSeq().equals(mappingForm.getSeq())).findAny();
            if (any.isPresent()) {
                final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr pdsIvr = any.get();
                dslContext.update(PDS_IVR)
                        .set(PDS_IVR.NAME, mappingForm.getName())
                        .set(PDS_IVR.TYPE, mappingForm.getType())
                        .set(PDS_IVR.TYPE_DATA, defaultString(mappingForm.getTypeData(), pdsIvr.getTypeData()))
                        .set(PDS_IVR.SOUND_CODE, defaultString(form.getSoundCode()))  // 음원 참조 정보는 parent_tree를 따라감
                        .where(compareCompanyId())
                        .and(PDS_IVR.SEQ.eq(mappingForm.getSeq()))
                        .execute();

                // 하위IVR로 연결 설정시 새로운 ivr node를 만들어줌
                if (IvrTreeType.LINK_TO_ANOTHER_IVR.getCode().equals(mappingForm.getType())) {
                    createChildIvr(dslContext, entity.getLevel(), pdsIvr, pdsIvr.getTreeName(), pdsIvr.getSeq());
                }
            } else {
                final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr pdsIvrRecord = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr();
                final String treeName = entity.getTreeName().concat("_").concat(decimalFormat.format(Button.of(mappingForm.getButton()).getIntValue()));
                pdsIvrRecord.setName(mappingForm.getName());
                pdsIvrRecord.setType(mappingForm.getType());
                pdsIvrRecord.setTypeData(defaultString(mappingForm.getTypeData()));
                pdsIvrRecord.setCode(code);
                pdsIvrRecord.setRoot(entity.getRoot());
                pdsIvrRecord.setParent(code);
                pdsIvrRecord.setLevel(entity.getLevel());
                pdsIvrRecord.setButton(mappingForm.getButton());
                pdsIvrRecord.setTreeName(treeName);
                pdsIvrRecord.setSoundCode(defaultString(form.getSoundCode()));
                pdsIvrRecord.setCompanyId(getCompanyId());

                final PdsIvrRecord r = dslContext.insertInto(PDS_IVR)
                        .set(dslContext.newRecord(PDS_IVR, pdsIvrRecord))
                        .returning()
                        .fetchOne();
                // 하위IVR로 연결 설정시 새로운 ivr node를 만들어줌
                if (IvrTreeType.LINK_TO_ANOTHER_IVR.getCode().equals(mappingForm.getType())) {
                    createChildIvr(dslContext, entity.getLevel(), pdsIvrRecord, treeName, r.getValue(PDS_IVR.SEQ));
                }
            }
        }
        // 참조하는 정보가 없는 parent_tree 정보는 삭제해뿜
        final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr> parentTrees = findAll(dslContext, PDS_IVR.LEVEL.ne(0).and(PDS_IVR.TYPE.eq((byte) 0)));
        final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr> all = findAll();

        parentTrees.stream()
                .filter(e -> all.stream().noneMatch(ivr -> String.valueOf(e.getCode()).equals(ivr.getTypeData())))
                .forEach(e -> dslContext.deleteFrom(PDS_IVR)
                        .where(PDS_IVR.SEQ.eq(e.getSeq()))
                        .and(PDS_IVR.COMPANY_ID.eq(getCompanyId()))
                        .execute());
    }

    public void createChildIvr(DSLContext dslContext, Integer level, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr pdsIvr, String treeName, Integer seq) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr insertRecord = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr();
        final Integer newIvrCode = dslContext.select(DSL.ifnull(DSL.max(PDS_IVR.CODE), 0).add(1))
                .from(PDS_IVR)
                .where(PDS_IVR.TYPE.eq((byte) 0))
                .fetchOneInto(Integer.class);

        pdsIvr.setLevel(level + 1);
        pdsIvr.setName("새로운IVR");
        pdsIvr.setTreeName(treeName.concat("_").concat(decimalFormat.format(newIvrCode)));
        pdsIvr.setType((byte) 0);
        pdsIvr.setCode(newIvrCode);
        pdsIvr.setTypeData(EMPTY);
        pdsIvr.setSoundCode(EMPTY);

        ReflectionUtils.copy(insertRecord, pdsIvr, "seq");

        dslContext.insertInto(PDS_IVR)
                .set(dslContext.newRecord(PDS_IVR, insertRecord))
                .execute();

        // 하위IVR code 는 parent pds_ivr.type_data에 저장됨
        dslContext.update(PDS_IVR)
                .set(PDS_IVR.TYPE_DATA, String.valueOf(newIvrCode))
                .where(PDS_IVR.SEQ.eq(seq))
                .execute();
    }

    public void deleteAllPbxServers(Integer code) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr entity = findAll(PDS_IVR.CODE.eq(code).and(PDS_IVR.TYPE.eq((byte) 0))).stream().findAny().orElseThrow(EntityNotFoundException::new);
        if (!IvrTreeType.PARENT_TREE.getCode().equals(entity.getType()))
            throw new IllegalArgumentException();

        dsl.deleteFrom(PDS_IVR)
                .where(PDS_IVR.CODE.eq(code))
                .and(compareCompanyId())
                .execute();

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                        pbxDsl.deleteFrom(PDS_IVR)
                                .where(PDS_IVR.CODE.eq(code))
                                .and(compareCompanyId())
                                .execute();
                    }
                });

        final Optional<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr> optionalPdsIvr = findAll(PDS_IVR.CODE.eq(entity.getParent()).and(PDS_IVR.TYPE.eq((byte) 5)), Collections.singletonList(PDS_IVR.TREE_NAME.desc()))
                .stream()
                .filter(e -> entity.getTreeName().startsWith(e.getTreeName()))
                .findFirst();
        // 상위노드 삭제
        if (optionalPdsIvr.isPresent()) {
            final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr parent = optionalPdsIvr.get();

            dsl.deleteFrom(PDS_IVR)
                    .where(PDS_IVR.SEQ.eq(parent.getSeq()))
                    .execute();

            cacheService.pbxServerList(getCompanyId())
                    .forEach(e -> {
                        try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                            pbxDsl.deleteFrom(PDS_IVR)
                                    .where(PDS_IVR.SEQ.eq(parent.getSeq()))
                                    .execute();
                        }
                    });
        }

        webSecureHistoryRepository.insert(WebSecureActionType.PDS_IVR, WebSecureActionSubType.DEL, entity.getName());
    }

    public Integer nextSequence() {
        return dsl.select(DSL.ifnull(DSL.max(PDS_IVR.CODE), 0).add(1))
                .from(PDS_IVR)
                .where(PDS_IVR.TYPE.eq((byte) 0))
                .fetchOneInto(Integer.class);
    }
}
