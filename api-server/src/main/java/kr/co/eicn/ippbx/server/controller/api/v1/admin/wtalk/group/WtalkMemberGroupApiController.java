package kr.co.eicn.ippbx.server.controller.api.v1.admin.wtalk.group;

import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.*;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.entity.eicn.WtalkMemberListEntity;
import kr.co.eicn.ippbx.model.enums.IdType;
import kr.co.eicn.ippbx.model.enums.TalkMemberDistributionType;
import kr.co.eicn.ippbx.model.form.TalkMemberGroupFormRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WtalkMemberGroupRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WtalkMemberListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WtalkServiceInfoRepository;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PersonList.PERSON_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkMemberGroup.WTALK_MEMBER_GROUP;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkMemberList.WTALK_MEMBER_LIST;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담톡관리 > 상담톡그룹/자동멘트관리 > 상담톡수신그룹관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/wtalk/group/reception-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class WtalkMemberGroupApiController extends ApiBaseController {

	private final WtalkMemberGroupRepository repository;
	private final WtalkMemberListRepository wtalkMemberListRepository;
	private final WtalkServiceInfoRepository wtalkServiceInfoRepository;
	private final PersonListRepository personListRepository;
	private final OrganizationService organizationService;

	/**
	 * 상담톡수신그룹관리 목록조회
	 */
	@GetMapping("")
	public ResponseEntity<JsonResult<List<WtalkMemberGroupSummaryResponse>>> list() {
		final Map<String, String> talkServiceMap = wtalkServiceInfoRepository.findAll().stream().collect(Collectors.toMap(WtalkServiceInfo::getSenderKey, WtalkServiceInfo::getKakaoServiceName));
		final List<WtalkMemberListEntity> talkMemberLists = wtalkMemberListRepository.getTalkMemberListEntities();

		final List<WtalkMemberGroupSummaryResponse> list = repository.findAll().stream()
				.map((e) -> {
					final WtalkMemberGroupSummaryResponse talkMemberGroupSummaryResponse = convertDto(e, WtalkMemberGroupSummaryResponse.class);

					final List<SummaryWtalkGroupPersonResponse> persons = talkMemberLists.stream()
							.filter(person -> person.getGroupId().equals(e.getGroupId()))
							.map(person -> {
								SummaryWtalkGroupPersonResponse summaryWtalkGroupPersonResponse = convertDto(person.getPerson(), SummaryWtalkGroupPersonResponse.class);

								summaryWtalkGroupPersonResponse.setDistributionSequence(person.getDistSequence());

								return summaryWtalkGroupPersonResponse;
							})
							.collect(Collectors.toList());

					talkMemberGroupSummaryResponse.setPersons(persons);
					talkMemberGroupSummaryResponse.setMemberCnt(persons.size());

					return talkMemberGroupSummaryResponse;
				})
				.collect(Collectors.toList());

		return ResponseEntity.ok(data(list));
	}

	/**
	 * 상담톡수신그룹관리 상세조회
	 */
	@GetMapping("{groupId}")
	public ResponseEntity<JsonResult<WtalkMemberGroupDetailResponse>> get(@PathVariable Integer groupId) {
		final WtalkMemberGroup entity = repository.findOneIfNullThrow(groupId);
		final Map<String, String> talkServiceMap = wtalkServiceInfoRepository.findAll().stream().collect(Collectors.toMap(WtalkServiceInfo::getSenderKey, WtalkServiceInfo::getKakaoServiceName));
		final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();
		final List<WtalkMemberListEntity> talkMemberLists = wtalkMemberListRepository.getTalkMemberListEntities();

		final WtalkMemberGroupDetailResponse detail = convertDto(entity, WtalkMemberGroupDetailResponse.class);

		detail.setDistributionPolicy(TalkMemberDistributionType.of(entity.getTalkStrategy()));
		detail.setPersons(
				talkMemberLists.stream()
						.filter(e -> e.getGroupId().equals(entity.getGroupId()))
						.map(e -> {
							final SummaryWtalkGroupPersonResponse summaryWtalkGroupPersonResponse = convertDto(e.getPerson(), SummaryWtalkGroupPersonResponse.class);
							companyTrees.stream()
										.filter(companyTree -> companyTree.getGroupCode().equals(e.getPerson().getGroupCode()))
										.findFirst()
										.ifPresent(companyTree -> summaryWtalkGroupPersonResponse.setOrganization(convertDto(companyTree, OrganizationSummaryResponse.class)));
							return summaryWtalkGroupPersonResponse;
						})
						.collect(Collectors.toList())
		);

		return ResponseEntity.ok(data(detail));
	}

	/**
	 * 상담톡수신그룹관리 추가하기
	 */
	@PostMapping("")
	public ResponseEntity<JsonResult<Integer>> post(@Valid @RequestBody TalkMemberGroupFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		return ResponseEntity.created(URI.create("api/v1/admin/talk/group/reception-group"))
				.body(data(repository.insertOnGeneratedKey(form).getValue(WTALK_MEMBER_GROUP.GROUP_ID)));
	}

	/**
	 * 상담톡수신그룹관리 수정하기
	 */
	@PutMapping("{groupId}")
	public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody TalkMemberGroupFormRequest form, BindingResult bindingResult, @PathVariable Integer groupId) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.updateByKey(form, groupId);
		return ResponseEntity.ok(create());
	}

	/**
	 * 상담톡수신그룹관리 삭제하기
	 */
	@DeleteMapping("{groupId}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer groupId) {
		repository.deleteWithPersons(groupId);
		return ResponseEntity.ok(create());
	}

	/**
	 * 관련상담톡서비스 목록조회
	 */
	@GetMapping("add-services")
	public ResponseEntity<JsonResult<List<SummaryWtalkServiceResponse>>> talkServices() {
		return ResponseEntity.ok(data(wtalkServiceInfoRepository.findAll().stream().map(e -> convertDto(e, SummaryWtalkServiceResponse.class)).collect(Collectors.toList())));
	}

	/**
	 * 추가 가능한 사용자 목록조회
	 */
	@GetMapping("add-on-persons")
	public ResponseEntity<JsonResult<List<SummaryWtalkGroupPersonResponse>>> addOnPersons(@RequestParam(required = false) Integer groupId) {
		final List<WtalkMemberList> talkMembers = Objects.nonNull(groupId) ? wtalkMemberListRepository.findAll(WTALK_MEMBER_LIST.GROUP_ID.eq(groupId)) : Collections.emptyList();
		final List<PersonList> personLists = personListRepository.findAll(PERSON_LIST.ID_TYPE.ne(IdType.MASTER.getCode())).stream().sorted(Comparator.comparing(PersonList::getIdName)).collect(Collectors.toList());
		final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();

		return ResponseEntity.ok(data(personLists.stream()
				.filter(e -> talkMembers.stream().noneMatch(member -> e.getId().equals(member.getUserid())))
				.map((e) -> {
					final SummaryWtalkGroupPersonResponse summaryWtalkGroupPersonResponse = convertDto(e, SummaryWtalkGroupPersonResponse.class);
					companyTrees.stream()
							.filter(companyTree -> companyTree.getGroupCode().equals(e.getGroupCode()))
							.findFirst()
							.ifPresent(companyTree -> summaryWtalkGroupPersonResponse.setOrganization(convertDto(companyTree, OrganizationSummaryResponse.class)));
					return summaryWtalkGroupPersonResponse;
				})
				.collect(Collectors.toList())
		));
	}
}