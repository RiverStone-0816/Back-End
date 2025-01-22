package kr.co.eicn.ippbx.model.search.atcenter;

import kr.co.eicn.ippbx.util.page.PageQueryable;
import lombok.Data;

@Data
public class AtCenterSearchRequest {
    @PageQueryable
    public String firmNm;       // 업체명
    @PageQueryable
    public String brno;         // 사업자번호
    @PageQueryable
    public String mbrNm;        // 회원명
    @PageQueryable
    public String moblTelNo;    // 휴대폰번호


    @Data
    public static class AtCenterSearchCodeRequest {
        @PageQueryable
        public String urnkCd;    // 코드id
    }

    @Data
    public static class AtCenterSearchTradeRequest {
        @PageQueryable
        public String firmNo;        // 업체번호
        @PageQueryable
        public String cnsltQstnCd;   // 질문유형코드
    }

    @Data
    public static class AtCenterSearchChatRoomRequest {
        @PageQueryable
        private String mbrNo;        // 대화참여자회원번호
    }

    @Data
    public static class AtCenterSearchChatRequest {
        @PageQueryable
        private String chnnlId;      // 채널id
        @PageQueryable
        private String cnvrsSn;      // 대화순번
    }
}
