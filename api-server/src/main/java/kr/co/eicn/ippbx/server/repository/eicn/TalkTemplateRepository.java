package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkTemplate;
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
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkTemplate.TALK_TEMPLATE;

@Getter
@Repository
public class TalkTemplateRepository extends EicnBaseRepository<TalkTemplate, TalkTemplateEntity, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(TalkTemplateRepository.class);

	TalkTemplateRepository() {
		super(TALK_TEMPLATE, TALK_TEMPLATE.SEQ, TalkTemplateEntity.class);

		addField(TALK_TEMPLATE);
		addField(PERSON_LIST);
	}

	@Override
	protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
		query.groupBy(getSelectingFields());

		return query
				.join(PERSON_LIST).on(TALK_TEMPLATE.WRITE_USERID.eq(PERSON_LIST.ID))
				.where();
	}

	public Record insertOnGeneratedKey(TalkTemplateFormRequest form){
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkTemplate record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkTemplate();

		record.setWriteUserid(g.getUser().getId());
		record.setType(form.getType());
		record.setTypeMent(form.getTypeMent());
		record.setMentName(form.getMentName());
		record.setMent(form.getMent());
		record.setCompanyId(getCompanyId());
		record.setTypeData(form.getTypeData());

		return super.insertOnGeneratedKey(record);
	}

	public Pagination<TalkTemplateEntity> pagination(TemplateSearchRequest search) {
		return super.pagination(search, conditions(search));
	}

	private List<Condition> conditions(TemplateSearchRequest search) {
		final List<Condition> conditions = new ArrayList<>();

		if (search.getType() != null && kr.co.eicn.ippbx.model.enums.TalkTemplate.PERSON.getCode().equals(search.getType()))
			conditions.add(TALK_TEMPLATE.TYPE.eq(search.getType()));
		else if(search.getType() != null && kr.co.eicn.ippbx.model.enums.TalkTemplate.GROUP.getCode().equals(search.getType()))
			conditions.add(TALK_TEMPLATE.TYPE.eq(search.getType()));
		else if(search.getType() != null && kr.co.eicn.ippbx.model.enums.TalkTemplate.COMPANY.getCode().equals(search.getType()))
			conditions.add(TALK_TEMPLATE.TYPE.eq(search.getType()));

		if (StringUtils.isNotEmpty(search.getMetaType()))
			conditions.add(TALK_TEMPLATE.TYPE_DATA.eq(search.getMetaType()));
		if (StringUtils.isNotEmpty(search.getUserName()))
			conditions.add(PERSON_LIST.ID_NAME.eq(search.getUserName()));
		if (StringUtils.isNotEmpty(search.getMentName()))
			conditions.add(TALK_TEMPLATE.MENT_NAME.eq(search.getMentName()));

		return conditions;
	}

	public List<TalkTemplateEntity> list(){

		return dsl.select(TALK_TEMPLATE.fields())
				.select(PERSON_LIST.fields())
				.from(TALK_TEMPLATE)
				.join(PERSON_LIST)
				.on(TALK_TEMPLATE.WRITE_USERID.eq(PERSON_LIST.ID))
				.where(compareCompanyId())
				.fetch(record -> {
					final TalkTemplateEntity entity = record.into(TALK_TEMPLATE).into(TalkTemplateEntity.class);
					entity.setPerson(record.into(PERSON_LIST).into(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList.class));
					return entity;
				});
	}
}
