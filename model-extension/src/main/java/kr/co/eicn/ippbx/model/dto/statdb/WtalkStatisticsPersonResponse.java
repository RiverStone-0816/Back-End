package kr.co.eicn.ippbx.model.dto.statdb;

import lombok.Data;

@Data
public class WtalkStatisticsPersonResponse {
    private String idName;
    private Integer startRoomCnt;
    private Integer endRoomCnt;
    private Integer inMsgCnt;
    private Integer outMsgCnt;
    private Integer autoMentCnt;
    private Integer autoMentExceedCnt;
}