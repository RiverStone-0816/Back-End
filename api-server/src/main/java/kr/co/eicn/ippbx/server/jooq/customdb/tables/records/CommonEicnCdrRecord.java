package kr.co.eicn.ippbx.server.jooq.customdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

import java.sql.Timestamp;

public class CommonEicnCdrRecord extends UpdatableRecordImpl<CommonEicnCdrRecord> {

    public CommonEicnCdrRecord(Table<CommonEicnCdrRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.seq</code>.
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.seq</code>.
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.ring_date</code>. 전화수발신시간
     */
    public void setRingDate(Timestamp value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.ring_date</code>. 전화수발신시간
     */
    public Timestamp getRingDate() {
        return (Timestamp) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.waiting_date</code>. 헌트에들어와서기다리기시작한시간
     */
    public void setWaitingDate(Timestamp value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.waiting_date</code>. 헌트에들어와서기다리기시작한시간
     */
    public Timestamp getWaitingDate() {
        return (Timestamp) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.dialup_date</code>. 상대방이전화를받은시간
     */
    public void setDialupDate(Timestamp value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.dialup_date</code>. 상대방이전화를받은시간
     */
    public Timestamp getDialupDate() {
        return (Timestamp) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.hangup_date</code>. 전화가종료된시간
     */
    public void setHangupDate(Timestamp value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.hangup_date</code>. 전화가종료된시간
     */
    public Timestamp getHangupDate() {
        return (Timestamp) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.duration</code>. hangup_date-ring_date
     */
    public void setDuration(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.duration</code>. hangup_date-ring_date
     */
    public Integer getDuration() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.billsec</code>. hangup_date-dialup_date
     */
    public void setBillsec(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.billsec</code>. hangup_date-dialup_date
     */
    public Integer getBillsec() {
        return (Integer) get(6);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.waitsec</code>. waiting_date-dialup_date
     */
    public void setWaitsec(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.waitsec</code>. waiting_date-dialup_date
     */
    public Integer getWaitsec() {
        return (Integer) get(7);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.in_out</code>. 수신/발신여부
     */
    public void setInOut(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.in_out</code>. 수신/발신여부
     */
    public String getInOut() {
        return (String) get(8);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.dcontext</code>. 다이얼플랜에서의컨텍스트
     */
    public void setDcontext(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.dcontext</code>. 다이얼플랜에서의컨텍스트
     */
    public String getDcontext() {
        return (String) get(9);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.peer</code>. 전화기아이디
     */
    public void setPeer(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.peer</code>. 전화기아이디
     */
    public String getPeer() {
        return (String) get(10);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.src</code>. 수신내선또는수신고객번호
     */
    public void setSrc(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.src</code>. 수신내선또는수신고객번호
     */
    public String getSrc() {
        return (String) get(11);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.dst</code>. 발신내선또는발신고객번호
     */
    public void setDst(String value) {
        set(12, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.dst</code>. 발신내선또는발신고객번호
     */
    public String getDst() {
        return (String) get(12);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.callstatus</code>. 전화상태 C-클릭투콜 R-링이가는중 D-전화받음 H-전화끊음
     */
    public void setCallstatus(String value) {
        set(13, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.callstatus</code>. 전화상태 C-클릭투콜 R-링이가는중 D-전화받음 H-전화끊음
     */
    public String getCallstatus() {
        return (String) get(13);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.detail_callstatus</code>. 다이얼플랜상디테일콜상태또는단계
     */
    public void setDetailCallstatus(String value) {
        set(14, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.detail_callstatus</code>. 다이얼플랜상디테일콜상태또는단계
     */
    public String getDetailCallstatus() {
        return (String) get(14);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.record_info</code>. 녹취 S-녹취없음 M-녹취됨 M_$순차 -부분녹취가 발생하여 녹취가 여려개임
     */
    public void setRecordInfo(String value) {
        set(15, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.record_info</code>. 녹취 S-녹취없음 M-녹취됨 M_$순차 -부분녹취가 발생하여 녹취가 여려개임
     */
    public String getRecordInfo() {
        return (String) get(15);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.record_file</code>. 녹취파일경로
     */
    public void setRecordFile(String value) {
        set(16, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.record_file</code>. 녹취파일경로
     */
    public String getRecordFile() {
        return (String) get(16);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.uniqueid</code>. 아스터리스크 발신쪽 콜유니크 아이디 돌려줬을경우 같을수도 있음
     */
    public void setUniqueid(String value) {
        set(17, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.uniqueid</code>. 아스터리스크 발신쪽 콜유니크 아이디 돌려줬을경우 같을수도 있음
     */
    public String getUniqueid() {
        return (String) get(17);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.dst_uniqueid</code>. 아스터리스크 수신쪽 콜유니크
     */
    public void setDstUniqueid(String value) {
        set(18, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.dst_uniqueid</code>. 아스터리스크 수신쪽 콜유니크
     */
    public String getDstUniqueid() {
        return (String) get(18);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.channel</code>. 아스터리스크 발신쪽 채널아이디
     */
    public void setChannel(String value) {
        set(19, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.channel</code>. 아스터리스크 발신쪽 채널아이디
     */
    public String getChannel() {
        return (String) get(19);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.dst_channel</code>. 아스터리스크 수신쪽 채널아이디
     */
    public void setDstChannel(String value) {
        set(20, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.dst_channel</code>. 아스터리스크 수신쪽 채널아이디
     */
    public String getDstChannel() {
        return (String) get(20);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.ini_num</code>. 인바운드에서는첫인입대표번호, 아웃바운드에서는 발신CID
     */
    public void setIniNum(String value) {
        set(21, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.ini_num</code>. 인바운드에서는첫인입대표번호, 아웃바운드에서는 발신CID
     */
    public String getIniNum() {
        return (String) get(21);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.second_num</code>. 인바운드에서는헌트번호 아웃바운드에서는 발신과금번호
     */
    public void setSecondNum(String value) {
        set(22, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.second_num</code>. 인바운드에서는헌트번호 아웃바운드에서는 발신과금번호
     */
    public String getSecondNum() {
        return (String) get(22);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.worktime_yn</code>.
     */
    public void setWorktimeYn(String value) {
        set(23, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.worktime_yn</code>.
     */
    public String getWorktimeYn() {
        return (String) get(23);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.stat_yn</code>.
     */
    public void setStatYn(String value) {
        set(24, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.stat_yn</code>.
     */
    public String getStatYn() {
        return (String) get(24);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.ivr_key</code>. 인바운드에서 IVR순서
     */
    public void setIvrKey(String value) {
        set(25, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.ivr_key</code>. 인바운드에서 IVR순서
     */
    public String getIvrKey() {
        return (String) get(25);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.queue_strategy</code>. 헌트의 콜분배 STRATEGY
     */
    public void setQueueStrategy(String value) {
        set(26, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.queue_strategy</code>. 헌트의 콜분배 STRATEGY
     */
    public String getQueueStrategy() {
        return (String) get(26);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.queue_sequence</code>. 헌트의 콜분배순서 전화기아이디
     */
    public void setQueueSequence(String value) {
        set(27, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.queue_sequence</code>. 헌트의 콜분배순서 전화기아이디
     */
    public String getQueueSequence() {
        return (String) get(27);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.callee_hangup</code>. Y-받은사람이먼저끊음 N-건사람이 먼저끊음
     */
    public void setCalleeHangup(String value) {
        set(28, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.callee_hangup</code>. Y-받은사람이먼저끊음 N-건사람이 먼저끊음
     */
    public String getCalleeHangup() {
        return (String) get(28);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.web_voice_info</code>. 보이는ARS사용여부
     */
    public void setWebVoiceInfo(String value) {
        set(29, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.web_voice_info</code>. 보이는ARS사용여부
     */
    public String getWebVoiceInfo() {
        return (String) get(29);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.vip_black</code>. VIP-V,BLACK-B
     */
    public void setVipBlack(String value) {
        set(30, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.vip_black</code>. VIP-V,BLACK-B
     */
    public String getVipBlack() {
        return (String) get(30);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.userid</code>. 전화기사용자아이디
     */
    public void setUserid(String value) {
        set(31, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.userid</code>. 전화기사용자아이디
     */
    public String getUserid() {
        return (String) get(31);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.group_code</code>. 전화기사용자소속코드
     */
    public void setGroupCode(String value) {
        set(32, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.group_code</code>. 전화기사용자소속코드
     */
    public String getGroupCode() {
        return (String) get(32);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.group_tree_name</code>. 전화기사용자소속트리명
     */
    public void setGroupTreeName(String value) {
        set(33, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.group_tree_name</code>. 전화기사용자소속트리명
     */
    public String getGroupTreeName() {
        return (String) get(33);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.group_level</code>. 전화기사용자소속트리레벨
     */
    public void setGroupLevel(Integer value) {
        set(34, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.group_level</code>. 전화기사용자소속트리레벨
     */
    public Integer getGroupLevel() {
        return (Integer) get(34);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.company_id</code>. 컴퍼니아이디
     */
    public void setCompanyId(String value) {
        set(35, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.company_id</code>. 컴퍼니아이디
     */
    public String getCompanyId() {
        return (String) get(35);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.host</code>. 콜이진행된PBX_SERVER
     */
    public void setHost(String value) {
        set(36, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.host</code>. 콜이진행된PBX_SERVER
     */
    public String getHost() {
        return (String) get(36);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.hangup_cause</code>. 전화가끊어진원인
     */
    public void setHangupCause(String value) {
        set(37, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.hangup_cause</code>. 전화가끊어진원인
     */
    public String getHangupCause() {
        return (String) get(37);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.cmp_click_key</code>. 전화돌려주기,당겨받기 순서
     */
    public void setCmpClickKey(String value) {
        set(38, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.cmp_click_key</code>. 전화돌려주기,당겨받기 순서
     */
    public String getCmpClickKey() {
        return (String) get(38);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.cmp_click_from</code>. 콜을시도한성격 PDS,PRV,MAINDB등등
     */
    public void setCmpClickFrom(String value) {
        set(39, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.cmp_click_from</code>. 콜을시도한성격 PDS,PRV,MAINDB등등
     */
    public String getCmpClickFrom() {
        return (String) get(39);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.cmp_group_id</code>. 캠페인그룹
     */
    public void setCmpGroupId(Integer value) {
        set(40, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.cmp_group_id</code>. 캠페인그룹
     */
    public Integer getCmpGroupId() {
        return (Integer) get(40);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.cmp_customid</code>. 캠페인고객아이디
     */
    public void setCmpCustomid(String value) {
        set(41, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.cmp_customid</code>. 캠페인고객아이디
     */
    public String getCmpCustomid() {
        return (String) get(41);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.turn_over_cnt</code>. 전화돌려주기,당겨받기 순서
     */
    public void setTurnOverCnt(Integer value) {
        set(42, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.turn_over_cnt</code>. 전화돌려주기,당겨받기 순서
     */
    public Integer getTurnOverCnt() {
        return (Integer) get(42);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.turn_over_kind</code>. 전화돌려주기,당겨받기 종류
     */
    public void setTurnOverKind(String value) {
        set(43, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.turn_over_kind</code>. 전화돌려주기,당겨받기 종류
     */
    public String getTurnOverKind() {
        return (String) get(43);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.turn_over_number</code>. 전화돌려주기,당겨받기 번호
     */
    public void setTurnOverNumber(String value) {
        set(44, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.turn_over_number</code>. 전화돌려주기,당겨받기 번호
     */
    public String getTurnOverNumber() {
        return (String) get(44);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.turn_over_uniqueid</code>. 전화돌려주기,당겨받기 uniqueid
     */
    public void setTurnOverUniqueid(String value) {
        set(45, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.turn_over_uniqueid</code>. 전화돌려주기,당겨받기 uniqueid
     */
    public String getTurnOverUniqueid() {
        return (String) get(45);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.etc1</code>.
     */
    public void setEtc1(String value) {
        set(46, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.etc1</code>.
     */
    public String getEtc1() {
        return (String) get(46);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.etc2</code>.
     */
    public void setEtc2(String value) {
        set(47, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.etc2</code>.
     */
    public String getEtc2() {
        return (String) get(47);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.etc3</code>.
     */
    public void setEtc3(String value) {
        set(48, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.etc3</code>.
     */
    public String getEtc3() {
        return (String) get(48);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.etc4</code>.
     */
    public void setEtc4(String value) {
        set(49, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.etc4</code>.
     */
    public String getEtc4() {
        return (String) get(49);
    }

    /**
     * Setter for <code>CUSTOMDB.eicn_cdr_*.etc5</code>.
     */
    public void setEtc5(String value) {
        set(50, value);
    }

    /**
     * Getter for <code>CUSTOMDB.eicn_cdr_*.etc5</code>.
     */
    public String getEtc5() {
        return (String) get(50);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }
}
