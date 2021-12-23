package kr.co.eicn.ippbx.server.controller.api.v1.admin.service.log;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebSecureHistory;
import kr.co.eicn.ippbx.model.dto.eicn.LoginInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebSecureHistoryResponse;
import kr.co.eicn.ippbx.model.enums.IdType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.model.search.WebSecureHistorySearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebSecureHistoryRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 *   서비스운영관리 > 로그인이력 > 웹로그관리
 **/
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/service/log/web-log", produces = MediaType.APPLICATION_JSON_VALUE)
public class WebSecureHistoryApiController extends ApiBaseController {

    private final WebSecureHistoryRepository repository;
    private final PersonListRepository personListRepository;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<WebSecureHistoryResponse>>> pagination(WebSecureHistorySearchRequest search) {
        final Pagination<WebSecureHistory> pagination = repository.pagination(search);
        final List<WebSecureHistoryResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    WebSecureHistoryResponse entity = convertDto(e, WebSecureHistoryResponse.class);

                    if (isNotEmpty(e.getIdType()))
                        entity.setIdType(IdType.of(e.getIdType()));

                    return entity;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping("{userId}/last-login")
    public ResponseEntity<JsonResult<LoginInfoResponse>> getLastLoginInfo(@PathVariable String userId) {
        final LoginInfoResponse response = new LoginInfoResponse();
        final LoginInfoResponse lastLoginInfo = repository.getLastLoginInfo(userId);
        final Timestamp passChangeDate = personListRepository.findOneById(userId).getPassChangeDate();
        Calendar current = Calendar.getInstance();

        if (lastLoginInfo != null)
            ReflectionUtils.copy(response, lastLoginInfo);
        else
            response.setInsertDate(null);

        response.setUserId(userId);
        response.setPasswordChangeDate(passChangeDate);
        response.setChangePasswordDays((int) ((current.getTimeInMillis() - (passChangeDate != null ? passChangeDate.getTime() : current.getTimeInMillis())) / (1000 * 60 * 60 * 24)));

        return ResponseEntity.ok(data(response));
    }

    @DeleteMapping("")
    public ResponseEntity<JsonResult<Void>> delete(@RequestParam List<Integer> seq) {
        repository.deleteIds(seq);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping("overwrite/{limit}")
    public ResponseEntity<JsonResult<Void>> overwrite(@PathVariable Integer limit) {
        repository.overwrite(limit);
        return ResponseEntity.ok(create());
    }

    @PutMapping("down/{type}")
    public ResponseEntity<JsonResult<Void>> updateWebLogDown(@PathVariable String type) {
        WebSecureActionType actionType = WebSecureActionType.of(type);

        if (actionType != null)
            repository.updateWebLogDown(actionType);
        else
            log.error("kr.co.eicn.ippbx.server.model.enums.WebSecureActionType[ " + type + " ] is not exist");

        return ResponseEntity.ok(create());
    }
}
