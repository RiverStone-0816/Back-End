package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonProhibitedKeyword;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.ProhibitedKeyword;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class ProhibitedKeywordRepository extends CustomDBBaseRepository<CommonProhibitedKeyword, ProhibitedKeyword, String>{
    private final Logger logger = LoggerFactory.getLogger(ProhibitedKeywordRepository.class);
    private CommonProhibitedKeyword TABLE;

    public ProhibitedKeywordRepository(String companyId) {
        super(new CommonProhibitedKeyword(companyId), new CommonProhibitedKeyword(companyId).KEYWORD, ProhibitedKeyword.class);
        this.TABLE = new CommonProhibitedKeyword(companyId);
    }

    public ProhibitedKeyword findByKeyword(String keyword) {
        return super.findOne(TABLE.KEYWORD.eq(keyword));
    }

    public void prohibitKeywordInsert(String keyword) {
        dsl.insertInto(TABLE)
                .set(TABLE.KEYWORD, keyword)
                .set(TABLE.CREATOR, g.getUser().getId())
                .set(TABLE.PROHIBIT_YN, "Y")
                .set(TABLE.COMPANY_ID, getCompanyId())
                .execute();
    }

    public void keywordInsert(String keyword) {
        dsl.insertInto(TABLE)
                .set(TABLE.KEYWORD, keyword)
                .set(TABLE.CREATOR, g.getUser().getId())
                .set(TABLE.KEYWORD_YN, "Y")
                .set(TABLE.COMPANY_ID, getCompanyId())
                .execute();
    }

    public void keywordDelete(String keyword) {
        dsl.delete(TABLE)
                .where(TABLE.KEYWORD.eq(keyword))
                .and(TABLE.COMPANY_ID.eq(getCompanyId()))
                .execute();
    }
}
