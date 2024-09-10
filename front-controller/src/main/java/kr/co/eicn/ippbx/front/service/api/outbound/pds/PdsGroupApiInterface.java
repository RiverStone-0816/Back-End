package kr.co.eicn.ippbx.front.service.api.outbound.pds;

import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchOutboundNumberResponse;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.form.PDSExecuteFormRequest;
import kr.co.eicn.ippbx.model.form.PDSGroupFormRequest;
import kr.co.eicn.ippbx.model.search.PDSGroupSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class PdsGroupApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(PdsGroupApiInterface.class);
    private static final String subUrl = "/api/v1/admin/outbound/pds/group/";

    public List<PDSGroupSummaryResponse> list(PDSGroupSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "search", search, PDSGroupSummaryResponse.class).getData();
    }

    public Pagination<PDSGroupSummaryResponse> pagination(PDSGroupSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, PDSGroupSummaryResponse.class).getData();
    }

    public PDSGroupDetailResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, PDSGroupDetailResponse.class).getData();
    }

    public void post(PDSGroupFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void put(Integer seq, PDSGroupFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }

    public void executeRequest(Integer seq, PDSExecuteFormRequest form) throws IOException, ResultFailException {
        post(subUrl + seq + "/execute", form);
    }

    public List<SummaryCommonTypeResponse> addCommonTypeLists(String kind) throws IOException, ResultFailException {
        return getList(subUrl + "add-common-type", Collections.singletonMap("kind", kind), SummaryCommonTypeResponse.class).getData();
    }

    public List<SummaryCompanyServerResponse> addServerLists() throws IOException, ResultFailException {
        return getList(subUrl + "add-server", null, SummaryCompanyServerResponse.class).getData();
    }

    public List<SearchOutboundNumberResponse> addRidNumberLists() throws IOException, ResultFailException {
        return getList(subUrl + "add-rid-numbers", null, SearchOutboundNumberResponse.class).getData();
    }

    public List<SummaryNumber070Response> addNumberLists() throws IOException, ResultFailException {
        return getList(subUrl + "add-numbers", null, SummaryNumber070Response.class).getData();
    }

    public List<SummaryPDSQueueNameResponse> addPDSQueueNameLists() throws IOException, ResultFailException {
        return getList(subUrl + "add-pds-queue", null, SummaryPDSQueueNameResponse.class).getData();
    }

    public List<SummaryIvrTreeResponse> addPDSIvrLists() throws IOException, ResultFailException {
        return getList(subUrl + "add-pds-ivr", null, SummaryIvrTreeResponse.class).getData();
    }

    public List<SummaryCommonTypeResponse> addConsultationResult() throws IOException, ResultFailException {
        return getList(subUrl + "add-consultation-result", null, SummaryCommonTypeResponse.class).getData();
    }

    public List<SummaryResearchListResponse> addResearchLists() throws IOException, ResultFailException {
        return getList(subUrl + "add-research", null, SummaryResearchListResponse.class).getData();
    }

    public List<SummaryCommonFieldResponse> addCommonFieldLists() throws IOException, ResultFailException {
        return getList(subUrl + "add-field", null, SummaryCommonFieldResponse.class).getData();
    }

    public void postFieldsByExcel(Integer seq, FileForm form) throws IOException, ResultFailException {
        sendByMultipartFile(HttpMethod.POST, subUrl + seq + "/fields/by-excel", form, Collections.singletonMap("file", new FileResource(form.getFilePath(), form.getOriginalName())));
    }
}
