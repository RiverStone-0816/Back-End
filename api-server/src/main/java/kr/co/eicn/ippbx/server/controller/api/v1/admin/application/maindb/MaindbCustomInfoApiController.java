package kr.co.eicn.ippbx.server.controller.api.v1.admin.application.maindb;


import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.model.dto.eicn.ConCodeFieldResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchMaindbGroupResponse;
import kr.co.eicn.ippbx.model.entity.customdb.MaindbCustomInfoEntity;
import kr.co.eicn.ippbx.model.enums.WebSecureActionSubType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.model.form.MaindbCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.MaindbDataSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CommonFieldRepository;
import kr.co.eicn.ippbx.server.repository.eicn.MaindbGroupRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebSecureHistoryRepository;
import kr.co.eicn.ippbx.server.service.FileSystemStorageService;
import kr.co.eicn.ippbx.server.service.MaindbCustomInfoService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.io.FilenameUtils.getName;
import static org.apache.commons.lang3.StringUtils.replace;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/application/maindb/custominfo", produces = MediaType.APPLICATION_JSON_VALUE)
public class MaindbCustomInfoApiController extends ApiBaseController {
    private final MaindbCustomInfoService service;
    private final MaindbGroupRepository maindbGroupRepository;
    private final CommonFieldRepository commonFieldRepository;
    private final FileSystemStorageService fileSystemStorageService;
    private final WebSecureHistoryRepository webSecureHistoryRepository;
    @Value("${file.path.custom}")
    private String savePath;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<MaindbCustomInfoEntity>>> getPagination(MaindbDataSearchRequest search) {
        return ResponseEntity.ok(data(service.getRepository().pagination(search, true)));
    }

    @GetMapping("{groupSeq}/data")
    public ResponseEntity<JsonResult<Pagination<MaindbCustomInfoEntity>>> getPagination(@PathVariable Integer groupSeq, MaindbDataSearchRequest search) {
        search.setGroupSeq(groupSeq);
        return ResponseEntity.ok(data(service.getRepository().pagination(search, false)));
    }

    //고객DB조회.
    @GetMapping("customdb_group")
    public ResponseEntity<JsonResult<List<SearchMaindbGroupResponse>>> customdb_group() {
        final List<SearchMaindbGroupResponse> list = maindbGroupRepository.findAll().stream()
                .map((e) -> convertDto(e, SearchMaindbGroupResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(list));
    }

    //검색항목조회.
    @GetMapping("search_item")
    public ResponseEntity<JsonResult<List<ConCodeFieldResponse>>> search_item() {
        final List<ConCodeFieldResponse> list = commonFieldRepository.findAll().stream()
                .map((e) -> convertDto(e, ConCodeFieldResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(list));
    }

    @GetMapping("{customId}")
    public JsonResult<MaindbCustomInfoEntity> getType(@PathVariable String customId) {
        return JsonResult.data(service.getRepository().findOne(customId));
    }

/*
    //고객정보필드목록조회
    @GetMapping("{type}")
    public ResponseEntity<JsonResult<List<MaindbCustomFieldResponse>>> getType(@PathVariable Integer type) {
        return ResponseEntity.ok(data(service.getRepository(g.getUser().getCompanyId()).getType(type).stream()
                .map((e) -> convertDto(e, MaindbCustomFieldResponse.class))
                .collect(Collectors.toList())
        ));
        //return ResponseEntity.ok(data(convertDto(cfrepository.getType(type), MaindbCustomFieldResponse.class)));
    }
*/

    //고객정보Insert
    @PostMapping
    public JsonResult<String> post(@Valid @RequestBody MaindbCustomInfoFormRequest form, BindingResult bindingResult) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        return data(service.getRepository().insert(form));
    }

    //고객정보Update
    @PutMapping(value = "{id}")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody MaindbCustomInfoFormRequest form, BindingResult bindingResult,
                                                @PathVariable String id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        service.getRepository().update(form, id);
        return ResponseEntity.ok(create());
    }

    //삭제
    @DeleteMapping("{id}")
    public ResponseEntity<JsonResult<Void>> deleteData(@PathVariable String id) {
        service.deleteWithFileStore(id);
        return ResponseEntity.ok(create());
    }

    /**
     * 파일 다운로드
     */
    @GetMapping(value = "resource", params = {"token"})
    public ResponseEntity<Resource> specificFileResource(@RequestParam("path") String path) {

        final Resource resource = this.fileSystemStorageService.loadAsResource(Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId())), getName(path));
        webSecureHistoryRepository.insert(WebSecureActionType.CUSTOM, WebSecureActionSubType.DOWN, getName(path));

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
                .headers(header -> header.add(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.builder("attachment").filename(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8).build().toString()))
                .body(resource);
    }
}
