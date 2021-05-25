package kr.co.eicn.ippbx.front.controller.web.admin.record.evaluation;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.record.evaluation.EvaluationFormApiInterface;
import kr.co.eicn.ippbx.front.service.api.record.evaluation.EvaluationItemApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationCategory;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationForm;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationItem;
import kr.co.eicn.ippbx.model.entity.eicn.EvaluationCategoryEntity;
import kr.co.eicn.ippbx.model.search.EvaluationFormSearchRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/record/evaluation/item")
public class EvaluationItemController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(EvaluationItemController.class);

    private final EvaluationItemApiInterface apiInterface;
    private final EvaluationFormApiInterface evaluationFormApiInterface;

    @GetMapping("")
    public String page(Model model, @RequestParam(required = false) Long formId) throws IOException, ResultFailException {
        final List<EvaluationForm> forms = evaluationFormApiInterface.search(new EvaluationFormSearchRequest());
        if (!forms.isEmpty()) {

            model.addAttribute("forms", forms.stream().collect(Collectors.toMap(EvaluationForm::getId, EvaluationForm::getName)));

            if (formId == null)
                formId = forms.get(0).getId();

            final Long finalFormId = formId;
            final Optional<EvaluationForm> formOptional = forms.stream().filter(e -> Objects.equals(e.getId(), finalFormId)).findFirst();
            if (!formOptional.isPresent())
                throw new IllegalArgumentException("존재하지 않는 평가지입니다: " + formId);

            model.addAttribute("formId", formId);

            final List<EvaluationCategoryEntity> categories = evaluationFormApiInterface.getCategories(formId);
            categories.sort(Comparator.comparingLong(EvaluationCategory::getId));
            categories.forEach(e -> e.items.sort(Comparator.comparingInt(EvaluationItem::getSequence)));

            model.addAttribute("categories", categories);
        }
        return "admin/record/evaluation/item/ground";
    }
}
