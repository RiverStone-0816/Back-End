package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.pds;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.*;
import kr.co.eicn.ippbx.server.model.dto.eicn.*;
import kr.co.eicn.ippbx.server.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.server.model.enums.ServerType;
import kr.co.eicn.ippbx.server.model.form.*;
import kr.co.eicn.ippbx.server.model.search.PDSResultGroupSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.server.util.FunctionUtils;
import kr.co.eicn.ippbx.server.util.JsonResult;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.impl.DSL;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.PersonList.PERSON_LIST;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.PhoneInfo.PHONE_INFO;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.QueueMemberTable.QUEUE_MEMBER_TABLE;
import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 *  아웃바운드관리 > PDS > 상담그룹설정
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/pds/result-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class PDSResultGroupApiController extends ApiBaseController {

	private final PDSQueueNameRepository repository;
	private final QueueMemberTableRepository queueMemberTableRepository;
	private final PhoneInfoRepository phoneInfoRepository;
	private final ServerInfoRepository serverInfoRepository;
	private final PersonListRepository personListRepository;
	private final OrganizationService organizationService;
	private final CompanyServerRepository companyServerRepository;
	private final ContextInfoRepository contextInfoRepository;

	private final CacheService cacheService;

	/**
	 *   상담그룹설정 목록조회
	 */
	@GetMapping("")
	public ResponseEntity<JsonResult<Pagination<PDSResultGroupSummaryResponse>>> pagination(PDSResultGroupSearchRequest search) {
		final List<CompanyServerEntity> companyServerEntities = cacheService.getCompanyServerList(g.getUser().getCompanyId());
		final List<QueueMemberTable> queueMemberTables = queueMemberTableRepository.findAllPDSMember();

		final Pagination<PdsQueueName> pagination = repository.pagination(search);
		final List<PDSResultGroupSummaryResponse> rows = pagination.getRows().stream()
				.map((e) -> {
					PDSResultGroupSummaryResponse response = convertDto(e, PDSResultGroupSummaryResponse.class);

					response.setCnt((int) queueMemberTables.stream().filter(q -> q.getQueueName().equals(response.getName())).count());

					if (Objects.nonNull(e.getHost())) {
						companyServerEntities.stream()
								.filter(company -> company.getServer().getHost().equals(e.getHost()))
								.findAny()
								.ifPresent(r -> response.setHostName(r.getServer().getName()));
					}
					return response;
				})
				.collect(Collectors.toList());

		return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
	}

	@GetMapping("{name}")
	public ResponseEntity<JsonResult<PDSResultGroupDetailResponse>> get(@PathVariable String name) {
		final PdsQueueName queue = repository.findOneIfNullThrow(name);
		final PDSResultGroupDetailResponse detail = convertDto(repository.findOne(name), PDSResultGroupDetailResponse.class);
		final List<ServerInfo> serverInfos = serverInfoRepository.findAll();

		final List<QueueMemberTable> queueMemberTables = queueMemberTableRepository.findAll(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queue.getName()), Arrays.asList(QUEUE_MEMBER_TABLE.PENALTY.asc(), QUEUE_MEMBER_TABLE.UNIQUEID.asc()));

		//final List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PhoneInfo> phoneInfos = phoneInfoRepository.findAll(PHONE_INFO.HOST.eq(queue.getHost()));

		final Map<String, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList> personListMap = personListRepository.findAll(PERSON_LIST.IS_PDS.eq("Y"))
				.stream()
				.collect(Collectors.toMap(kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList::getId, e -> e));

		// 추가된 사용자
		if (queueMemberTables.size() > 0) {
			final List<SummaryQueuePersonResponse> addPersons  = queueMemberTables.stream()
					.map((e) -> {
						final SummaryQueuePersonResponse summaryPerson = convertDto(e, SummaryQueuePersonResponse.class);
						summaryPerson.setUserId(e.getMembername());

						final PersonList personList = personListMap.get(e.getMembername());
						if (personList != null) {
							summaryPerson.setIdName(personList.getIdName());
							summaryPerson.setPeer(personList.getPeer());
							summaryPerson.setExtension(personList.getExtension());
						}
						else
							summaryPerson.setIdName("");

						if (isNotEmpty(queue.getHost())) {
							serverInfos.stream().filter(server -> isNotEmpty(server.getHost()) && server.getHost().equals(queue.getHost())).findFirst()
									.ifPresent(server -> summaryPerson.setHostName(server.getName()));
						}

						return summaryPerson;
					})
					.collect(toList());

			detail.setAddPersons(addPersons);
		}

		return ResponseEntity.ok(data(detail));
	}

	@PostMapping("")
	public ResponseEntity<JsonResult<String>> post(@Valid @RequestBody PDSResultGroupFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		return ResponseEntity.created(URI.create("/api/v1/admin/outbound/pds/result-group")).body(data(repository.insertOnGeneratedKey(form)));
	}

	@PutMapping("{name}")
	public ResponseEntity<JsonResult<Void>> update(@Valid @RequestBody PDSResultGroupUpdateFormRequest form, BindingResult bindingResult, @PathVariable String name) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		form.setName(name);
		repository.update(form);
		return ResponseEntity.ok(create());
	}

	@DeleteMapping("{name}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable String name) {
		repository.delete(name);
		return ResponseEntity.ok(create());
	}

	/**
	 * 실행할교환기 목록 조회 //PDSGroupApiController참조
	 */
	@GetMapping("add-server")
	public ResponseEntity<JsonResult<List<SummaryCompanyServerResponse>>> addServerLists() {
		return ResponseEntity.ok(data(companyServerRepository.findAllType(ServerType.PBX)
				.stream()
				.map(e -> {
					SummaryCompanyServerResponse response = convertDto(e, SummaryCompanyServerResponse.class);
					response.setName(e.getServer().getName());
					return response;
				})
				.collect(Collectors.toList()))
		);
	}

	/**
	 *  컨텍스트 목록 조회//QueueApiController 참조
	 */
	@GetMapping("context")
	public ResponseEntity<JsonResult<List<SummaryContextInfoResponse>>> context() {
		return ResponseEntity.ok(
				data(contextInfoRepository.findAll().stream()
						.map((e) -> convertDto(e, SummaryContextInfoResponse.class))
						.sorted(comparing(SummaryContextInfoResponse::getName))
						.collect(Collectors.toList())
				)
		);
	}

	/**
	 *  추가 가능한 사용자 목록조회 //QueueApiController 참조
	 */
	@GetMapping("add-on-persons")
	public ResponseEntity<JsonResult<List<SummaryPersonResponse>>> addOnPersons(@RequestParam(required = false) String name) {
		PdsQueueName queue = isNotEmpty(name) ? repository.findOneIfNullThrow(name) : null;
		//final List<ServerInfo> serverInfos = serverInfoRepository.findAll();
		final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();

		final List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PhoneInfo> phoneInfos = phoneInfoRepository.findAll(queue != null
						? PHONE_INFO.HOST.eq(queue.getHost()) : DSL.noCondition()
				, PHONE_INFO.EXTENSION.asc());

		final List<QueueMemberTable> queueMemberTables = queueMemberTableRepository.findAll(queue != null
						? QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queue.getName()) : DSL.noCondition()
				, Arrays.asList(QUEUE_MEMBER_TABLE.PENALTY.asc(), QUEUE_MEMBER_TABLE.UNIQUEID.asc()));

/*
		final Map<String, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList> personListMap = personListRepository.findAll()
				.stream()
				.collect(Collectors.toMap(kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList::getExtension, e -> e));
*/
		final List<SummaryPersonResponse> persons = personListRepository.findAll(PERSON_LIST.IS_PDS.eq("Y")).stream()
				.map((e) -> {
					final SummaryPersonResponse summaryPerson = convertDto(e, SummaryPersonResponse.class);

					summaryPerson.setUserId(e.getId());
					summaryPerson.setIdName(e.getIdName());
					summaryPerson.setExtension(isNotEmpty(e.getExtension()) ? e.getExtension() : EMPTY);
					summaryPerson.setPeer(isNotEmpty(e.getPeer()) ? e.getExtension() : EMPTY);
					if(queue != null) {
						summaryPerson.setHostName(queue.getHost());
					}
					if (isNotEmpty(e.getGroupCode()))
						summaryPerson.setCompanyTrees(organizationService.getCompanyTrees(companyTrees, e.getGroupCode())
								.stream()
								.map(group -> convertDto(group, OrganizationSummaryResponse.class))
								.collect(toList()));
					return summaryPerson;
				}).collect(toList());

		//!!@@##PhoneInfo 필요 없어서 혹시 나중을 위한 주석 *****필요 없음이 확인 되면 주석 다 삭제 해도 무방.
		/*final List<SummaryPersonResponse> persons = phoneInfos
				.stream()
				.filter(e -> {
							if (queue != null) {
								return queueMemberTables.stream()
										.noneMatch(queueMemberTable -> queueMemberTable.getMembername().equals(e.getPeer()));
							}
							return true;
						}
				)
				.map((e) -> {
					final SummaryPersonResponse summaryPerson = convertDto(e, SummaryPersonResponse.class);

					if (isNotEmpty(e.getHost()))
						serverInfos.stream().filter(server -> isNotEmpty(server.getHost()) && server.getHost().equals(e.getHost())).findFirst()
								.ifPresent(server -> summaryPerson.setHostName(server.getName()));

					final PersonList personList = personListMap.get(e.getExtension());
					summaryPerson.setIdName(EMPTY);

					if (personList != null) {
						summaryPerson.setUserId(personList.getId());
						summaryPerson.setIdName(personList.getIdName());

						if (isNotEmpty(personList.getGroupCode()))
							summaryPerson.setCompanyTrees(organizationService.getCompanyTrees(companyTrees, personList.getGroupCode())
									.stream()
									.map(group -> convertDto(group, OrganizationSummaryResponse.class))
									.collect(toList()));
					}

					return summaryPerson;
				})
				.collect(toList());*/

		return ResponseEntity.ok(data(persons));
	}

}
