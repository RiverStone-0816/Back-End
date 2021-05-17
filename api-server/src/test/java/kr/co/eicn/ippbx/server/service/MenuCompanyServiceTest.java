package kr.co.eicn.ippbx.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class MenuCompanyServiceTest {

	@Autowired
	private MenuCompanyService service;

//	@Test
//	public void get_menus() {
//		final List<MenuBasic> menus = service.getMenus();
//		for (MenuBasic menu : menus) {
//			log.info("{} {}.,  메뉴명: {}", (prefix(menu.getMenuLevel())), menu.getMenuLevel(), menu.getMenuName());
//		}
//	}
//
//	@Test
//	public void get_user_menus() {
//		List<CommonMenuCompany> menus = service.getUserMenus();
//		for (CommonMenuCompany menu : menus) {
//			log.info("{} {}.,  메뉴명: {}", (prefix(menu.getMenuLevel())), menu.getMenuLevel(), menu.getMenuName());
//		}
//	}

	private String prefix(Integer level) {
		StringBuilder prefix = new StringBuilder("-");
		for (int i = 0; i < level ; i++) {
			prefix.append("--");
		}
		return prefix.toString();
	}
}
