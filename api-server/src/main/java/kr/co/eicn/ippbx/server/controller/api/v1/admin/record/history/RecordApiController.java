package kr.co.eicn.ippbx.server.controller.api.v1.admin.record.history;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.GradeList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.model.RecordFile;
import kr.co.eicn.ippbx.model.dto.customdb.CommonEicnCdrResponse;
import kr.co.eicn.ippbx.model.entity.customdb.EicnCdrEntity;
import kr.co.eicn.ippbx.model.entity.customdb.EicnCdrEvaluationEntity;
import kr.co.eicn.ippbx.model.enums.WebSecureActionSubType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.model.form.RecordDownFormRequest;
import kr.co.eicn.ippbx.model.search.RecordCallSearch;
import kr.co.eicn.ippbx.server.repository.customdb.EicnCdrRepository;
import kr.co.eicn.ippbx.server.repository.eicn.GradeListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ServiceRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebSecureHistoryRepository;
import kr.co.eicn.ippbx.server.service.EicnCdrService;
import kr.co.eicn.ippbx.server.service.RecordDownService;
import kr.co.eicn.ippbx.server.service.RecordFileService;
import kr.co.eicn.ippbx.server.service.StorageService;
import kr.co.eicn.ippbx.util.FunctionUtils;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static kr.co.eicn.ippbx.util.JsonResult.Result.success;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * 녹취관리 > 녹취/통화이력조회 > 녹취/통화이력조회
 */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/record/history", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecordApiController extends ApiBaseController {

    private final PersonListRepository personListRepository;
    private final GradeListRepository gradeListRepository;
    private final ServiceRepository serviceRepository;
    private final WebSecureHistoryRepository webSecureHistoryRepository;
    private final EicnCdrService eicnCdrService;
    private final RecordFileService recordFileService;
    private final RecordDownService recordDownService;
    private final StorageService fileSystemStorageService;
    private final Period LIMIT_INQUIRY_PERIOD = Period.ofMonths(3);

    /**
     * 녹취통화이력 목록조회
     */
    @GetMapping("")
    public ResponseEntity<JsonResult<List<CommonEicnCdrResponse>>> list(RecordCallSearch search) {
        final LocalDate toDay = LocalDate.now();
        if (Objects.isNull(search.getStartTimestamp()))
            search.setStartTimestamp(Timestamp.valueOf(LocalDateTime.of(toDay, LocalTime.MIN)));
        if (Objects.isNull(search.getEndTimestamp()))
            search.setEndTimestamp(Timestamp.valueOf(LocalDateTime.of(toDay, LocalTime.MAX)));
        if (search.getStartTimestamp().after(search.getEndTimestamp()))
            return ResponseEntity.ok(data(Collections.emptyList()));
        /*if (search.getEndTimestamp().toLocalDateTime().minusMonths(LIMIT_INQUIRY_PERIOD.getMonths()).isAfter(search.getStartTimestamp().toLocalDateTime()))
            return ResponseEntity.ok(data(Collections.emptyList()));*/
        if (search.getBatchEvaluationMode()) {
            search.setStartTimestamp(null);
            search.setEndTimestamp(null);
        }

        if (RecordCallSearch.Sort.of(search.getSort()) != null)
            search.setOrderDirection(search.getSort().getOrderDirection());

        final List<EicnCdrEntity> list = eicnCdrService.getRepository().findAll(search);
        final Map<String, PersonList> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, e -> e));
        final Map<String, ServiceList> serviceListMap = serviceRepository.findAll().stream().filter(FunctionUtils.distinctByKey(ServiceList::getSvcNumber))
                .collect(Collectors.toMap(ServiceList::getSvcNumber, e -> e));
        final Map<String, String> gradeMap = gradeListRepository.findAll().stream().collect(Collectors.toMap(GradeList::getGradeNumber, GradeList::getGrade));

        final List<CommonEicnCdrResponse> rows = list.stream()
                .map((e) -> {
                    final CommonEicnCdrResponse response = convertDto(e, CommonEicnCdrResponse.class);

                    if (Objects.nonNull(personListMap.get(response.getUserid())))
                        response.setPersonList(personListMap.get(response.getUserid()));
                    if (Objects.nonNull(gradeMap.get(response.getSrc())) || Objects.nonNull(gradeMap.get(response.getDst())))
                        response.setGrade(Objects.nonNull(gradeMap.get(response.getSrc())) ? gradeMap.get(response.getSrc()) : gradeMap.get(response.getDst()));
                    if (isNotEmpty(response.getIniNum()))
                        if (Objects.nonNull(serviceListMap.get(response.getIniNum())))
                            response.setService(serviceListMap.get(response.getIniNum()));

                    return response;
                })
                .collect(toList());

        return ResponseEntity.ok(data(rows));
    }

    /**
     * 녹취통화이력 목록조회
     */
    @GetMapping("search")
    public ResponseEntity<JsonResult<Pagination<CommonEicnCdrResponse>>> pagination(RecordCallSearch search) {
        final LocalDate toDay = LocalDate.now();
        if (Objects.isNull(search.getStartTimestamp()))
            search.setStartTimestamp(Timestamp.valueOf(LocalDateTime.of(toDay, LocalTime.MIN)));
        if (Objects.isNull(search.getEndTimestamp()))
            search.setEndTimestamp(Timestamp.valueOf(LocalDateTime.of(toDay, LocalTime.MAX)));
        if (search.getStartTimestamp().after(search.getEndTimestamp()))
            return ResponseEntity.ok(data(new Pagination<>(Collections.emptyList(), 1, 0, search.getLimit())));
        if (search.getEndTimestamp().toLocalDateTime().minusMonths(LIMIT_INQUIRY_PERIOD.getMonths()).isAfter(search.getStartTimestamp().toLocalDateTime()))
            return ResponseEntity.ok(data(new Pagination<>(Collections.emptyList(), 1, 0, search.getLimit())));

        if (RecordCallSearch.Sort.of(search.getSort()) != null)
            search.setOrderDirection(search.getSort().getOrderDirection());

        final Pagination<EicnCdrEntity> pagination = eicnCdrService.getRepository().pagination(search);
        final Map<String, PersonList> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, e -> e));
        final Map<String, ServiceList> serviceListMap = serviceRepository.findAll().stream().filter(FunctionUtils.distinctByKey(ServiceList::getSvcNumber))
                .collect(Collectors.toMap(ServiceList::getSvcNumber, e -> e));
        final Map<String, String> gradeMap = gradeListRepository.findAll().stream().collect(Collectors.toMap(GradeList::getGradeNumber, GradeList::getGrade));

        final List<CommonEicnCdrResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final CommonEicnCdrResponse response = convertDto(e, CommonEicnCdrResponse.class);

                    if (Objects.nonNull(personListMap.get(response.getUserid())))
                        response.setPersonList(personListMap.get(response.getUserid()));
                    if (Objects.nonNull(gradeMap.get(response.getSrc())) || Objects.nonNull(gradeMap.get(response.getDst())))
                        response.setGrade(Objects.nonNull(gradeMap.get(response.getSrc())) ? gradeMap.get(response.getSrc()) : gradeMap.get(response.getDst()));
                    if (isNotEmpty(response.getIniNum()))
                        if (Objects.nonNull(serviceListMap.get(response.getIniNum())))
                            response.setService(serviceListMap.get(response.getIniNum()));

                    return response;
                })
                .collect(toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping("{seq}")
    public ResponseEntity<JsonResult<CommonEicnCdrResponse>> get(@PathVariable Integer seq) {
        final CommonEicnCdrResponse detail = convertDto(eicnCdrService.getRepository().findOneIfNullThrow(seq), CommonEicnCdrResponse.class);

        final Map<String, PersonList> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, e -> e));
        final Map<String, ServiceList> serviceListMap = serviceRepository.findAll().stream().filter(FunctionUtils.distinctByKey(ServiceList::getSvcNumber))
                .collect(Collectors.toMap(ServiceList::getSvcNumber, e -> e));

        if (Objects.nonNull(personListMap.get(detail.getUserid())))
            detail.setPersonList(personListMap.get(detail.getUserid()));
        if (isNotEmpty(detail.getIniNum()))
            if (Objects.nonNull(serviceListMap.get(detail.getIniNum())))
                detail.setService(serviceListMap.get(detail.getIniNum()));

        return ResponseEntity.ok().body(data(detail));
    }

    /**
     * 녹취파일 목록 조회
     */
    @GetMapping("{seq}/record-files")
    public ResponseEntity<JsonResult<List<RecordFile>>> getFiles(@PathVariable Integer seq) {
        return ResponseEntity.ok().body(data(recordFileService.fetchAll(Collections.singletonList(eicnCdrService.getRepository().findOneIfNullThrow(seq)))));
    }

    @GetMapping(value = "resource", params = {"token"})
    public ResponseEntity<Resource> resource(@RequestParam("path") String recordFile/*파일명을 포함한 파일경로*/, @RequestParam("mode") String mode) {
        final RecordFileService.Result actualExistingFile = recordFileService.getActualExistingFile(recordFile, mode);

        if (actualExistingFile.getCode() != 1)
            throw new IllegalArgumentException(actualExistingFile.getMessage());

        final Resource resource = fileSystemStorageService.loadAsResource(actualExistingFile.getPath(), actualExistingFile.getFileName());

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
                .headers(header -> {
                    header.add(HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.builder("attachment")
                                    .filename(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8)
                                    .build().toString());
                    header.setPragma("no-cache");
                    header.setCacheControl(CacheControl.noCache());
                })
                .body(resource);
    }

    /**
     * 녹취 일괄다운로드 등록
     */
    @PostMapping(value = "record-batch-download-register")
    public ResponseEntity<JsonResult<String>> recordInBatchesRegister(@Valid @RequestBody RecordDownFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        recordDownService.recordInBatchesRegister(form);

        return ResponseEntity.ok(create(success, "녹취 다운로드관리에서 확인"));
    }

    /**
     * 상담원 평가 조회
     */
    @GetMapping("{seq}/evaluation")
    public ResponseEntity<JsonResult<EicnCdrEvaluationEntity>> evaluation(@PathVariable Integer seq) {
        return ResponseEntity.ok(data(eicnCdrService.getRepository().getEvaluation(seq)));
    }

    /**
     * 녹취통화이력 > 평가지 리스트
     */
    @GetMapping("evaluations")
    public ResponseEntity<JsonResult<List<EicnCdrEvaluationEntity>>> evaluations(@RequestParam List<Integer> sequences) {
        return ResponseEntity.ok(data(eicnCdrService.getRepository().getEvaluationLists(sequences)));
    }

    @DeleteMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        EicnCdrRepository repository = eicnCdrService.getRepository();
        EicnCdrEntity entity = repository.findOne(seq);

        final RecordFileService.Result actualExistingFile = recordFileService.getActualExistingFile(entity.getRecordFile(), "DOWN");

        if (actualExistingFile.getCode() != 1)
            throw new IllegalArgumentException(actualExistingFile.getMessage());

        boolean removeResult = recordFileService.removeFile(actualExistingFile.getPath() + "/" + actualExistingFile.getFileName());

        if (removeResult) {
            repository.removeRecordFile(seq);
            webSecureHistoryRepository.insert(WebSecureActionType.RECORD_FILE, WebSecureActionSubType.DEL, actualExistingFile.getPath() + "/" + actualExistingFile.getFileName());
        }

        return ResponseEntity.ok(create());
    }
}
