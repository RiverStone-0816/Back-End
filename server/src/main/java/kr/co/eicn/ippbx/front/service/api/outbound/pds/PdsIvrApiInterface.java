package kr.co.eicn.ippbx.front.service.api.outbound.pds;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.*;
import kr.co.eicn.ippbx.server.model.form.PDSIvrFormRequest;
import kr.co.eicn.ippbx.server.model.form.PDSIvrFormUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PdsIvrApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(PdsIvrApiInterface.class);
    private static final String subUrl = "/api/v1/admin/outbound/pds/ivr/";

    public List<PDSIvrResponse> list() throws IOException, ResultFailException {
        return getList(subUrl, null, PDSIvrResponse.class).getData();
    }

    public PDSIvrDetailResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, PDSIvrDetailResponse.class).getData();
    }

    public void post(PDSIvrFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void put(Integer code, PDSIvrFormUpdateRequest form) throws IOException, ResultFailException {
        put(subUrl + code, form);
    }

    public void delete(Integer code) throws IOException, ResultFailException {
        delete(subUrl + code);
    }

    public List<SummaryIvrTreeResponse> rootNodes() throws IOException, ResultFailException {
        return getList(subUrl + "root-node", null, SummaryIvrTreeResponse.class).getData();
    }

    public List<SummaryPDSQueueNameResponse> addPdsQueueNames() throws IOException, ResultFailException {
        return getList(subUrl + "add-pds-queue", null, SummaryPDSQueueNameResponse.class).getData();
    }
}
