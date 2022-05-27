package kr.co.eicn.ippbx.front.service.api.wtalk.group;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryWtalkGroupPersonResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryWtalkServiceResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkMemberGroupDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkMemberGroupSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkMemberGroupFormRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class WtalkReceptionGroupApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(WtalkReceptionGroupApiInterface.class);

    private static final String subUrl = "/api/v1/admin/wtalk/group/reception-group/";

    public List<WtalkMemberGroupSummaryResponse> list() throws IOException, ResultFailException {
        return getList(subUrl, null, WtalkMemberGroupSummaryResponse.class).getData();
    }

    public WtalkMemberGroupDetailResponse get(Integer groupId) throws IOException, ResultFailException {
        return getData(subUrl + groupId, null, WtalkMemberGroupDetailResponse.class).getData();
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

    public List<SummaryWtalkServiceResponse> talkServices() throws IOException, ResultFailException {
        return getList(subUrl + "add-services", null, SummaryWtalkServiceResponse.class).getData();
    }

    public List<SummaryWtalkGroupPersonResponse> addOnPersons() throws IOException, ResultFailException {
        return addOnPersons(null);
    }

    public List<SummaryWtalkGroupPersonResponse> addOnPersons(Integer groupId) throws IOException, ResultFailException {
        return getList(subUrl + "add-on-persons?groupId=" + (groupId != null ? "" + groupId : ""), null, SummaryWtalkGroupPersonResponse.class).getData();
    }
}
