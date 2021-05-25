package kr.co.eicn.ippbx.front.service.api.record.history;

import kr.co.eicn.ippbx.front.model.search.RecordCallSearchForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.RecordFile;
import kr.co.eicn.ippbx.model.dto.customdb.CommonEicnCdrResponse;
import kr.co.eicn.ippbx.model.form.RecordDownFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Service
public class RecordingHistoryApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(RecordingHistoryApiInterface.class);
    private static final String subUrl = "/api/v1/admin/record/history/";
    private static final Long ONE_SECOND = 1000L;
    private static final Long ONE_MINUTE = 60 * ONE_SECOND;
    private static final Long ONE_HOUR = 60 * ONE_MINUTE;
    @Value("${eicn.service.servicekind}")
    private String serviceKind;

    public List<CommonEicnCdrResponse> list(RecordCallSearchForm search) throws IOException, ResultFailException {
        if (search.getBatchDownloadMode()) {
            search.setEndDate(search.getStartDate());
            search.setEndHour(23);
        }

        if (search.getStartDate() != null && search.getStartHour() != null)
            search.setStartTimestamp(new Timestamp(search.getStartDate().getTime() + search.getStartHour() * ONE_HOUR));
        if (search.getEndDate() != null && search.getEndHour() != null)
            search.setEndTimestamp(new Timestamp(search.getEndDate().getTime() + (search.getEndHour() + 1) * ONE_HOUR - 1));

        if (serviceKind.equals("CC")) {
            if (search.getCallStartMinutes() != null || search.getCallStartSeconds() != null)
                search.setStartTime(new Time(
                        ((search.getCallStartMinutes() != null ? search.getCallStartMinutes() : 0) * ONE_MINUTE)
                                + ((search.getCallStartSeconds() != null ? search.getCallStartSeconds() : 0) * ONE_SECOND)
                ));

            if (search.getCallEndMinutes() != null || search.getCallEndSeconds() != null)
                search.setEndTime(new Time(
                        ((search.getCallEndMinutes() != null ? search.getCallEndMinutes() : 0) * ONE_MINUTE)
                                + ((search.getCallEndSeconds() != null ? search.getCallEndSeconds() : 0) * ONE_SECOND)
                ));
        }

        return getList(subUrl, search, CommonEicnCdrResponse.class).getData();
    }

    public Pagination<CommonEicnCdrResponse> pagination(RecordCallSearchForm search) throws IOException, ResultFailException {
        if (search.getBatchDownloadMode()) {
            search.setEndDate(search.getStartDate());
            search.setEndHour(23);
        }

        if (search.getStartDate() != null && search.getStartHour() != null)
            search.setStartTimestamp(new Timestamp(search.getStartDate().getTime() + search.getStartHour() * ONE_HOUR));
        if (search.getEndDate() != null && search.getEndHour() != null)
            search.setEndTimestamp(new Timestamp(search.getEndDate().getTime() + (search.getEndHour() + 1) * ONE_HOUR - 1));

        if (serviceKind.equals("CC")) {
            if (search.getCallStartMinutes() != null || search.getCallStartSeconds() != null)
                search.setStartTime(new Time(
                        ((search.getCallStartMinutes() != null ? search.getCallStartMinutes() : 0) * ONE_MINUTE)
                                + ((search.getCallStartSeconds() != null ? search.getCallStartSeconds() : 0) * ONE_SECOND)
                ));

            if (search.getCallEndMinutes() != null || search.getCallEndSeconds() != null)
                search.setEndTime(new Time(
                        ((search.getCallEndMinutes() != null ? search.getCallEndMinutes() : 0) * ONE_MINUTE)
                                + ((search.getCallEndSeconds() != null ? search.getCallEndSeconds() : 0) * ONE_SECOND)
                ));
        }

        return getPagination(subUrl + "search", search, CommonEicnCdrResponse.class).getData();
    }

    public CommonEicnCdrResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, CommonEicnCdrResponse.class).getData();
    }

    public List<RecordFile> getFiles(Integer seq) throws IOException, ResultFailException {
        return getList(subUrl + seq + "/record-files", null, RecordFile.class).getData();
    }

    public String recordInBatchesRegister(RecordDownFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl + "record-batch-download-register", form, String.class, false).getData();
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }
}
