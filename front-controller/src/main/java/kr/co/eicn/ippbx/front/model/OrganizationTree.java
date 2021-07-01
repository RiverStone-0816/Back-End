package kr.co.eicn.ippbx.front.model;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.model.entity.eicn.Organization;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.TreeSet;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class OrganizationTree extends Organization {
    private TreeSet<OrganizationTree> children = new TreeSet<>(Comparator.comparing(CompanyTree::getGroupName));

    public OrganizationTree(Organization o) {
        ReflectionUtils.copy(this, o);
    }
}
