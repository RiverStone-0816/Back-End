package kr.co.eicn.ippbx.server.controller.api.v1.admin.application.code;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonField;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonType;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConCsCodeInfo;
import kr.co.eicn.ippbx.server.model.dto.eicn.ConCodeFieldResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.ConCodeResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.ConGroupResponse;
import kr.co.eicn.ippbx.server.model.form.ConCodeFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CommonFieldRepository;
import kr.co.eicn.ippbx.server.repository.eicn.CommonTypeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ConCsCodeInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ConGroupRepository;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;

/**
 *  상담어플리케이션관리 > 코드관리 > 연동코드관리
 **/
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/application/code/con", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConCodeApiController extends ApiBaseController {
    private final CommonTypeRepository commonTypeRepository;
    private final CommonFieldRepository commonFieldRepository;
    private final ConCsCodeInfoRepository conCsCodeInfoRepository;
    private final ConGroupRepository conGroupRepository;

    /**
     *  연동코드 목록 조회
     * */
    @GetMapping("")
    public ResponseEntity<JsonResult<List<ConCodeResponse>>> list() {
        List<CommonType> commonTypeList = commonTypeRepository.findAllType();
        List<CommonField> commonFieldList = commonFieldRepository.findAllConCode();
        List<ConCsCodeInfo> conCsCodeInfoList = conCsCodeInfoRepository.findAll();

        return ResponseEntity.ok(data(
                commonTypeList.stream()
                        .map(commonType -> {
                            ConCodeResponse conCodeResponse = convertDto(commonType, ConCodeResponse.class);
                            conCodeResponse.setConFields(
                                    commonFieldList.stream().filter(commonField -> commonField.getType().equals(commonType.getSeq()))
                                            .map(commonField -> {
                                                ConCodeFieldResponse conCodeFieldResponse = convertDto(commonField, ConCodeFieldResponse.class);
                                                conCodeFieldResponse.setConGroupId(
                                                        conCsCodeInfoList.stream().filter(conCsCodeInfo -> conCsCodeInfo.getType().equals(commonType.getSeq()))
                                                                .map(ConCsCodeInfo::getConGroupId).findFirst()
                                                                .orElse(null)
                                                );

                                                return conCodeFieldResponse;
                                            })
                                            .collect(Collectors.toList())
                            );

                            if (conCodeResponse.getConFields().size() == 0)
                                return null;

                            return conCodeResponse;
                        }).filter(Objects::nonNull).collect(Collectors.toList())
        ));
    }

    /**
     *  연동코드 수정
     * */
    @PutMapping("type/{type}/field/{fieldId}")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody ConCodeFormRequest form, BindingResult bindingResult, @PathVariable Integer type, @PathVariable String fieldId) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        ConCsCodeInfo conCode = conCsCodeInfoRepository.findOne(type, fieldId);

        if (conCode != null) {
            conCsCodeInfoRepository.updateCsCode(conCode.getSeq(), form);
        } else {
            conCsCodeInfoRepository.insertCsCode(form);
        }

        return ResponseEntity.ok(create());
    }

    /**
     *  연동DB목록 조회
     * */
    @GetMapping("group")
    public ResponseEntity<JsonResult<List<ConGroupResponse>>> getConGroupList() {
        return ResponseEntity.ok(data(
                conGroupRepository.findAll().stream()
                        .map(conGroup -> convertDto(conGroup, ConGroupResponse.class))
                        .collect(Collectors.toList())
        ));
    }
}
