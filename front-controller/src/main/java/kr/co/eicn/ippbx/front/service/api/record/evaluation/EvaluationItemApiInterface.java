package kr.co.eicn.ippbx.front.service.api.record.evaluation;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationItem;
import kr.co.eicn.ippbx.model.form.EvaluationWholeCategoryFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EvaluationItemApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(EvaluationItemApiInterface.class);
    private static final String subUrl = "/api/v1/admin/record/evaluation/item/";

    public List<EvaluationItem> list() throws IOException, ResultFailException {
        return getList(subUrl, null, EvaluationItem.class).getData();
    }

    public EvaluationItem get(Long id) throws IOException, ResultFailException {
        return getData(subUrl + id, null, EvaluationItem.class).getData();
    }

    public void post(Long evaluationId, EvaluationWholeCategoryFormRequest form) throws IOException, ResultFailException {
        post(subUrl + "target/" + evaluationId, form);
    }

    public List<EvaluationItem> search() throws IOException, ResultFailException {
        return getList(subUrl + "search", null, EvaluationItem.class).getData();
    }
}
