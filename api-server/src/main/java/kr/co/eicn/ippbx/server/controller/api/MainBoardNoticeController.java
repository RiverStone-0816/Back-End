package kr.co.eicn.ippbx.server.controller.api;

import kr.co.eicn.ippbx.model.entity.eicn.MainBoardEntity;
import kr.co.eicn.ippbx.server.repository.eicn.MainBoardRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/main-board-notice", produces = MediaType.APPLICATION_JSON_VALUE)
public class MainBoardNoticeController {
    private final MainBoardRepository mainBoardRepository;

    /**
     * 로그인 전 공지팝업
     * */
    @GetMapping("after")
    public JsonResult<List<MainBoardEntity>> after() {
        return JsonResult.data(mainBoardRepository.findAllLoginAfter());
    }

    /**
     * 로그인 전 공지팝업
     * */
    @GetMapping("before")
    public JsonResult<List<MainBoardEntity>> before() {
        return JsonResult.data(mainBoardRepository.findAllLoginBefore());
    }
}
