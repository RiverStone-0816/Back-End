package kr.co.eicn.ippbx.front.service.api.record.file;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RecordEnc;
import kr.co.eicn.ippbx.server.model.dto.eicn.RecordEncKeyResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.RecordEncKeySummaryResponse;
import kr.co.eicn.ippbx.server.model.form.RecordEncFormRequest;
import kr.co.eicn.ippbx.server.model.form.RecordEncKeyFormRequest;
import kr.co.eicn.ippbx.server.model.search.RecordEncSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RecordFileEncApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(RecordFileEncApiInterface.class);

    private static final String subUrl = "/api/v1/admin/record/file/enc/";

    public RecordEnc getCurrentEncryptionType() throws IOException, ResultFailException {
        return getData(subUrl, null, RecordEnc.class).getData();
    }

    public void updateCurrentEncryptionType(RecordEncFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public Pagination<RecordEncKeySummaryResponse> pagination(RecordEncSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl + "key", search, RecordEncKeySummaryResponse.class).getData();
    }

    public RecordEncKeyResponse get(Integer id) throws IOException, ResultFailException {
        return getData(subUrl + "key/" + id, null, RecordEncKeyResponse.class).getData();
    }

    public Integer registerKey(RecordEncKeyFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl + "key", form, Integer.class, false).getData();
    }

    public void updateKey(Integer id, RecordEncKeyFormRequest form) throws IOException, ResultFailException {
        put(subUrl + "key/" + id, form);
    }

    public void deleteByKey(Integer id) throws IOException, ResultFailException {
        delete(subUrl + "key/" + id);
    }
}
