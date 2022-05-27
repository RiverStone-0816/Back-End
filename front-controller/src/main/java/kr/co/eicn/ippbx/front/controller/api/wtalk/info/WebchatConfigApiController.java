package kr.co.eicn.ippbx.front.controller.api.wtalk.info;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.front.service.api.WebchatConfigApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatServiceInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatServiceSummaryInfoResponse;
import kr.co.eicn.ippbx.model.form.WebchatServiceInfoFormRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping(value = "api/chat-service", produces = MediaType.APPLICATION_JSON_VALUE)
public class WebchatConfigApiController extends BaseController {

    private final WebchatConfigApiInterface apiInterface;

    @SneakyThrows
    @GetMapping("")
    public List<WebchatServiceSummaryInfoResponse> list() {
        return apiInterface.list();
    }

    @SneakyThrows
    @GetMapping("{seq}")
    public WebchatServiceInfoResponse get(@PathVariable Integer seq) {
        return apiInterface.get(seq);
    }

    @SneakyThrows
    @PostMapping("")
    public Integer post(@Valid @RequestBody WebchatServiceInfoFormRequest form, BindingResult bindingResult) {
        return apiInterface.post(form);
    }

    @SneakyThrows
    @PutMapping("{seq}")
    public void update(@PathVariable Integer seq, @Valid @RequestBody WebchatServiceInfoFormRequest form, BindingResult bindingResult) {
         apiInterface.update(seq, form);
    }

    @SneakyThrows
    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) {
        apiInterface.delete(seq);
    }

    @SneakyThrows
    @PostMapping("image")
    public String uploadImage(@Valid @RequestBody FileForm form, BindingResult bindingResult) {
        return apiInterface.uploadImage(form, g.getUser().getCompanyId());
    }

}
