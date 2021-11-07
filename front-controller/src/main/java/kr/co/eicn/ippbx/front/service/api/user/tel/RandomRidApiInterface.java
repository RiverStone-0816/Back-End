package kr.co.eicn.ippbx.front.service.api.user.tel;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.RandomCidResponse;
import kr.co.eicn.ippbx.model.form.RandomCidFormRequest;
import kr.co.eicn.ippbx.model.search.RandomCidSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RandomRidApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(RandomRidApiInterface.class);

    private static final String subUrl = "/api/v1/admin/user/tel/random-rid/";

    public Pagination<RandomCidResponse> pagination(RandomCidSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, RandomCidResponse.class).getData();
    }

    public RandomCidResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, RandomCidResponse.class).getData();
    }

    public void register(RandomCidFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void update(Integer seq, RandomCidFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        super.delete(subUrl + seq);
    }
}
