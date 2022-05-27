package kr.co.eicn.ippbx.front.service.api.wtalk.schedule;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryWtalkScheduleInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryWtalkServiceResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkScheduleInfoDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkServiceInfoResponse;
import kr.co.eicn.ippbx.model.entity.eicn.WtalkScheduleGroupEntity;
import kr.co.eicn.ippbx.model.form.TalkScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.form.TalkScheduleInfoFormUpdateRequest;
import kr.co.eicn.ippbx.model.search.TalkServiceInfoSearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class WtalkWeekScheduleApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(WtalkWeekScheduleApiInterface.class);
    private static final String subUrl = "/api/v1/admin/wtalk/schedule/week/";

    public List<WtalkServiceInfoResponse> list(TalkServiceInfoSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, WtalkServiceInfoResponse.class).getData();
    }

    public WtalkScheduleInfoDetailResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, WtalkScheduleInfoDetailResponse.class).getData();
    }

    public void post(TalkScheduleInfoFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void delete(String senderKey) throws IOException, ResultFailException {
        super.delete(subUrl + senderKey);
    }

    public WtalkScheduleGroupEntity getType(Integer parent) throws IOException, ResultFailException {
        return getData(subUrl + "service/type/" + parent, null, WtalkScheduleGroupEntity.class).getData();
    }

    public void updateType(Integer seq, TalkScheduleInfoFormUpdateRequest form) throws IOException, ResultFailException {
        put(subUrl + "service/type/" + seq, form);
    }

    public void deleteType(Integer seq) throws IOException, ResultFailException {
        super.delete(subUrl + "service/type/" + seq);
    }

    public List<SummaryWtalkScheduleInfoResponse> scheduleInfos() throws IOException, ResultFailException {
        return getList(subUrl + "schedule-info", null, SummaryWtalkScheduleInfoResponse.class).getData();
    }

    public List<SummaryWtalkServiceResponse> talkServices() throws IOException, ResultFailException {
        return getList(subUrl + "add-services", null, SummaryWtalkServiceResponse.class).getData();
    }
}
