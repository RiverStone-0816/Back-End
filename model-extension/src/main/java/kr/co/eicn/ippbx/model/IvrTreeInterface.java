package kr.co.eicn.ippbx.model;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree;

import java.util.List;

public interface IvrTreeInterface {
    <T extends IvrTree> void setNodes(List<T> nodes);
}
