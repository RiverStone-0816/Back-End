package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotTree;
import kr.co.eicn.ippbx.model.form.WebchatBotTreeFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatBotTreeRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class WebchatBotTreeService extends ApiBaseService {
    private final Logger logger = LoggerFactory.getLogger(WebchatBotTreeService.class);

    private final WebchatBotTreeRepository webchatBotTreeRepository;

    public String insert(Integer botId, Integer blockId, Integer rootId, Integer parentId, Integer buttonId, String parentTreeName, Integer level) {
        WebchatBotTreeFormRequest data = new WebchatBotTreeFormRequest();

        String treeName = createTreeName(blockId, parentTreeName);

        data.setChatBotId(botId);
        data.setBlockId(blockId);
        data.setRootId(rootId);
        data.setParentId(parentId);
        data.setParentButtonId(buttonId);
        data.setLevel(level);
        data.setTreeName(treeName);

        webchatBotTreeRepository.insert(data);

        return treeName;
    }

    public String createTreeName(Integer blockId, String parentTreeName) {
        final DecimalFormat decimalFormat = new DecimalFormat("0000");

        String treeName = decimalFormat.format(blockId);

        if (StringUtils.isNotEmpty(parentTreeName))
            return parentTreeName + "_" + treeName;
        else
            return treeName;
    }

    public List<Integer> findBlockIdListByBotId(Integer botId) {
        return webchatBotTreeRepository.findTreeByBotId(botId, null).stream().map(WebchatBotTree::getBlockId).collect(Collectors.toList());
    }

    public void deleteByBotId(Integer botId) {
        webchatBotTreeRepository.deleteByBotId(botId);
    }

    public WebchatBotTree findRootBlock(Integer botId) {
        return webchatBotTreeRepository.findRootBlock(botId);
    }

    public Map<Integer, List<Integer>> findBlockIdByParentIdMapByBotId(Integer botId, Integer rootBlockId) {
        return webchatBotTreeRepository.findTreeByBotId(botId, rootBlockId).stream().collect(Collectors.groupingBy(WebchatBotTree::getParentId, Collectors.mapping(WebchatBotTree::getBlockId, Collectors.toList())));
    }
}
