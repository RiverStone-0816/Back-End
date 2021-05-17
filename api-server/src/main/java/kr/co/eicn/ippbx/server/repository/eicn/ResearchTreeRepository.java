package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.exception.EntityNotFoundException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.ResearchTree;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchItem;
import kr.co.eicn.ippbx.server.model.ResearchAnswerItemComposite;
import kr.co.eicn.ippbx.server.model.ResearchQuestionItemComposite;
import kr.co.eicn.ippbx.server.model.entity.eicn.ResearchListEntity;
import kr.co.eicn.ippbx.server.model.form.ResearchTreeFormRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import lombok.Getter;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.ResearchItem.RESEARCH_ITEM;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.ResearchList.RESEARCH_LIST;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.ResearchTree.RESEARCH_TREE;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class ResearchTreeRepository extends EicnBaseRepository<ResearchTree, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree, Integer> {
    private final Logger logger = LoggerFactory.getLogger(ResearchTreeRepository.class);

    private final ResearchListRepository researchListRepository;
    private final ResearchItemRepository researchItemRepository;
    private final CacheService cacheService;
    private final PBXServerInterface pbxServerInterface;

    public ResearchTreeRepository(ResearchListRepository researchListRepository, ResearchItemRepository researchItemRepository, CacheService cacheService, PBXServerInterface pbxServerInterface) {
        super(RESEARCH_TREE, RESEARCH_TREE.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree.class);
        this.researchListRepository = researchListRepository;
        this.researchItemRepository = researchItemRepository;
        this.cacheService = cacheService;
        this.pbxServerInterface = pbxServerInterface;
    }

    public ResearchListEntity getResearchScenarioLists(Integer researchId) {
        final ResearchListEntity researchListEntity = dsl.select()
                .from(RESEARCH_LIST)
                .where(RESEARCH_LIST.COMPANY_ID.eq(getCompanyId())
                        .and(RESEARCH_LIST.RESEARCH_ID.eq(researchId)))
                .fetchOneInto(ResearchListEntity.class);

        if (researchListEntity == null)
            throw new EntityNotFoundException("설문정보를 찾을 수 없습니다.");
        if ("N".equals(researchListEntity.getHaveTree()))
            throw new IllegalArgumentException("시나리오 정보를 등록 해 주세요.");

        final List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree> researchTrees = findAll(RESEARCH_TREE.RESEARCH_ID.eq(researchId));
        final List<ResearchItem> researchItems = researchItemRepository.findAll().stream().sorted(Comparator.comparing(ResearchItem::getMappingNumber)).collect(Collectors.toList());

        final List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree> childTrees = getChildTree(researchTrees, "0");

        final Optional<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree> rootNodeAny = childTrees.stream().findFirst();

        if (rootNodeAny.isPresent()) {
            final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree rootTree = rootNodeAny.get();
            final List<ResearchItem> currentRowResearchItems = researchItems.stream().filter(e -> e.getItemId().equals(rootTree.getItemId())).collect(Collectors.toList());

            final ResearchQuestionItemComposite root = new ResearchQuestionItemComposite();

            for (ResearchItem item : currentRowResearchItems) {
                Optional<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree> any = childTrees.stream()
                        .filter(e -> e.getItemId().equals(item.getItemId()) && e.getMappingNumber().equals(item.getMappingNumber())).findAny();

                String path = EMPTY;

                if (any.isPresent())
                    path = any.get().getPath();

                if (item.getMappingNumber() == 0) {
                    root.setLevel(rootTree.getLevel());
                    root.setWord(item.getWord());
                    root.setPath(path);
                    root.setParent(rootTree.getParent());
                } else {
                    final ResearchAnswerItemComposite answerItem = new ResearchAnswerItemComposite();
                    answerItem.setLevel(rootTree.getLevel());
                    answerItem.setWord(item.getWord());
                    answerItem.setPath(path);
                    answerItem.setParent(rootTree.getParent());

                    root.addAnswerItems(answerItem);
                }
            }

            buildQuestionHierarchyTree(root, root.getPath(), researchItems, researchTrees);

            if (root.getAnswerItems() != null)
                root.getAnswerItems().forEach(e -> buildHierarchyTree(e, e.getPath(), researchItems, researchTrees));

            researchListEntity.setScenario(root);
        }

        return researchListEntity;
    }

    public void buildQuestionHierarchyTree(ResearchQuestionItemComposite parentNode, String path, List<ResearchItem> items, List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree> trees) {
        final List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree> childTrees = getChildTree(trees, path);

        final Optional<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree> nodeAny = childTrees.stream().findAny();

        if (nodeAny.isPresent()) {
            final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree anyTree = nodeAny.get();
            final List<ResearchItem> currentRowResearchItems = items.stream().filter(e -> e.getItemId().equals(anyTree.getItemId())).collect(Collectors.toList());
            final ResearchQuestionItemComposite questionItemNode = new ResearchQuestionItemComposite();

            for (ResearchItem item : currentRowResearchItems) {
                Optional<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree> any = childTrees.stream()
                        .filter(e -> e.getItemId().equals(item.getItemId()) && e.getMappingNumber().equals(item.getMappingNumber())).findAny();

                String subPath = EMPTY;

                if (any.isPresent())
                    subPath = any.get().getPath();

                if (item.getMappingNumber() == 0) {
                    questionItemNode.setLevel(anyTree.getLevel());
                    questionItemNode.setWord(item.getWord());
                    questionItemNode.setPath(subPath);
                    questionItemNode.setParent(anyTree.getParent());
                } else {
                    final ResearchAnswerItemComposite answerItem = new ResearchAnswerItemComposite();
                    answerItem.setLevel(anyTree.getLevel());
                    answerItem.setWord(item.getWord());
                    answerItem.setPath(subPath);
                    answerItem.setParent(anyTree.getParent());

                    questionItemNode.addAnswerItems(answerItem);
                }
            }

            parentNode.addChildNode(questionItemNode);

            buildQuestionHierarchyTree(questionItemNode, questionItemNode.getPath(), items, trees);

            if (questionItemNode.getAnswerItems() != null)
                questionItemNode.getAnswerItems().forEach(e -> buildHierarchyTree(e, e.getPath(), items, trees));
        }
    }

    public void buildHierarchyTree(ResearchAnswerItemComposite parentNode, String path, List<ResearchItem> items, List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree> trees) {
        final List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree> childTrees = getChildTree(trees, path);

        final Optional<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree> nodeAny = childTrees.stream().findAny();

        if (nodeAny.isPresent()) {
            final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree anyTree = nodeAny.get();
            final List<ResearchItem> currentRowResearchItems = items.stream().filter(e -> e.getItemId().equals(anyTree.getItemId())).collect(Collectors.toList());
            final ResearchQuestionItemComposite questionItemNode = new ResearchQuestionItemComposite();

            for (ResearchItem item : currentRowResearchItems) {
                String subPath = EMPTY;
                Optional<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree> any = childTrees.stream()
                        .filter(e -> e.getItemId().equals(item.getItemId()) && e.getMappingNumber().equals(item.getMappingNumber())).findAny();

                if (any.isPresent())
                    subPath = any.get().getPath();

                if (item.getMappingNumber() == 0) {
                    questionItemNode.setLevel(anyTree.getLevel());
                    questionItemNode.setWord(item.getWord());
                    questionItemNode.setPath(subPath);
                    questionItemNode.setParent(anyTree.getParent());
                } else {
                    final ResearchAnswerItemComposite answerItem = new ResearchAnswerItemComposite();
                    answerItem.setLevel(anyTree.getLevel());
                    answerItem.setWord(item.getWord());
                    answerItem.setPath(subPath);
                    answerItem.setParent(anyTree.getParent());

                    questionItemNode.addAnswerItems(answerItem);
                }
            }

            parentNode.setChild(questionItemNode);

            buildQuestionHierarchyTree(questionItemNode, questionItemNode.getPath(), items, trees);

            if (parentNode.getChild().getAnswerItems() != null)
                parentNode.getChild().getAnswerItems().forEach(e -> buildHierarchyTree(e, e.getPath(), items, trees));
        }
    }


    public void insert(ResearchTreeFormRequest form, Integer researchId) {
        final String ROOT_PARENT = "0";
        final Integer ROOT_LEVEL = 1;

        researchListRepository.findOneByResearchIdIfNullThrow(researchId);
        delete(RESEARCH_TREE.RESEARCH_ID.eq(researchId));

        processChild(form, researchId, ROOT_LEVEL, ROOT_PARENT);

        final List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree> currentInsertRows = findAllResearchId(researchId);
        final List<Integer> distinctItemIds = currentInsertRows.stream().map(kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree::getItemId).distinct().collect(Collectors.toList());
        final List<ResearchItem> useSoundCodeTtsItems = researchItemRepository.findAll(RESEARCH_ITEM.MAPPING_NUMBER.eq((byte) 0).and(RESEARCH_ITEM.SOUND_KIND.eq("T")));

        final StringBuilder ttsBuffer = new StringBuilder();
        final Pattern pattern = Pattern.compile("[\\[\\[](.*?)[\\]\\]]");

        useSoundCodeTtsItems.stream().filter(e -> distinctItemIds.stream().anyMatch(item -> item.equals(e.getItemId()))).forEach(e -> {
            if (isNotEmpty(e.getWord())) {
                if (e.getWord().contains("[[") && e.getWord().contains("]]")) {
                    final Matcher matcher = pattern.matcher(e.getWord());
                    while (matcher.find()) {
                        String relaxedField = matcher.group(1).replaceAll("\\[", EMPTY);
                        if (ttsBuffer.indexOf(relaxedField) == -1)
                            ttsBuffer.append("|").append(relaxedField);
                        if (matcher.group(1) == null)
                            break;
                    }
                }
            }
        });

        dsl.update(RESEARCH_LIST)
                .set(RESEARCH_LIST.HAVE_TREE, "Y")
                .set(RESEARCH_LIST.TTS_FIELD_NAME, ttsBuffer.toString().contains("|") ? ttsBuffer.append("|").toString() : ttsBuffer.toString())
                .where(RESEARCH_LIST.RESEARCH_ID.eq(researchId))
                .execute();

        cacheService.pbxServerList(getCompanyId()).forEach(e -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                pbxDsl.update(RESEARCH_LIST)
                        .set(RESEARCH_LIST.HAVE_TREE, "Y")
                        .set(RESEARCH_LIST.TTS_FIELD_NAME, ttsBuffer.toString().contains("|") ? ttsBuffer.append("|").toString() : ttsBuffer.toString())
                        .where(RESEARCH_LIST.RESEARCH_ID.eq(researchId))
                        .execute();
            }
        });
    }

    private void insertMappedAnswerNumber(ResearchTreeFormRequest form, Integer researchId, Integer level, Byte answerNumber, String parentCode) {
        final Integer itemId = form.getItemId();
        final String code = code(researchId, level, itemId, answerNumber);
        final String path = parentCode + "-" + code;
        final String[] splicedCode = path.split("-");
        if (splicedCode.length < 2)
            throw new IllegalStateException("정상적인 흐름이라면 최소한 '-'로 구분된 code가 두 구문이 나타나야 한다.: '0', '{researchId}-1-{itemId}-{number}'");
        final String rootCode = splicedCode[0] + "-" + splicedCode[1];

        dsl.insertInto(RESEARCH_TREE)
                .set(RESEARCH_TREE.CODE, code)
                .set(RESEARCH_TREE.PATH, path)
                .set(RESEARCH_TREE.ROOT, rootCode)
                .set(RESEARCH_TREE.PARENT, parentCode)
                .set(RESEARCH_TREE.LEVEL, level)
                .set(RESEARCH_TREE.TYPE, (byte) 0)
                .set(RESEARCH_TREE.ITEM_ID, itemId)
                .set(RESEARCH_TREE.MAPPING_NUMBER, answerNumber)
                .set(RESEARCH_TREE.RESEARCH_ID, researchId)
                .set(RESEARCH_TREE.COMPANY_ID, getCompanyId())
                .execute();

        cacheService.pbxServerList(getCompanyId()).forEach(e -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                pbxDsl.insertInto(RESEARCH_TREE)
                        .set(RESEARCH_TREE.CODE, code)
                        .set(RESEARCH_TREE.PATH, path)
                        .set(RESEARCH_TREE.ROOT, rootCode)
                        .set(RESEARCH_TREE.PARENT, parentCode)
                        .set(RESEARCH_TREE.LEVEL, level)
                        .set(RESEARCH_TREE.TYPE, (byte) 0)
                        .set(RESEARCH_TREE.ITEM_ID, itemId)
                        .set(RESEARCH_TREE.MAPPING_NUMBER, answerNumber)
                        .set(RESEARCH_TREE.RESEARCH_ID, researchId)
                        .set(RESEARCH_TREE.COMPANY_ID, getCompanyId())
                        .execute();
            }
        });

        if (form.getChildNotMappedByAnswerNumber() != null)
            processChild(form.getChildNotMappedByAnswerNumber(), researchId, level + 1, path);

        form.getChildrenMappedByAnswerNumber().values().forEach(childForm -> processChild(childForm, researchId, level + 1, path));
    }

    private void processChild(ResearchTreeFormRequest form, Integer researchId, Integer level, String path) {
        final Set<Byte> answerNumbers = researchItemRepository.findAllItemId(form.getItemId())
                .stream()
                .filter(e -> e.getMappingNumber() != null && !e.getMappingNumber().equals((byte) 0))
                .map(ResearchItem::getMappingNumber)
                .collect(Collectors.toSet());

        if (!form.isHasableAnswersChildTree() || answerNumbers.isEmpty()) {
            insertNotMappedAnswerNumber(form, researchId, level, path);
        } else {
            answerNumbers.forEach(childAnswerNumber -> {
                final ResearchTreeFormRequest splicedForm = new ResearchTreeFormRequest(form.getItemId(), form.isHasableAnswersChildTree());

                if (form.getChildrenMappedByAnswerNumber().get(childAnswerNumber) != null)
                    splicedForm.getChildrenMappedByAnswerNumber().put(childAnswerNumber, form.getChildrenMappedByAnswerNumber().get(childAnswerNumber));

                insertMappedAnswerNumber(splicedForm, researchId, level, childAnswerNumber, path);
            });
        }
    }

    private void insertNotMappedAnswerNumber(ResearchTreeFormRequest form, Integer researchId, Integer level, String parentCode) {
        insertMappedAnswerNumber(form, researchId, level, (byte) 0, parentCode);
    }

    private String code(Integer researchId, Integer level, Integer itemId) {
        final byte NOT_MAPPED_ANSWER_NUMBER = (byte) 0;
        return code(researchId, level, itemId, NOT_MAPPED_ANSWER_NUMBER);
    }

    private String code(Integer researchId, Integer level, Integer itemId, Byte answerNumber) {
        return researchId + "_" + level + "_" + itemId + "_" + answerNumber;
    }

    public Integer getMaxTreeLevel(Integer researchId) {
        return dsl.select(DSL.ifnull(DSL.max(RESEARCH_TREE.LEVEL), 0)).from(RESEARCH_TREE)
                .where(compareCompanyId())
                .and(RESEARCH_TREE.RESEARCH_ID.eq(researchId))
                .fetchOneInto(Integer.class);
    }

    public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree> findAllResearchId(Integer researchId) {
        return findAll(RESEARCH_TREE.RESEARCH_ID.eq(researchId));
    }

    private List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree> getChildTree(List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree> targetList, final String path) {
        return targetList.stream().filter(e -> e.getParent().equals(path)).collect(Collectors.toList());
    }

    public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree> findAllByResearchId(Integer researchId) {
        return findAll(RESEARCH_TREE.RESEARCH_ID.eq(researchId));
    }
}
