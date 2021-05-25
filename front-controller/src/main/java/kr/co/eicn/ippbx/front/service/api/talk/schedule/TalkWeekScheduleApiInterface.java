package kr.co.eicn.ippbx.front.service.api.talk.schedule;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryTalkScheduleInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryTalkServiceResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkScheduleInfoDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkServiceInfoResponse;
import kr.co.eicn.ippbx.model.entity.eicn.TalkScheduleGroupEntity;
import kr.co.eicn.ippbx.model.form.TalkScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.form.TalkScheduleInfoFormUpdateRequest;
import kr.co.eicn.ippbx.model.search.TalkServiceInfoSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TalkWeekScheduleApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(TalkWeekScheduleApiInterface.class);
    private static final String subUrl = "/api/v1/admin/talk/schedule/week/";

    public List<TalkServiceInfoResponse> list(TalkServiceInfoSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, TalkServiceInfoResponse.class).getData();
    }

    public TalkScheduleInfoDetailResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, TalkScheduleInfoDetailResponse.class).getData();
    }

    public void post(TalkScheduleInfoFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void delete(String senderKey) throws IOException, ResultFailException {
        super.delete(subUrl + senderKey);
    }

    public TalkScheduleGroupEntity getType(Integer parent) throws IOException, ResultFailException {
        return getData(subUrl + "service/type/" + parent, null, TalkScheduleGroupEntity.class).getData();
    }

    public void updateType(Integer seq, TalkScheduleInfoFormUpdateRequest form) throws IOException, ResultFailException {
        put(subUrl + "service/type/" + seq, form);
    }

    public void deleteType(Integer seq) throws IOException, ResultFailException {
        super.delete(subUrl + "service/type/" + seq);
    }

    public List<SummaryTalkScheduleInfoResponse> scheduleInfos() throws IOException, ResultFailException {
        return getList(subUrl + "schedule-info", null, SummaryTalkScheduleInfoResponse.class).getData();
    }

    public List<SummaryTalkServiceResponse> talkServices() throws IOException, ResultFailException {
        return getList(subUrl + "add-services", null, SummaryTalkServiceResponse.class).getData();
    }
}
