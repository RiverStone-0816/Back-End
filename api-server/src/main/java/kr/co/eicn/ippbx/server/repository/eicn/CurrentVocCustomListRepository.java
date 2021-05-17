package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.CurrentVocCustomList;
import kr.co.eicn.ippbx.server.model.form.ResultCustomInfoFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.CURRENT_VOC_CUSTOM_LIST;

@Getter
@Repository
public class CurrentVocCustomListRepository extends EicnBaseRepository<CurrentVocCustomList, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CurrentVocCustomList, Integer>  {
    protected final Logger logger = LoggerFactory.getLogger(CurrentMemberStatusRepository.class);


    public CurrentVocCustomListRepository() {
        super(CURRENT_VOC_CUSTOM_LIST, CURRENT_VOC_CUSTOM_LIST.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CurrentVocCustomList.class);
    }

    public void insertByVocGroup(ResultCustomInfoFormRequest form) {
        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CurrentVocCustomList vocCustomList = new kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CurrentVocCustomList();
        vocCustomList.setUserid(g.getUser().getId());
        vocCustomList.setSender("MEMBER");
        vocCustomList.setExtension(g.getUser().getExtension());
        vocCustomList.setCustomNumber(form.getCustomNumber());
        vocCustomList.setVocGroupId(form.getVocGroup());
        vocCustomList.setCompanyId(getCompanyId());

        super.insertOnGeneratedKey(vocCustomList);
    }
}
