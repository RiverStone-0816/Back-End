package kr.co.eicn.ippbx.server.controller.api.v1.admin.conf.info;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.*;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.enums.CallType;
import kr.co.eicn.ippbx.model.form.ConfInfoDuplicateFormRequest;
import kr.co.eicn.ippbx.model.form.ConfInfoFormRequest;
import kr.co.eicn.ippbx.model.form.ConfInfoMinutesSaveFormRequest;
import kr.co.eicn.ippbx.model.form.ConfInfoUpdateFormRequest;
import kr.co.eicn.ippbx.model.search.ConfInfoSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.server.service.StorageService;
import kr.co.eicn.ippbx.util.FunctionUtils;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.CONF_INFO;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ConfMember.CONF_MEMBER;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PersonList.PERSON_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PhoneInfo.PHONE_INFO;
import static kr.co.eicn.ippbx.util.JsonResult.Result.failure;
import static kr.co.eicn.ippbx.util.JsonResult.Result.success;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * 상담톡관리 > 상담톡정보관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/conf/info", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConfInfoApiController extends ApiBaseController {

	private final ConfInfoRepository repository;

	private final ConfRoomRepository confRoomRepository;
	private final Number070Repository numberRepository;
	private final CacheService cacheService;
	private final OrganizationService organizationService;

	private final PhoneInfoRepository phoneInfoRepository;
	private final PersonListRepository personListRepository;
	private final ConfMemberRepository confMemberRepository;

	private final SoundListRepository soundListRepository;

	@Value("${file.path.conf-minutes-save}")
	private String saveMinutesPath;
	private final StorageService fileSystemStorageService;

	/**
	 * 회의실 목록조회
	 */
	@GetMapping("confroom-list")
	public ResponseEntity<JsonResult<List<ConfRoomSummaryResponse>>> getConfRoomList(/*ConfRoomSearchRequest search*/) {
		final List<CompanyServerEntity> companyServerEntities = cacheService.getCompanyServerList(g.getUser().getCompanyId());
		Map<String, Number_070> numberStringObjectMap =
				numberRepository.findAll().stream().filter(FunctionUtils.distinctByKey(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070::getNumber)).collect(Collectors.toMap(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070::getNumber, e -> e));

		List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();
		final List<ConfRoom> confRooms = confRoomRepository.findAll();
		final List<ConfRoomSummaryResponse> rows = confRooms.stream()
				.map((e) -> {
					ConfRoomSummaryResponse response = convertDto(e, ConfRoomSummaryResponse.class);

					if (Objects.nonNull(numberStringObjectMap.get(e.getRoomNumber()))) {
						kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070 number_070 = numberStringObjectMap.get(e.getRoomNumber());
						companyServerEntities.stream()
								.filter(company -> company.getServer().getHost().equals(number_070.getHost()))
								.findAny()
								.ifPresent(r -> response.setHostName(r.getServer().getName()));
					}
					response.setCompanyTrees(organizationService.getCompanyTrees(companyTrees, e.getGroupCode()).stream().map(company -> convertDto(company, OrganizationSummaryResponse.class)).collect(Collectors.toList()));
					return response;
				})
				.collect(Collectors.toList());

		return ResponseEntity.ok(data(rows));
	}

	/**
	 * 회의실예약 목록조회
	 */
	@GetMapping("")
	public ResponseEntity<JsonResult<List<ConfInfoSummaryResponse>>> list(ConfInfoSearchRequest search) {
		final List<ConfInfoSummaryResponse> list = repository.search(search).stream()
				.map((e) -> convertDto(e, ConfInfoSummaryResponse.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(data(list));
	}

	/**
	 * 추가 가능한 내부참여자 목록조회
	 */
	@GetMapping("add_on_conf_persons")
	public ResponseEntity<JsonResult<List<SummaryPersonResponse>>> addOnConfPersons(@RequestParam(required = false) Integer seq) {
		final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();
		final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo> phoneInfos = phoneInfoRepository.findAll(DSL.noCondition(), PHONE_INFO.EXTENSION.asc());
		final List<ConfMember> confMembers = confMemberRepository.findAll(seq != null
						? CONF_MEMBER.CONF_KEY.eq(seq).and(CONF_MEMBER.MEMBER_TYPE.eq("I")) : DSL.noCondition()
				, Arrays.asList(CONF_MEMBER.MEMBER_NAME.asc(), CONF_MEMBER.SEQ.asc()));

		final Map<String, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList> personListMap = personListRepository.findAll(PERSON_LIST.EXTENSION.isNotNull().and(PERSON_LIST.EXTENSION.notEqual(EMPTY)))
				.stream()
				.collect(Collectors.toMap(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList::getExtension, e -> e));

		final List<SummaryPersonResponse> persons = phoneInfos
				.stream()
				.filter(e -> {
							if (seq != null) {
								return confMembers.stream()
										.noneMatch(confMember -> confMember.getMemberNumber().equals(e.getPeer()));
							}
							return true;
						}
				)
				.map((e) -> {
					final SummaryPersonResponse summaryPerson = convertDto(e, SummaryPersonResponse.class);

					/*if (isNotEmpty(e.getHost()))
						serverInfos.stream().filter(server -> isNotEmpty(server.getHost()) && server.getHost().equals(e.getHost())).findFirst()
								.ifPresent(server -> summaryPerson.setHostName(server.getName()));*/

					final PersonList personList = personListMap.get(e.getExtension());
					summaryPerson.setIdName(EMPTY);
					if (personList != null) {
						summaryPerson.setIdName(personList.getIdName());

						if (isNotEmpty(personList.getGroupCode()))
							summaryPerson.setCompanyTrees(organizationService.getCompanyTrees(companyTrees, personList.getGroupCode())
									.stream()
									.map(group -> convertDto(group, OrganizationSummaryResponse.class))
									.collect(toList()));

					}

					return summaryPerson;
				})
				.collect(toList());

		return ResponseEntity.ok(data(persons));
	}

	/**
	 *
	 * 회의참석시음원 목록 조회
	 */
	@GetMapping("add-sounds-list")
	public ResponseEntity<JsonResult<List<SummarySoundListResponse>>> addSoundList() {
		return ResponseEntity.ok(data(soundListRepository.findAll()
				.stream()
				.sorted(comparing(SoundList::getSoundName))
				.map(e -> convertDto(e, SummarySoundListResponse.class))
				.collect(Collectors.toList()))
		);
	}

	/**
	 * 회의예약 상세조회
	 */
	@GetMapping("{seq}")
	public ResponseEntity<JsonResult<ConfInfoDetailResponse>> get(@PathVariable Integer seq) {
		final ConfInfoDetailResponse detail = convertDto(repository.findOneIfNullThrow(seq), ConfInfoDetailResponse.class);
		final List<CompanyServerEntity> companyServerEntities = cacheService.getCompanyServerList(g.getUser().getCompanyId());
		final Map<String, Number_070> numberStringObjectMap = numberRepository.findAll().stream()
				.filter(FunctionUtils.distinctByKey(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070::getNumber))
				.collect(Collectors.toMap(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070::getNumber, e -> e));
		final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();
		final List<ConfMember> members = confMemberRepository.findAll(CONF_MEMBER.CONF_KEY.eq(seq));
		String aaa = detail.getReserveAdmin();
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList admin =  personListRepository.findOne(PERSON_LIST.ID.eq(aaa));
		if(admin != null) {
			if(admin.getIdName() != null && !admin.getIdName().equals("")) {
				detail.setReserveAdminName(admin.getIdName());
			} else {
				detail.setReserveAdminName(detail.getReserveAdmin());
			}
		} else {
			detail.setReserveAdminName(detail.getReserveAdmin());
		}

		final Map<String, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList> personListMap = personListRepository.findAll(PERSON_LIST.EXTENSION.isNotNull().and(PERSON_LIST.EXTENSION.notEqual(EMPTY)))
				.stream()
				.filter(FunctionUtils.distinctByKey(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList::getPeer))
				.collect(Collectors.toMap(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList::getPeer, e -> e));

		detail.setInMemberList(
				members.stream().filter(e -> CallType.INBOUND.getCode().equals(e.getMemberType()))
				.map(e -> {
					final SummaryPersonResponse response = convertDto(e, SummaryPersonResponse.class);
					final PersonList personList = personListMap.get(e.getMemberNumber());
					response.setIdName(EMPTY);
					response.setPeer(e.getMemberNumber());

					if (personList != null) {
						response.setIdName(personList.getIdName());
						response.setPeer(personList.getPeer());
						response.setExtension(personList.getExtension());

						if (Objects.nonNull(numberStringObjectMap.get(e.getRoomNumber()))) {
							final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070 number_070 = numberStringObjectMap.get(e.getRoomNumber());
							companyServerEntities.stream()
									.filter(company -> company.getServer().getHost().equals(number_070.getHost()))
									.findAny()
									.ifPresent(r -> response.setHostName(r.getServer().getName()));
						}
						if (isNotEmpty(personList.getGroupCode())) {
							response.setCompanyTrees(organizationService.getCompanyTrees(companyTrees, personList.getGroupCode())
									.stream()
									.map(group -> convertDto(group, OrganizationSummaryResponse.class))
									.collect(toList()));
						}
					}

					return response;
				})
				.collect(Collectors.toList())
		);
		detail.setOutMemberList(
				members.stream().filter(e -> CallType.OUTBOUND.getCode().equals(e.getMemberType()))
				.map(e -> convertDto(e, ConfMemberOutPersonsResponse.class))
				.collect(Collectors.toList())
		);

		return ResponseEntity.ok(data(detail));
	}

	/**
	 *   회의실 예약 추가
	 */
	@PostMapping("")
	public ResponseEntity<JsonResult<Integer>> post(@Valid @RequestBody ConfInfoFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		return ResponseEntity.created(URI.create("api/v1/admin/conf/info/"))
				.body(data(repository.insertOnGeneratedKey(form).getValue(CONF_INFO.SEQ)));
	}

	/**
	 *   회의실 예약 수정
	 */
	@PutMapping(value = "{seq}")
	public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody ConfInfoFormRequest form, BindingResult bindingResult,
												@PathVariable Integer seq) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.confInfoUpdate(form, seq);
		return ResponseEntity.ok(create());
	}

    /**
     *   회의실 예약 부분 수정
     */
    @PutMapping("update/{seq}")
    public ResponseEntity<JsonResult<Void>> update(@Valid @RequestBody ConfInfoUpdateFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) {

        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ConfInfo record = repository.findOneIfNullThrow(seq);

        if(form.getStatus() == null) {
			form.setStatus(record.getStatus());
		}

		if(form.getConfSound() == null) {
			form.setConfSound(record.getConfSound());
		}

		if(form.getTotalMemberCnt() == null) {
			form.setTotalMemberCnt(Integer.valueOf(record.getTotalMemberCnt()));
		}

		if(form.getAttendedMemberCnt() == null) {
			form.setAttendedMemberCnt(Integer.valueOf(record.getAttendedMemberCnt()));
		}

        repository.updateByKey(form, seq);

        return ResponseEntity.ok(create());
    }

	/**
	 *   회의실 예약 삭제
	 */
	@DeleteMapping(value = "{seq}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
		repository.confInfoDelete(seq);
		return ResponseEntity.ok(create());
	}

	/**
	 *   회의실 시간 중복 체크
	 */
	@PostMapping("duplicate")
	public ResponseEntity<JsonResult<Void>> duplicate(@Valid @RequestBody ConfInfoDuplicateFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		return ResponseEntity.ok(create(repository.findAll(
				CONF_INFO.COMPANY_ID.eq(g.getUser().getCompanyId())
				.and(CONF_INFO.ROOM_NUMBER.eq(form.getRoomNumber()))
				.and(CONF_INFO.STATUS.notEqual("C"))
				.and(CONF_INFO.RESERVE_DATE.eq(Date.valueOf(form.getReserveDate())))
				.and((CONF_INFO.RESERVE_FROMTIME.greaterThan(form.getReserveFromTime()).and(CONF_INFO.RESERVE_FROMTIME.lessThan(form.getReserveToTime())))
					.or(CONF_INFO.RESERVE_TOTIME.greaterThan(form.getReserveFromTime()).and(CONF_INFO.RESERVE_TOTIME.lessThan(form.getReserveToTime()))))
		).isEmpty() ? success : failure));
	}

	/**
	 *   회의록 보기
	 */
	@GetMapping("minutes/{confInfoId}")
	public ResponseEntity<JsonResult<ConfInfoMinutesResponse>> minutes(@PathVariable int confInfoId) {
		final ConfInfoMinutesResponse response = new ConfInfoMinutesResponse();
		final ConfInfo confInfo = repository.findOne(confInfoId);
		final File file = new File("/var/log/eicn_log/CONF/"+ g.getUser().getCompanyId() + "/CONF_MINUTE." + confInfoId);
		final StringBuilder buff = new StringBuilder();
		if (file.exists()) {
			try (final FileInputStream in = new FileInputStream(file);
				 final InputStreamReader reader = new InputStreamReader(in);
				 final BufferedReader br = new BufferedReader(reader)) {
				String line;
				while ((line = br.readLine()) != null) {
					buff.append(line).append("\n");
				}

			} catch (IOException e) {
				log.error("File ERROR[error={}]", e.getMessage());
				throw new IllegalArgumentException("파일 정보를 읽어올 수 없습니다.");
			}
		} else {

			final List<ConfMember> members = confMemberRepository.findAll(CONF_MEMBER.CONF_KEY.eq(confInfoId));

			final Map<String, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList> personListMap = personListRepository.findAll(PERSON_LIST.EXTENSION.isNotNull().and(PERSON_LIST.EXTENSION.notEqual(EMPTY)))
					.stream()
					.filter(FunctionUtils.distinctByKey(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList::getPeer))
					.collect(Collectors.toMap(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList::getPeer, e -> e));

			final List<CompanyServerEntity> companyServerEntities = cacheService.getCompanyServerList(g.getUser().getCompanyId());
			final Map<String, Number_070> numberStringObjectMap = numberRepository.findAll().stream()
					.filter(FunctionUtils.distinctByKey(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070::getNumber))
					.collect(Collectors.toMap(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070::getNumber, e -> e));

			List<SummaryPersonResponse> inMemberList =
					members.stream().filter(e -> CallType.INBOUND.getCode().equals(e.getMemberType()))
							.map(e -> {
								final SummaryPersonResponse response1 = convertDto(e, SummaryPersonResponse.class);
								final PersonList personList = personListMap.get(e.getMemberNumber());
								response1.setIdName(EMPTY);
								response1.setPeer(e.getMemberNumber());

								if (personList != null) {
									response1.setIdName(personList.getIdName());
									response1.setPeer(personList.getPeer());
									response1.setExtension(personList.getExtension());

									if (Objects.nonNull(numberStringObjectMap.get(e.getRoomNumber()))) {
										final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070 number_070 = numberStringObjectMap.get(e.getRoomNumber());
										companyServerEntities.stream()
												.filter(company -> company.getServer().getHost().equals(number_070.getHost()))
												.findAny()
												.ifPresent(r -> response1.setHostName(r.getServer().getName()));
									}
								}
								return response1;
							})
							.collect(Collectors.toList());
			List<ConfMemberOutPersonsResponse> outMemberList =
					members.stream().filter(e -> CallType.OUTBOUND.getCode().equals(e.getMemberType()))
							.map(e -> convertDto(e, ConfMemberOutPersonsResponse.class))
							.collect(Collectors.toList());

			buff.append("회의명 : ").append(confInfo.getConfName()).append("\n");
			buff.append("참가자: ");
			for(SummaryPersonResponse inMember : inMemberList) {
				buff.append(inMember.getIdName()).append("(").append(inMember.getExtension()).append(") ");
			}
			for(ConfMemberOutPersonsResponse outMember : outMemberList) {
				buff.append(outMember.getMemberName()).append(" ");
			}
		}

		String minutesText = buff.toString();
		response.setMinutesText(minutesText);
		return ResponseEntity.ok().body(data(response));
	}

	/**
	 *   회의록 저장
	 */
	@PostMapping("minutes-save")
	public ResponseEntity<JsonResult<Void>> minutesSave(@Valid @RequestBody ConfInfoMinutesSaveFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		final Path down_dir = Paths.get(replace(saveMinutesPath, "{0}", g.getUser().getCompanyId()));
		String minute_file = down_dir+"/CONF_MINUTE."+form.getConfId();
		File mf = new File(minute_file);

		if (Files.notExists(down_dir)) {
			try {
				Files.createDirectories(down_dir);
			} catch (IOException ignored) {
			}
		}

		if(mf.exists())
		{
			mf.delete();
		}

		PrintWriter pw = null;
		try {
			log.info(form.getConfMinute());
			pw = new PrintWriter(new FileWriter(mf, true), true);
			pw.println(form.getConfMinute());
			pw.close();
		}catch (Exception fe) {
			log.error("ConfInfoApiController.minutesSave ERROR[error={}]", fe.getMessage());
			throw new IllegalArgumentException("에러가 발생하였습니다.");
		}
		return ResponseEntity.ok(create());
	}
}
