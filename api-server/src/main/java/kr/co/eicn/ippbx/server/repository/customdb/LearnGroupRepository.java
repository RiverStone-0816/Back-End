package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonTranscribeLearn;
import kr.co.eicn.ippbx.model.entity.customdb.TranscribeLearnEntity;
import kr.co.eicn.ippbx.model.form.LearnGroupFormRequest;
import kr.co.eicn.ippbx.model.search.LearnGroupSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LearnGroupRepository extends CustomDBBaseRepository<CommonTranscribeLearn, TranscribeLearnEntity, Integer>{
    protected final Logger logger = LoggerFactory.getLogger(LearnGroupRepository.class);

    private final CommonTranscribeLearn TABLE;

    public LearnGroupRepository(String companyId) {
        super(new CommonTranscribeLearn(companyId), new CommonTranscribeLearn(companyId).SEQ, TranscribeLearnEntity.class);
        this.TABLE = new CommonTranscribeLearn(companyId);
    }

    public Pagination<TranscribeLearnEntity> pagination(LearnGroupSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(LearnGroupSearchRequest search) {
        List<Condition> conditions = new ArrayList<>();
        if (search.getLearnGroup() != null) {
            conditions.add(TABLE.SEQ.eq(search.getLearnGroup()));
        }
        if (StringUtils.isNotEmpty(search.getGroupName())) {
            conditions.add(TABLE.GROUPNAME.contains(search.getGroupName()));
        }

        return conditions;
    }

    public void insert(LearnGroupFormRequest request) {
        dsl.insertInto(TABLE)
                .set(TABLE.GROUPNAME, request.getGroupName())
                .set(TABLE.LEARNGROUPCODE, request.getGroupList().stream().reduce((e, f) -> e + "|" + f).orElse(""))
                .set(TABLE.LEARNSTATUS, "NOK")
                .set(TABLE.COMPANY_ID, getCompanyId())
                .execute();
    }

    public void update(Integer seq, LearnGroupFormRequest request) {
        dsl.update(TABLE)
                .set(TABLE.GROUPNAME, request.getGroupName())
                .set(TABLE.LEARNGROUPCODE, request.getGroupList().stream().reduce((e, f) -> e + "|" + f).orElse(""))
                .where(TABLE.SEQ.eq(seq))
                .execute();
    }

    public void statusUpdate(Integer seq) {
        dsl.update(TABLE)
                .set(TABLE.LEARNSTATUS, "B")
                .where(TABLE.SEQ.eq(seq))
                .execute();
    }
}
