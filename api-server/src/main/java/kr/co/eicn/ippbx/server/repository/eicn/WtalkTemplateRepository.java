package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkTemplate;
import kr.co.eicn.ippbx.model.entity.eicn.TalkTemplateEntity;
import kr.co.eicn.ippbx.model.form.TalkTemplateFormRequest;
import kr.co.eicn.ippbx.model.search.TemplateSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PersonList.PERSON_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkTemplate.WTALK_TEMPLATE;

@Getter
@Repository
public class WtalkTemplateRepository extends EicnBaseRepository<WtalkTemplate, TalkTemplateEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(WtalkTemplateRepository.class);

    WtalkTemplateRepository() {
        super(WTALK_TEMPLATE, WTALK_TEMPLATE.SEQ, TalkTemplateEntity.class);

        addField(WTALK_TEMPLATE);
        addField(PERSON_LIST);
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        query.groupBy(getSelectingFields());

        return query
                .join(PERSON_LIST).on(WTALK_TEMPLATE.WRITE_USERID.eq(PERSON_LIST.ID))
                .where();
    }

    public Record insertOnGeneratedKey(TalkTemplateFormRequest form) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkTemplate record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkTemplate();

        record.setWriteUserid(g.getUser().getId());
        record.setType(form.getType());
        record.setTypeMent(form.getTypeMent().getCode());
        record.setMentName(form.getMentName());
        record.setMent(form.getMent());
        record.setCompanyId(getCompanyId());
        record.setTypeData(form.getTypeData());
        record.setFilePath(form.getFilePath());
        record.setOriginalFileName(form.getOriginalFileName());

        return super.insertOnGeneratedKey(record);
    }

    public Pagination<TalkTemplateEntity> pagination(TemplateSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(TemplateSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (search.getType() != null && kr.co.eicn.ippbx.model.enums.TalkTemplate.PERSON.getCode().equals(search.getType()))
            conditions.add(WTALK_TEMPLATE.TYPE.eq(search.getType()));
        else if (search.getType() != null && kr.co.eicn.ippbx.model.enums.TalkTemplate.GROUP.getCode().equals(search.getType()))
            conditions.add(WTALK_TEMPLATE.TYPE.eq(search.getType()));
        else if (search.getType() != null && kr.co.eicn.ippbx.model.enums.TalkTemplate.COMPANY.getCode().equals(search.getType()))
            conditions.add(WTALK_TEMPLATE.TYPE.eq(search.getType()));

        if (StringUtils.isNotEmpty(search.getMetaType()))
            conditions.add(WTALK_TEMPLATE.TYPE_DATA.eq(search.getMetaType()));
        if (StringUtils.isNotEmpty(search.getUserName()))
            conditions.add(PERSON_LIST.ID_NAME.eq(search.getUserName()));
        if (StringUtils.isNotEmpty(search.getMentName()))
            conditions.add(WTALK_TEMPLATE.MENT_NAME.eq(search.getMentName()));

        return conditions;
    }

    public List<TalkTemplateEntity> list() {

        return dsl.select(WTALK_TEMPLATE.fields())
                .select(PERSON_LIST.fields())
                .from(WTALK_TEMPLATE)
                .join(PERSON_LIST)
                .on(WTALK_TEMPLATE.WRITE_USERID.eq(PERSON_LIST.ID))
                .where(compareCompanyId())
                .fetch(record -> {
                    final TalkTemplateEntity entity = record.into(WTALK_TEMPLATE).into(TalkTemplateEntity.class);
                    entity.setPerson(record.into(PERSON_LIST).into(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList.class));
                    return entity;
                });
    }

    public Integer insert(TalkTemplateFormRequest form) {
        return dsl.insertInto(WTALK_TEMPLATE)
                .set(WTALK_TEMPLATE.TYPE, form.getType())
                .set(WTALK_TEMPLATE.TYPE_MENT, form.getTypeMent().getCode())
                .set(WTALK_TEMPLATE.TYPE_DATA, form.getTypeData())
                .set(WTALK_TEMPLATE.MENT_NAME, form.getMentName())
                .set(WTALK_TEMPLATE.MENT, form.getMent())
                .set(WTALK_TEMPLATE.ORIGINAL_FILE_NAME, form.getOriginalFileName())
                .set(WTALK_TEMPLATE.FILE_PATH, form.getFilePath())
                .set(WTALK_TEMPLATE.WRITE_USERID, g.getUser().getId())
                .set(WTALK_TEMPLATE.COMPANY_ID, g.getUser().getCompanyId())
                .returning(WTALK_TEMPLATE.SEQ)
                .fetchOne()
                .get(WTALK_TEMPLATE.SEQ);
    }

    public void update(Integer seq, TalkTemplateFormRequest form) {
        dsl.update(WTALK_TEMPLATE)
                .set(WTALK_TEMPLATE.TYPE, form.getType())
                .set(WTALK_TEMPLATE.TYPE_MENT, form.getTypeMent().getCode())
                .set(WTALK_TEMPLATE.TYPE_DATA, form.getTypeData())
                .set(WTALK_TEMPLATE.MENT_NAME, form.getMentName())
                .set(WTALK_TEMPLATE.MENT, form.getMent())
                .set(WTALK_TEMPLATE.ORIGINAL_FILE_NAME, form.getOriginalFileName())
                .set(WTALK_TEMPLATE.FILE_PATH, form.getFilePath())
                .where(WTALK_TEMPLATE.SEQ.eq(seq))
                .execute();
    }
}
