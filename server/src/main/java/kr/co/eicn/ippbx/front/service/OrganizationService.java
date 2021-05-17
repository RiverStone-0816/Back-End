package kr.co.eicn.ippbx.front.service;

import kr.co.eicn.ippbx.front.service.api.OrganizationApiInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.CompanyTreeLevelNameResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.OrganizationSummaryResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@Service
public class OrganizationService {
    private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    @Autowired
    private OrganizationApiInterface organizationApiInterface;

    public List<String> getHierarchicalOrganizationNames(String groupCode) throws IOException, ResultFailException {
        if (StringUtils.isEmpty(groupCode))
            return new ArrayList<>();


        return organizationApiInterface.getCompanyTreeGroupCodeList(groupCode)
                .stream().sorted(Comparator.comparingInt(OrganizationSummaryResponse::getGroupLevel))
                .map(OrganizationSummaryResponse::getGroupName).collect(Collectors.toList());
    }

    public Map<Integer, String> metaTypeMap() throws IOException, ResultFailException {
        return organizationApiInterface.listMetaType().stream().collect(Collectors.toMap(CompanyTreeLevelNameResponse::getGroupLevel, CompanyTreeLevelNameResponse::getGroupTreeName));
    }
}
