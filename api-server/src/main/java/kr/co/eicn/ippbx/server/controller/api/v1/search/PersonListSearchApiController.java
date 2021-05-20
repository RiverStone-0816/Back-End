package kr.co.eicn.ippbx.server.controller.api.v1.search;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchPersonListResponse;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.data;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/search/person", produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonListSearchApiController extends ApiBaseController {
    private final PersonListRepository repository;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<SearchPersonListResponse>>> search(@RequestParam (required = false) List<String> group){
        List<SearchPersonListResponse> list = repository.findAllByGroup(group).stream().map(e -> convertDto(e, SearchPersonListResponse.class)).collect(Collectors.toList());
        return ResponseEntity.ok(data(list));
    }
}
