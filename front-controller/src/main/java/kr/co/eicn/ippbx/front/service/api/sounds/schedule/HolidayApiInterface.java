package kr.co.eicn.ippbx.front.service.api.sounds.schedule;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.HolyInfoResponse;
import kr.co.eicn.ippbx.model.form.HolyInfoFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class HolidayApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(HolidayApiInterface.class);
    private static final String subUrl = "/api/v1/admin/schedule/holy/";

    public List<HolyInfoResponse> list() throws IOException, ResultFailException {
        return getList(subUrl, null, HolyInfoResponse.class).getData();
    }

    public HolyInfoResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, HolyInfoResponse.class).getData();
    }

    public void post(HolyInfoFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void put(Integer seq, HolyInfoFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }
}
