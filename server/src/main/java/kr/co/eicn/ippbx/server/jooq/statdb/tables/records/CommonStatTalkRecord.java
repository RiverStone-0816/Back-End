package kr.co.eicn.ippbx.server.jooq.statdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

import java.sql.Date;

public class CommonStatTalkRecord extends UpdatableRecordImpl<CommonStatTalkRecord> {

	public CommonStatTalkRecord(Table<CommonStatTalkRecord> table) {
		super(table);
	}

	/**
	 * Setter for <code>STATDB.stat_talk.seq</code>.
	 */
	public void setSeq(Integer value) {
		set(0, value);
	}

	/**
	 * Getter for <code>STATDB.stat_talk.seq</code>.
	 */
	public Integer getSeq() {
		return (Integer) get(0);
	}

	/**
	 * Setter for <code>STATDB.stat_talk.sender_key</code>.
	 */
	public void setSenderKey(String value) {
		set(1, value);
	}

	/**
	 * Getter for <code>STATDB.stat_talk.sender_key</code>.
	 */
	public String getSenderKey() {
		return (String) get(1);
	}

	/**
	 * Setter for <code>STATDB.stat_talk.userid</code>. 상담원 아이디
	 */
	public void setUserid(String value) {
		set(2, value);
	}

	/**
	 * Getter for <code>STATDB.stat_talk.userid</code>. 상담원 아이디
	 */
	public String getUserid() {
		return (String) get(2);
	}

	/**
	 * Setter for <code>STATDB.stat_talk.group_code</code>. 같은 group_level 에서 unique한 코드 4자리 ex&gt;0001
	 */
	public void setGroupCode(String value) {
		set(3, value);
	}

	/**
	 * Getter for <code>STATDB.stat_talk.group_code</code>. 같은 group_level 에서 unique한 코드 4자리 ex&gt;0001
	 */
	public String getGroupCode() {
		return (String) get(3);
	}

	/**
	 * Setter for <code>STATDB.stat_talk.group_tree_name</code>. 윗레벨의 코드를 포함한 코드의 나열 ex&gt;0003_0008_0001
	 */
	public void setGroupTreeName(String value) {
		set(4, value);
	}

	/**
	 * Getter for <code>STATDB.stat_talk.group_tree_name</code>. 윗레벨의 코드를 포함한 코드의 나열 ex&gt;0003_0008_0001
	 */
	public String getGroupTreeName() {
		return (String) get(4);
	}

	/**
	 * Setter for <code>STATDB.stat_talk.group_level</code>. 해당조직의 레벨 MAX 보다 같거나 작을것
	 */
	public void setGroupLevel(Integer value) {
		set(5, value);
	}

	/**
	 * Getter for <code>STATDB.stat_talk.group_level</code>. 해당조직의 레벨 MAX 보다 같거나 작을것
	 */
	public Integer getGroupLevel() {
		return (Integer) get(5);
	}

	/**
	 * Setter for <code>STATDB.stat_talk.stat_date</code>.
	 */
	public void setStatDate(Date value) {
		set(6, value);
	}

	/**
	 * Getter for <code>STATDB.stat_talk.stat_date</code>.
	 */
	public Date getStatDate() {
		return (Date) get(6);
	}

	/**
	 * Setter for <code>STATDB.stat_talk.stat_hour</code>.
	 */
	public void setStatHour(Byte value) {
		set(7, value);
	}

	/**
	 * Getter for <code>STATDB.stat_talk.stat_hour</code>.
	 */
	public Byte getStatHour() {
		return (Byte) get(7);
	}

	/**
	 * Setter for <code>STATDB.stat_talk.start_room_cnt</code>.
	 */
	public void setStartRoomCnt(Integer value) {
		set(8, value);
	}

	/**
	 * Getter for <code>STATDB.stat_talk.start_room_cnt</code>.
	 */
	public Integer getStartRoomCnt() {
		return (Integer) get(8);
	}

	/**
	 * Setter for <code>STATDB.stat_talk.end_room_cnt</code>.
	 */
	public void setEndRoomCnt(Integer value) {
		set(9, value);
	}

	/**
	 * Getter for <code>STATDB.stat_talk.end_room_cnt</code>.
	 */
	public Integer getEndRoomCnt() {
		return (Integer) get(9);
	}

	/**
	 * Setter for <code>STATDB.stat_talk.in_msg_cnt</code>.
	 */
	public void setInMsgCnt(Integer value) {
		set(10, value);
	}

	/**
	 * Getter for <code>STATDB.stat_talk.in_msg_cnt</code>.
	 */
	public Integer getInMsgCnt() {
		return (Integer) get(10);
	}

	/**
	 * Setter for <code>STATDB.stat_talk.out_msg_cnt</code>.
	 */
	public void setOutMsgCnt(Integer value) {
		set(11, value);
	}

	/**
	 * Getter for <code>STATDB.stat_talk.out_msg_cnt</code>.
	 */
	public Integer getOutMsgCnt() {
		return (Integer) get(11);
	}

	/**
	 * Setter for <code>STATDB.stat_talk.auto_ment_cnt</code>.
	 */
	public void setAutoMentCnt(Integer value) {
		set(12, value);
	}

	/**
	 * Getter for <code>STATDB.stat_talk.auto_ment_cnt</code>.
	 */
	public Integer getAutoMentCnt() {
		return (Integer) get(12);
	}

	/**
	 * Setter for <code>STATDB.stat_talk.auto_ment_exceed_cnt</code>.
	 */
	public void setAutoMentExceedCnt(Integer value) {
		set(13, value);
	}

	/**
	 * Getter for <code>STATDB.stat_talk.auto_ment_exceed_cnt</code>.
	 */
	public Integer getAutoMentExceedCnt() {
		return (Integer) get(13);
	}

	/**
	 * Setter for <code>STATDB.stat_talk.room_time_sum</code>.
	 */
	public void setRoomTimeSum(Integer value) {
		set(14, value);
	}

	/**
	 * Getter for <code>STATDB.stat_talk.room_time_sum</code>.
	 */
	public Integer getRoomTimeSum() {
		return (Integer) get(14);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	@Override
	public Record1<Integer> key() {
		return (Record1) super.key();
	}
}
