package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.pds;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HistoryPdsUpload;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsGroup;
import kr.co.eicn.ippbx.model.dto.eicn.GroupUploadLogResponse;
import kr.co.eicn.ippbx.model.dto.eicn.GroupUploadLogDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PDSUploadSummaryResponse;
import kr.co.eicn.ippbx.model.enums.UploadStatus;
import kr.co.eicn.ippbx.model.search.PDSUploadSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PDSGroupRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PDSUploadRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 아웃바운드 관리 > PDS 관리 > 업로드이력 관리
 */

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/pds/upload", produces = MediaType.APPLICATION_JSON_VALUE)
public class PDSUploadApiController extends ApiBaseController {

    private final PDSUploadRepository repository;
    private final PDSGroupRepository groupRepository;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<PDSUploadSummaryResponse>>> pagination(PDSUploadSearchRequest search) {
        final Pagination<HistoryPdsUpload> pagination = repository.pagination(search);
        final Map<Integer, String> pdsGroupMap = groupRepository.findAll().stream().collect(Collectors.toMap(PdsGroup::getSeq, PdsGroup::getName));

        final List<PDSUploadSummaryResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final PDSUploadSummaryResponse response =  convertDto(e, PDSUploadSummaryResponse.class);
                    if (Objects.nonNull(pdsGroupMap.get(e.getPdsGroupId())))
                        response.setPdsGroupName(pdsGroupMap.get(e.getPdsGroupId()));

                    if (Objects.nonNull(e.getUploadStatus()) && Objects.equals("", e.getUploadStatus())) {
                        if (Objects.equals(UploadStatus.UPLOAD.getCode(), e.getUploadStatus()))
                            response.setUploadStatusName(UploadStatus.UPLOAD.getCode());
                        if (Objects.equals(UploadStatus.UPLOADING.getCode(), e.getUploadStatus()))
                            response.setUploadStatusName(UploadStatus.UPLOADING.getCode());
                        else
                            response.setUploadStatusName(UploadStatus.UPLOAD_ERROR.getCode());
                    } else
                        response.setUploadStatusName(UploadStatus.NONE.getCode());

                    return response;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping("upload-log/{uploadId}")
    public ResponseEntity<JsonResult<GroupUploadLogDetailResponse>> uploadLog(@PathVariable String uploadId) {
        final HistoryPdsUpload pdsUploadInfo = repository.findOne(uploadId);
        final GroupUploadLogDetailResponse response = convertDto(pdsUploadInfo, GroupUploadLogDetailResponse.class);

        final File file = new File("/data/UPLOAD_LOG/"+ g.getUser().getCompanyId() + "/" + uploadId);
        final StringBuilder buffer = new StringBuilder();
        if (file.exists()) {
            try (final FileInputStream in = new FileInputStream(file);
                 final InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
                 final BufferedReader br = new BufferedReader(reader)) {
                String line;
                while ((line = br.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

            } catch (IOException e) {
                log.error("File ERROR[error={}]", e.getMessage());
                throw new IllegalArgumentException("파일 정보를 읽어올 수 없습니다.");
            }
        } else {
            System.out.println("FILE ERROR[파일이 존재하지 않습니다]");
        }

        response.setMentText(buffer.toString());

        return ResponseEntity.ok().body(data(response));
    }

    @GetMapping("pds-group")
    public ResponseEntity<JsonResult<List<GroupUploadLogResponse>>> pdsGroup() {
        return ResponseEntity.ok(data(groupRepository.findAll().stream()
                .map(e -> convertDto(e, GroupUploadLogResponse.class))
                .sorted(comparing(GroupUploadLogResponse::getSeq))
                .collect(Collectors.toList())));
    }
}
