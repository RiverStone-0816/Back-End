package kr.co.eicn.ippbx.model.entity.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMaindbCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.dto.customdb.MultiChannelInfoResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResultCustomInfoEntity extends CommonResultCustomInfo {
    private CommonMaindbCustomInfo customInfo;
    private List<MultiChannelInfoResponse> multiChannelInfo;
    private List<MaindbMultichannelInfoEntity> multichannelList = new ArrayList<>();
    private String userName;
    private String userOrgName;
    private String userTrName;
    private PersonList personList; // 상담원 정보
    private String inOutValue;

    public String getInOutValue() {
        if (StringUtils.isNotEmpty(getCallType())) {
            if (getCallType().equals("O"))
                return "발신";
            else if (getCallType().equals("I"))
                return "수신";
        }

        return "";
    }
}
