package kr.co.eicn.ippbx.front.service.api.sounds.schedule;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroup;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.form.ScheduleGroupFormRequest;
import kr.co.eicn.ippbx.model.form.ScheduleGroupListFormRequest;
import kr.co.eicn.ippbx.model.form.ScheduleGroupListTimeUpdateFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ScheduleGroupApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleGroupApiInterface.class);
    private static final String subUrl = "/api/v1/admin/sounds/schedule/";

    public List<ScheduleGroupSummaryResponse> list() throws IOException, ResultFailException {
        return getList(subUrl, null, ScheduleGroupSummaryResponse.class).getData();
    }

    public void post(ScheduleGroupFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void put(ScheduleGroupFormRequest form, Integer parent) throws IOException, ResultFailException {
        put(subUrl + parent, form);
    }

    public void delete(Integer parent) throws IOException, ResultFailException {
        delete(subUrl + parent);
    }

    public ScheduleGroup getParent(Integer parent) throws IOException, ResultFailException {
        return getData(subUrl + parent, null, ScheduleGroup.class).getData();
    }

    public ScheduleGroupListDetailResponse getItem(Integer child) throws IOException, ResultFailException {
        return getData(subUrl + "item/" + child, null, ScheduleGroupListDetailResponse.class).getData();
    }

    public void registerItem(ScheduleGroupListFormRequest form) throws IOException, ResultFailException {
        post(subUrl + "item", form);
    }

    public void updateItem(Integer child, ScheduleGroupListFormRequest form) throws IOException, ResultFailException {
        put(subUrl + "item/" + child, form);
    }

    public void updateTime(Integer child, ScheduleGroupListTimeUpdateFormRequest form) throws IOException, ResultFailException {
        put(subUrl + "item/time/" + child, form);
    }

    public void deleteItem(Integer child) throws IOException, ResultFailException {
        delete(subUrl + "item/" + child);
    }

    public List<SummarySoundListResponse> addSoundList() throws IOException, ResultFailException {
        return getList(subUrl + "add-sounds-list", null, SummarySoundListResponse.class).getData();
    }

    public List<SummaryNumber070Response> addNumber070List() throws IOException, ResultFailException {
        return getList(subUrl + "add-number-list", null, SummaryNumber070Response.class).getData();
    }

    public List<SummaryIvrTreeResponse> addIvrTreeList() throws IOException, ResultFailException {
        return getList(subUrl + "add-ivr-list", null, SummaryIvrTreeResponse.class).getData();
    }

    public List<SummaryContextInfoResponse> addContextList() throws IOException, ResultFailException {
        return getList(subUrl + "add-context-list", null, SummaryContextInfoResponse.class).getData();
    }

    public List<SummaryCallbotResponse> addCallbotList() throws IOException, ResultFailException {
        return getList(subUrl + "add-callbot-list", null, SummaryCallbotResponse.class).getData();
    }

    public void itemCopy(Integer parent, Integer targetParent) throws IOException, ResultFailException {
        post(subUrl + parent + "/" + targetParent + "/copy", null);
    }
}
