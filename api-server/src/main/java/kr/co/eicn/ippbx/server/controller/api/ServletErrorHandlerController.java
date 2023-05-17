package kr.co.eicn.ippbx.server.controller.api;

import kr.co.eicn.ippbx.util.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static kr.co.eicn.ippbx.util.JsonResult.Result.failure;
import static kr.co.eicn.ippbx.util.JsonResult.create;

@Slf4j
@RestController
public class ServletErrorHandlerController extends AbstractErrorController {

    public ServletErrorHandlerController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @GetMapping("/error")
    public ResponseEntity<JsonResult<?>> error(HttpServletRequest request) {
        final HttpStatus status = getStatus(request);
        final Map<String, Object> errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.EXCEPTION, ErrorAttributeOptions.Include.MESSAGE, ErrorAttributeOptions.Include.BINDING_ERRORS));

        errorAttributes.forEach((key, value) -> log.error("ServletErrorHandlerController.error ERROR[key={}, error={}]", key, value));
        return ResponseEntity.status(status).body(create(failure, status.getReasonPhrase()));
    }
}
