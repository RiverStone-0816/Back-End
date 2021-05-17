package kr.co.eicn.ippbx.front.controller.web.admin.record.evaluation;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.record.evaluation.EvaluationFormApiInterface;
import kr.co.eicn.ippbx.front.util.FormUtils;
import kr.co.eicn.ippbx.front.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.jooq.eicn.enums.EvaluationFormUseType;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.EvaluationForm;
import kr.co.eicn.ippbx.server.model.entity.eicn.EvaluationCategoryEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.EvaluationFormEntity;
import kr.co.eicn.ippbx.server.model.form.EvaluationFormRequest;
import kr.co.eicn.ippbx.server.util.page.PageForm;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/record/evaluation/form")
public class EvaluationFormController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(EvaluationFormController.class);

    private final EvaluationFormApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") PageForm search) throws IOException, ResultFailException {
        final Pagination<EvaluationForm> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        return "admin/record/evaluation/form/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") EvaluationFormRequest form, @RequestParam(required = false) Long copyingTargetId) throws IOException, ResultFailException {
        final Map<String, String> useTypes = FormUtils.options(EvaluationFormUseType.class);
        model.addAttribute("useTypes", useTypes);

        if (copyingTargetId != null) {
            final EvaluationFormEntity entity = apiInterface.get(copyingTargetId);
            ReflectionUtils.copy(form, entity);
            model.addAttribute("copyingEntity", entity);
        }

        return "admin/record/evaluation/form/modal";
    }

    @GetMapping("{id}/modal")
    public String modal(Model model, @PathVariable Long id, @ModelAttribute("form") EvaluationFormRequest form) throws IOException, ResultFailException {
        final EvaluationFormEntity entity = apiInterface.get(id);
        ReflectionUtils.copy(form, entity);

        model.addAttribute("entity", entity);

        return modal(model, form, null);
    }

    @GetMapping("{id}/modal-preview")
    public String modalPreview(Model model, @PathVariable Long id) throws IOException, ResultFailException {
        final String memo = apiInterface.get(id).getMemo();
        if (memo != null) {
            model.addAttribute("memo", memo);
        }
        model.addAttribute("id", id);

        return "admin/record/evaluation/form/modal-preview";
    }

    @GetMapping("{id}/form")
    public String form(Model model, @PathVariable Long id) throws IOException, ResultFailException {
        final List<EvaluationCategoryEntity> categories = apiInterface.getCategories(id);
        model.addAttribute("categories", categories);

        return "admin/record/evaluation/form/form";
    }
}
