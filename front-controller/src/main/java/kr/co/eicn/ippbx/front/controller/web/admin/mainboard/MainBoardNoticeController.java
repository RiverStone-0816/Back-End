package kr.co.eicn.ippbx.front.controller.web.admin.mainboard;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.MainBoardNoticeApiInterface;
import kr.co.eicn.ippbx.model.entity.eicn.MainBoardEntity;
import kr.co.eicn.ippbx.model.search.MainBoardRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.page.Pagination;
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

@LoginRequired
@Controller
@RequestMapping("admin/main-board/notice")
public class MainBoardNoticeController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MainBoardNoticeController.class);

    @Autowired
    private MainBoardNoticeApiInterface mainBoardNoticeApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") MainBoardRequest search) throws IOException, ResultFailException {
        final Pagination<MainBoardEntity> pagination = mainBoardNoticeApiInterface.page(search);
        final List<MainBoardEntity> topFix = mainBoardNoticeApiInterface.topFix();
        model.addAttribute("pagination", pagination);
        model.addAttribute("topFix", topFix);

        return "admin/mainboard/notice/ground";
    }

    @GetMapping("{id}")
    public String get(Model model, @PathVariable Long id) throws IOException, ResultFailException {
        final MainBoardEntity entity = mainBoardNoticeApiInterface.get(id);
        model.addAttribute("entity", entity);

        return "admin/mainboard/notice/modal";
    }
}
