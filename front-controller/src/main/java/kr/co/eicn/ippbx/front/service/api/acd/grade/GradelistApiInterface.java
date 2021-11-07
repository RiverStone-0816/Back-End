package kr.co.eicn.ippbx.front.service.api.acd.grade;

import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchQueueResponse;
import kr.co.eicn.ippbx.model.entity.eicn.GradeListEntity;
import kr.co.eicn.ippbx.model.form.GradeListFormRequest;
import kr.co.eicn.ippbx.model.search.GradeListSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class GradelistApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(GradelistApiInterface.class);

    private static final String subUrl = "/api/v1/admin/acd/grade/gradelist/";

    public Pagination<GradeListEntity> pagination(GradeListSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, GradeListEntity.class).getData();
    }

    public GradeListEntity get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, GradeListEntity.class).getData();
    }

    public Integer post(GradeListFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    public void put(Integer seq, GradeListFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }

    public List<SearchQueueResponse> queues() throws IOException, ResultFailException {
        return getList(subUrl + "queue", null, SearchQueueResponse.class).getData();
    }

    public void postFieldsByExcel(FileForm form) throws IOException, ResultFailException {
        sendByMultipartFile(HttpMethod.POST, subUrl + "/fields/by-excel", form, Collections.singletonMap("file", new FileResource(form.getFilePath(), form.getOriginalName())));
    }
}
