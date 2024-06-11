package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.ContextInfo;
import kr.co.eicn.ippbx.model.form.ContextInfoFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Objects;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ContextInfo.CONTEXT_INFO;

@Getter
@Repository
public class ContextInfoRepository extends EicnBaseRepository<ContextInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ContextInfo, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(ContextInfoRepository.class);

    public ContextInfoRepository() {
        super(CONTEXT_INFO, CONTEXT_INFO.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ContextInfo.class);
        orderByFields.add(CONTEXT_INFO.NAME.asc());
    }

    public void insert(ContextInfoFormRequest form) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ContextInfo entity = findOne(CONTEXT_INFO.CONTEXT.eq(form.getContext()));
        if (Objects.nonNull(entity))
            throw new IllegalArgumentException("이미 등록된 컨텍스트입니다.");
        dsl.insertInto(CONTEXT_INFO)
                .set(CONTEXT_INFO.CONTEXT, form.getContext())
                .set(CONTEXT_INFO.NAME, form.getName())
                .set(CONTEXT_INFO.COMPANY_ID, getCompanyId())
                .set(CONTEXT_INFO.IS_WEB_VOICE, form.getIsWebVoice())
                .execute();
    }

    public void updateByKey(ContextInfoFormRequest form, Integer seq) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ContextInfo entity = findOne(CONTEXT_INFO.CONTEXT.eq(form.getContext()).and(CONTEXT_INFO.SEQ.ne(seq)));
        if (Objects.nonNull(entity))
            throw new IllegalArgumentException("이미 등록된 컨텍스트입니다.");

        super.updateByKey(form, seq);
    }

    public void updateByContext(ContextInfoFormRequest form, String context) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ContextInfo entity = findOne(CONTEXT_INFO.CONTEXT.eq(form.getContext()));
        if (Objects.nonNull(entity) && !entity.getContext().equals(context))
            throw new IllegalArgumentException("이미 등록된 컨텍스트입니다.");
        update(dsl(), form, CONTEXT_INFO.CONTEXT.eq(context));
    }

    public void deleteByContext(String context) {
        delete(CONTEXT_INFO.CONTEXT.eq(context));
    }

    public kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ContextInfo findOneByContext(String context) {
        return findOne(CONTEXT_INFO.CONTEXT.eq(context));
    }
}
