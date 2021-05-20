package kr.co.eicn.ippbx.server.controller.api.v1.admin.application.maindb;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HistoryUploadInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MaindbGroup;
import kr.co.eicn.ippbx.model.dto.eicn.GroupUploadLogDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.GroupUploadLogResponse;
import kr.co.eicn.ippbx.model.dto.eicn.MaindbUploadResponse;
import kr.co.eicn.ippbx.model.search.MaindbUploadSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.MaindbGroupRepository;
import kr.co.eicn.ippbx.server.repository.eicn.MaindbUploadRepository;
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
 * 상담어플리케이션 관리 > 고객DB 관리 > 업로드이력
 */

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/application/maindb/upload", produces = MediaType.APPLICATION_JSON_VALUE)
public class MaindbUploadApiController extends ApiBaseController {

    private final MaindbUploadRepository repository;
    private final MaindbGroupRepository maindbGroupRepository;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<MaindbUploadResponse>>> pagination(MaindbUploadSearchRequest search) {
        final Pagination<HistoryUploadInfo> pagination = repository.pagination(search);
        final Map<Integer, String> maindbGroupMap = maindbGroupRepository.findAll().stream().collect(Collectors.toMap(MaindbGroup::getSeq, MaindbGroup::getName));

        final List<MaindbUploadResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final MaindbUploadResponse response = convertDto(e, MaindbUploadResponse.class);
                    if (Objects.nonNull(maindbGroupMap.get(e.getGroupId())))
                        response.setMaindbGroupName(maindbGroupMap.get(e.getGroupId()));

                    return response;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    /**
     * 업로드 로그보기
     */
    @GetMapping("upload-log/{uploadId}")
    public ResponseEntity<JsonResult<GroupUploadLogDetailResponse>> uploadLog(@PathVariable String uploadId) {
        final HistoryUploadInfo uploadInfo = repository.findOne(uploadId);
        final GroupUploadLogDetailResponse response = convertDto(uploadInfo, GroupUploadLogDetailResponse.class);

        final File file = new File("/data/UPLOAD_LOG/" + g.getUser().getCompanyId() + "/" + uploadId);
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

    /**
     * 고객DB 그룹 목록조회
     */
    @GetMapping("maindb-group")
    public ResponseEntity<JsonResult<List<GroupUploadLogResponse>>> maindbGroup() {
        return ResponseEntity.ok(data(maindbGroupRepository.findAll().stream().map(e -> convertDto(e, GroupUploadLogResponse.class))
                .sorted(comparing(GroupUploadLogResponse::getSeq))
                .collect(Collectors.toList())));
    }
}
