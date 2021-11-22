package kr.co.eicn.ippbx.front.controller.web.admin.talk;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author tinywind
 */
@Slf4j
@LoginRequired
@Controller
@RequestMapping("admin/talk/chat-bot")
public class ChatBotController extends BaseController {

    @SneakyThrows
    @GetMapping("")
    public String page(Model model) {
        return "admin/talk/chat-bot/ground";
    }

    @SneakyThrows
    @GetMapping("{id}/modal-test")
    public String modal(Model model, @PathVariable Long id) {
        return "admin/talk/chat-bot/modal-test";
    }
}
