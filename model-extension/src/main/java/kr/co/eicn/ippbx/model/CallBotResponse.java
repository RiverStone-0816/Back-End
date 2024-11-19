package kr.co.eicn.ippbx.model;

import lombok.Data;

import java.util.List;

@Data
public class CallBotResponse {
    private String resultCode;
    private String resultMsg;
    private Result result;

    @Data
    private static class Result {
        private String        callStartTime;
        private String        callEndTime;
        private String        ani;
        private String        dnis;
        private List<Dialogs> dialogs;

        @Data
        private static class Dialogs {
            private String  startTs;
            private String  endTs;
            private String  message;
            private Boolean ai;
        }
    }
}
