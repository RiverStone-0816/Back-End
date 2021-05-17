package kr.co.eicn.ippbx.server.controller.api.v1.admin.stat.result;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonCode;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonField;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonType;
import kr.co.eicn.ippbx.server.model.dto.customdb.StatQaResultCodeResponse;
import kr.co.eicn.ippbx.server.model.dto.customdb.StatQaResultFieldResponse;
import kr.co.eicn.ippbx.server.model.dto.customdb.StatQaResultIndividualResponse;
import kr.co.eicn.ippbx.server.model.dto.customdb.StatQaResultResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.IndividualCodeResponse;
import kr.co.eicn.ippbx.server.model.search.StatQaResultIndividualSearchRequest;
import kr.co.eicn.ippbx.server.model.search.StatQaResultSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CommonCodeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.CommonFieldRepository;
import kr.co.eicn.ippbx.server.repository.eicn.CommonTypeRepository;
import kr.co.eicn.ippbx.server.service.StatQaResultService;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.util.JsonResult.data;

/**
 * 통계관리 > 상담결과통계
 */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/stat/result", produces = MediaType.APPLICATION_JSON_VALUE)
public class StatQaResultApiController extends ApiBaseController {
    private final StatQaResultService statQaResultService;
    private final CommonTypeRepository commonTypeRepository;
    private final CommonFieldRepository commonFieldRepository;
    private final CommonCodeRepository commonCodeRepository;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<StatQaResultResponse>>> list(StatQaResultSearchRequest search) {
        if (search.getStartDate().after(search.getEndDate()))
            throw new IllegalArgumentException(message.getText("messages.validator.enddate.after.startdate"));
        if ((search.getEndDate().getTime() - search.getStartDate().getTime()) / 1000 > 6 * 30 * 24 * 60 * 60)
            throw new IllegalArgumentException(message.getText("messages.validator.enddate.indays", "180일"));

        List<CommonType> typeList = commonTypeRepository.findAllByKind("RS");
        List<CommonField> fieldList = commonFieldRepository.findAllByFieldId();
        List<CommonCode> codeList = commonCodeRepository.findAll();

        return ResponseEntity.ok(data(
                typeList.stream().map(type -> {
                    final StatQaResultResponse response = convertDto(type, StatQaResultResponse.class);
                    response.setFieldResponses(
                            fieldList.stream().filter(field -> field.getType().equals(type.getSeq()))
                                    .map(field -> {
                                        final StatQaResultFieldResponse fieldResponse = convertDto(field, StatQaResultFieldResponse.class);

                                        fieldResponse.setCodeResponses(
                                                codeList.stream().filter(code -> code.getType().equals(type.getSeq()) && code.getFieldId().equals(field.getFieldId()))
                                                        .map(code -> {
                                                            final StatQaResultCodeResponse codeResponse = convertDto(code, StatQaResultCodeResponse.class);

                                                            return statQaResultService.convertToStatQaResultField(codeResponse, code, search);
                                                        })
                                                        .collect(Collectors.toList())
                                        );

                                        return fieldResponse;
                                    }).collect(Collectors.toList())
                    );

                    return response;
                }).collect(Collectors.toList())
        ));
    }

    @GetMapping("individual")
    public ResponseEntity<JsonResult<List<StatQaResultIndividualResponse>>> getIndividualResult(StatQaResultIndividualSearchRequest search) {
        return ResponseEntity.ok(data(statQaResultService.convertIndividualResult(search)));
    }

    @GetMapping("individual/field-list")
    public ResponseEntity<JsonResult<List<IndividualCodeResponse>>> getIndividualList() {
        return ResponseEntity.ok(data(commonFieldRepository.individualFieldList().stream().map(e -> convertDto(e, IndividualCodeResponse.class)).collect(Collectors.toList())));
    }
}