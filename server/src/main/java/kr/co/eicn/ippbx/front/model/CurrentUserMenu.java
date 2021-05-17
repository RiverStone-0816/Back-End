package kr.co.eicn.ippbx.front.model;

import kr.co.eicn.ippbx.server.model.dto.configdb.UserMenuCompanyResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CurrentUserMenu {
    private List<UserMenuCompanyResponse> menus;

    public List<UserMenuCompanyResponse> findMenuByUrl(String url) {
        val menus = findMenuByUrl(this.menus, new Stack<>(), url);
        return menus != null ? Arrays.asList(menus) : new ArrayList<>();
    }

    private UserMenuCompanyResponse[] findMenuByUrl(List<UserMenuCompanyResponse> menus, Stack<UserMenuCompanyResponse> stack, String url) {
        for (UserMenuCompanyResponse menu : menus) {
            stack.add(menu);

            if (Objects.equals(menu.getMenuActionExeId(), url))
                return stack.toArray(new UserMenuCompanyResponse[0]);

            if (menu.getChildren().size() > 0) {
                final UserMenuCompanyResponse[] tree = findMenuByUrl(menu.getChildren(), stack, url);
                if (tree != null)
                    return tree;
            }

            stack.pop();
        }

        return null;
    }
}
