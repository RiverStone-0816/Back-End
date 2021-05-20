package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExcellentConsultant {
    private Type type;
    private String userName; //상담원명
    private Integer callCount;
    private Integer time;

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
