package kr.co.eicn.ippbx.server.controller.api;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SipBuddies;
import kr.co.eicn.ippbx.server.repository.eicn.SipBuddiesRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.SipBuddies.SIP_BUDDIES;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/softphone-auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class SoftPhoneAuthController extends ApiBaseController {
    private final SipBuddiesRepository sipBuddiesRepository;

    @GetMapping("{peer}")
    public JsonResult<SipBuddies> get(@PathVariable String peer) {
        return data(sipBuddiesRepository.findOne(SIP_BUDDIES.NAME.eq(peer)));
    }

    @PutMapping("{peer}/{secret}")
    public ResponseEntity<JsonResult<Void>> put(@PathVariable String peer, @PathVariable String secret){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        Date time = new Date();
        String newSecret = secret + fmt.format(time);;
        sipBuddiesRepository.updateAndMD5SecretAllPbxServers(peer, newSecret);

        return ResponseEntity.ok(create());
    }
}
