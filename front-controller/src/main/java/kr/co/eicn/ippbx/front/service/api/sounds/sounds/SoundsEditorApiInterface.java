package kr.co.eicn.ippbx.front.service.api.sounds.sounds;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.form.SoundEditorFormRequest;
import kr.co.eicn.ippbx.model.form.SoundEditorListenRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;

@Service
public class SoundsEditorApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(SoundsEditorApiInterface.class);

    private static final String subUrl = "/api/v1/admin/sounds/editor/";

    public Integer make(String soundType, SoundEditorFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl + "make?soundType=" + soundType, form, Integer.class, false).getData();
    }

    public URI preListen(SoundEditorListenRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl + "pre-listen", form, URI.class, false).getData();
    }
}
