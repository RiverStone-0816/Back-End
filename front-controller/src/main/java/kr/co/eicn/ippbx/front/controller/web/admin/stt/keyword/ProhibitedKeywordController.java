package kr.co.eicn.ippbx.front.controller.web.admin.stt.keyword;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.stt.keyword.ProhibitedKeywordApiInterface;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/stt/keyword/prohibited-keyword")
public class ProhibitedKeywordController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ProhibitedKeywordController.class);

    private final ProhibitedKeywordApiInterface apiInterface;

    @GetMapping
    public String list(Model model) throws IOException, ResultFailException {
        model.addAttribute("list", apiInterface.getList());
        return "admin/stt/keyword/prohibited-keyword/ground";
    }
}
