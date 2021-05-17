package kr.co.eicn.ippbx.server.model.search;

import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import kr.co.eicn.ippbx.server.util.page.PageQueryableForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MonitorQueueSearchRequest extends PageQueryableForm {
    @PageQueryable
    private List<String> queueNameToSummary = new ArrayList<>();    //요약보기
    @PageQueryable
    private List<String> queueNameToStatus = new ArrayList<>();     //상담원 상태 보기
}
