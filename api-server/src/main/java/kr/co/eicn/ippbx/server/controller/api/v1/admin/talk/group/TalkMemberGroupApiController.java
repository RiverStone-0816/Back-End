package kr.co.eicn.ippbx.server.controller.api.v1.admin.talk.group;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.*;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.entity.eicn.TalkMemberListEntity;
import kr.co.eicn.ippbx.model.enums.IdType;
import kr.co.eicn.ippbx.model.form.TalkMemberGroupFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.TalkMemberGroupRepository;
import kr.co.eicn.ippbx.server.repository.eicn.TalkMemberListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.TalkServiceInfoRepository;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PersonList.PERSON_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkMemberGroup.TALK_MEMBER_GROUP;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkMemberList.TALK_MEMBER_LIST;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담톡관리 > 상담톡그룹/자동멘트관리 > 상담톡수신그룹관리
 */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/talk/group/reception-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class TalkMemberGroupApiController extends ApiBaseController {

	private final TalkMemberGroupRepository repository;
	private final TalkMemberListRepository talkMemberListRepository;
	private final TalkServiceInfoRepository talkServiceInfoRepository;
	private final PersonListRepository personListRepository;
	private final OrganizationService organizationService;

	/**
	 * 상담톡수신그룹관리 목록조회
	 */
	@GetMapping("")
	public ResponseEntity<JsonResult<List<TalkMemberGroupSummaryResponse>>> list() {
		final Map<String, String> talkServiceMap = talkServiceInfoRepository.findAll().stream().collect(Collectors.toMap(TalkServiceInfo::getSenderKey, TalkServiceInfo::getKakaoServiceName));
		final List<TalkMemberListEntity> talkMemberLists = talkMemberListRepository.getTalkMemberListEntities();

		final List<TalkMemberGroupSummaryResponse> list = repository.findAll().stream()
				.map((e) -> {
					final TalkMemberGroupSummaryResponse talkMemberGroupSummaryResponse = convertDto(e, TalkMemberGroupSummaryResponse.class);
					talkMemberGroupSummaryResponse.setKakaoServiceName(talkServiceMap.get(e.getSenderKey()));

					final List<SummaryTalkGroupPersonResponse> persons = talkMemberLists.stream()
							.filter(person -> person.getGroupId().equals(e.getGroupId()))
							.map(person -> convertDto(person.getPerson(), SummaryTalkGroupPersonResponse.class))
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
	public ResponseEntity<JsonResult<TalkMemberGroupDetailResponse>> get(@PathVariable Integer groupId) {
		final TalkMemberGroup entity = repository.findOneIfNullThrow(groupId);
		final Map<String, String> talkServiceMap = talkServiceInfoRepository.findAll().stream().collect(Collectors.toMap(TalkServiceInfo::getSenderKey, TalkServiceInfo::getKakaoServiceName));
		final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();
		final List<TalkMemberListEntity> talkMemberLists = talkMemberListRepository.getTalkMemberListEntities();

		final TalkMemberGroupDetailResponse detail = convertDto(entity, TalkMemberGroupDetailResponse.class);

		detail.setKakaoServiceName(talkServiceMap.get((entity.getSenderKey())));
		detail.setPersons(
				talkMemberLists.stream()
						.filter(e -> e.getGroupId().equals(entity.getGroupId()))
						.map(e -> {
							final SummaryTalkGroupPersonResponse summaryTalkGroupPersonResponse = convertDto(e.getPerson(), SummaryTalkGroupPersonResponse.class);
							companyTrees.stream()
										.filter(companyTree -> companyTree.getGroupCode().equals(e.getPerson().getGroupCode()))
										.findFirst()
										.ifPresent(companyTree -> summaryTalkGroupPersonResponse.setOrganization(convertDto(companyTree, OrganizationSummaryResponse.class)));
							return summaryTalkGroupPersonResponse;
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
				.body(data(repository.insertOnGeneratedKey(form).getValue(TALK_MEMBER_GROUP.GROUP_ID)));
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
	public ResponseEntity<JsonResult<List<SummaryTalkServiceResponse>>> talkServices() {
		return ResponseEntity.ok(data(talkServiceInfoRepository.findAll().stream().map(e -> convertDto(e, SummaryTalkServiceResponse.class)).collect(Collectors.toList())));
	}

	/**
	 * 추가 가능한 사용자 목록조회
	 */
	@GetMapping("add-on-persons")
	public ResponseEntity<JsonResult<List<SummaryTalkGroupPersonResponse>>> addOnPersons(@RequestParam(required = false) Integer groupId) {
		final List<TalkMemberList> talkMembers = Objects.nonNull(groupId) ? talkMemberListRepository.findAll(TALK_MEMBER_LIST.GROUP_ID.eq(groupId)) : Collections.emptyList();
		final List<PersonList> personLists = personListRepository.findAll(PERSON_LIST.ID_TYPE.ne(IdType.MASTER.getCode())).stream().sorted(Comparator.comparing(PersonList::getIdName)).collect(Collectors.toList());
		final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();

		return ResponseEntity.ok(data(personLists.stream()
				.filter(e -> talkMembers.stream().noneMatch(member -> e.getId().equals(member.getUserid())))
				.map((e) -> {
					final SummaryTalkGroupPersonResponse summaryTalkGroupPersonResponse = convertDto(e, SummaryTalkGroupPersonResponse.class);
					companyTrees.stream()
							.filter(companyTree -> companyTree.getGroupCode().equals(e.getGroupCode()))
							.findFirst()
							.ifPresent(companyTree -> summaryTalkGroupPersonResponse.setOrganization(convertDto(companyTree, OrganizationSummaryResponse.class)));
					return summaryTalkGroupPersonResponse;
				})
				.collect(Collectors.toList())
		));
	}
}
