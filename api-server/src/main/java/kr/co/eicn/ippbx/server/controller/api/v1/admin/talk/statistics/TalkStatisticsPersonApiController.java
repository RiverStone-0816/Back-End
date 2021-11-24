package kr.co.eicn.ippbx.server.controller.api.v1.admin.talk.statistics;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.dto.statdb.TalkStatisticsPersonResponse;
import kr.co.eicn.ippbx.model.entity.statdb.StatTalkEntity;
import kr.co.eicn.ippbx.model.search.TalkStatisticsSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.service.TalkStatisticsService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담톡관리 > 상담톡통계 > 상담톡상담원별통계
 */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/talk/statistics/person", produces = MediaType.APPLICATION_JSON_VALUE)
public class TalkStatisticsPersonApiController extends ApiBaseController {
    private final TalkStatisticsService talkStatisticsService;
    private final PersonListRepository personListRepository;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<TalkStatisticsPersonResponse>>> list(TalkStatisticsSearchRequest search) {
        if (search.getStartDate() != null && search.getEndDate() != null)
            if (search.getStartDate().after(search.getEndDate()))
                throw new IllegalArgumentException("시작시간이 종료시간보다 이전이어야 합니다.");

        final List<PersonList> personLists = personListRepository.findAll();

        final List<StatTalkEntity> list = talkStatisticsService.getRepository().personStatList(search);
        final List<TalkStatisticsPersonResponse> rows = list.stream()
                .map(e -> {
                    TalkStatisticsPersonResponse entity = convertDto(e, TalkStatisticsPersonResponse.class);
                    entity.setIdName(
                            personLists.stream().filter(person -> e.getUserid().equals(person.getId()))
                                    .map(PersonList::getIdName).findFirst().orElse("")
                    );

                    return entity;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(rows));
    }
}
