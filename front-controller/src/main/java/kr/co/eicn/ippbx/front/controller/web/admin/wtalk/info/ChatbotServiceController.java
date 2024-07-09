package kr.co.eicn.ippbx.front.controller.web.admin.wtalk.info;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.WebchatConfigApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkServerInfo;
import kr.co.eicn.ippbx.model.enums.IntroChannelType;
import kr.co.eicn.ippbx.model.form.WebchatServiceInfoFormRequest;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@Slf4j
@RequiredArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/wtalk/info/chat-service/")
public class ChatbotServiceController extends BaseController {
    private final WebchatConfigApiInterface apiInterface;
    private final SearchApiInterface searchApiInterface;

    @SneakyThrows
    @GetMapping("")
    public String page(Model model) {
        val list = apiInterface.list();
        model.addAttribute("list", list);
        final List<WtalkServerInfo> serverInfos = searchApiInterface.getWtalkServerList();
        model.addAttribute("server", CollectionUtils.isEmpty(serverInfos) ? null : serverInfos.get(0));

        return "admin/wtalk/info/chat-service/ground";
    }

    @SneakyThrows
    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") WebchatServiceInfoFormRequest form) {
        model.addAttribute("channelTypes", IntroChannelType.class.getEnumConstants());

        return "admin/wtalk/info/chat-service/modal";
    }

    @SneakyThrows
    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") WebchatServiceInfoFormRequest form) {
        val entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);
        form.setEnableChat(entity.getEnableChat());
        form.setImage(entity.getImageFileName());
        form.setProfile(entity.getProfileFileName());
        form.setIntroChannelList(entity.getIntroChannelList().stream().map(e -> new WebchatServiceInfoFormRequest.IntroChannel(e.getChannelId(), e.getChannelType())).collect(Collectors.toList()));

        return modal(model, form);
    }
}
