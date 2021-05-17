package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.repository.eicn.EicnMonitDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class EicnMonitDataService extends ApiBaseService {
    private final static Logger logger = LoggerFactory.getLogger(EicnMonitDataService.class);
    private final EicnMonitDataRepository eicnMonitDataRepository;
    private final static String COMMAND = "/home/ippbxmng/lib/eicn_monit.sh";

    public List<String> getMonitData(String host) {
        return eicnMonitDataRepository.getMonitData(host);
    }

    public void runExecuteServerMonit() {
        try {
            Runtime.getRuntime().exec(COMMAND);
        } catch (IOException ignore) {
        }
    }
}
