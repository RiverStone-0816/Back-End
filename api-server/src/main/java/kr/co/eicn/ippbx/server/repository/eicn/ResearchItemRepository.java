package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.ResearchItem;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.records.ResearchItemRecord;
import kr.co.eicn.ippbx.server.model.form.ResearchItemFormRequest;
import kr.co.eicn.ippbx.server.model.search.ResearchItemSearchRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.server.util.ReflectionUtils;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.ResearchItem.RESEARCH_ITEM;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class ResearchItemRepository extends EicnBaseRepository<ResearchItem, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchItem, Integer> {
    private final Logger logger = LoggerFactory.getLogger(ResearchItemRepository.class);

    private final CacheService cacheService;
    private final PBXServerInterface pbxServerInterface;
    private final CompanyTreeRepository companyTreeRepository;

    public ResearchItemRepository(CacheService cacheService, PBXServerInterface pbxServerInterface, CompanyTreeRepository companyTreeRepository) {
        super(RESEARCH_ITEM, RESEARCH_ITEM.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchItem.class);
        this.cacheService = cacheService;
        this.pbxServerInterface = pbxServerInterface;
        this.companyTreeRepository = companyTreeRepository;

        addOrderingField(RESEARCH_ITEM.ITEM_ID.asc());
    }

    public Pagination<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchItem> pagination(ResearchItemSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchItem> findAll(ResearchItemSearchRequest search) {
        return super.findAll(conditions(search));
    }

    private List<Condition> conditions(ResearchItemSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        conditions.add(RESEARCH_ITEM.COMPANY_ID.eq(getCompanyId()));
        if (isNotEmpty(search.getItemName()))
            conditions.add(RESEARCH_ITEM.ITEM_NAME.like("%" + search.getItemName() + "%"));
        if (isNotEmpty(search.getWord()))
            conditions.add(RESEARCH_ITEM.WORD.like("%" + search.getWord() + "%"));
        if (search.getItemIds() != null && !search.getItemIds().isEmpty())
            conditions.add(RESEARCH_ITEM.ITEM_ID.in(search.getItemIds()));
        if (search.getMappingNumber() != null)
            conditions.add(RESEARCH_ITEM.MAPPING_NUMBER.eq(search.getMappingNumber()));

        return conditions;
    }

    public void insert(ResearchItemFormRequest form) {
        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchItem record = new kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchItem();
        ReflectionUtils.copy(record, form);

        if (isNotEmpty(form.getGroupCode())) {
            final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
            if (companyTree != null) {
                record.setGroupCode(form.getGroupCode());
                record.setGroupTreeName(companyTree.getGroupTreeName());
                record.setGroupLevel(companyTree.getGroupLevel());
            }
        }
        record.setItemId(nextSequence());
        record.setSoundKind(defaultString(form.getSoundKind(), "N"));
        record.setMappingNumber((byte) 0);
        record.setCompanyId(getCompanyId());

        super.insert(record);

        cacheService.pbxServerList(getCompanyId()).forEach(e -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                super.insert(pbxDsl, record);
            }
        });

        byte rowIndex = 1;
        if (form.getAnswerRequests() != null) {
            for (String word : form.getAnswerRequests()) {
                record.setWord(word);
                record.setMappingNumber(rowIndex++);

                super.insert(record);

                cacheService.pbxServerList(getCompanyId()).forEach(e -> {
                    try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                        super.insert(pbxDsl, record);
                    }
                });
            }
        }
    }

    public void updateByKey(ResearchItemFormRequest form, Integer seq) {
        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchItem record = findOneIfNullThrow(seq);

        record.setItemName(form.getItemName());
        record.setWord(form.getWord());
        record.setSoundKind(defaultString(form.getSoundKind(), "N"));
        record.setSoundCode(form.getSoundCode());

        if (isNotEmpty(form.getGroupCode())) {
            final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
            if (companyTree != null) {
                record.setGroupCode(form.getGroupCode());
                record.setGroupTreeName(companyTree.getGroupTreeName());
                record.setGroupLevel(companyTree.getGroupLevel());
            }
        }

        dsl.update(RESEARCH_ITEM)
                .set(dsl.newRecord(RESEARCH_ITEM, record))
                .where(RESEARCH_ITEM.SEQ.eq(seq))
                .execute();

        delete(RESEARCH_ITEM.ITEM_ID.eq(record.getItemId()).and(RESEARCH_ITEM.MAPPING_NUMBER.ne((byte) 0)));

        cacheService.pbxServerList(getCompanyId()).forEach(e -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                pbxDsl.update(RESEARCH_ITEM)
                        .set(pbxDsl.newRecord(RESEARCH_ITEM, record))
                        .where(RESEARCH_ITEM.SEQ.eq(seq))
                        .execute();

                delete(pbxDsl, RESEARCH_ITEM.ITEM_ID.eq(record.getItemId()).and(RESEARCH_ITEM.MAPPING_NUMBER.ne((byte) 0)));
            }
        });

        byte rowIndex = 1;
        if (form.getAnswerRequests() != null) {
            final ResearchItemRecord newRecord = new ResearchItemRecord();
            newRecord.setItemId(record.getItemId());
            newRecord.setItemName(record.getItemName());
            newRecord.setSoundKind(record.getSoundKind());
            newRecord.setSoundCode(record.getSoundCode());
            newRecord.setItemId(record.getItemId());
            newRecord.setItemName(record.getItemName());
            newRecord.setSoundKind(record.getSoundKind());
            newRecord.setSoundCode(record.getSoundCode());
            newRecord.setCompanyId(getCompanyId());
            if (isNotEmpty(record.getGroupCode())) {
                newRecord.setGroupCode(record.getGroupCode());
                newRecord.setGroupTreeName(record.getGroupTreeName());
                newRecord.setGroupLevel(record.getGroupLevel());
            }

            for (String word : form.getAnswerRequests()) {
                newRecord.setWord(word);
                newRecord.setMappingNumber(rowIndex++);

                dsl.insertInto(RESEARCH_ITEM)
                        .set(newRecord)
                        .execute();

                cacheService.pbxServerList(getCompanyId()).forEach(e -> {
                    try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                        pbxDsl.insertInto(RESEARCH_ITEM)
                                .set(newRecord)
                                .execute();
                    }
                });
            }
        }
    }

    public void deleteItemId(Integer seq) {
        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchItem entity = findOneIfNullThrow(RESEARCH_ITEM.SEQ.eq(seq).and(RESEARCH_ITEM.MAPPING_NUMBER.eq((byte) 0)));

        delete(RESEARCH_ITEM.ITEM_ID.eq(entity.getItemId()));

        cacheService.pbxServerList(getCompanyId()).forEach(e -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                delete(pbxDsl, RESEARCH_ITEM.ITEM_ID.eq(entity.getItemId()));
            }
        });
    }

    public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchItem> findAllItemId(Integer itemId) {
        return findAll(RESEARCH_ITEM.ITEM_ID.eq(itemId), Arrays.asList(RESEARCH_ITEM.MAPPING_NUMBER.asc()));
    }

    private Integer nextSequence() {
        final ResearchItem sequenceSeed = RESEARCH_ITEM.as("SEQUENCE_SEED");
        return dsl.select(DSL.ifnull(DSL.max(sequenceSeed.ITEM_ID), 0).add(1)).from(sequenceSeed).fetchOneInto(Integer.class);
    }
}
