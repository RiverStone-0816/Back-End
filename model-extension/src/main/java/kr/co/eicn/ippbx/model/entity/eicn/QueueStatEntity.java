package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueueStatEntity extends QueueName {
	private Integer paused0;
	private Integer paused1;
	private Integer paused2;
	private Integer paused3;
	private Integer paused4;
	private Integer paused5;
	private Integer paused6;
	private Integer paused7;
	private Integer paused8;
	private Integer paused9;

	private Integer totalCnt;    /*-- 전체*/
	private Integer loginCnt;    /*-- 로그인*/
	private Integer logoutCnt;   /*-- 로그아웃*/
	private Integer noLoginCnt;  /*-- 전화기 대기*/
}
