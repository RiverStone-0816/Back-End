package kr.co.eicn.ippbx.front.controller.web.admin.application.code;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.code.EnumerationValueApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.CommonFieldResponse;
import kr.co.eicn.ippbx.model.dto.eicn.CommonTypeDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.RelatedFieldResponse;
import kr.co.eicn.ippbx.model.form.CommonCodeUpdateFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/application/code/enumeration-value")
public class EnumerationValueController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(EnumerationValueController.class);

    @Autowired
    private EnumerationValueApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<CommonTypeDetailResponse> list = apiInterface.list().stream().filter(e -> !e.getType().equals("OUT")).collect(Collectors.toList());
        model.addAttribute("list", list);

        return "admin/application/code/enumeration-value/ground";
    }

    @GetMapping("modal-field")
    public String modal(Model model, @ModelAttribute("form") CommonCodeUpdateFormRequest form, @RequestParam Integer type, @RequestParam String fieldId) throws IOException, ResultFailException {
        final CommonFieldResponse entity = apiInterface.getField(type, fieldId);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        final Map<String, String> relatedFields = apiInterface.getRelatedField(type, fieldId).stream().collect(Collectors.toMap(RelatedFieldResponse::getFieldId, RelatedFieldResponse::getFieldInfo));
        model.addAttribute("relatedFields", relatedFields);

        model.addAttribute("type", type);

        return "admin/application/code/enumeration-value/modal-field";
    }
}
