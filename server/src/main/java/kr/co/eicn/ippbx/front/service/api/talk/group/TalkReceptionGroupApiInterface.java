package kr.co.eicn.ippbx.front.service.api.talk.group;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryTalkGroupPersonResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryTalkServiceResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.TalkMemberGroupDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.TalkMemberGroupSummaryResponse;
import kr.co.eicn.ippbx.server.model.form.TalkMemberGroupFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TalkReceptionGroupApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(TalkReceptionGroupApiInterface.class);

    private static final String subUrl = "/api/v1/admin/talk/group/reception-group/";

    public List<TalkMemberGroupSummaryResponse> list() throws IOException, ResultFailException {
        return getList(subUrl, null, TalkMemberGroupSummaryResponse.class).getData();
    }

    public TalkMemberGroupDetailResponse get(Integer groupId) throws IOException, ResultFailException {
        return getData(subUrl + groupId, null, TalkMemberGroupDetailResponse.class).getData();
    }

    public Integer post(TalkMemberGroupFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    public void put(Integer groupId, TalkMemberGroupFormRequest form) throws IOException, ResultFailException {
        put(subUrl + groupId, form);
    }

    public void delete(Integer groupId) throws IOException, ResultFailException {
        delete(subUrl + groupId);
    }

    public List<SummaryTalkServiceResponse> talkServices() throws IOException, ResultFailException {
        return getList(subUrl + "add-services", null, SummaryTalkServiceResponse.class).getData();
    }

    public List<SummaryTalkGroupPersonResponse> addOnPersons() throws IOException, ResultFailException {
        return addOnPersons(null);
    }

    public List<SummaryTalkGroupPersonResponse> addOnPersons(Integer groupId) throws IOException, ResultFailException {
        return getList(subUrl + "add-on-persons?groupId=" + (groupId != null ? "" + groupId : ""), null, SummaryTalkGroupPersonResponse.class).getData();
    }
}
