package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.exception.DuplicateKeyException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.IvrTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebvoiceInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.IvrTreeRecord;
import kr.co.eicn.ippbx.model.IvrTreeComposite;
import kr.co.eicn.ippbx.model.MonitorIvrTree;
import kr.co.eicn.ippbx.model.dto.eicn.PersonOnHunt;
import kr.co.eicn.ippbx.model.dto.eicn.QueueNameResponse;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.enums.*;
import kr.co.eicn.ippbx.model.form.*;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.IvrTree.IVR_TREE;
import static org.apache.commons.lang3.StringUtils.*;
import static org.jooq.impl.DSL.noCondition;

@Getter
@Repository
public class IvrTreeRepository extends EicnBaseRepository<IvrTree, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree, Integer> {
    private final Logger logger = LoggerFactory.getLogger(IvrTreeRepository.class);
    private final CacheService cacheService;
    private final PBXServerInterface pbxServerInterface;
    private final WebSecureHistoryRepository webSecureHistoryRepository;
    private final QueueNameRepository queueNameRepository;
    private final PersonListRepository personListRepository;
    private final WebVoiceInfoRepository webVoiceInfoRepository;

    public IvrTreeRepository(CacheService cacheService, PBXServerInterface pbxServerInterface, WebSecureHistoryRepository webSecureHistoryRepository, QueueNameRepository queueNameRepository, PersonListRepository personListRepository, WebVoiceInfoRepository webVoiceInfoRepository) {
        super(IVR_TREE, IVR_TREE.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree.class);
        this.cacheService = cacheService;
        this.pbxServerInterface = pbxServerInterface;
        this.webSecureHistoryRepository = webSecureHistoryRepository;
        this.queueNameRepository = queueNameRepository;
        this.personListRepository = personListRepository;
        this.webVoiceInfoRepository = webVoiceInfoRepository;

        orderByFields.add(IVR_TREE.TREE_NAME.asc());
        orderByFields.add(IVR_TREE.BUTTON.asc());
    }

    public List<IvrTreeComposite> getRootIvrTrees() {
        return findAll(IVR_TREE.LEVEL.eq(0).and(
                IVR_TREE.BUTTON.isNull().or(IVR_TREE.BUTTON.eq(""))
        )).stream()
                .map(e -> modelMapper.map(e, IvrTreeComposite.class))
                .collect(Collectors.toList());
    }

    public List<IvrTreeComposite> getIvrTreeLists() {
        final List<IvrTreeComposite> all = findAll(noCondition())
                .stream()
                .map(e -> modelMapper.map(e, IvrTreeComposite.class))
                .collect(Collectors.toList());

        final Predicate<IvrTreeComposite> rootPredicate = e -> e.getType() == 1 && e.getLevel() == 0 && isEmpty(e.getButton());

        final List<IvrTreeComposite> rootNodes = all.stream()
                .filter(rootPredicate)
                .collect(Collectors.toList());

        final List<IvrTreeComposite> childNodes = all.stream()
                .filter(e -> !rootPredicate.test(e))
                .collect(Collectors.toList());

        // call by reference
        rootNodes.forEach(e -> recursiveTree(e, childNodes));

        return rootNodes;
    }

    public IvrTreeComposite getIvr(Integer seq) {
        final IvrTreeComposite entity = modelMapper.map(findOneIfNullThrow(seq), IvrTreeComposite.class);
        final List<IvrTreeComposite> childNodes = findAll(IVR_TREE.SEQ.ne(entity.getSeq()).and(IVR_TREE.TREE_NAME.like(entity.getTreeName() + "%")))
                .stream()
                .map(e -> modelMapper.map(e, IvrTreeComposite.class))
                .collect(Collectors.toList());
        recursiveTree(entity, childNodes);

        return entity;
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree> getIvrTreeList() {
        return findAll(IVR_TREE.BUTTON.eq("").or(IVR_TREE.BUTTON.isNull()));
    }

    private void recursiveTree(IvrTreeComposite parentNode, List<IvrTreeComposite> childNodes) {
        List<IvrTreeComposite> subTrees = childNodes.stream().filter(e -> e.getTreeName().startsWith(parentNode.getTreeName())).collect(Collectors.toList());

        final Integer minLevel = subTrees.stream().min(comparingInt(value -> countMatches(value.getTreeName(), '_'))).map(e -> countMatches(e.getTreeName(), '_')).orElse(0);

        List<IvrTreeComposite> collect = subTrees.stream()
                .filter(e -> countMatches(e.getTreeName(), "_") == minLevel)
                .collect(Collectors.toList());

        parentNode.setNodes(collect);

        subTrees.removeAll(parentNode.getNodes());

        collect.forEach(e -> recursiveTree(e, subTrees));
    }

    public MonitorIvrTree getMonitorIvrTree(Integer groupCode) {
        final List<QueueName> queueNameList = queueNameRepository.findAll();
        final List<PersonOnHunt> queueMemberList = personListRepository.findAllPersonOnHunt();
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree rootIvr = findOne(IVR_TREE.CODE.eq(groupCode).and(IVR_TREE.BUTTON.eq("")));
        if (rootIvr != null) {
            final MonitorIvrTree entity = modelMapper.map(rootIvr, MonitorIvrTree.class);
            final List<MonitorIvrTree> childNodes = findAll(IVR_TREE.SEQ.ne(entity.getSeq()).and(IVR_TREE.TREE_NAME.like(entity.getTreeName() + "%")))
                    .stream()
                    .map(e -> modelMapper.map(e, MonitorIvrTree.class))
                    .collect(Collectors.toList());

            recursiveTree(entity, setMonitorIvrTreeInfo(childNodes, queueNameList, queueMemberList), queueNameList, queueMemberList);
            return entity;
        } else {
            return null;
        }
    }

    private void recursiveTree(MonitorIvrTree parentNode, List<MonitorIvrTree> childNodes, List<QueueName> queueNameList, List<PersonOnHunt> queueMemberList) {
        List<MonitorIvrTree> subTrees = childNodes.stream().filter(e -> e.getTreeName().startsWith(parentNode.getTreeName())).collect(Collectors.toList());

        final Integer minLevel = subTrees.stream().min(comparingInt(value -> countMatches(value.getTreeName(), '_'))).map(e -> countMatches(e.getTreeName(), '_')).orElse(0);

        List<MonitorIvrTree> collect = subTrees.stream()
                .filter(e -> countMatches(e.getTreeName(), "_") == minLevel)
                .collect(Collectors.toList());

        parentNode.setNodes(setMonitorIvrTreeInfo(collect, queueNameList, queueMemberList));

        subTrees.removeAll(parentNode.getNodes());

        collect.forEach(e -> recursiveTree(e, subTrees, queueNameList, queueMemberList));
    }

    private List<MonitorIvrTree> setMonitorIvrTreeInfo(List<MonitorIvrTree> childNodes, List<QueueName> queueNameList, List<PersonOnHunt> queueMemberList) {
        return childNodes.stream().peek(e -> {
            if (e.getType().equals(IvrMenuType.CONNECT_HUNT_NUMBER.getCode())) {
                e.setQueueNameResponse(
                        queueNameList.stream().filter(queueName -> queueName.getNumber().equals(e.getTypeData().split("\\|")[0]))
                                .map(queueName -> modelMapper.map(queueName, QueueNameResponse.class)).findFirst().orElse(null)
                );

                if (e.getQueueNameResponse() != null) {
                    e.setQueueMemberList(
                            queueMemberList.stream()
                                    .filter(
                                            person -> person.getQueueNumber().equals(e.getQueueNameResponse().getNumber())
                                    )
                                    .collect(Collectors.toList())
                    );
                }

                e.setProcessingCustomerCount((int) queueMemberList.stream().filter(person -> person.getQueueNumber().equals(e.getQueueNameResponse().getNumber()) && person.getPaused().equals(0)).count());
            }
        }).collect(Collectors.toList());
    }

    public Integer insertMenu(IvrFormRequest form) {
        final Optional<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree> duplicatedName =
                findAll(IVR_TREE.NAME.eq(form.getName()).and(IVR_TREE.TYPE.eq((byte) 1)).and(IVR_TREE.LEVEL.eq(0))).stream().findAny();
        if (duplicatedName.isPresent())
            throw new DuplicateKeyException("중복된 IVR명이 있습니다.");

        final IvrMenuType type = Objects.requireNonNull(IvrMenuType.of(form.getType()));
        if (!type.isMenu())
            throw new IllegalArgumentException("잘못된 호출입니다.");

        final IvrTreeRecord record = new IvrTreeRecord();

        final Integer code = nextSequence();
        final DecimalFormat decimalFormat = new DecimalFormat("0000");

        record.setCode(code);

        int root = code, parent = code, level = 0;
        String treeName = decimalFormat.format(code);

        if (form.getParentSeq() != null) {
            final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree parentNode = findOneIfNullThrow(form.getParentSeq());
            root = parentNode.getRoot();
            parent = parentNode.getCode();
            level = parentNode.getLevel() + 1;
            treeName = parentNode.getTreeName().concat("_").concat(treeName);

            if (g.getUser().getCompany().getService().contains("TYPE2"))
                if (level == 1 && IvrMenuType.CONNECT_MENU.getCode().equals(form.getType()))
                    throw new IllegalArgumentException("IVR은 2단계까지 생성 가능합니다.");

            // 하위IVR code 는 parent ivr_tree.type_data에 저장됨
            dsl.update(IVR_TREE)
                    .set(IVR_TREE.TYPE_DATA, String.valueOf(code))
                    .where(IVR_TREE.SEQ.eq(parentNode.getSeq()))
                    .execute();

            cacheService.pbxServerList(getCompanyId()).forEach(e -> {
                DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                pbxDsl.update(IVR_TREE)
                        .set(IVR_TREE.TYPE_DATA, String.valueOf(code))
                        .where(IVR_TREE.SEQ.eq(pbxDsl.select(IVR_TREE.SEQ).from(IVR_TREE).where(compareCompanyId()).and(IVR_TREE.TREE_NAME.eq(parentNode.getTreeName()))))
                        .execute();
            });
        }

        record.setCode(code);
        record.setRoot(root);
        record.setParent(parent);
        record.setLevel(level);
        record.setTreeName(treeName);
        record.setName(form.getName());
        record.setType(Objects.requireNonNull(IvrMenuType.of(form.getType())).getCode());
        record.setButton(EMPTY);
        record.setIsWebVoice(defaultString(form.getIsWebVoice(), "N"));
        record.setTypeData(defaultString(join(form.getTypeDataStrings(), "|")));
        record.setSoundCode(defaultString(form.getSoundCode()));
        record.setIntroSoundCode(defaultString(form.getIntroSoundCode()));
        record.setPosX(form.getPosX());
        record.setPosY(form.getPosY());
        record.setTtsData(defaultString(join(form.getTtsDataStrings(), "|")));
        record.setCompanyId(getCompanyId());

        final Integer seq = dsl.insertInto(IVR_TREE)
                .set(record)
                .returning(IVR_TREE.SEQ)
                .fetchOne().value1();

        record.setSeq(seq);

        cacheService.pbxServerList(getCompanyId()).forEach(e -> {
            DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
            pbxDsl.insertInto(IVR_TREE)
                    .set(record)
                    .execute();
        });

        if (form.getButtons() != null) {
            for (int i = 0, length = form.getButtons().size(); i < length; i++) {
                final IvrButtonMappingFormRequest button = form.getButtons().get(i);
                Integer buttonSeq = dsl.insertInto(IVR_TREE)
                        .set(IVR_TREE.CODE, code)
                        .set(IVR_TREE.ROOT, record.getRoot())
                        .set(IVR_TREE.PARENT, record.getCode())
                        .set(IVR_TREE.NAME, button.getName())
                        .set(IVR_TREE.TREE_NAME, record.getTreeName().concat("_").concat(decimalFormat.format(Objects.requireNonNull(Button.of(button.getButton())).getIntValue())))
                        .set(IVR_TREE.LEVEL, record.getLevel())
                        .set(IVR_TREE.BUTTON, button.getButton())
                        .set(IVR_TREE.COMPANY_ID, getCompanyId())
                        .returning(IVR_TREE.SEQ)
                        .fetchOne().value1();

                cacheService.pbxServerList(getCompanyId()).forEach(e -> {
                    DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                    pbxDsl.insertInto(IVR_TREE)
                            .set(IVR_TREE.SEQ, buttonSeq)
                            .set(IVR_TREE.CODE, code)
                            .set(IVR_TREE.ROOT, record.getRoot())
                            .set(IVR_TREE.PARENT, record.getCode())
                            .set(IVR_TREE.NAME, button.getName())
                            .set(IVR_TREE.TREE_NAME, record.getTreeName().concat("_").concat(decimalFormat.format(Objects.requireNonNull(Button.of(button.getButton())).getIntValue())))
                            .set(IVR_TREE.LEVEL, record.getLevel())
                            .set(IVR_TREE.BUTTON, button.getButton())
                            .set(IVR_TREE.COMPANY_ID, getCompanyId())
                            .execute();
                });
            }
        }
        if (form.getIsWebVoice().contains(IsWebVoiceYn.WEB_VOICE_Y.getCode()))
            webVoiceInfoRepository.updateIvrCode(code, new WebVoiceItemsFormRequest());

        webSecureHistoryRepository.insert(WebSecureActionType.IVR, WebSecureActionSubType.ADD, form.getName());

        return seq;
    }

    public void updateByKeyAllPbxServers(IvrFormUpdateRequest form, Integer seq) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree entity = findOneIfNullThrow(seq);

        if (g.getUser().getCompany().getService().contains("TYPE2"))
            if (entity.getLevel() == 1 && IvrMenuType.CONNECT_MENU.getCode().equals(form.getType()))
                throw new IllegalArgumentException("IVR은 2단계까지 생성 가능합니다.");

        updateOrInsert(dsl, form, entity);

        cacheService.pbxServerList(getCompanyId())
                .forEach(server -> {
                    DSLContext pbxDsl = pbxServerInterface.using(server.getHost());
                    updateOrInsert(pbxDsl, form, entity);
                });

        webSecureHistoryRepository.insert(WebSecureActionType.IVR, WebSecureActionSubType.MOD, form.getName());
    }

    public void updateOrInsert(DSLContext dslContext, IvrFormUpdateRequest form, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree entity) {
        final DecimalFormat decimalFormat = new DecimalFormat("0000");
        final Integer code = entity.getCode();
        final IvrMenuType type = Objects.requireNonNull(IvrMenuType.of(form.getType()));

        dslContext.update(IVR_TREE)
                .set(IVR_TREE.NAME, form.getName())
                .set(IVR_TREE.TYPE, form.getType())
                .set(IVR_TREE.SOUND_CODE, defaultString(form.getSoundCode()))
                .set(IVR_TREE.INTRO_SOUND_CODE, defaultString(form.getIntroSoundCode()))
                .set(IVR_TREE.IS_WEB_VOICE, defaultString(form.getIsWebVoice(), "N"))
                .set(IVR_TREE.TYPE_DATA, defaultString(join(form.getTypeDataStrings(), "|"), entity.getTypeData()))
                .set(IVR_TREE.TTS_DATA, defaultString(join(form.getTtsDataStrings(), "|")))
                .set(IVR_TREE.POS_X, form.getPosX())
                .set(IVR_TREE.POS_Y, form.getPosY())
                .where(IVR_TREE.SEQ.eq(entity.getSeq()))
                .execute();

        // 새로운 메뉴 연결이라면 버튼정보를 업데이트
        if (type.isMenu()) {
            if (form.getButtons() != null) {
                final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree> ivrTrees = findAll(dslContext, IVR_TREE.CODE.eq(code));
                final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree> deleteTrees = ivrTrees.stream()
                        .filter(e -> isNotEmpty(e.getButton()))
                        .filter(e -> form.getButtons() != null && form.getButtons().stream().noneMatch(button -> e.getSeq().equals(button.getSeq())))
                        .collect(Collectors.toList());

                for (kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree deleteTree : deleteTrees) {
                    dslContext.deleteFrom(IVR_TREE)
                            .where(IVR_TREE.SEQ.eq(deleteTree.getSeq()))
                            .and(IVR_TREE.COMPANY_ID.eq(getCompanyId()))
                            .execute();
                }

                for (int i = 0, length = Objects.requireNonNull(form.getButtons()).size(); i < length; i++) {
                    final IvrButtonMappingFormRequest button = form.getButtons().get(i);
                    final Optional<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree> any = ivrTrees.stream().filter(e -> e.getSeq().equals(button.getSeq())).findAny();
                    final String treeName = entity.getTreeName().concat("_").concat(decimalFormat.format(Objects.requireNonNull(Button.of(button.getButton())).getIntValue()));

                    if (any.isPresent()) {
                        dslContext.update(IVR_TREE)
                                .set(IVR_TREE.NAME, button.getName())
                                .set(IVR_TREE.BUTTON, button.getButton())
                                .set(IVR_TREE.TREE_NAME, treeName)
                                .where(compareCompanyId())
                                .and(IVR_TREE.SEQ.eq(button.getSeq()))
                                .execute();
                    } else {
                        button.setSeq(
                                dslContext.insertInto(IVR_TREE)
                                        .set(IVR_TREE.SEQ, button.getSeq())
                                        .set(IVR_TREE.CODE, code)
                                        .set(IVR_TREE.ROOT, entity.getRoot())
                                        .set(IVR_TREE.PARENT, entity.getCode())
                                        .set(IVR_TREE.NAME, button.getName())
                                        .set(IVR_TREE.TREE_NAME, treeName)
                                        .set(IVR_TREE.LEVEL, entity.getLevel())
                                        .set(IVR_TREE.BUTTON, button.getButton())
                                        .set(IVR_TREE.COMPANY_ID, getCompanyId())
                                        .returning()
                                        .fetchOne().value1()
                        );
                        form.getButtons().set(i, button);
                    }
                }
            }
        }
        if (form.getIsWebVoice().contains(IsWebVoiceYn.WEB_VOICE_Y.getCode())) {
            final WebvoiceInfo webvoiceInfo = webVoiceInfoRepository.findOneByIvrCode(code);
            if (webvoiceInfo == null)
                webVoiceInfoRepository.updateIvrCode(code, new WebVoiceItemsFormRequest());
        }
    }

    public void deleteAllPbxServers(Integer seq) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree entity = findOneIfNullThrow(seq);
        final IvrMenuType type = IvrMenuType.of(entity.getType());

        if (type != null && type.isMenu()) {
            dsl.deleteFrom(IVR_TREE)
                    .where(IVR_TREE.TREE_NAME.like(entity.getTreeName() + "%"))
                    .and(compareCompanyId())
                    .execute();

            if (entity.getLevel() != 0) {
                dsl.update(IVR_TREE)
                        .set(IVR_TREE.TYPE_DATA, "")
                        .where(IVR_TREE.COMPANY_ID.eq(getCompanyId()))
                        .and(IVR_TREE.TYPE_DATA.eq(String.valueOf(entity.getCode())))
                        .execute();

                cacheService.pbxServerList(getCompanyId()).forEach(e -> {
                    DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                    pbxDsl.update(IVR_TREE)
                            .set(IVR_TREE.TYPE_DATA, "")
                            .where(IVR_TREE.COMPANY_ID.eq(getCompanyId()))
                            .and(IVR_TREE.TYPE_DATA.eq(String.valueOf(entity.getCode())))
                            .execute();
                });
            }

            cacheService.pbxServerList(getCompanyId()).forEach(e -> {
                DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                pbxDsl.deleteFrom(IVR_TREE)
                        .where(IVR_TREE.TREE_NAME.like(entity.getTreeName() + "%"))
                        .and(compareCompanyId())
                        .execute();
            });
        } else {
            dsl.update(IVR_TREE)
                    .set(IVR_TREE.TYPE, (byte) 0)
                    .setNull(IVR_TREE.TYPE_DATA)
                    .setNull(IVR_TREE.TTS_DATA)
                    .set(IVR_TREE.SOUND_CODE, EMPTY)
                    .set(IVR_TREE.INTRO_SOUND_CODE, EMPTY)
                    .where(IVR_TREE.SEQ.eq(entity.getSeq()))
                    .execute();

            cacheService.pbxServerList(getCompanyId()).forEach(e -> {
                DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                pbxDsl.update(IVR_TREE)
                        .set(IVR_TREE.TYPE, (byte) 0)
                        .setNull(IVR_TREE.TYPE_DATA)
                        .setNull(IVR_TREE.TTS_DATA)
                        .set(IVR_TREE.SOUND_CODE, EMPTY)
                        .set(IVR_TREE.INTRO_SOUND_CODE, EMPTY)
                        .where(IVR_TREE.TREE_NAME.eq(entity.getTreeName()))
                        .and(IVR_TREE.BUTTON.eq(entity.getButton()))
                        .and(IVR_TREE.COMPANY_ID.eq(getCompanyId()))
                        .execute();
            });
        }

        final Optional<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree> optionalParent = findAll(IVR_TREE.CODE.eq(entity.getParent()), Collections.singletonList(IVR_TREE.TREE_NAME.desc()))
                .stream()
                .filter(e -> entity.getTreeName().startsWith(e.getTreeName()))
                .findFirst();

        // 상위노드 속성정보 초기화
        if (optionalParent.isPresent()) {
            final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree parentTree = optionalParent.get();

            dsl.update(IVR_TREE)
                    .set(IVR_TREE.TYPE, (byte) 0)
                    .setNull(IVR_TREE.TYPE_DATA)
                    .setNull(IVR_TREE.TTS_DATA)
                    .set(IVR_TREE.SOUND_CODE, EMPTY)
                    .set(IVR_TREE.INTRO_SOUND_CODE, EMPTY)
                    .where(IVR_TREE.SEQ.eq(parentTree.getSeq()))
                    .execute();

            cacheService.pbxServerList(getCompanyId()).forEach(e -> {
                DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                pbxDsl.update(IVR_TREE)
                        .set(IVR_TREE.TYPE, (byte) 0)
                        .setNull(IVR_TREE.TYPE_DATA)
                        .setNull(IVR_TREE.TTS_DATA)
                        .set(IVR_TREE.SOUND_CODE, EMPTY)
                        .set(IVR_TREE.INTRO_SOUND_CODE, EMPTY)
                        .where(IVR_TREE.TREE_NAME.eq(parentTree.getTreeName()))
                        .and(IVR_TREE.BUTTON.eq(parentTree.getButton()))
                        .and(IVR_TREE.COMPANY_ID.eq(getCompanyId()))
                        .execute();
            });
        }
        if (StringUtils.isEmpty(entity.getButton()))
            webVoiceInfoRepository.deleteIvrCode(entity.getCode());

        webSecureHistoryRepository.insert(WebSecureActionType.IVR, WebSecureActionSubType.DEL, entity.getName());
    }

    public Integer nextSequence() {
        return dsl.select(DSL.ifnull(DSL.max(IVR_TREE.CODE), 0).add(1))
                .from(IVR_TREE)
                .where(IVR_TREE.TYPE.eq(IvrMenuType.CONNECT_MENU.getCode()).or(IVR_TREE.TYPE.eq(IvrMenuType.CONNECT_MENU_AFTER_DONE_EXCEPTION.getCode())))
                .and(IVR_TREE.BUTTON.eq(EMPTY))
                .fetchOneInto(Integer.class);
    }

    /**
     * @param sourceId 복사할 아이디
     * @param targetId 복사대상 아이디
     */
    public void copy(Integer sourceId, Integer targetId) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree target = findOneIfNullThrow(targetId);
        final IvrTreeComposite ivr = getIvr(sourceId);
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree source = findOne(sourceId);
        target.setIntroSoundCode(source.getIntroSoundCode());
        target.setTtsData(source.getTtsData());
        target.setPosX(source.getPosX());
        target.setPosY(source.getPosY());
        super.updateByKey(target, targetId);

        if (sourceId.equals(targetId))
            throw new IllegalStateException("복사할 IVR과 복사대상 IVR이 같습니다.");
        if (ivr.isLeaf())
            throw new IllegalStateException("IVR하위 정보를 찾을 수 없습니다.");

        dsl.deleteFrom(IVR_TREE)
                .where(compareCompanyId())
                .and(IVR_TREE.TREE_NAME.like(target.getTreeName() + "%"))
                .and(IVR_TREE.SEQ.ne(target.getSeq()))
                .execute();

        cacheService.pbxServerList(getCompanyId()).forEach(e -> {
            DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
            pbxDsl.deleteFrom(IVR_TREE)
                    .where(compareCompanyId())
                    .and(IVR_TREE.TREE_NAME.like(target.getTreeName() + "%"))
                    .and(IVR_TREE.TREE_NAME.ne(target.getTreeName()))
                    .execute();
        });


        recursiveCopy(target, ivr.getNodes());
    }

    public void recursiveCopy(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree parentNode, List<IvrTreeComposite> nodes) {
        final DecimalFormat decimalFormat = new DecimalFormat("0000");

        for (IvrTreeComposite node : nodes) {
            int code = parentNode.getCode();
            String treeName;

            if (isNotEmpty(node.getButton())) {
                treeName = parentNode.getTreeName().concat("_").concat(decimalFormat.format(Objects.requireNonNull(Button.of(node.getButton())).getIntValue()));
                if (isNotEmpty(node.getTypeData()) && node.getType() == 0) {
                    node.setTypeData(String.valueOf(nextSequence()));
                }
            } else {
                code = nextSequence();
                treeName = parentNode.getTreeName().concat("_").concat(decimalFormat.format(code));
            }

            final IvrTreeRecord ivrTreeRecord = dsl.insertInto(IVR_TREE)
                    .set(IVR_TREE.CODE, code)
                    .set(IVR_TREE.ROOT, parentNode.getRoot())
                    .set(IVR_TREE.PARENT, parentNode.getCode())
                    .set(IVR_TREE.LEVEL, node.getLevel())
                    .set(IVR_TREE.TREE_NAME, treeName)
                    .set(IVR_TREE.NAME, node.getName())
                    .set(IVR_TREE.BUTTON, node.getButton())
                    .set(IVR_TREE.TYPE, node.getType())
                    .set(IVR_TREE.TYPE_DATA, node.getTypeData())
                    .set(IVR_TREE.INTRO_SOUND_CODE, node.getIntroSoundCode())
                    .set(IVR_TREE.SOUND_CODE, node.getSoundCode())
                    .set(IVR_TREE.TTS_DATA, node.getTtsData())
                    .set(IVR_TREE.IS_WEB_VOICE, node.getIsWebVoice())
                    .set(IVR_TREE.POS_X, node.getPosX())
                    .set(IVR_TREE.POS_Y, node.getPosY())
                    .set(IVR_TREE.COMPANY_ID, getCompanyId())
                    .returning()
                    .fetchOne();

            final List<CompanyServerEntity> companyServerEntities = cacheService.pbxServerList(getCompanyId());

            for (CompanyServerEntity e : companyServerEntities) {
                DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                pbxDsl.insertInto(IVR_TREE)
                        .set(IVR_TREE.CODE, code)
                        .set(IVR_TREE.ROOT, parentNode.getRoot())
                        .set(IVR_TREE.PARENT, parentNode.getCode())
                        .set(IVR_TREE.LEVEL, node.getLevel())
                        .set(IVR_TREE.TREE_NAME, treeName)
                        .set(IVR_TREE.NAME, node.getName())
                        .set(IVR_TREE.BUTTON, node.getButton())
                        .set(IVR_TREE.TYPE, node.getType())
                        .set(IVR_TREE.TYPE_DATA, node.getTypeData())
                        .set(IVR_TREE.INTRO_SOUND_CODE, node.getIntroSoundCode())
                        .set(IVR_TREE.SOUND_CODE, node.getSoundCode())
                        .set(IVR_TREE.TTS_DATA, node.getTtsData())
                        .set(IVR_TREE.IS_WEB_VOICE, node.getIsWebVoice())
                        .set(IVR_TREE.POS_X, node.getPosX())
                        .set(IVR_TREE.POS_Y, node.getPosY())
                        .set(IVR_TREE.COMPANY_ID, getCompanyId())
                        .execute();
            }

            if (!node.isLeaf())
                recursiveCopy(modelMapper.map(ivrTreeRecord, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree.class), node.getNodes());
        }
    }

    //인입 경로 통계
    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree> findAllByRoot(List<String> treeNames) {
        List<Condition> conditions = new ArrayList<>();
        Condition ivrCondition = DSL.noCondition();
        conditions.add(IVR_TREE.LEVEL.eq(0).and(IVR_TREE.BUTTON.eq("")));

        if (treeNames.size() > 0) {
            for (int i = 0; i < treeNames.size(); i++) {
                if (i == 0)
                    ivrCondition = ivrCondition.and(IVR_TREE.TREE_NAME.like("%" + treeNames.get(i) + "%"));
                else
                    ivrCondition = ivrCondition.or(IVR_TREE.TREE_NAME.like("%" + treeNames.get(i) + "%"));
            }
            conditions.add(ivrCondition);
        }
        return findAll(conditions);
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree> findAllByParentCode(Integer parentRoot, Integer parentSeq) {
        return dsl.selectFrom(IVR_TREE)
                .where(IVR_TREE.ROOT.eq(parentRoot))
                .and(IVR_TREE.SEQ.notEqual(parentSeq))
                .orderBy(IVR_TREE.TREE_NAME)
                .fetchInto(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree.class);
    }

    public void updatePosition(Integer seq, IvrPositionFormRequest form) {
        dsl.update(IVR_TREE)
                .set(IVR_TREE.POS_X, form.getPosX())
                .set(IVR_TREE.POS_Y, form.getPosY())
                .where(IVR_TREE.SEQ.eq(seq))
                .execute();
    }
}
