package kr.co.eicn.ippbx.front.controller.api.chatbot;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.front.service.api.ChatbotApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryWebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotBlockSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author tinywind
 */
@Slf4j
@AllArgsConstructor
@LoginRequired
@RestController
@RequestMapping(value = "api/chatbot", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatbotApiController extends BaseController {

    private final ChatbotApiInterface apiInterface;

    @SneakyThrows
    @GetMapping("")
    public List<SummaryWebchatBotInfoResponse> list() {
        return apiInterface.list();
    }

    @SneakyThrows
    @GetMapping("{id}")
    public WebchatBotInfoResponse getById(@PathVariable Integer id) {
        return apiInterface.getBotInfo(id);
    }

    @SneakyThrows
    @GetMapping("blocks/{blockId}")
    public WebchatBotInfoResponse.BlockInfo getByBlockId(@PathVariable Integer blockId) {
        return apiInterface.getBlock(blockId);
    }

    @SneakyThrows
    @PostMapping("")
    public Integer post(@Valid @RequestBody WebchatBotFormRequest form, BindingResult bindingResult) {
        return apiInterface.post(form);
    }

    @SneakyThrows
    @GetMapping("blocks/template")
    public List<WebchatBotBlockSummaryResponse> getTemplateBlockList() {
        return apiInterface.getTemplateBlockList();
    }

    @SneakyThrows
    @PutMapping("{id}")
    public void update(@PathVariable Integer id, @Valid @RequestBody WebchatBotFormRequest form, BindingResult bindingResult) {
        apiInterface.update(id, form);
    }

    @SneakyThrows
    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        apiInterface.delete(id);
    }

    @SneakyThrows
    @PostMapping("{id}/copy")
    public Integer copy(@PathVariable Integer id) {
        return apiInterface.copy(id);
    }

    @SneakyThrows
    @PostMapping("image")
    public String uploadImage(@Valid @RequestBody FileForm form, BindingResult bindingResult) {
        return apiInterface.uploadImage(form, g.getUser().getCompanyId());
    }

}
