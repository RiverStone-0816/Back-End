package kr.co.eicn.ippbx.server.controller.api;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MainBoardFile;
import kr.co.eicn.ippbx.model.entity.eicn.MainBoardEntity;
import kr.co.eicn.ippbx.model.search.MainBoardRequest;
import kr.co.eicn.ippbx.server.config.RequestGlobal;
import kr.co.eicn.ippbx.server.repository.eicn.MainBoardFileRepository;
import kr.co.eicn.ippbx.server.repository.eicn.MainBoardRepository;
import kr.co.eicn.ippbx.server.service.FileSystemStorageService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.io.FilenameUtils.getFullPath;
import static org.apache.commons.io.FilenameUtils.getName;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.MainBoard.MAIN_BOARD;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.MainBoardTarget.MAIN_BOARD_TARGET;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/main-board-notice", produces = MediaType.APPLICATION_JSON_VALUE)
public class MainBoardNoticeController {
    private final MainBoardRepository mainBoardRepository;
    private final MainBoardFileRepository mainBoardFileRepository;
    private final FileSystemStorageService fileSystemStorageService;

    @Autowired
    protected RequestGlobal g;

    /**
     * 로그인 전 공지팝업
     * */
    @GetMapping("before")
    public ResponseEntity<JsonResult<List<MainBoardEntity>>> before() {
        final List<MainBoardEntity> mainBoardList = mainBoardRepository.findAllLoginBefore();
        mainBoardList.forEach(e -> e.setMainBoardFiles(mainBoardFileRepository.findByMainBoardId(e.getId())));

        return ResponseEntity.ok(data(mainBoardList));
    }

    /**
     * 로그인 후 공지팝업
     * */
    @GetMapping("after")
    public ResponseEntity<JsonResult<List<MainBoardEntity>>> after() {
        final List<MainBoardEntity> mainBoardList = mainBoardRepository.findAllLoginAfter();
        mainBoardList.forEach(e -> e.setMainBoardFiles(mainBoardFileRepository.findByMainBoardId(e.getId())));

        return ResponseEntity.ok(data(mainBoardList));
    }

    /**
     * 페이지 공지
     * */
    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<MainBoardEntity>>> pagination(MainBoardRequest search) {
        return ResponseEntity.ok(data(mainBoardRepository.pagination(search)));
    }

    /**
     * 상단 고정 공지
     * */
    @GetMapping("top-fix")
    public ResponseEntity<JsonResult<List<MainBoardEntity>>> topPage() {
        return ResponseEntity.ok(data(mainBoardRepository.topFixList()));
    }

    /**
     * 공지 view
     * */
    @GetMapping("{id}")
    public ResponseEntity<JsonResult<MainBoardEntity>> get(@PathVariable Long id) {
        return ResponseEntity.ok(data(mainBoardRepository.findOne(MAIN_BOARD.ID.eq(id).and(MAIN_BOARD_TARGET.COMPANY_ID.eq(g.getUser().getCompanyId()).or(MAIN_BOARD_TARGET.COMPANY_ID.eq("A"))))));
    }

    /**
     * 로그인 전 특정 파일 다운로드
     */
    @GetMapping(value = "before/{fileId}/{boardId}/specific-file-resource")
    public ResponseEntity<Resource> specificFileResourceBefore(@PathVariable Long fileId, @PathVariable Long boardId, HttpServletRequest request) {
        final MainBoardFile entity = mainBoardFileRepository.findByFileAndBoardId(fileId, boardId);

        final Resource resource = this.fileSystemStorageService.loadAsResource(Paths.get(getFullPath(entity.getPath())), getName(entity.getPath()));

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
                .headers(headers -> headers.add(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.builder("attachment").filename(Objects.requireNonNull(entity.getOriginalName()), StandardCharsets.UTF_8).build().toString()))
                .body(resource);
    }

    /**
     * 로그인 후 특정 파일 다운로드
     */
    @GetMapping(value = "after/{fileId}/{boardId}/specific-file-resource", params = {"token"})
    public ResponseEntity<Resource> specificFileResourceAfter(@PathVariable Long fileId, @PathVariable Long boardId, HttpServletRequest request) {
        final MainBoardFile entity = mainBoardFileRepository.findByFileAndBoardId(fileId, boardId);

        final Resource resource = this.fileSystemStorageService.loadAsResource(Paths.get(getFullPath(entity.getPath())), getName(entity.getPath()));

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
                .headers(headers -> headers.add(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.builder("attachment").filename(Objects.requireNonNull(entity.getOriginalName()), StandardCharsets.UTF_8).build().toString()))
                .body(resource);
    }

}
