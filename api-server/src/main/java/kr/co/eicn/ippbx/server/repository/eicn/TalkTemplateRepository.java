package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkTemplate;
import kr.co.eicn.ippbx.model.entity.eicn.TalkTemplateEntity;
import kr.co.eicn.ippbx.model.form.TalkTemplateFormRequest;
import kr.co.eicn.ippbx.model.search.TemplateSearchRequest;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PersonList.PERSON_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkTemplate.TALK_TEMPLATE;
import static org.jooq.impl.DSL.noCondition;

@Getter
@Repository
public class TalkTemplateRepository extends EicnBaseRepository<TalkTemplate, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkTemplate, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(TalkTemplateRepository.class);

	TalkTemplateRepository() {
		super(TALK_TEMPLATE, TALK_TEMPLATE.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkTemplate.class);
	}

	public Record insertOnGeneratedKey(TalkTemplateFormRequest form){
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkTemplate record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkTemplate();

		record.setWriteUserid(g.getUser().getId());
		record.setType(form.getType());
		record.setMentName(form.getMentName());
		record.setMent(form.getMent());
		record.setCompanyId(getCompanyId());
		record.setTypeData(form.getTypeData());

		return super.insertOnGeneratedKey(record);
	}

	public List<TalkTemplateEntity> list(TemplateSearchRequest search){
		Condition condition = noCondition();
		if (Objects.nonNull(search.getType()))
			condition.and(TALK_TEMPLATE.TYPE.eq(search.getType().getCode()));
		if (Objects.nonNull(search.getMetaType()) && !search.getMetaType().equals(""))
			condition.and(TALK_TEMPLATE.TYPE_DATA.eq(search.getMetaType()));
		if (Objects.nonNull(search.getUserName()) && !search.getUserName().equals(""))
			condition.and(PERSON_LIST.ID_NAME.eq(search.getUserName()));
		if (Objects.nonNull(search.getMentName()) && !search.getMentName().equals(""))
			condition.and(TALK_TEMPLATE.MENT_NAME.eq(search.getMentName()));

		return dsl.select(TALK_TEMPLATE.fields())
				.select(PERSON_LIST.fields())
				.from(TALK_TEMPLATE)
				.join(PERSON_LIST)
				.on(TALK_TEMPLATE.WRITE_USERID.eq(PERSON_LIST.ID))
				.where(compareCompanyId())
				.and(condition)
				.fetch(record -> {
					final TalkTemplateEntity entity = record.into(TALK_TEMPLATE).into(TalkTemplateEntity.class);
					entity.setPerson(record.into(PERSON_LIST).into(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList.class));
					return entity;
				});
	}
}
