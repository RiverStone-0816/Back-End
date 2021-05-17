package kr.co.eicn.ippbx.front.service.api.user.user;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.PersonSummaryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryPhoneInfoResponse;
import kr.co.eicn.ippbx.server.model.form.PersonFormRequest;
import kr.co.eicn.ippbx.server.model.form.PersonFormUpdateRequest;
import kr.co.eicn.ippbx.server.model.form.PersonPasswordUpdateRequest;
import kr.co.eicn.ippbx.server.model.search.PersonSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class UserApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(UserApiInterface.class);

    private static final String subUrl = "/api/v1/admin/user/user/";

    public List<PersonSummaryResponse> list(PersonSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, PersonSummaryResponse.class).getData();
    }

    public Pagination<PersonSummaryResponse> pagination(PersonSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl + "search", search, PersonSummaryResponse.class).getData();
    }

    public PersonDetailResponse get(String id) throws IOException, ResultFailException {
        return getData(subUrl + id, null, PersonDetailResponse.class).getData();
    }

    public void post(PersonFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void update(String id, PersonFormUpdateRequest form) throws IOException, ResultFailException {
        put(subUrl + id, form);
    }

    public void delete(String id) throws IOException, ResultFailException {
        super.delete(subUrl + id);
    }

    public void post(List<PersonFormRequest> form) throws IOException, ResultFailException {
        post(subUrl + "data-upload", form);
    }

    public void updatePassword(String id, PersonPasswordUpdateRequest form) throws IOException, ResultFailException {
        patch(subUrl + id + "/password", form);
    }

    public void isIdAvailable(String id) throws IOException, ResultFailException {
        getResult(subUrl + id + "/duplicate", null);
    }

    public List<SummaryPhoneInfoResponse> extensions() throws IOException, ResultFailException {
        return extensions("");
    }

    public List<SummaryPhoneInfoResponse> extensions(String extension) throws IOException, ResultFailException {
        final Map<String, String> param = Collections.singletonMap("extension", extension);
        return getList(subUrl + "extensions", param, SummaryPhoneInfoResponse.class).getData();
    }
}
