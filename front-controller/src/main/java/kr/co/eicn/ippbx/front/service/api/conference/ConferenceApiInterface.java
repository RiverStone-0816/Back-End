package kr.co.eicn.ippbx.front.service.api.conference;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.form.ConfInfoDuplicateFormRequest;
import kr.co.eicn.ippbx.model.form.ConfInfoFormRequest;
import kr.co.eicn.ippbx.model.form.ConfInfoMinutesSaveFormRequest;
import kr.co.eicn.ippbx.model.form.ConfInfoUpdateFormRequest;
import kr.co.eicn.ippbx.model.search.ConfInfoSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class ConferenceApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(ConferenceApiInterface.class);
    private static final String subUrl = "/api/v1/admin/conf/info/";

    public List<ConfRoomSummaryResponse> getConfRoomList(/*ConfRoomSearchRequest search*/) throws IOException, ResultFailException {
        return getList(subUrl + "confroom-list", null, ConfRoomSummaryResponse.class).getData();
    }

    public List<ConfInfoSummaryResponse> list(ConfInfoSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, ConfInfoSummaryResponse.class).getData();
    }

    public List<SummaryPersonResponse> addOnConfPersons(Integer seq) throws IOException, ResultFailException {
        return getList(subUrl + "add_on_conf_persons", Collections.singletonMap("seq", seq), SummaryPersonResponse.class).getData();
    }

    public List<SummarySoundListResponse> addSoundList() throws IOException, ResultFailException {
        return getList(subUrl + "add-sounds-list", null, SummarySoundListResponse.class).getData();
    }

    public ConfInfoDetailResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, ConfInfoDetailResponse.class).getData();
    }

    public Integer post(ConfInfoFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    public void put(Integer seq, ConfInfoFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void update(Integer seq, ConfInfoUpdateFormRequest form) throws IOException, ResultFailException {
        put(subUrl + "update/" + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }

    public void duplicate(ConfInfoDuplicateFormRequest form) throws IOException, ResultFailException {
        post(subUrl + "duplicate", form);
    }

    public ConfInfoMinutesResponse minutes(Integer confInfoId) throws IOException, ResultFailException {
        return getData(subUrl + "minutes/" + confInfoId, null, ConfInfoMinutesResponse.class).getData();
    }

    public void minutesSave(ConfInfoMinutesSaveFormRequest form) throws IOException, ResultFailException {
        post(subUrl + "minutes-save", form);
    }
}
