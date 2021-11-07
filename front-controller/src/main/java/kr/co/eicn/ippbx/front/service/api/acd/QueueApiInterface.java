package kr.co.eicn.ippbx.front.service.api.acd;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.form.QueueFormRequest;
import kr.co.eicn.ippbx.model.form.QueueFormUpdateRequest;
import kr.co.eicn.ippbx.model.form.QueueUpdateBlendingFormRequest;
import kr.co.eicn.ippbx.model.search.QueueSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class QueueApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(QueueApiInterface.class);

    private static final String subUrl = "/api/v1/admin/user/user/queue/";

    public Pagination<QueueSummaryResponse> pagination(QueueSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, QueueSummaryResponse.class).getData();
    }

    public QueueDetailResponse get(String name) throws IOException, ResultFailException {
        return getData(subUrl + name, null, QueueDetailResponse.class).getData();
    }

    public String post(QueueFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, String.class, false).getData();
    }

    public void update(String name, QueueFormUpdateRequest form) throws IOException, ResultFailException {
        put(subUrl + name, form);
    }

    public void delete(String name) throws IOException, ResultFailException {
        super.delete(subUrl + name);
    }

    public void updateBlending(String name, QueueUpdateBlendingFormRequest request) throws IOException, ResultFailException {
        patch(subUrl + name + "/blending", request);
    }

    public List<ServiceList> services() throws IOException, ResultFailException {
        return services("");
    }

    public List<SummaryQueueResponse> subGroups() throws IOException, ResultFailException {
        return subGroups("");
    }

    public List<SummaryPersonResponse> addOnPersons() throws IOException, ResultFailException {
        return addOnPersons("");
    }

    public List<ServiceList> services(String host) throws IOException, ResultFailException {
        return getList(subUrl + "services", Collections.singletonMap("host", host), ServiceList.class).getData();
    }

    public List<SummaryQueueResponse> subGroups(String name/*queue_name.name*/) throws IOException, ResultFailException {
        return getList(subUrl + "sub-groups", Collections.singletonMap("name", name), SummaryQueueResponse.class).getData();
    }

    public List<SummaryPersonResponse> addOnPersons(String name) throws IOException, ResultFailException {
        return getList(subUrl + "add-on-persons", Collections.singletonMap("name", name), SummaryPersonResponse.class).getData();
    }

    public List<SummaryMohListResponse> ringBackTone() throws IOException, ResultFailException {
        return getList(subUrl + "ring-back-tones", null, SummaryMohListResponse.class).getData();
    }

    public List<SummaryContextInfoResponse> context() throws IOException, ResultFailException {
        return getList(subUrl + "context", null, SummaryContextInfoResponse.class).getData();
    }

    public List<SummaryQueueResponse> addQueueNames() throws IOException, ResultFailException {
        return getList(subUrl + "add-queue", null, SummaryQueueResponse.class).getData();
    }

    public List<PDSQueuePersonResponse> getPdsQueuePersonStatus() throws IOException, ResultFailException {
        return getList(subUrl + "pds-queue-status", null, PDSQueuePersonResponse.class).getData();
    }
}
