package kr.co.eicn.ippbx.front.controller.web.admin.stt.transcribe;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.stt.transcribe.TranscribeDataApiInterface;
import kr.co.eicn.ippbx.front.service.api.stt.transcribe.TranscribeWriteApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.TranscribeDataResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TranscribeGroupResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TranscribeWriteResponse;
import kr.co.eicn.ippbx.model.search.TranscribeDataSearchRequest;
import kr.co.eicn.ippbx.model.search.TranscribeWriteSearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/stt/transcribe/write")
public class TranscribeWriteController extends BaseController {
    private final TranscribeWriteApiInterface transcribeWriteApiInterface;
    private final TranscribeDataApiInterface transcribeDataApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") TranscribeWriteSearchRequest search) throws IOException, ResultFailException {
        List<TranscribeGroupResponse> groupList = transcribeDataApiInterface.list(new TranscribeDataSearchRequest());
        model.addAttribute("groupList", groupList);
        List<TranscribeDataResponse> dataList = new ArrayList<>();
        groupList.forEach(e -> dataList.addAll(e.getDataInfos()));
        model.addAttribute("dataList", dataList);

        if (search.getFileSeq() != null) {
            TranscribeWriteResponse entity = transcribeWriteApiInterface.get(search.getFileSeq());

            model.addAttribute("entity", entity);
        }

        return "admin/stt/transcribe/write/ground";
    }
}
