package kr.co.eicn.ippbx.front.controller.web.admin.wtalk;

import io.vavr.control.Option;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.ChatbotApiInterface;
import kr.co.eicn.ippbx.front.service.api.wtalk.group.WtalkReceptionGroupApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.form.WebchatBotFallbackFormRequest;
import kr.co.eicn.ippbx.model.search.ChatbotSearchRequest;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tinywind
 */
@Slf4j
@RequiredArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/wtalk/chat-bot")
public class ChatBotController extends BaseController {

    private final ChatbotApiInterface apiInterface;
    private final WtalkReceptionGroupApiInterface talkReceptionGroupApiInterface;

    @Value("${eicn.chatbot.resource.location}")
    private String resourceLocation;
    private File location;

    @PostConstruct
    public void setup() {
        location = new File(resourceLocation);
        location.mkdirs();
    }

    @SneakyThrows
    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") ChatbotSearchRequest search, @RequestParam(required = false) String ip, HttpSession session, HttpServletRequest request) throws IOException, ResultFailException {
        model.addAttribute("pagination", apiInterface.pagination(search));
        model.addAttribute("sessionId", session.getId());
        model.addAttribute("ip", Option.of(ip).getOrElse(Option.of(request.getHeader("X-FORWARDED-FOR")).getOrElse(request.getRemoteAddr())));

        return "admin/wtalk/chat-bot/ground";
    }

    @SneakyThrows
    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") WebchatBotFallbackFormRequest form) {
        model.addAttribute("talkReceptionGroups", talkReceptionGroupApiInterface.list());

        return "admin/wtalk/chat-bot/modal";
    }

    @SneakyThrows
    @GetMapping("{id}/modal")
    public String modal(@PathVariable Integer id, Model model, @ModelAttribute("form") WebchatBotFallbackFormRequest form) {
        model.addAttribute("id", id);
        val entity = apiInterface.getBotInfo(id);
        model.addAttribute("entity", entity);
        if (entity.getBlockInfo() != null) model.addAttribute("blocks", extractBlocks(entity.getBlockInfo()));

        ReflectionUtils.copy(form, entity);

        return modal(model, form);
    }

    private Map<Integer, String> extractBlocks(WebchatBotInfoResponse.BlockInfo block) {
        val map = new HashMap<Integer, String>();
        map.put(block.getId(), block.getName());

        if (block.getChildren() != null)
            for (WebchatBotInfoResponse.BlockInfo child : block.getChildren())
                map.putAll(extractBlocks(child));

        return map;
    }

    @SneakyThrows
    @GetMapping("editor")
    public String editor(Model model, @RequestParam(required = false) Integer id) {
        model.addAttribute("id", id);

        return "admin/wtalk/chat-bot/editor";
    }

    @SneakyThrows
    @GetMapping("{id}/modal-test")
    public String modal(Model model, @PathVariable Long id, @RequestParam(required = false) String ip, HttpSession session, HttpServletRequest request) {
        model.addAttribute("botId", id);
        model.addAttribute("sessionId", session.getId());
        model.addAttribute("ip", Option.of(ip).getOrElse(Option.of(request.getHeader("X-FORWARDED-FOR")).getOrElse(request.getRemoteAddr())));
        return "admin/wtalk/chat-bot/modal-test";
    }

    @SneakyThrows
    @GetMapping("image")
    public void getImage(HttpServletResponse response, @RequestParam String fileName) {
        val file = new File(location, fileName);
        if (!file.exists()) Files.copy(apiInterface.getImage(fileName).getInputStream(), file.toPath());

        response.setContentType("application/download; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8").replaceAll("[+]", "%20") + "\";");
        response.setHeader("Content-Transfer-Encoding", "BINARY");
        IOUtils.copy(new FileInputStream(file), response.getOutputStream());
    }

}