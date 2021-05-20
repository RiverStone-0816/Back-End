package kr.co.eicn.ippbx.front.controller.web.admin.user.tel;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.user.tel.NumberApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.tel.ServiceApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.ServiceListDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ServiceListSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryNumber070Response;
import kr.co.eicn.ippbx.model.form.ServiceListFormRequest;
import kr.co.eicn.ippbx.model.search.ServiceListSearchRequest;
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
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/user/tel/service")
public class ServiceController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    private ServiceApiInterface apiInterface;
    @Autowired
    private NumberApiInterface numberApiInterface;
    @Autowired
    private ServiceApiInterface serviceApiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        List<ServiceListSummaryResponse> list = apiInterface.list(new ServiceListSearchRequest());

        model.addAttribute("list", list);

        return "admin/user/tel/service/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") ServiceListFormRequest form) throws IOException, ResultFailException {
        final List<SummaryNumber070Response> numbers = numberApiInterface.typeNumbers((byte)2, "");
        final ServiceListSearchRequest serviceSearch = new ServiceListSearchRequest();
        final List<ServiceListSummaryResponse> services = serviceApiInterface.list(serviceSearch);
        model.addAttribute("numbers", numbers.stream().filter(e -> services.stream().noneMatch(service -> service.getSvcNumber().equals(e.getNumber()))).collect(toList()));

        return "admin/user/tel/service/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") ServiceListFormRequest form) throws IOException, ResultFailException {
        final ServiceListDetailResponse entity = apiInterface.get(seq);
        ReflectionUtils.copy(form, entity);

        model.addAttribute("entity", entity);

        return modal(model, form);
    }
}
