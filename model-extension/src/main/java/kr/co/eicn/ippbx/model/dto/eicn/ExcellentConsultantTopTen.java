package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.dto.statdb.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExcellentConsultantTopTen {

    private List<StatUserRankingInSuccess> inSuccessTopTen;
    private List<StatUserRankingOutSuccess> outSuccessTopTen;
    private List<StatUserRankingInBillsecSum> inBillSecTopTen;
    private List<StatUserRankingOutBillsecSum> outBillSecTopTen;
    private List<StatUserRankingCallbackSuccess> callbackSuccessTopTen;

    public enum Type {
        MOST_RECEIVE("in_success"), MOST_SEND("out_success"), LONGEST_RECEIVE("in_billsec_sum"), LONGEST_SEND("out_billsec_sum"), LONGEST_CALL("total_billsec_sum"), MOST_CALLBACK("callback_success");

        private String fieldName;

        Type(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldName() {
            return fieldName;
        }
    }
}