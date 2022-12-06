package kr.co.eicn.ippbx.server.controller.api.v1.search;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkServerInfo;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchTalkServiceInfoResponse;
import kr.co.eicn.ippbx.server.repository.eicn.WtalkServerInfoRepository;
import kr.co.eicn.ippbx.server.service.CacheService;
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
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkServerInfo.WTALK_SERVER_INFO;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/search/wtalk-service", produces = MediaType.APPLICATION_JSON_VALUE)
public class WtalkServiceInfoSearchApiController extends ApiBaseController {
    private final WtalkServiceInfoService talkServiceInfoService;
    private final WtalkServerInfoRepository wtalkServerInfoRepository;
    private final CacheService cacheService;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<SearchTalkServiceInfoResponse>>> search() {
        return ResponseEntity.ok(data(talkServiceInfoService.getAllTalkServiceList()));
    }

    @GetMapping("server")
    public ResponseEntity<JsonResult<List<WtalkServerInfo>>> serverList() {

        return ResponseEntity.ok(data(wtalkServerInfoRepository.findAll(WTALK_SERVER_INFO.TALK_HOST.eq(
                cacheService.getCompanyServerList(g.getUser().getCompanyId()).stream().filter(e -> e.getType().equals("K")).findFirst().get().getHost()
        ))));
    }
}
