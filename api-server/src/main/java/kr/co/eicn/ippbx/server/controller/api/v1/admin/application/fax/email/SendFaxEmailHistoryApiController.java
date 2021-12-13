package kr.co.eicn.ippbx.server.controller.api.v1.admin.application.fax.email;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendCategory;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendFaxEmail;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendFile;
import kr.co.eicn.ippbx.model.dto.eicn.SendFaxEmailHistoryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SendFaxEmailResponse;
import kr.co.eicn.ippbx.model.search.SendFaxEmailHistorySearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.SendFaxEmailCategoryRepository;
import kr.co.eicn.ippbx.server.repository.eicn.SendFaxEmailHistoryRepository;
import kr.co.eicn.ippbx.server.repository.eicn.SendFileRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담어플리케이션 관리 > FAX/EMAIL 관리 > 발송이력
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/application/fax-email/history", produces = MediaType.APPLICATION_JSON_VALUE)
public class SendFaxEmailHistoryApiController extends ApiBaseController {

    private final SendFaxEmailHistoryRepository repository;
    private final SendFileRepository fileRepository;
    private final SendFaxEmailCategoryRepository categoryRepository;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<SendFaxEmailHistoryResponse>>> pagination(SendFaxEmailHistorySearchRequest search) {
        final Pagination<SendFaxEmail> pagination = repository.pagination(search);
        final List<SendFaxEmail> faxEmailList = repository.findAll();
        final Map<Long, String> fileList = fileRepository.findAll().stream().collect(Collectors.toMap(SendFile::getId, SendFile::getCategoryCode));

        final List<SendFaxEmailHistoryResponse> rows = pagination.getRows().stream()
                .map(e -> {
                    final SendFaxEmailHistoryResponse response = convertDto(e, SendFaxEmailHistoryResponse.class);

                    final List<SendFaxEmailResponse> receiveNumberList = new ArrayList<>();
                    receiveNumberList.add(
                            faxEmailList.stream()
                                    .filter(f -> f.getId().equals(e.getId()))
                                    .map(f -> convertDto(e, SendFaxEmailResponse.class))
                                    .findFirst().orElse(null)
                    );

                    if (Objects.nonNull(fileList.get(e.getFile()))) {
                        final SendCategory category = categoryRepository.findOne(fileList.get(e.getFile()));
                        response.setCategoryName(category.getCategoryName());
                    }

                    response.setReceiverNumber(receiveNumberList);
                    return response;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }
}
