package kr.co.eicn.ippbx.server.controller.api;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ArsAuth;
import kr.co.eicn.ippbx.server.repository.eicn.ArsAuthRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kr.co.eicn.ippbx.util.JsonResult.data;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/ars-auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class ArsAuthController extends ApiBaseController {
    private final ArsAuthRepository arsAuthRepository;

    @GetMapping("{id}")
    public JsonResult<ArsAuth> get(@PathVariable String id) {
        return data(arsAuthRepository.findOneByUserId(id));
    }
}
