package kr.co.eicn.ippbx.front.service.api.talk.schedule;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryTalkMentResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.TalkScheduleGroupListDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.TalkScheduleGroupSummaryResponse;
import kr.co.eicn.ippbx.server.model.form.TalkScheduleGroupFormRequest;
import kr.co.eicn.ippbx.server.model.form.TalkScheduleGroupListFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TalkScheduleGroupApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(TalkScheduleGroupApiInterface.class);

    private static final String subUrl = "/api/v1/admin/talk/schedule/type/";

    public List<TalkScheduleGroupSummaryResponse> list() throws IOException, ResultFailException {
        return getList(subUrl, null, TalkScheduleGroupSummaryResponse.class).getData();
    }

    public void post(TalkScheduleGroupFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void delete(Integer parent) throws IOException, ResultFailException {
        delete(subUrl + parent);
    }

    public TalkScheduleGroupListDetailResponse getItem(Integer child) throws IOException, ResultFailException {
        return getData(subUrl + "item/" + child, null, TalkScheduleGroupListDetailResponse.class).getData();
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

    public List<SummaryTalkMentResponse> getTalkMent() throws IOException, ResultFailException {
        return getList(subUrl + "talk-ment", null, SummaryTalkMentResponse.class).getData();
    }
}
