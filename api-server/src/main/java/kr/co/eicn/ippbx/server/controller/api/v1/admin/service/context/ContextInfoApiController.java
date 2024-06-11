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
import kr.co.eicn.ippbx.util.spring.IsAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    @IsAdmin
    @GetMapping("")
    public ResponseEntity<JsonResult<List<ContextInfoResponse>>> list() {
        return ResponseEntity.ok(data(repository.findAll().stream().map(e -> modelMapper.map(e, ContextInfoResponse.class)).collect(Collectors.toList())));
    }

    @GetMapping("{seq}")
    public JsonResult<ContextInfoResponse> get(@PathVariable Integer seq) {
        return data(modelMapper.map(repository.findOneIfNullThrow(seq), ContextInfoResponse.class));
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
    @PutMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody ContextInfoFormRequest form, BindingResult bindingResult,
                                                @PathVariable Integer seq) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        final ContextInfo entity = repository.findOneIfNullThrow(seq);
        repository.updateByKey(form, seq);
        webVoiceInfoRepository.updateContextName(entity.getContext(), form.getContext());

        return ResponseEntity.ok(create());
    }

    /**
     * 컨텍스트삭제
     **/
    @DeleteMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        final ContextInfo entity = repository.findOneIfNullThrow(seq);
        repository.deleteOnIfNullThrow(seq);
        webVoiceInfoRepository.deleteContext(entity.getContext());
        return ResponseEntity.ok(create());
    }

    /**
     * 보이는 ARS
     **/
    @GetMapping("{seq}/web-voice")
    public JsonResult<WebVoiceResponse> getWebVoice(@PathVariable Integer seq) {
        final ContextInfo contextEntity = repository.findOneIfNullThrow(seq);

        final WebVoiceResponse webVoiceEntity = convertDto(webVoiceInfoRepository.findOneByContext(contextEntity.getContext()), WebVoiceResponse.class);
        webVoiceEntity.setItems(
                webVoiceItemsRepository.findAllByContext(webVoiceEntity.getContext()).stream()
                        .map(f -> convertDto(f, WebVoiceItemsResponse.class))
                        .collect(Collectors.toList())
        );

        return data(webVoiceEntity);
    }

    /**
     * 보이는 ARS 적용
     **/
    @PostMapping("{seq}/apply")
    public ResponseEntity<JsonResult<Void>> apply(@Valid @RequestBody WebVoiceItemsFormRequest form, BindingResult bindingResult,
                                                  @PathVariable Integer seq) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        final ContextInfo entity = repository.findOneIfNullThrow(seq);
        webVoiceInfoRepository.updateContext(entity.getContext(), form);

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
