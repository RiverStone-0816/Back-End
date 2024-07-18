package kr.co.eicn.ippbx.server.controller.api.v1.admin.stt.transcribe;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonTranscribeGroup;
import kr.co.eicn.ippbx.model.dto.eicn.TranscribeDataResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TranscribeGroupResponse;
import kr.co.eicn.ippbx.model.entity.customdb.TranscribeDataEntity;
import kr.co.eicn.ippbx.model.entity.customdb.TranscribeGroupEntity;
import kr.co.eicn.ippbx.model.search.TranscribeDataSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.service.TranscribeDataService;
import kr.co.eicn.ippbx.server.service.TranscribeGroupService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.data;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/stt/transcribe/data", produces = MediaType.APPLICATION_JSON_VALUE)
public class TranscribeDataApiController extends ApiBaseController {
    private final TranscribeGroupService transcribeGroupService;
    private final TranscribeDataService transcribeDataService;

    /**
     *
     * 전사툴 파일 목록조회
     */
    @GetMapping("")
    public ResponseEntity<JsonResult<List<TranscribeGroupResponse>>> list(TranscribeDataSearchRequest search) {
        List<TranscribeGroupEntity> groupList = transcribeGroupService.getRepository().list(search);
        List<TranscribeDataEntity> dataList = transcribeDataService.getRepository().list(search, groupList.stream().map(CommonTranscribeGroup::getSeq).collect(Collectors.toSet()));

        List<TranscribeGroupResponse> result = groupList.stream().map(e -> {
            TranscribeGroupResponse response = convertDto(e, TranscribeGroupResponse.class);

            response.setDataInfos(
                    dataList.stream().filter(f -> e.getSeq().equals(f.getGroupcode()))
                            .map(f -> convertDto(f, TranscribeDataResponse.class))
                            .collect(Collectors.toList())
            );

            return response;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(data(result));
    }

    @PutMapping("{seq}/{status}")
    public ResponseEntity<JsonResult<Void>> updateLearnStatus(@PathVariable Integer seq, @PathVariable String status) {
        transcribeDataService.updateLearnStatus(seq, status);

        return ResponseEntity.ok(data(null));
    }
}
