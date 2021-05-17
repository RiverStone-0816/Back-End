package kr.co.eicn.ippbx.front.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScreenDataForByService {
    private List<ScreenDataForIntegration> services = new ArrayList<>();
}
