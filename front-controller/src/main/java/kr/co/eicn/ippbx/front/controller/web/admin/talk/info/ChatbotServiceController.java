package kr.co.eicn.ippbx.front.controller.web.admin.talk.info;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.WebchatConfigApiInterface;
import kr.co.eicn.ippbx.model.form.WebchatServiceInfoFormRequest;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

/**
 * @author tinywind
 */
@Slf4j
@RequiredArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/talk/info/chat-service/")
public class ChatbotServiceController extends BaseController {
    private final WebchatConfigApiInterface apiInterface;

    @SneakyThrows
    @GetMapping("")
    public String page(Model model) {
        val list = apiInterface.list();
        model.addAttribute("list", list);

        return "admin/talk/info/chat-service/ground";
    }

    @SneakyThrows
    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") WebchatServiceInfoFormRequest form) {
        return "admin/talk/info/chat-service/modal";
    }

    @SneakyThrows
    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") WebchatServiceInfoFormRequest form) {
        val entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);
        form.setEnableChat(Objects.equals(entity.getEnableChat(), "Y"));
        form.setImage(entity.getImageFileName());
        form.setProfile(entity.getProfileFileName());

        return modal(model, form);
    }
}
