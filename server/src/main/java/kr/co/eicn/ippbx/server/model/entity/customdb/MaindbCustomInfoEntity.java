package kr.co.eicn.ippbx.server.model.entity.customdb;

import kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonMaindbCustomInfo;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonField;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonType;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.MaindbGroup;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.MultichannelList;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MaindbCustomInfoEntity extends CommonMaindbCustomInfo {
    private MaindbGroup group;
    private CommonType type;
    private List<CommonField> fields = new ArrayList<>();
    private List<MaindbMultichannelInfoEntity> multichannelList = new ArrayList<>();
    private String maindbSysDamdangName;
}
