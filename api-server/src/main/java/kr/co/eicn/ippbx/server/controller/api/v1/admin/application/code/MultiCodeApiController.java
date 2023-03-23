package kr.co.eicn.ippbx.server.controller.api.v1.admin.application.code;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonType;
import kr.co.eicn.ippbx.model.dto.eicn.CommonCodeDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.CommonFieldResponse;
import kr.co.eicn.ippbx.model.dto.eicn.CommonTypeDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.RelatedFieldResponse;
import kr.co.eicn.ippbx.model.form.CommonCodeUpdateFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CommonCodeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.CommonFieldRepository;
import kr.co.eicn.ippbx.server.repository.eicn.CommonTypeRepository;
import kr.co.eicn.ippbx.server.service.CommonFieldPoster;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담어플리케이션관리 > 코드관리 > 코드/멀티코드
 **/
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/application/code/multi", produces = MediaType.APPLICATION_JSON_VALUE)
public class MultiCodeApiController extends ApiBaseController {
    private final CommonTypeRepository commonTypeRepository;
    private final CommonFieldRepository commonFieldRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final CommonFieldPoster commonFieldPoster;

    /**
     * 멀티코드 조회
     **/
    @GetMapping("")
    public ResponseEntity<JsonResult<List<CommonTypeDetailResponse>>> list() {
        List<CommonType> commonTypeList = commonTypeRepository.findAllType();
        List<CommonField> commonFieldList = commonFieldRepository.findAllCodeField();
        List<CommonCode> commonCodeList = commonCodeRepository.findAll();

        return ResponseEntity.ok(data(
                commonTypeList.stream()
                        .map(commonType -> {
                            CommonTypeDetailResponse commonTypeResponse = convertDto(commonType, CommonTypeDetailResponse.class);
                            commonTypeResponse.setCommonFields(
                                    commonFieldList.stream().filter(commonField -> commonField.getType().equals(commonType.getSeq()))
                                            .map(commonField -> {
                                                CommonFieldResponse commonFieldResponse = convertDto(commonField, CommonFieldResponse.class);

                                                commonFieldResponse.setCommonCodes(
                                                        commonCodeList.stream()
                                                                .filter(commonCode -> commonCode.getType().equals(commonType.getSeq()) && commonCode.getFieldId().equals(commonField.getFieldId()) && commonCode.getHide().equals("N"))
                                                                .map(commonCode -> {
                                                                    CommonCodeDetailResponse commonCodeResponse = convertDto(commonCode, CommonCodeDetailResponse.class);
                                                                    commonFieldList.stream().filter(commonField1 -> commonField1.getType().equals(commonType.getSeq()) && commonField1.getFieldId().equals(commonCode.getRelatedFieldId()))
                                                                            .sorted(Comparator.comparing(CommonField::getSeq))
                                                                            .forEach(commonField1 -> {
                                                                                commonFieldResponse.setRelatedFieldId(commonField1.getFieldId());
                                                                                commonFieldResponse.setRelatedFieldInfo(commonField1.getFieldInfo());
                                                                            });

                                                                    return commonCodeResponse;
                                                                }).sorted(Comparator.comparing(CommonCodeDetailResponse::getSequence))
                                                                .collect(Collectors.toList())
                                                );

                                                return commonFieldResponse;
                                            }).sorted(Comparator.comparing(CommonFieldResponse::getDisplaySeq)).collect(Collectors.toList())
                            );

                            if (commonTypeResponse.getCommonFields().size() == 0)
                                return null;

                            return commonTypeResponse;
                        }).filter(Objects::nonNull).sorted(Comparator.comparing(CommonTypeDetailResponse::getName)).collect(Collectors.toList())
        ));
    }

    /**
     * 코드 수정
     **/
    @PutMapping("type/{type}/field/{fieldId}/code")
    public ResponseEntity<JsonResult<Void>> put(@RequestBody CommonCodeUpdateFormRequest form, @PathVariable Integer type, @PathVariable String fieldId) {
        commonTypeRepository.findOneIfNullThrow(type);
        commonFieldRepository.findOneByTypeFieldId(type, fieldId);

        commonCodeRepository.deleteByKeyType(type, fieldId);
        commonCodeRepository.insertCodes(type, fieldId, form.getRelatedFieldId(), form.getCodes());

        return ResponseEntity.ok(create());
    }

    /**
     * 연동필드 목록조회
     **/
    @GetMapping("type/{type}/field/{fieldId}/related")
    public ResponseEntity<JsonResult<List<RelatedFieldResponse>>> getRelatedField(@PathVariable Integer type, @PathVariable String fieldId) {
        return ResponseEntity.ok(data(
                commonFieldRepository.findAllRelatedField(type, fieldId).stream()
                        .map(relatedField -> convertDto(relatedField, RelatedFieldResponse.class))
                        .collect(Collectors.toList())
        ));
    }

    /**
     *  코드 엑셀업로드
     * */
    @PostMapping(value = "{type}/{fieldId}/codes/by-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResult<Void>> postFieldsByExcel(@PathVariable Integer type,  @PathVariable String fieldId, @RequestParam MultipartFile file) throws IOException {
        commonFieldPoster.postByExcel(CommonFieldPoster.ExcelType.MAINDB, type, file);
        return ResponseEntity.ok(create());
    }

    /**
     *  field정보 조회
     **/
    @GetMapping("type/{type}/field/{fieldId}")
    public ResponseEntity<JsonResult<CommonFieldResponse>> getField(@PathVariable Integer type, @PathVariable String fieldId) {
        CommonField commonField = commonFieldRepository.findOneFieldId(type, fieldId);
        List<CommonCode> commonCodeList = commonCodeRepository.findAllByTypeField(type, fieldId);

        CommonFieldResponse commonFieldResponse = convertDto(commonField, CommonFieldResponse.class);

        commonFieldResponse.setCommonCodes(commonCodeList.stream().map(commonCode -> convertDto(commonCode, CommonCodeDetailResponse.class)).collect(Collectors.toList()));

        if(!CollectionUtils.isEmpty(commonCodeList) && StringUtils.isNotEmpty(commonCodeList.get(0).getRelatedFieldId()))
            commonFieldResponse.setRelatedFieldId(commonCodeList.get(0).getRelatedFieldId());

        return ResponseEntity.ok(data(commonFieldResponse));
    }
}
