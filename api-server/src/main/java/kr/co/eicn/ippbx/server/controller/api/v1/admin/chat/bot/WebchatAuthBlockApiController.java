package kr.co.eicn.ippbx.server.controller.api.v1.admin.chat.bot;

import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotAuthBlockInfoResponse;
import kr.co.eicn.ippbx.model.form.WebchatAuthBlocKFormRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.service.WebchatBotAuthBlockService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/chat/bot/auth-block/", produces = MediaType.APPLICATION_JSON_VALUE)
public class WebchatAuthBlockApiController extends ApiBaseController {
    private final WebchatBotAuthBlockService webchatBotAuthBlockService;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<WebchatBotAuthBlockInfoResponse>>> getAuthBlockList(@RequestParam(required = false) Integer botId) {
        List<WebchatBotAuthBlockInfoResponse> response = webchatBotAuthBlockService.findAllByBotId(botId);

        return ResponseEntity.ok(data(response));
    }

    @PostMapping("{botId}")
    public ResponseEntity<JsonResult<Void>> post(@PathVariable Integer botId, @RequestBody @Valid WebchatAuthBlocKFormRequest request, BindingResult bindingResult) {
        webchatBotAuthBlockService.insertAuthBlock(botId, request);
        return ResponseEntity.ok(create());
    }

    @PutMapping("{authBlockId}")
    public ResponseEntity<JsonResult<Void>> update(@PathVariable Integer authBlockId, @RequestBody @Valid WebchatAuthBlocKFormRequest request, BindingResult bindingResult) {
        webchatBotAuthBlockService.updateAuthBlock(authBlockId, request);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping("{authBlockId}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer authBlockId) {
        webchatBotAuthBlockService.deleteById(authBlockId);

        return ResponseEntity.ok(create());
    }
}
