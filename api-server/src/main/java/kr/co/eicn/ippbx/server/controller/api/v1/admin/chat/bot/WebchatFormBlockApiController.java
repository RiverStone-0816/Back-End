package kr.co.eicn.ippbx.server.controller.api.v1.admin.chat.bot;

import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotFormBlockInfoResponse;
import kr.co.eicn.ippbx.model.form.WebchatFormBlocKFormRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.service.WebchatBotFormBlockService;
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
public class WebchatFormBlockApiController extends ApiBaseController {
    private final WebchatBotFormBlockService webchatBotFormBlockService;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<WebchatBotFormBlockInfoResponse>>> getAuthBlockList() {
        List<WebchatBotFormBlockInfoResponse> response = webchatBotFormBlockService.getAll();

        return ResponseEntity.ok(data(response));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Integer>> post(@RequestBody @Valid WebchatFormBlocKFormRequest request, BindingResult bindingResult) {
        final Integer formBlockId = webchatBotFormBlockService.insertAuthBlock(request);
        return ResponseEntity.ok(data(formBlockId));
    }

    @PutMapping("{formBlockId}")
    public ResponseEntity<JsonResult<Void>> update(@PathVariable Integer formBlockId, @RequestBody @Valid WebchatFormBlocKFormRequest request, BindingResult bindingResult) {
        webchatBotFormBlockService.updateAuthBlock(formBlockId, request);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping("{formBlockId}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer formBlockId) {
        webchatBotFormBlockService.deleteById(formBlockId);

        return ResponseEntity.ok(create());
    }
}
