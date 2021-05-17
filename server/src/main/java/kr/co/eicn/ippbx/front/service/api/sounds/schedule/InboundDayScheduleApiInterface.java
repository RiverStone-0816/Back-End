package kr.co.eicn.ippbx.front.service.api.sounds.schedule;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.Number070ScheduleInfoDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.Number070ScheduleInfoResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryNumber070Response;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryScheduleGroupResponse;
import kr.co.eicn.ippbx.server.model.entity.eicn.ScheduleGroupEntity;
import kr.co.eicn.ippbx.server.model.enums.ScheduleType;
import kr.co.eicn.ippbx.server.model.form.DayScheduleInfoFormRequest;
import kr.co.eicn.ippbx.server.model.form.DayScheduleInfoUpdateFormRequest;
import kr.co.eicn.ippbx.server.model.form.HolyScheduleInfoFormRequest;
import kr.co.eicn.ippbx.server.model.search.ScheduleInfoSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class InboundDayScheduleApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(InboundDayScheduleApiInterface.class);
    private static final String subUrl = "/api/v1/admin/sounds/schedule/inbound/day/";

    public List<Number070ScheduleInfoResponse> list(ScheduleInfoSearchRequest search) throws IOException, ResultFailException {
        search.setType(ScheduleType.DAY);
        return getList(subUrl, search, Number070ScheduleInfoResponse.class).getData();
    }

    public ScheduleGroupEntity getType(Integer parent) throws IOException, ResultFailException {
        return getData(subUrl + "service/type/" + parent, null, ScheduleGroupEntity.class).getData();
    }

    public Number070ScheduleInfoDetailResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, Number070ScheduleInfoDetailResponse.class).getData();
    }

    public void post(DayScheduleInfoFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void delete(String number) throws IOException, ResultFailException {
        super.delete(subUrl + number);
    }

    public void deleteType(Integer seq) throws IOException, ResultFailException {
        super.delete(subUrl + "service/type/" + seq);
    }

    public void holyPost(HolyScheduleInfoFormRequest form) throws IOException, ResultFailException {
        post(subUrl + "holy", form);
    }

    public void updateType(Integer seq, DayScheduleInfoUpdateFormRequest form) throws IOException, ResultFailException {
        put(subUrl + "service/type/" + seq, form);
    }

    public List<SummaryNumber070Response> searchNumbers() throws IOException, ResultFailException {
        return getList(subUrl + "search-number-list", null, SummaryNumber070Response.class).getData();
    }

    public List<SummaryScheduleGroupResponse> scheduleGroups() throws IOException, ResultFailException {
        return getList(subUrl + "schedule-group", null, SummaryScheduleGroupResponse.class).getData();
    }

    public List<SummaryNumber070Response> addNumber070List() throws IOException, ResultFailException {
        return getList(subUrl + "add-number-list", null, SummaryNumber070Response.class).getData();
    }
}
