package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.research;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 아웃바운드관리 > 설문관리 > 설문결과이력
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/research/result-history", produces = MediaType.APPLICATION_JSON_VALUE)
public class ResearchResultHistoryApiController extends ApiBaseController {
}
