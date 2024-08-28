package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.voc;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.*;
import kr.co.eicn.ippbx.model.entity.customdb.VocResearchResultEntity;
import kr.co.eicn.ippbx.model.search.CustomDBVOCResultSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.service.VocResearchResultService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ResearchItem.RESEARCH_ITEM;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.*;

/**
 *  아웃바운드관리 > VOC관리 > VOC결과이력
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/voc/result-history", produces = MediaType.APPLICATION_JSON_VALUE)
public class VOCResultHistoryApiController extends ApiBaseController {

	private final VocResearchResultService service;
	private final VOCGroupRepository vocGroupRepository;
	private final ResearchListRepository researchListRepository;
	private final ResearchItemRepository researchItemRepository;
	private final ResearchTreeRepository researchTreeRepository;
	private final PersonListRepository personListRepository;

	@GetMapping("")
	public ResponseEntity<JsonResult<Pagination<VocResearchResultEntity>>> pagination(CustomDBVOCResultSearchRequest search) {
		if (search.getStartDate() != null && search.getEndDate() != null)
			if (search.getStartDate().after(search.getEndDate()))
				throw new IllegalArgumentException("시작시간이 종료시간보다 이전이어야 합니다.");
		if (search.getVocGroupSeq() == null) {
			final VocGroup latestRegisterGroup = vocGroupRepository.getLatestRegisterGroup();
			if (latestRegisterGroup != null) {
				search.setVocGroupSeq(latestRegisterGroup.getSeq());
			}
		}

		final Pagination<VocResearchResultEntity> pagination = service.getRepository(search.getVocGroupSeq()).pagination(search);
		final Map<Integer, ResearchList> researchListMap = researchListRepository.findAll().stream().collect(Collectors.toMap(ResearchList::getResearchId, e -> e));
		final List<ResearchItem> items = researchItemRepository.findAll(RESEARCH_ITEM.MAPPING_NUMBER.ne((byte) 0)).stream()
				.sorted(Comparator.comparing(ResearchItem::getMappingNumber)).toList();
		final List<ResearchTree> trees = researchTreeRepository.findAll();
		final Map<String, String> personListMap = personListRepository.getIdAndNameMap();

		final List<VocResearchResultEntity> rows = pagination.getRows().stream()
				.peek(e -> {
					if (isNotEmpty(e.getUserid()))
						e.setIdName(personListMap.get(e.getUserid()));

					if (e.getGroup() != null) {
                        e.getGroup().setResearch(researchListMap.get(e.getGroup().getArsResearchId()));

                        final String treePath = e.getTreePath();
                        if (isNotEmpty(treePath)) {

                            final String s = treePath.replaceFirst("0-", EMPTY);
                            final String[] pathArr = split(s, "-");
                            if (pathArr.length > 0) {
                                e.setResults(
                                        Arrays.stream(pathArr)
                                                .map(path -> {
                                                    final Optional<ResearchTree> any = trees.stream().filter(tree -> tree.getCode().equals(path)).findAny();
                                                    if (any.isPresent()) {
                                                        final ResearchTree researchTree = any.get();
                                                        final Optional<ResearchItem> optionalResearchItem = items.stream()
                                                                .filter(item -> item.getItemId().equals(researchTree.getItemId()) && item.getMappingNumber().equals(researchTree.getMappingNumber()))
                                                                .findAny();

                                                        if (optionalResearchItem.isPresent())
                                                            return optionalResearchItem.get().getWord();
                                                    }
                                                    return EMPTY;
                                                })
                                                .filter(StringUtils::isNotEmpty)
                                        .collect(Collectors.toList())
                                );
                            }
                        }
                    }
				})
				.collect(Collectors.toList());

		return ResponseEntity.ok(data(new Pagination<>(rows, search.getPage(), pagination.getTotalCount(), search.getLimit())));
	}

	/**
	 * VOC그룹을 참조하는 설문 시나리오 정보에 MAX LEVEL를 가져온다.
	 * @param seq voc_group.seq
	 * @return
	 */
	@GetMapping("{seq}/max-level")
	public ResponseEntity<JsonResult<Integer>> getMaxTreeLevel(@PathVariable Integer seq) {
		Integer maxLevel = 0;
		final VocGroup entity = vocGroupRepository.findOne(seq);

		if (entity != null)
			maxLevel = researchTreeRepository.getMaxTreeLevel(entity.getArsResearchId());

		return ResponseEntity.ok(data(maxLevel));
	}
}
