package kr.co.eicn.ippbx.server.controller.api;

import kr.co.eicn.ippbx.server.repository.eicn.DaemonRepository;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static kr.co.eicn.ippbx.server.util.JsonResult.data;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/daemon", produces = MediaType.APPLICATION_JSON_VALUE)
public class DaemonInfoApiController extends ApiBaseController{
    private final DaemonRepository daemonRepository;

    @GetMapping("")
    public ResponseEntity<JsonResult<Map<String, String>>> get() {
        return ResponseEntity.ok(data(daemonRepository.findAllNodeJSDaemon()));
    }
}
