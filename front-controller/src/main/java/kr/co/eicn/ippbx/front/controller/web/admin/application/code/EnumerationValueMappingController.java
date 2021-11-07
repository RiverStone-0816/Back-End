package kr.co.eicn.ippbx.front.controller.web.admin.application.code;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.code.EnumerationValueMappingApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.ConCodeResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ConGroupResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/application/code/enumeration-value-mapping")
public class EnumerationValueMappingController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(EnumerationValueMappingController.class);

    @Autowired
    private EnumerationValueMappingApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<ConCodeResponse> list = apiInterface.list();
        model.addAttribute("list", list);

        final Map<Integer, String> conGroupList = apiInterface.getConGroupList().stream().collect(Collectors.toMap(ConGroupResponse::getSeq, ConGroupResponse::getName));
        model.addAttribute("conGroupList", conGroupList);

        return "admin/application/code/enumeration-value-mapping/ground";
    }
}
