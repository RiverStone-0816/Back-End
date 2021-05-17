package kr.co.eicn.ippbx.front.controller.web.admin.email;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.email.EmailReceptionGroupApiInterface;
import kr.co.eicn.ippbx.front.util.ReflectionUtils;
import kr.co.eicn.ippbx.server.model.dto.eicn.EmailMemberListSummaryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.EmailMngSummaryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.EmailReceiveGroupDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.EmailReceiveGroupSummaryResponse;
import kr.co.eicn.ippbx.server.model.form.EmailReceiveGroupFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/email/reception-group")
public class EmailReceptionGroupController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(EmailReceptionGroupController.class);

    @Autowired
    private EmailReceptionGroupApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<EmailReceiveGroupSummaryResponse> list = apiInterface.list();
        model.addAttribute("list", list);

        return "admin/email/reception-group/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") EmailReceiveGroupFormRequest form) throws IOException, ResultFailException {
        final Map<String, String> addOnPersons = new HashMap<>();
        apiInterface.availableMembers().forEach(e -> addOnPersons.put(e.getId(), e.getIdName()));
        model.addAttribute("addOnPersons", addOnPersons);
        final Map<Integer, String> services = apiInterface.services().stream().collect(Collectors.toMap(EmailMngSummaryResponse::getSeq, EmailMngSummaryResponse::getServiceName));
        model.addAttribute("services", services);

        return "admin/email/reception-group/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") EmailReceiveGroupFormRequest form) throws IOException, ResultFailException {
        final EmailReceiveGroupDetailResponse entity = apiInterface.get(seq);
        ReflectionUtils.copy(form, entity);
        model.addAttribute("entity", entity);

        final Map<String, String> addOnPersons = new HashMap<>();
        apiInterface.availableMembers(entity.getGroupId()).forEach(e -> addOnPersons.put(e.getId(), e.getIdName()));
        model.addAttribute("addOnPersons", addOnPersons);
        final Map<Integer, String> services = apiInterface.services().stream().collect(Collectors.toMap(EmailMngSummaryResponse::getSeq, EmailMngSummaryResponse::getServiceName));
        model.addAttribute("services", services);

        if (entity.getServiceId() != null && !services.containsKey(entity.getServiceId()))
            services.put(entity.getServiceId(), entity.getServiceName());

        for (EmailMemberListSummaryResponse person : entity.getEmailMemberLists())
            addOnPersons.remove(person.getId());

        return "admin/email/reception-group/modal";
    }
}
