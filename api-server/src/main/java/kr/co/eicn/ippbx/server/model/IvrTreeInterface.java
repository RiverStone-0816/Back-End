package kr.co.eicn.ippbx.server.model;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.IvrTree;

import java.util.List;

public interface IvrTreeInterface {
    <T extends IvrTree> void setNodes(List<T> nodes);
}
