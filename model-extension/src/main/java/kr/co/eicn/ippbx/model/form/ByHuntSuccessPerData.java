package kr.co.eicn.ippbx.model.form;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ByHuntSuccessPerData {
    private List<ByHuntSuccessPer> byHuntSuccessPerList = new ArrayList<>();

    @Data
    public static class ByHuntSuccessPer{
        private String queueNumber;
        private String queueName;
        private Double successPer;
    }
}
