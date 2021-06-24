package kr.co.eicn.ippbx.model.dto.statdb;

import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

@Data
public class StatMyCallByTimeResponse {
    private Map<Byte, StatMyCallByTimeData> myStatData = new TreeMap<>(); //시간별 데이타
    private Integer totalSum = 0; //합계: 총건수
    private Integer outTotalSum = 0; //합계: 아웃시도
    private Integer outSuccessSum = 0; //합계: 아웃성공
    private Integer inTotalSum = 0; //합계: 인시도
    private Integer inSuccessSum = 0; //합계: 인성공
    private Integer memberStatSum = 0; //합계: 후처리건수

    public Integer getTotalSum() {
        for(Map.Entry<Byte, StatMyCallByTimeData> entity : myStatData.entrySet()){
            this.totalSum = this.totalSum + myStatData.get(entity.getKey()).getTotalCnt();
        }
        return this.totalSum;
    }

    public Integer getOutTotalSum() {
        for(Map.Entry<Byte, StatMyCallByTimeData> entity : myStatData.entrySet()){
            this.outTotalSum = this.outTotalSum + myStatData.get(entity.getKey()).getOutTotal();
        }
        return this.outTotalSum;
    }

    public Integer getOutSuccessSum() {
        for(Map.Entry<Byte, StatMyCallByTimeData> entity : myStatData.entrySet()){
            this.outSuccessSum = this.outSuccessSum + myStatData.get(entity.getKey()).getOutSuccess();
        }
        return this.outSuccessSum;
    }

    public Integer getInTotalSum() {
        for(Map.Entry<Byte, StatMyCallByTimeData> entity : myStatData.entrySet()){
            this.inTotalSum = this.inTotalSum + myStatData.get(entity.getKey()).getInTotal();
        }
        return this.inTotalSum;
    }

    public Integer getInSuccessSum() {
        for(Map.Entry<Byte, StatMyCallByTimeData> entity : myStatData.entrySet()){
            this.inSuccessSum = this.inSuccessSum + myStatData.get(entity.getKey()).getInSuccess();
        }
        return this.inSuccessSum;
    }

    public Integer getMemberStatSum() {
        for(Map.Entry<Byte, StatMyCallByTimeData> entity : myStatData.entrySet()){
            this.memberStatSum = this.memberStatSum + myStatData.get(entity.getKey()).getMemberTotal();
        }
        return this.memberStatSum;
    }
}
