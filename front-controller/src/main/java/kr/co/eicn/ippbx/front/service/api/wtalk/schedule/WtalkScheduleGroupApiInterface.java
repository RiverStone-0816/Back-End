package kr.co.eicn.ippbx.front.service.api.wtalk.schedule;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryWtalkMentResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkScheduleGroupListDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkScheduleGroupSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkScheduleGroupFormRequest;
import kr.co.eicn.ippbx.model.form.TalkScheduleGroupListFormRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class WtalkScheduleGroupApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(WtalkScheduleGroupApiInterface.class);

    private static final String subUrl = "/api/v1/admin/wtalk/schedule/type/";

    public List<WtalkScheduleGroupSummaryResponse> list() throws IOException, ResultFailException {
        return getList(subUrl, null, WtalkScheduleGroupSummaryResponse.class).getData();
    }

    public void post(TalkScheduleGroupFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void delete(Integer parent) throws IOException, ResultFailException {
        delete(subUrl + parent);
    }

    public WtalkScheduleGroupListDetailResponse getItem(Integer child) throws IOException, ResultFailException {
        return getData(subUrl + "item/" + child, null, WtalkScheduleGroupListDetailResponse.class).getData();
    }

    public void registerItem(TalkScheduleGroupListFormRequest form) throws IOException, ResultFailException {
        post(subUrl + "item/", form);
    }

    public void updateItem(Integer child, TalkScheduleGroupListFormRequest form) throws IOException, ResultFailException {
        put(subUrl + "item/" + child, form);
    }

    public void deleteItem(Integer child) throws IOException, ResultFailException {
        delete(subUrl + "item/" + child);
    }

    public List<SummaryWtalkMentResponse> getTalkMent() throws IOException, ResultFailException {
        return getList(subUrl + "wtalk-ment", null, SummaryWtalkMentResponse.class).getData();
    }
}
