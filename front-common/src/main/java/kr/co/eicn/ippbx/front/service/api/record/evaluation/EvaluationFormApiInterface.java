package kr.co.eicn.ippbx.front.service.api.record.evaluation;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationForm;
import kr.co.eicn.ippbx.model.entity.eicn.EvaluationCategoryEntity;
import kr.co.eicn.ippbx.model.entity.eicn.EvaluationFormEntity;
import kr.co.eicn.ippbx.model.form.EvaluationFormRequest;
import kr.co.eicn.ippbx.model.form.EvaluationFormVisibleRequest;
import kr.co.eicn.ippbx.model.search.EvaluationFormSearchRequest;
import kr.co.eicn.ippbx.util.page.PageForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EvaluationFormApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(EvaluationFormApiInterface.class);
    private static final String subUrl = "/api/v1/admin/record/evaluation/form/";


    public Pagination<EvaluationForm> pagination(PageForm search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, EvaluationForm.class).getData();
    }

    public List<EvaluationCategoryEntity> getCategories(Long id) throws IOException, ResultFailException {
        return getList(subUrl + id + "/category", null, EvaluationCategoryEntity.class).getData();
    }

    public EvaluationFormEntity get(Long id) throws IOException, ResultFailException {
        return getData(subUrl + id, null, EvaluationFormEntity.class).getData();
    }

    public Long post(EvaluationFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, Long.class, false).getData();
    }

    public void put(Long id, EvaluationFormRequest form) throws IOException, ResultFailException {
        put(subUrl + id, form);
    }

    public void hiddenUpdate(List<EvaluationFormVisibleRequest> ids) throws IOException, ResultFailException {
        put(subUrl + "hidden", ids);
    }

    public void copy(Long id, EvaluationFormRequest form) throws IOException, ResultFailException {
        post(subUrl + id + "/copy", form);
    }

    public void delete(Long id) throws IOException, ResultFailException {
        delete(subUrl + id);
    }

    public List<EvaluationForm> search(EvaluationFormSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "search", search, EvaluationForm.class).getData();
    }

    public void hide(Long id) throws IOException, ResultFailException {
        put(subUrl + id + "/hide", null);
    }

    public void show(Long id) throws IOException, ResultFailException {
        put(subUrl + id + "/show", null);
    }
}
