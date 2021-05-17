package kr.co.eicn.ippbx.front.service.api.record.evaluation;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.entity.eicn.EvaluationResultEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.EvaluationResultStatResponse;
import kr.co.eicn.ippbx.server.model.form.DisputeEvaluationFormRequest;
import kr.co.eicn.ippbx.server.model.form.EvaluationResultFormRequest;
import kr.co.eicn.ippbx.server.model.search.EvaluationResultSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EvaluationResultApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(EvaluationResultApiInterface.class);
    private static final String subUrl = "/api/v1/admin/record/evaluation/result/";

    public Pagination<EvaluationResultEntity> pagination(EvaluationResultSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, EvaluationResultEntity.class).getData();
    }

    public EvaluationResultEntity get(Integer id) throws IOException, ResultFailException {
        return getData(subUrl + id, null, EvaluationResultEntity.class).getData();
    }

    public List<EvaluationResultStatResponse> statistics(EvaluationResultSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "stat", search, EvaluationResultStatResponse.class).getData();
    }

    public List<EvaluationResultEntity> evaluations(String targetUserId, Long evaluationId) throws IOException, ResultFailException {
        return getList(subUrl + targetUserId + "/" + evaluationId, null, EvaluationResultEntity.class).getData();
    }

    public void evaluationRegister(EvaluationResultFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void delete(Integer id) throws IOException, ResultFailException {
        delete(subUrl + id);
    }

    public void complete(Integer id, EvaluationResultFormRequest form) throws IOException, ResultFailException {
        put(subUrl + id + "/complete", form);
    }

    public void dispute(Integer id, DisputeEvaluationFormRequest form) throws IOException, ResultFailException {
        put(subUrl + id + "/dispute", form);
    }

    public void confirm(Integer id) throws IOException, ResultFailException {
        put(subUrl + id + "/confirm", null);
    }
}
