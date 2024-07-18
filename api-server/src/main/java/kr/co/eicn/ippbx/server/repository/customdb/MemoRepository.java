package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonMemo;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.Memo;
import kr.co.eicn.ippbx.model.form.MemoFormRequest;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;

@Getter
public class MemoRepository extends CustomDBBaseRepository<CommonMemo, Memo, Long> {
    private final Logger logger = LoggerFactory.getLogger(MemoRepository.class);
    private CommonMemo TABLE;

    public MemoRepository(String companyId) {
        super(new CommonMemo(companyId), new CommonMemo(companyId).ID, Memo.class);
        this.TABLE = new CommonMemo(companyId);
        orderByFields.add(TABLE.BOOKMARKED.desc());
        orderByFields.add(TABLE.CREATED_AT.desc());
    }

    public List<Memo> findAllToUserId(String keyword) {
        if(StringUtils.isNotEmpty(keyword)){
            return findAll(TABLE.USER.eq(g.getUser().getId()).and(TABLE.DELETED_AT.isNull()).and(TABLE.TITLE.contains(keyword)).or(TABLE.CONTENT.contains(keyword)), orderByFields);
        }else{
            return findAll(TABLE.USER.eq(g.getUser().getId()).and(TABLE.DELETED_AT.isNull()), orderByFields);
        }
    }

    public void insertMemo(MemoFormRequest request) {
        dsl.insertInto(TABLE)
                .set(TABLE.USER, g.getUser().getId())
                .set(TABLE.COMPANY_ID, g.getUser().getCompanyId())
                .set(TABLE.TITLE, request.getTitle())
                .set(TABLE.CONTENT, request.getContent())
                .execute();
    }

    public void updateMemo(Long id, MemoFormRequest request) {
        dsl.update(TABLE)
                .set(TABLE.TITLE, request.getTitle())
                .set(TABLE.CONTENT, request.getContent())
                .set(TABLE.UPDATED_AT, new Timestamp(System.currentTimeMillis()))
                .where(TABLE.ID.eq(id).and(TABLE.USER.eq(g.getUser().getId())))
                .execute();
    }

    public void bookmarkedMemo(Long id, boolean bookmarked) {
        dsl.update(TABLE)
                .set(TABLE.BOOKMARKED, bookmarked)
                .where(TABLE.ID.eq(id).and(TABLE.USER.eq(g.getUser().getId())))
                .execute();
    }

    public void deleteMemo(Long id) {
        dsl.update(TABLE)
                .set(TABLE.DELETED_AT, new Timestamp(System.currentTimeMillis()))
                .where(TABLE.ID.eq(id).and(TABLE.USER.eq(g.getUser().getId())))
                .execute();
    }
}
