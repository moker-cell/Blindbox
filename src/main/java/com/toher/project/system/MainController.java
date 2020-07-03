package com.toher.project.system;

import com.toher.common.utils.shiro.ShiroUtils;
import com.toher.framework.configurer.ReadApplicationYmlUtil;
import com.toher.project.system.menu.entity.Menu;
import com.toher.project.system.menu.service.MenuService;
import com.toher.project.system.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/6 17:17
 */
@Controller
@RequestMapping("/main")
public class MainController {

    @Autowired
    private ReadApplicationYmlUtil readApplicationYmlUtil;
    //引入menuController 调用其递归方法查询
    @Resource
    private MenuService menuServiceImpl;

    /**
     * 进入系统主页
     *
     * @return
     */
    @RequestMapping("/index")
    public String main(HttpServletRequest request) {
        //查询菜单信息
        Map<String, Object> params = new HashMap();
        params.put("queryParentId", "isNull");
        List<Menu> objects = menuServiceImpl.getObjects(params);
        objects = getChildrenMenu(objects);
        request.setAttribute("menus", objects);
        //获取Shiro 用户身份信息
        User user = ShiroUtils.getAuthUser();
        request.setAttribute("user", user);

        //获取网址
        String path = request.getContextPath();
        String webUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/websocket/" + user.getUserId();
        request.setAttribute("webUrl", webUrl);
        return "system/main";
    }

    /**
     * 递归查询子集
     **/
    public List<Menu> getChildrenMenu(List<Menu> menus) {
        for (Menu m : menus) {
            String parentId = m.getMenuId();
            Map<String, Object> params = new HashMap();
            params.put("parentId", parentId);
            params.put("isNotShowFunction", 3);
            List<Menu> cmenus = menuServiceImpl.getObjects(params);
            m.setMenus(cmenus);
            if (cmenus != null && cmenus.size() > 0) {
                getChildrenMenu(cmenus);
            }
        }
        return menus;
    }

    /**
     * 图标选择
     *
     * @return
     */
    @RequestMapping("/icon.do")
    public String icon() {
        return "/system/main/icon";
    }
}
