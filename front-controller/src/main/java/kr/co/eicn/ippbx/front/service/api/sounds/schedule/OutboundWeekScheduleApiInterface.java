package kr.co.eicn.ippbx.front.service.api.sounds.schedule;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.OutScheduleSeedDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryPhoneInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummarySoundListResponse;
import kr.co.eicn.ippbx.model.entity.eicn.OutScheduleSeedEntity;
import kr.co.eicn.ippbx.model.enums.ScheduleType;
import kr.co.eicn.ippbx.model.form.OutScheduleSeedFormRequest;
import kr.co.eicn.ippbx.model.search.OutScheduleSeedSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class OutboundWeekScheduleApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(OutboundWeekScheduleApiInterface.class);
    private static final String subUrl = "/api/v1/admin/sounds/schedule/outbound/week/";

    public List<OutScheduleSeedEntity> list(OutScheduleSeedSearchRequest search) throws IOException, ResultFailException {
        search.setType(ScheduleType.WEEK);
        return getList(subUrl, search, OutScheduleSeedEntity.class).getData();
    }

    public OutScheduleSeedDetailResponse get(Integer parent) throws IOException, ResultFailException {
        return getData(subUrl + parent, null, OutScheduleSeedDetailResponse.class).getData();
    }

    public void post(OutScheduleSeedFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void update(Integer parent, OutScheduleSeedFormRequest form) throws IOException, ResultFailException {
        put(subUrl + parent, form);
    }

    public void delete(Integer parent) throws IOException, ResultFailException {
        super.delete(subUrl + parent);
    }

    public List<SummaryPhoneInfoResponse> addExtensions() throws IOException, ResultFailException {
        return getList(subUrl + "add-extensions", null, SummaryPhoneInfoResponse.class).getData();
    }

    public List<SummarySoundListResponse> addSounds() throws IOException, ResultFailException {
        return getList(subUrl + "add-sounds", null, SummarySoundListResponse.class).getData();
    }
}
