package kr.co.eicn.ippbx.meta.jooq.configdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class CommonMenuCompany implements Serializable {
    private Integer seq;
    private String  userid;
    private String  menuCode;
    private String  menuName;
    private String  menuTreeName;
    private Integer menuLevel;
    private String  parentMenuCode;
    private String  parentTreeName;
    private String  menuActionExeId;
    private Integer sequence;
    private String  viewYn;
    private String  icon;
    private String  actionType;
    private String  authType;
    private String  groupLevelAuthYn;
    private String  groupCode;
    private Integer groupLevel;
    private String  serviceKind;
    private String  service;
    private String  solution;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonMenuCompany that = (CommonMenuCompany) o;
        return Objects.equals(menuCode, that.menuCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuCode);
    }
}
