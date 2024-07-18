package kr.co.eicn.ippbx.model.dto.eicn;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TranscribeWriteResponse {
    private Integer seq;
    private List<HypInfo> dataList;
    private List<HypInfo> refList;

    @Data
    public static class HypInfo {
        private Integer seq;
        @JsonProperty("start_ms")
        private Integer startMs;
        @JsonProperty("stop_ms")
        private Integer stopMs;
        @JsonAlias({"hyp_text", "ref_text"})
        private String text;
        @JsonProperty("learn")
        private String learn;
    }
}
