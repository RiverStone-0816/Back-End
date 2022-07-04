package kr.co.eicn.ippbx.model.dto.statdb;

import lombok.Data;

@Data
public class WtalkStatisticsPersonResponse {
    private String idName;
    private Integer startRoomCnt = 0;
    private Integer endRoomCnt = 0;
    private Integer inMsgCnt = 0;
    private Integer outMsgCnt = 0;
    private Integer autoMentCnt = 0;
    private Integer autoMentExceedCnt = 0;
}
