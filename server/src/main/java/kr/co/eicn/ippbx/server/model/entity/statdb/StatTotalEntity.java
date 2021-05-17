package kr.co.eicn.ippbx.server.model.entity.statdb;

import lombok.Data;

@Data
public class StatTotalEntity {
    public Integer getTotalCnt(Integer total, Integer totalCnt) {if (totalCnt == null) {totalCnt = 0;} return total + totalCnt; }

    public Long getTotalBillSec(Integer billSec, Long totalBillSec) {if (totalBillSec == null) {totalBillSec = (long) 0;} return (long)billSec + totalBillSec; }


    public Integer getTTotalCnt(Integer totalCnt, Integer tTotalCnt) {if (tTotalCnt == null) {tTotalCnt = 0;} return totalCnt + tTotalCnt; }

    public Long getTTotalBillSec(Long totalBillSec, Long tTotalBillSec) {if (tTotalBillSec == null) {tTotalBillSec = (long) 0;} return totalBillSec + tTotalBillSec; }
}
