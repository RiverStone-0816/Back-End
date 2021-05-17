package kr.co.eicn.ippbx.server.model.entity.statdb;

import lombok.Data;

@Data
public class StatHuntEntity {
    private Integer connectionRequest;
    private Integer success;
    private Integer cancelCustom;
    private Integer cancelSystem;
    private Integer callback;
    private Integer billSecSum;
}
