package kr.co.eicn.ippbx.server.model.enums;

/**
 * MEMBER : 상담원그룹
 * PDS_IVR : PDS IVR
 * ARS_RSCH : ARS설문
 */
public enum ConnectKind {
    MEMBER("MEMBER"), PDS_IVR("PDS_IVR"), ARS_RSCH("ARS_RSCH");

    private final String code;

    ConnectKind(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
