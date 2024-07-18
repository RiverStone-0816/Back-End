package kr.co.eicn.ippbx.model.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TranscribeWriteFormRequest {
    private List<TextInfo> hypData;
    private List<TextInfo> refData;

    @Data
    public static class TextInfo {
        private Integer seq;
        @JsonProperty("start_ms")
        private Integer startMs;
        @JsonProperty("stop_ms")
        private Integer stopMs;
        private String text;
        private String learn;
    }
}
