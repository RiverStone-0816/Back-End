package kr.co.eicn.ippbx.server.controller.api.v1.admin.wtalk.template;

import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkTemplateSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchPersonListResponse;
import kr.co.eicn.ippbx.model.entity.eicn.WtalkTemplateEntity;
import kr.co.eicn.ippbx.model.enums.TalkTemplate;
import kr.co.eicn.ippbx.model.form.TalkTemplateFormRequest;
import kr.co.eicn.ippbx.model.search.TemplateSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.repository.eicn.CompanyInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WtalkTemplateRepository;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.server.service.WtalkTemplateFileUploadService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담톡관리 > 상담톡템플릿관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/wtalk/template", produces = MediaType.APPLICATION_JSON_VALUE)
public class WtalkTemplateApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(WtalkTemplateApiController.class);

    private final WtalkTemplateRepository repository;
    private final PersonListRepository personListRepository;
    private final CompanyInfoRepository companyInfoRepository;
    private final OrganizationService organizationService;
    private final WtalkTemplateFileUploadService service;

    //리스트
    @GetMapping("list")
    public ResponseEntity<JsonResult<List<WtalkTemplateSummaryResponse>>> list(TemplateSearchRequest search) {
        final Map<String, String> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, PersonList::getIdName));
        final Map<String, String> companyInfoMap = companyInfoRepository.findAll().stream().collect(Collectors.toMap(CompanyInfo::getCompanyId, CompanyInfo::getCompanyName));
        final Map<String, CompanyTree> companyTreeMap = organizationService.getAllCompanyTrees().stream().collect(Collectors.toMap(CompanyTree::getGroupCode, e -> e));


        final List<WtalkTemplateSummaryResponse> list = repository.list().stream()
                .filter(e -> {
                    if (search.getIsMy()) {
                        if (TalkTemplate.PERSON.getCode().equals(e.getType()) && g.getUser().getId().equals(e.getTypeData()))
                            return true;
                        else if (TalkTemplate.GROUP.getCode().equals(e.getType()) && companyTreeMap.containsKey(e.getTypeData()) && companyTreeMap.get(e.getTypeData()).getGroupTreeName().contains(g.getUser().getGroupCode()))
                            return true;
                        else return TalkTemplate.COMPANY.getCode().equals(e.getType()) && g.getUser().getCompanyId().equals(e.getTypeData());
                    } else
                        return true;
                })
                .map((e) -> {
                    final WtalkTemplateSummaryResponse talkTemplateSummaryResponse = convertDto(e, WtalkTemplateSummaryResponse.class);
                    talkTemplateSummaryResponse.setWriteUserName(Objects.nonNull(personListMap.get(e.getWriteUserid()))
                            ? personListMap.get(e.getWriteUserid()) : personListRepository.findOneById(e.getWriteUserid()).getIdName());

                    if (Objects.equals(TalkTemplate.GROUP.getCode(), e.getType()))
                        talkTemplateSummaryResponse.setTypeGroup(companyTreeMap.containsKey(e.getTypeData()) ? companyTreeMap.get(e.getTypeData()).getGroupTreeName() : "");
                    else if (Objects.equals(TalkTemplate.PERSON.getCode(), e.getType()))

                        talkTemplateSummaryResponse.setTypeDataName(Objects.nonNull(personListMap.get(e.getTypeData())) ?
                                personListMap.get(e.getTypeData()) : personListRepository.findOneById(e.getWriteUserid()).getIdName());
                    else if (Objects.equals(TalkTemplate.COMPANY.getCode(), e.getType()))
                        talkTemplateSummaryResponse.setTypeDataName(companyInfoMap.get(e.getTypeData()));
                    else {
                        if (companyTreeMap.containsKey(e.getTypeData())) {
                            final CompanyTree companyTree = companyTreeMap.get(e.getTypeData());
                            talkTemplateSummaryResponse.setTypeDataName(companyTree.getGroupName());
                            talkTemplateSummaryResponse.setCompanyTreeLevel(companyTree.getGroupLevel());
                        }
                    }
                    return talkTemplateSummaryResponse;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(list));
    }

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<WtalkTemplateSummaryResponse>>> getPagination(TemplateSearchRequest search) {

        final Map<String, String> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, PersonList::getIdName));
        final Map<String, String> companyInfoMap = companyInfoRepository.findAll().stream().collect(Collectors.toMap(CompanyInfo::getCompanyId, CompanyInfo::getCompanyName));
        final Map<String, CompanyTree> companyTreeMap = organizationService.getAllCompanyTrees().stream().collect(Collectors.toMap(CompanyTree::getGroupCode, e -> e));

        final Pagination<WtalkTemplateEntity> pagination = repository.pagination(search);

        final List<WtalkTemplateSummaryResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final WtalkTemplateSummaryResponse talkTemplateSummaryResponse = convertDto(e, WtalkTemplateSummaryResponse.class);
                    talkTemplateSummaryResponse.setWriteUserName(Objects.nonNull(personListMap.get(e.getWriteUserid()))
                            ? personListMap.get(e.getWriteUserid()) : personListRepository.findOneById(e.getWriteUserid()).getIdName());

                    if (Objects.equals(TalkTemplate.GROUP.getCode(), e.getType()))
                        talkTemplateSummaryResponse.setTypeGroup(companyTreeMap.containsKey(e.getTypeData()) ? companyTreeMap.get(e.getTypeData()).getGroupTreeName() : "");
                    else if (Objects.equals(TalkTemplate.PERSON.getCode(), e.getType()))
                        talkTemplateSummaryResponse.setTypeDataName(Objects.nonNull(personListMap.get(e.getTypeData())) ?
                                personListMap.get(e.getTypeData()) : personListRepository.findOneById(e.getWriteUserid()).getIdName());
                    else if (Objects.equals(TalkTemplate.COMPANY.getCode(), e.getType()))
                        talkTemplateSummaryResponse.setTypeDataName(companyInfoMap.get(e.getTypeData()));
                    else {
                        if (companyTreeMap.containsKey(e.getTypeData())) {
                            final CompanyTree companyTree = companyTreeMap.get(e.getTypeData());
                            talkTemplateSummaryResponse.setTypeDataName(companyTree.getGroupName());
                            talkTemplateSummaryResponse.setCompanyTreeLevel(companyTree.getGroupLevel());
                        }
                    }
                    return talkTemplateSummaryResponse;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    //수정을 위한 SEQ조회
    @GetMapping("{seq}")
    public ResponseEntity<JsonResult<WtalkTemplateSummaryResponse>> get(@PathVariable Integer seq) {
        return ResponseEntity.ok(data(convertDto(repository.findOneIfNullThrow(seq), WtalkTemplateSummaryResponse.class)));
    }

    //템플릿 추가
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResult<String>> post(@Valid @ModelAttribute TalkTemplateFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        final String uploadFilename = service.insertTalkTemplateFileUpload(form);

        return ResponseEntity.created(URI.create("api/v1/admin/wtalk/template"))
                .body(data(uploadFilename));
    }

    //템플릿 수정
    @PutMapping(value = "{seq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResult<Void>> put(@Valid @ModelAttribute TalkTemplateFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        service.updateTalkTemplateFileUpload(form, seq);
        return ResponseEntity.ok(create());
    }

    //삭제
    @DeleteMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        repository.deleteOnIfNullThrow(seq);
        return ResponseEntity.ok(create());
    }

    //상담원목록불러오기
    @GetMapping("person")
    public ResponseEntity<JsonResult<List<SearchPersonListResponse>>> person() {
        final List<SearchPersonListResponse> list = personListRepository.findAll().stream()
                .map((e) -> convertDto(e, SearchPersonListResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(list));
    }

    @GetMapping("image")
    public ResponseEntity<Resource> getImage(@RequestParam String filePath) {
        final Resource resource = service.getImage(filePath);

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
                .headers(header -> header.setCacheControl(CacheControl.noCache().getHeaderValue()))
                .body(resource);
    }
}
