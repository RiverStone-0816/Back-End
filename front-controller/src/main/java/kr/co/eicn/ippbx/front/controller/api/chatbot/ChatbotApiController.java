package kr.co.eicn.ippbx.front.controller.api.chatbot;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.front.service.api.ChatbotApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.form.WebchatFormBlocKFormRequest;
import kr.co.eicn.ippbx.model.form.WebchatBotFallbackFormRequest;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.model.search.ChatbotSearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
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
    public List<WebchatBotSummaryInfoResponse> list() {
        return apiInterface.list();
    }

    @SneakyThrows
    @GetMapping("pagination")
    public Pagination<WebchatBotSummaryInfoResponse> pagination(ChatbotSearchRequest request) {
        return apiInterface.pagination(request);
    }

    @SneakyThrows
    @GetMapping("{id}")
    public WebchatBotInfoResponse getById(@PathVariable Integer id) {
        return apiInterface.getBotInfo(id);
    }

    @SneakyThrows
    @GetMapping("{id}/fallback")
    public WebchatBotFallbackInfoResponse getBotFallbackInfo(@PathVariable Integer id) {
        return apiInterface.getBotFallbackInfo(id);
    }

    @SneakyThrows
    @GetMapping("blocks/{blockId}")
    public WebchatBotInfoResponse.BlockInfo getByBlockId(@PathVariable Integer blockId) {
        return apiInterface.getBlock(blockId);
    }

    @GetMapping("auth-blocks")
    public List<WebchatBotFormBlockInfoResponse> getAuthBlockList() throws IOException, ResultFailException {
        return apiInterface.getFormBlockList();
    }

    @PostMapping("auth-block")
    public Integer addAuthBlock(@Valid @RequestBody WebchatFormBlocKFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.addFormBlock(form);
    }

    @PutMapping("auth-block/{formBlockId}")
    public void updateAuthBlock(@PathVariable Integer formBlockId, @Valid @RequestBody WebchatFormBlocKFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.updateFormBlock(formBlockId, form);
    }

    @DeleteMapping("auth-block/{formBlockId}")
    public void deleteAuthBlock(@PathVariable Integer formBlockId) throws IOException, ResultFailException {
        apiInterface.deleteFormBlock(formBlockId);
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
    @PutMapping("{id}/all")
    public void update(@PathVariable Integer id, @Valid @RequestBody WebchatBotFormRequest form, BindingResult bindingResult) {
        apiInterface.update(id, form);
    }

    @SneakyThrows
    @PutMapping("{id}")
    public void updateBotInfo(@PathVariable Integer id, @Valid @RequestBody WebchatBotFormRequest form, BindingResult bindingResult) {
        apiInterface.updateBotInfo(id, form);
    }

    @SneakyThrows
    @PutMapping("{id}/fallback")
    public void fallbackUpdate(@PathVariable Integer id, @Valid @RequestBody WebchatBotFallbackFormRequest form, BindingResult bindingResult) {
        apiInterface.fallbackUpdate(id, form);
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
