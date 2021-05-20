package kr.co.eicn.ippbx.server.controller.api.v1.admin.service.context;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ContextInfo;
import kr.co.eicn.ippbx.model.dto.eicn.ContextInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryContextInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebVoiceItemsResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebVoiceResponse;
import kr.co.eicn.ippbx.model.form.ContextInfoFormRequest;
import kr.co.eicn.ippbx.model.form.WebVoiceItemsFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CompanyInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ContextInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebVoiceInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebVoiceItemsRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 서비스운영관리 > 컨텍스트관리 > 컨텍스트관리
 **/
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/service/context/context", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContextInfoApiController extends ApiBaseController {
    private final ContextInfoRepository repository;
    private final WebVoiceInfoRepository webVoiceInfoRepository;
    private final WebVoiceItemsRepository webVoiceItemsRepository;
    private final CompanyInfoRepository companyInfoRepository;

    /**
     * 컨텍스트목록조회
     **/
    @GetMapping("")
    public ResponseEntity<JsonResult<List<ContextInfoResponse>>> list() {
        final List<ContextInfoResponse> list = repository.findAll().stream()
                .map(e -> modelMapper.map(e, ContextInfoResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(list));
    }

    @GetMapping("{context}")
    public JsonResult<ContextInfoResponse> get(@PathVariable String context) {
        return data(modelMapper.map(repository.findOneByContext(context), ContextInfoResponse.class));
    }

    /**
     * 컨텍스트추가
     **/
    @PostMapping("")
    public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody ContextInfoFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.insert(form);

        return ResponseEntity.ok(create());
    }

    /**
     * 컨텍스트수정
     **/
    @PutMapping("{context}")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody ContextInfoFormRequest form, BindingResult bindingResult,
                                                @PathVariable String context) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.updateByContext(form, context);
        webVoiceInfoRepository.updateContextName(context, form.getContext());

        return ResponseEntity.ok(create());
    }

    /**
     * 컨텍스트삭제
     **/
    @DeleteMapping("{context}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable String context) {
        repository.deleteByContext(context);
        webVoiceInfoRepository.deleteContext(context);
        return ResponseEntity.ok(create());
    }

    /**
     * 보이는 ARS
     **/
    @GetMapping("{context}/web-voice")
    public JsonResult<WebVoiceResponse> getWebVoice(@PathVariable String context) {
        final WebVoiceResponse entity = convertDto(webVoiceInfoRepository.findOneByContext(context), WebVoiceResponse.class);
        entity.setItems(
                webVoiceItemsRepository.findAllByContext(entity.getContext()).stream()
                        .map(f -> convertDto(f, WebVoiceItemsResponse.class))
                        .collect(Collectors.toList())
        );

        return data(entity);
    }

    /**
     * 보이는 ARS 적용
     **/
    @PostMapping("{context}/apply")
    public ResponseEntity<JsonResult<Void>> apply(@Valid @RequestBody WebVoiceItemsFormRequest form, BindingResult bindingResult,
                                                  @PathVariable String context) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        webVoiceInfoRepository.updateContext(context, form);

        // TODO: 이슈! WEB_VOICE 서버 필요
//        final WebvoiceInfo info = webVoiceInfoRepository.findOneByContext(context);
//        final String serviceKey = companyInfoRepository.findServiceKey();
//        String result = "";
//
//        if (info != null)
//            result = webVoiceInfoRepository.apply(serviceKey,info,context);

        return ResponseEntity.ok(create());
    }

    @GetMapping("add-context")
    public ResponseEntity<JsonResult<List<SummaryContextInfoResponse>>> addContext() {
        final List<SummaryContextInfoResponse> list = repository.findAll().stream()
                .sorted(Comparator.comparing(ContextInfo::getName))
                .map(e -> convertDto(e, SummaryContextInfoResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(list));
    }
}
