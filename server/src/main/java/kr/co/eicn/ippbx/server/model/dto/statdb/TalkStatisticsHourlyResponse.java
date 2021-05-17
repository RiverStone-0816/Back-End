package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.Data;

@Data
public class TalkStatisticsHourlyResponse {
    private Byte statHour;
    private Integer startRoomCnt;
    private Integer endRoomCnt;
    private Integer inMsgCnt;
    private Integer outMsgCnt;
    private Integer autoMentCnt;
    private Integer autoMentExceedCnt;
}
