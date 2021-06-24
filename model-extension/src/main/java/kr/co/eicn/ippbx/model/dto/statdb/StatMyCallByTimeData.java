package kr.co.eicn.ippbx.model.dto.statdb;


import lombok.Data;

@Data
public class StatMyCallByTimeData {
    private Integer statHour;
    private String idName;
    private Integer totalCnt = 0;
    private Integer outTotal = 0;
    private Integer outSuccess = 0;
    private Integer inTotal = 0;
    private Integer inSuccess = 0;
    private Integer memberTotal = 0;

    public void setInTotalSum(Integer num){
        this.inTotal = this.inTotal + num;
    }

    public void setInSuccessSum(Integer num){
        this.inSuccess = this.inSuccess + num;
    }

    public void setOutTotalSum(Integer num){
        this.outTotal = this.outTotal + num;
    }

    public void setOutSuccessSum(Integer num){
        this.outSuccess = this.outSuccess + num;
    }

    public void setTotalCntSum(Integer num) {
        this.totalCnt = this.totalCnt + num;
    }

    public void setMemberTotalSum(Integer num) {
        this.memberTotal = this.memberTotal + num;
    }
}
