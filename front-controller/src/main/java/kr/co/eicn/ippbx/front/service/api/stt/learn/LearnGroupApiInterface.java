package kr.co.eicn.ippbx.front.service.api.stt.learn;


import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.LearnGroupResponse;
import kr.co.eicn.ippbx.model.form.LearnGroupFormRequest;
import kr.co.eicn.ippbx.model.search.LearnGroupSearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.page.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class LearnGroupApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(LearnGroupApiInterface.class);
    private static final String subUrl = "/api/v1/admin/stt/learn/group/";

    public Pagination<LearnGroupResponse> pagination(LearnGroupSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, LearnGroupResponse.class).getData();
    }

    public LearnGroupResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, LearnGroupResponse.class).getData();
    }

    public Integer post(LearnGroupFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    public void put(Integer seq, LearnGroupFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public List<LearnGroupResponse> learnGroup() throws IOException, ResultFailException {
        return getList(subUrl + "learn-group", null, LearnGroupResponse.class).getData();
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }

    public void statusUpdate(Integer seq) throws IOException, ResultFailException {
        put(subUrl + "status/" + seq, null);
    }

}
