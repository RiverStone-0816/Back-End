package kr.co.eicn.ippbx.front.service.api.conference;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.ConfRoomDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ConfRoomSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryNumber070Response;
import kr.co.eicn.ippbx.model.form.ConfRoomFormRequest;
import kr.co.eicn.ippbx.model.search.ConfRoomSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ConferenceRoomApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(ConferenceRoomApiInterface.class);
    private static final String subUrl = "/api/v1/admin/conf/room/";

    public Pagination<ConfRoomSummaryResponse> pagination(ConfRoomSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, ConfRoomSummaryResponse.class).getData();
    }

    public ConfRoomDetailResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, ConfRoomDetailResponse.class).getData();
    }

    public void post(ConfRoomFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void put(Integer seq, ConfRoomFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }

    public List<SummaryNumber070Response> getConfRoomNumber() throws IOException, ResultFailException {
        return getList(subUrl + "unused-confroom-number", null, SummaryNumber070Response.class).getData();
    }
}
