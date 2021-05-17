package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.voc;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.VocGroup;
import kr.co.eicn.ippbx.server.model.dto.statdb.StatVocResponse;
import kr.co.eicn.ippbx.server.model.entity.statdb.StatVOCEntity;
import kr.co.eicn.ippbx.server.model.search.StatDBVOCStatisticsSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.ResearchTreeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.VOCGroupRepository;
import kr.co.eicn.ippbx.server.service.StatDBStatVOCService;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.*;

/**
 *  아웃바운드관리 > VOC관리 > VOC통계
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/voc/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
public class VOCStatisticsApiController extends ApiBaseController {

	private final StatDBStatVOCService service;
	private final VOCGroupRepository vocGroupRepository;
	private final ResearchTreeRepository researchTreeRepository;

	@GetMapping("")
	public ResponseEntity<JsonResult<List<StatVocResponse>>> list(StatDBVOCStatisticsSearchRequest search) {
		if (search.getStartDate() != null && search.getEndDate() != null)
			if (search.getStartDate().after(search.getEndDate()))
				throw new IllegalArgumentException(message.getText("messages.validator.enddate.after.startdate"));
		if (search.getVocGroupSeq() == null) {
			final VocGroup latestRegisterGroup = vocGroupRepository.getLatestRegisterGroup();
			if (latestRegisterGroup != null) {
				search.setVocGroupSeq(latestRegisterGroup.getSeq());
			}
		}

		final List<StatVOCEntity> entities = service.getRepository(search.getVocGroupSeq()).findAll(search);
		final Optional<StatVOCEntity> any = entities.stream().findAny();

		List<StatVocResponse> statVocResponses = Collections.emptyList();

		if (any.isPresent()) {
			final StatVOCEntity statVOCEntity = any.get();
			final VocGroup group = vocGroupRepository.findOne(statVOCEntity.getVocGroupId());

			final List<String> paths = entities.stream()
					.map(e -> {
						if (isNotEmpty(e.getResearchTreePath())) {
							final String treePath = e.getResearchTreePath();
							split(e.getResearchTreePath(), "-");
							final String s = treePath.replaceFirst("0-", EMPTY);
							return split(s, "-");
						}
						return null;
					})
					.flatMap(array -> array != null ? Arrays.stream(array) : null)
					.collect(Collectors.toList());

			if (group != null) {
				final List<ResearchTree> trees = researchTreeRepository.findAllResearchId(group.getArsResearchId());

				if (!trees.isEmpty()) {
					statVocResponses = trees.stream()
							.map(e -> {
								final StatVocResponse statVocResponse = convertDto(e, StatVocResponse.class);

								statVocResponse.setLevel(e.getLevel());
								statVocResponse.setCount(paths.stream().filter(path -> path.equals(e.getCode())).mapToInt(path -> 1).sum());
								statVocResponse.setPath(e.getPath());

								return statVocResponse;
							})
							.collect(Collectors.toList());
				}
			}
		}

		return ResponseEntity.ok(data(statVocResponses));
	}
}
