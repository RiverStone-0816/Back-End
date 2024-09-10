package kr.co.eicn.ippbx.front.controller.web.admin.outbound;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.type.OutboundCommonCodeApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.CommonFieldResponse;
import kr.co.eicn.ippbx.model.dto.eicn.RelatedFieldResponse;
import kr.co.eicn.ippbx.model.form.CommonCodeUpdateFormRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@Slf4j
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/outbound/type/code")
public class OutboundCodeController extends BaseController {

    private final OutboundCommonCodeApiInterface apiInterface;

    @GetMapping("modal-field")
    public String modal(Model model, @ModelAttribute("form") CommonCodeUpdateFormRequest form, @RequestParam Integer type, @RequestParam String fieldId) throws IOException, ResultFailException {
        final CommonFieldResponse entity = apiInterface.getField(type, fieldId);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        final Map<String, String> relatedFields = apiInterface.getRelatedField(type, fieldId).stream().collect(Collectors.toMap(RelatedFieldResponse::getFieldId, RelatedFieldResponse::getFieldInfo));
        model.addAttribute("relatedFields", relatedFields);
        model.addAttribute("type", type);

        return "admin/outbound/type/code/modal-field";
    }

    @GetMapping("{seq}/{fieldId}/modal-upload")
    public String modalUpload(Model model, @PathVariable Integer seq, @ModelAttribute("form") FileForm form, @PathVariable String fieldId) throws IOException, ResultFailException {
        model.addAttribute("seq", seq);
        model.addAttribute("fieldId", fieldId);
        return "admin/outbound/type/code/modal-upload";
    }

}
