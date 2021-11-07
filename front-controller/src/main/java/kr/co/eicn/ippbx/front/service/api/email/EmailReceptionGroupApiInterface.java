package kr.co.eicn.ippbx.front.service.api.email;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.EmailMemberListSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.EmailMngSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.EmailReceiveGroupDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.EmailReceiveGroupSummaryResponse;
import kr.co.eicn.ippbx.model.form.EmailReceiveGroupFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EmailReceptionGroupApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(EmailReceptionGroupApiInterface.class);

    private static final String subUrl = "/api/v1/admin/email/mng/receive-group/";

    public List<EmailReceiveGroupSummaryResponse> list() throws IOException, ResultFailException {
        return getList(subUrl, null, EmailReceiveGroupSummaryResponse.class).getData();
    }

    public EmailReceiveGroupDetailResponse get(Integer groupId) throws IOException, ResultFailException {
        return getData(subUrl + groupId, null, EmailReceiveGroupDetailResponse.class).getData();
    }

    public Integer post(EmailReceiveGroupFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    public void put(Integer groupId, EmailReceiveGroupFormRequest form) throws IOException, ResultFailException {
        put(subUrl + groupId, form);
    }

    public void delete(Integer groupId) throws IOException, ResultFailException {
        delete(subUrl + groupId);
    }

    public List<EmailMngSummaryResponse> services() throws IOException, ResultFailException {
        return getList(subUrl + "services", null, EmailMngSummaryResponse.class).getData();
    }

    public List<EmailMemberListSummaryResponse> availableMembers() throws IOException, ResultFailException {
        return availableMembers(null);
    }

    public List<EmailMemberListSummaryResponse> availableMembers(Integer groupId) throws IOException, ResultFailException {
        return getList(subUrl + "add-member?groupId=" + (groupId != null ? groupId : ""), null, EmailMemberListSummaryResponse.class).getData();
    }
}
