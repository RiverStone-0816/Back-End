package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkTemplate;
import kr.co.eicn.ippbx.model.entity.eicn.TalkTemplateEntity;
import kr.co.eicn.ippbx.model.form.TalkTemplateFormRequest;
import lombok.Getter;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PersonList.PERSON_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkTemplate.TALK_TEMPLATE;

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
