package kr.co.eicn.ippbx.front.model;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchItem;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree;
import kr.co.eicn.ippbx.server.model.dto.statdb.StatVocResponse;
import lombok.Data;
import org.springframework.security.access.method.P;

import java.util.*;

@Data
public class TreeItem {
    private final String path;
    private final String code;
    private final int level;
    private final Integer itemId;
    private final Integer mappedNumber;
    private final String itemName;
    private final String mappedNumberDescription;
    private final Integer count;
    private final Set<TreeItem> children = new TreeSet<>(Comparator.comparing(TreeItem::getCode));

    public TreeItem(String path, List<ResearchTree> trees, List<ResearchItem> items, List<StatVocResponse> stat) {
        this.path = path;
        final String[] splicedPath = path.split("-");
        this.level = splicedPath.length - 1;
        this.code = splicedPath[splicedPath.length - 1];
        this.count = stat.stream().filter(e -> Objects.equals(e.getPath(), path)).mapToInt(StatVocResponse::getCount).findFirst().orElse(0);

        if (level == 0) {
            this.itemId = null;
            this.mappedNumber = null;
            this.itemName = null;
            this.mappedNumberDescription = null;
        } else {
            final String[] splicedCode = code.split("_");
            this.itemId = Integer.valueOf(splicedCode[2]);
            this.mappedNumber = Integer.valueOf(splicedCode[3]);

            final Optional<ResearchItem> itemOptional = items.stream().filter(e -> Objects.equals(e.getItemId(), itemId) && Objects.equals(e.getMappingNumber(), mappedNumber.byteValue())).findFirst();
            if (itemOptional.isPresent()) {
                final ResearchItem item = itemOptional.get();
                this.itemName = item.getItemName();
                this.mappedNumberDescription = item.getWord();
            } else {
                this.itemName = null;
                this.mappedNumberDescription = null;
            }
        }

        trees.stream().filter(e -> Objects.equals(e.getParent(), path)).forEach(e -> children.add(new TreeItem(e.getPath(), trees, items, stat)));
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public int getLeafDescendantCount() {
        if (isLeaf())
            return 1;

        return children.stream().mapToInt(TreeItem::getLeafDescendantCount).sum();
    }
}
