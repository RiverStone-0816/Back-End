package kr.co.eicn.ippbx.server.controller.api.v1.admin.user.tel;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServerInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.model.dto.eicn.NumberSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryNumber070Response;
import kr.co.eicn.ippbx.model.enums.Bool;
import kr.co.eicn.ippbx.model.enums.NumberType;
import kr.co.eicn.ippbx.model.form.NumberTypeChangeRequest;
import kr.co.eicn.ippbx.model.search.NumberSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.impl.DSL;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.Number_070.NUMBER_070;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueName.QUEUE_NAME;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * 번호/그룹/사용자 > 번호/서비스관리 > 번호관리
 */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/user/tel/number", produces = MediaType.APPLICATION_JSON_VALUE)
public class NumberApiController extends ApiBaseController {
    private final Number070Repository repository;
    private final ServiceRepository serviceRepository;
    private final ScheduleInfoRepository scheduleInfoRepository;
    private final QueueNameRepository queueNameRepository;
    private final ServerInfoRepository serverInfoRepository;
    private final PhoneInfoRepository phoneInfoRepository;

    /**
     * 번호타입에 따른 전체 번호조회
     */
    @GetMapping("")
    public ResponseEntity<JsonResult<List<NumberSummaryResponse>>> list(NumberSearchRequest search) {
        final List<NumberSummaryResponse> summaryResponses = new ArrayList<>();
        final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070> rows = repository.findAll(search);

        final List<ServiceList> serviceLists = serviceRepository.findAll();
        final List<ScheduleInfo> scheduleInfos = scheduleInfoRepository.findAll();
        final List<ServerInfo> serverInfos = serverInfoRepository.findAll();
        final List<PhoneInfo> phoneInfos = phoneInfoRepository.findAll();

        for (kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070 row : rows) {
            final NumberSummaryResponse number = modelMapper.map(row, NumberSummaryResponse.class);

            number.setUseService("비사용중");

            if (row.getStatus() == 1) {
                if (row.getType() == 0) {
                    queueNameRepository.findAll(QUEUE_NAME.NUMBER.eq(number.getNumber())).stream().findFirst()
                            .ifPresent(e -> number.setUseService(e.getHanName()));
                } else if (row.getType() == 1) {
                    phoneInfos.stream().filter(e -> number.getNumber().equals(e.getVoipTel())).findFirst()
                            .ifPresent(e -> number.setUseService(e.getExtension()));
                } else {
                    number.setUseService("대표번호");
                }
            }

            serviceLists.stream().filter(e -> e.getSvcNumber().equals(row.getNumber())).findFirst()
                    .ifPresent(e -> {
                        number.setSvcCid(e.getSvcCid());
                        number.setUseService(e.getSvcName());
                    });

            final Optional<ScheduleInfo> optionalSchedule =
                    scheduleInfos.stream().filter(e -> e.getNumber().equals(row.getNumber())).findFirst();
            if (optionalSchedule.isPresent())
                number.setIsSchedule(Bool.Y);
            else
                number.setIsSchedule(Bool.N);

            serverInfos.stream().filter(e -> isNotEmpty(e.getHost()) && e.getHost().equals(row.getHost())).findFirst()
                    .ifPresent(e -> number.setHostName(e.getName()));

            if (Objects.equals(Bool.N, number.getIsSchedule()) && number.getUseService().contains("비사용중"))
                number.setIsTypeChange(Bool.Y);
            else
                number.setIsTypeChange(Bool.N);

            phoneInfos.stream()
                    .filter(e -> number.getNumber().equals(e.getVoipTel()))
                    .findFirst()
                    .ifPresent(e -> number.setOriginalNumber(e.getOriginalNumber()));

            summaryResponses.add(number);
        }
        return ResponseEntity.ok(data(summaryResponses));
    }

    /**
     * 번호 타입 변경
     */
    @PatchMapping("{number}/type")
    public ResponseEntity<JsonResult<Void>> typeChange(@Valid @RequestBody NumberTypeChangeRequest form, BindingResult bindingResult,
                                                       @PathVariable String number) {
        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult);

        repository.typeChange(form, number);
        return ResponseEntity.ok(create());
    }

    /**
     *  번호타입에 따른 070번호 조회
     */
    @GetMapping(value = "type-numbers", params = "type")
    public ResponseEntity<JsonResult<List<SummaryNumber070Response>>> typeNumbers(@RequestParam("type") Byte type, @RequestParam(required = false) String host) {
        Objects.requireNonNull(NumberType.of(type));

        return ResponseEntity.ok(
                data(repository.findAll((isEmpty(host) ? DSL.noCondition() : NUMBER_070.HOST.eq(host)).and(NUMBER_070.TYPE.eq(type).and(NUMBER_070.STATUS.eq((byte) 0))))
                        .stream()
                        .map(number -> convertDto(number, SummaryNumber070Response.class))
                        .collect(Collectors.toList())
                )
        );
    }
}
