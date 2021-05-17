package kr.co.eicn.ippbx.server.model.search;

import kr.co.eicn.ippbx.server.util.page.PageForm;
import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResearchItemSearchRequest extends PageForm {
    @PageQueryable
    private String itemName; // 문항제목
    @PageQueryable
    private String word; // 질문
    @PageQueryable
    private List<Integer> itemIds = new ArrayList<>();
    @PageQueryable
    private Byte mappingNumber;
}
