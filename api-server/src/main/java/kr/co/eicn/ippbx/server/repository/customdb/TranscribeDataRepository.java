package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonTranscribeData;
import kr.co.eicn.ippbx.model.entity.customdb.TranscribeDataEntity;
import kr.co.eicn.ippbx.model.search.TranscribeDataSearchRequest;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
public class TranscribeDataRepository extends CustomDBBaseRepository<CommonTranscribeData, TranscribeDataEntity, Integer>{

    protected final Logger logger = LoggerFactory.getLogger(TranscribeGroupRepository.class);

    private final CommonTranscribeData TABLE;

    public TranscribeDataRepository(String companyId){
        super (new CommonTranscribeData(companyId), new CommonTranscribeData(companyId).SEQ, TranscribeDataEntity.class);
        this.TABLE = new CommonTranscribeData(companyId);
    }

    /*
    public Pagination<TranscribeDataEntity> pagination(TranscribeDataSearchRequest search) {
        return super.pagination(search, conditions(search));
    }
    */

    public List<TranscribeDataEntity> list(TranscribeDataSearchRequest search, Set<Integer> groupSeqSet) {
        return findAll(conditions(search, groupSeqSet));
    }

    private List<Condition> conditions(TranscribeDataSearchRequest search, Set<Integer> groupSeqSet) {
        List<Condition> conditions = new ArrayList<>();

        if (groupSeqSet.size() > 0)
            conditions.add(TABLE.GROUPCODE.in(groupSeqSet));

        if (StringUtils.isNotEmpty(search.getUserId()))
            conditions.add(TABLE.USERID.eq(search.getUserId()));

        return conditions;
    }

    public void updateInfo(Integer fileSeq, String hypInfo, String refInfo) {
        dsl.update(TABLE)
                .set(TABLE.USERID, g.getUser().getId())
                .set(TABLE.HYPINFO, hypInfo)
                .set(TABLE.REFINFO, refInfo)
                .where(TABLE.SEQ.eq(fileSeq))
                .execute();
    }

    public void updateLearnStatus(Integer seq, String status) {
        dsl.update(TABLE)
                .set(TABLE.LEARN, "true".equals(status) ? "Y" : "N")
                .where(TABLE.SEQ.eq(seq))
                .execute();
    }
}
