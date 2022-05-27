package kr.co.eicn.ippbx.server.controller.api.v1.search;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchTalkServiceInfoResponse;
import kr.co.eicn.ippbx.server.service.WtalkServiceInfoService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static kr.co.eicn.ippbx.util.JsonResult.data;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/search/wtalk-service", produces = MediaType.APPLICATION_JSON_VALUE)
public class WtalkServiceInfoSearchApiController extends ApiBaseController {
    private final WtalkServiceInfoService talkServiceInfoService;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<SearchTalkServiceInfoResponse>>> search() {
        return ResponseEntity.ok(data(talkServiceInfoService.getAllTalkServiceList()));
    }
}
