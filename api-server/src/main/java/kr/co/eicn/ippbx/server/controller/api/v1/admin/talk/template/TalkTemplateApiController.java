package kr.co.eicn.ippbx.server.controller.api.v1.admin.talk.template;

import kr.co.eicn.ippbx.model.entity.eicn.TalkTemplateEntity;
import kr.co.eicn.ippbx.model.search.TemplateSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.dto.eicn.TalkTemplateSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchPersonListResponse;
import kr.co.eicn.ippbx.model.enums.TalkTemplate;
import kr.co.eicn.ippbx.model.form.TalkTemplateFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CompanyInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.TalkTemplateRepository;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkTemplate.TALK_TEMPLATE;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담톡관리 > 상담톡템플릿관리
 */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/talk/template", produces = MediaType.APPLICATION_JSON_VALUE)
public class TalkTemplateApiController extends ApiBaseController {

    private final TalkTemplateRepository repository;
    private final PersonListRepository personListRepository;
    private final CompanyInfoRepository companyInfoRepository;
    private final OrganizationService organizationService;

    //리스트
    @GetMapping("list")
    public ResponseEntity<JsonResult<List<TalkTemplateSummaryResponse>>> list() {
        final Map<String, String> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, PersonList::getIdName));
        final Map<String, String> companyInfoMap = companyInfoRepository.findAll().stream().collect(Collectors.toMap(CompanyInfo::getCompanyId, CompanyInfo::getCompanyName));
        final Map<String, CompanyTree> companyTreeMap = organizationService.getAllCompanyTrees().stream().collect(Collectors.toMap(CompanyTree::getGroupCode, e -> e));

        final List<TalkTemplateSummaryResponse> list = repository.list().stream()
                .map((e) -> {
                    final TalkTemplateSummaryResponse talkTemplateSummaryResponse = convertDto(e, TalkTemplateSummaryResponse.class);
                    talkTemplateSummaryResponse.setWriteUserName(Objects.nonNull(personListMap.get(e.getWriteUserid()))
                            ? personListMap.get(e.getWriteUserid()) : personListRepository.findOneById(e.getWriteUserid()).getIdName());

                    if (Objects.equals(TalkTemplate.PERSON.getCode(), e.getType()))
                        talkTemplateSummaryResponse.setTypeData(Objects.nonNull(personListMap.get(e.getTypeData())) ?
                                personListMap.get(e.getTypeData()) : personListRepository.findOneById(e.getWriteUserid()).getIdName());
                    else if (Objects.equals(TalkTemplate.COMPANY.getCode(), e.getType()))
                        talkTemplateSummaryResponse.setTypeData(companyInfoMap.get(e.getTypeData()));
                    else {
                        if (Objects.nonNull(companyTreeMap.get(e.getTypeData()))) {
                            final CompanyTree companyTree = companyTreeMap.get(e.getTypeData());
                            talkTemplateSummaryResponse.setTypeData(companyTree.getGroupName());
                            talkTemplateSummaryResponse.setCompanyTreeLevel(companyTree.getGroupLevel());
                        }
                    }
                    return talkTemplateSummaryResponse;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(list));
    }

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<TalkTemplateSummaryResponse>>> getPagination(TemplateSearchRequest search) {
        final Map<String, String> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, PersonList::getIdName));
        final Map<String, String> companyInfoMap = companyInfoRepository.findAll().stream().collect(Collectors.toMap(CompanyInfo::getCompanyId, CompanyInfo::getCompanyName));
        final Map<String, CompanyTree> companyTreeMap = organizationService.getAllCompanyTrees().stream().collect(Collectors.toMap(CompanyTree::getGroupCode, e -> e));

        final Pagination<TalkTemplateEntity> pagination = repository.pagination(search);

        final List<TalkTemplateSummaryResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final TalkTemplateSummaryResponse talkTemplateSummaryResponse = convertDto(e, TalkTemplateSummaryResponse.class);
                    talkTemplateSummaryResponse.setWriteUserName(Objects.nonNull(personListMap.get(e.getWriteUserid()))
                            ? personListMap.get(e.getWriteUserid()) : personListRepository.findOneById(e.getWriteUserid()).getIdName());

                    if (Objects.equals(TalkTemplate.PERSON.getCode(), e.getType()))
                        talkTemplateSummaryResponse.setTypeData(Objects.nonNull(personListMap.get(e.getTypeData())) ?
                                personListMap.get(e.getTypeData()) : personListRepository.findOneById(e.getWriteUserid()).getIdName());
                    else if (Objects.equals(TalkTemplate.COMPANY.getCode(), e.getType()))
                        talkTemplateSummaryResponse.setTypeData(companyInfoMap.get(e.getTypeData()));
                    else {
                        if (Objects.nonNull(companyTreeMap.get(e.getTypeData()))) {
                            final CompanyTree companyTree = companyTreeMap.get(e.getTypeData());
                            talkTemplateSummaryResponse.setTypeData(companyTree.getGroupName());
                            talkTemplateSummaryResponse.setCompanyTreeLevel(companyTree.getGroupLevel());
                        }
                    }
                    return talkTemplateSummaryResponse;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(),search.getLimit())));
    }

    //수정을 위한 SEQ조회
    @GetMapping("{seq}")
    public ResponseEntity<JsonResult<TalkTemplateSummaryResponse>> get(@PathVariable Integer seq) {
        return ResponseEntity.ok(data(convertDto(repository.findOneIfNullThrow(seq), TalkTemplateSummaryResponse.class)));
    }

    //템플릿 추가
    @PostMapping("")
    public ResponseEntity<JsonResult<Integer>> post(@Valid @RequestBody TalkTemplateFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        return ResponseEntity.created(URI.create("api/v1/admin/talk/template"))
                .body(data(repository.insertOnGeneratedKey(form).getValue(TALK_TEMPLATE.SEQ)));
    }

    //템플릿 수정
    @PutMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody TalkTemplateFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.updateByKey(form, seq);
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
}
