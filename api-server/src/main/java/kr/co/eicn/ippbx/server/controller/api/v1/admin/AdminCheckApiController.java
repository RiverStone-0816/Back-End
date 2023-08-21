package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.spring.IsAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/check", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminCheckApiController extends ApiBaseController {

    @IsAdmin
    @GetMapping("")
    public ResponseEntity<JsonResult<Void>> check() {
        return ResponseEntity.ok(JsonResult.create());
    }
}
