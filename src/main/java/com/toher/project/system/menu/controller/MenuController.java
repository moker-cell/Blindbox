package com.toher.project.system.menu.controller;

import com.toher.common.dto.DataTableJson;
import com.toher.common.dto.ReturnJsonData;
import com.toher.common.utils.CommonUtils;
import com.toher.common.utils.GsonUtil;
import com.toher.framework.annotation.Log;
import com.toher.project.common.BaseController;
import com.toher.project.system.menu.entity.Menu;
import com.toher.project.system.menu.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.toher.common.utils.CommonUtils.parameterMapToMap;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/10 10:31
 */
@Controller
@RequestMapping("/system/menu")
public class MenuController extends BaseController {
    @Resource
    private MenuService menuServiceImpl;


    /**
     * 进入菜单列表
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequiresPermissions("system:menu:index")
    @Log(title = "菜单管理", action = "view")
    @RequestMapping("/index")
    public String index(HttpServletRequest request){
        return "system/menu/list";
    }

    /**
     * 获取dataTable JSON数据
     * @param request
     * @return
     */
    @RequestMapping("/dataJson")
    @ResponseBody
    public DataTableJson dataJson(HttpServletRequest request){
        List<Menu> menus = new ArrayList();
        List<Menu> objects = getTreeMeun(menus,null);
        DataTableJson<Menu> result = new DataTableJson<Menu>(objects);
        return result;
    }

    /**
     * 递归查询方法,用于树形菜单的展现
     * @param objects
     * @param parentId
     */
    public List<Menu> getTreeMeun(List<Menu> objects, String parentId){
        Map<String, Object> params = new HashMap();
        //默认查询顶级菜单
        if(parentId==null) {
            params.put("queryParentId", "isNull");
        }else{
            params.put("parentId", parentId);
        }
        List<Menu> menus = menuServiceImpl.getObjects(params);
        return menus;
    }

    /**
     * 菜单添加列表
     *
     * @param request 添加下级菜单查询当前菜单对象
     * @return
     */
    @RequiresPermissions("system:menu:add")
    @Log(title = "菜单管理", action = "add")
    @RequestMapping("/add")
    public String addView(HttpServletRequest request, String menuId) {
        if (StringUtils.isNotBlank(menuId)) {
            Menu pmenu = menuServiceImpl.findObjectByPrimaryKey(menuId);
            request.setAttribute("pmenu", pmenu);
        }
        return "system/menu/add_edit";
    }

    @RequiresPermissions("system:menu:edit")
    @Log(title = "菜单管理", action = "edit")
    @RequestMapping("/edit/{menuId}")
    public String editView(HttpServletRequest request, @PathVariable String menuId) {
        if (StringUtils.isNotBlank(menuId)) {
            Menu menu = menuServiceImpl.findObjectByPrimaryKey(menuId);
            request.setAttribute("menu", menu);
            //查找上级菜单名称
            if(menu!=null) {
                Menu pmenu = menuServiceImpl.findObjectByPrimaryKey(menu.getParentId());
                request.setAttribute("pmenu", pmenu);
            }
        }
        return "system/menu/add_edit";
    }

    /**
     * 菜单保存方法
     *
     * @param request
     * @param menu    菜单对象
     * @return JSON 作为前端AJAX回调判断
     */
    @RequiresPermissions("system:menu:save")
    @Log(title = "菜单管理", action = "save")
    @RequestMapping("/save")
    @ResponseBody
    public ReturnJsonData save(HttpServletRequest request, Menu menu) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = 0;
        if (menu != null) {
            //判断是修改还是 添加
            if (StringUtils.isBlank(menu.getMenuId())) {
                menu.setMenuId(CommonUtils.snGen("ME"));
                success = menuServiceImpl.saveObjectSelective(menu);
            } else {
                success = menuServiceImpl.editObjectSelective(menu);
            }
            if (success > 0) {
                json.setCode("success");
                json.setMessage("操作成功");
            }
        }
        return json;
    }

    /**
     * 删除菜单
     * @param request
     * @param menuId 菜单ID
     * @return
     */
    @RequiresPermissions("system:menu:delete")
    @Log(title = "菜单管理", action = "delete")
    @RequestMapping("/delete/{menuId}")
    @ResponseBody
    public ReturnJsonData delete(HttpServletRequest request , @PathVariable String menuId) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = 0;
            if (StringUtils.isNotBlank(menuId)) {
                Menu m = new Menu();
                m.setParentId(menuId);
                List<Menu> objects = menuServiceImpl.getObjectsByEntity(m);
                //删除判断是否存在子集记录，存在不允许删除
                if(objects!=null && objects.size()>0){
                    json.setMessage("菜单存在下级菜单，请先删除子菜单");
                }else {
                    success = menuServiceImpl.deleteObjectByPrimaryKey(menuId);
                }
            }
        if (success > 0) {
            json.setCode("success");
            json.setMessage("成功删除 " + success + " 条记录");
        }
        return json;
    }

    /**
     * 批量更新排序
     * @param request
     * @param menuSortId 更新的主键
     * @param menuSort 更新的排序值
     * @return
     */
    @RequiresPermissions("system:menu:sort")
    @RequestMapping("/updateSort")
    @ResponseBody
    public ReturnJsonData updateSort(HttpServletRequest request,String[] menuSortId,int[] menuSort){
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = 0;
        for (int i=0; i<menuSortId.length ;i++) {
            Menu m = new Menu();
            m.setMenuId(menuSortId[i]);
            m.setMenuSort(menuSort[i]);
            success = success + menuServiceImpl.editObjectSelective(m);
        }
        if (success > 0) {
            json.setCode("success");
            json.setMessage("成功更新 " + success + " 条记录");
        }
        return json;
    }

    /**
     * 更新状态
     * @param menu 对象接收参数
     * @return JSON 作为前端AJAX回调判断
     */
    @RequiresPermissions("system:menu:status")
    @RequestMapping("/updateStatus")
    @ResponseBody
    public ReturnJsonData updateStatus(Menu menu){
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = menuServiceImpl.editObjectSelective(menu);
        if (success > 0) {
            json.setCode("success");
            json.setMessage("成功更新 " + success + " 条记录");
        }
        return json;
    }

    /**
     * 获取属性菜单过滤功能类型
     * @param request
     * @param menu
     * @return
     */
    @RequestMapping("/menuTree")
    public String treeAll(HttpServletRequest request, Menu menu) {
        Map<String, Object> map = parameterMapToMap(request);
        //树形菜单默认取菜单类型过滤功能类型
        map.put("isNotShowFunction", 3);
        map.put("parentId","");
        //将结果集转换JSON
        List<Map<String, Object>> list = new ArrayList();
        Map<String, Object> MAPjson = new HashMap();
        List<Menu> objects = menuServiceImpl.getObjects(map);
        for (Menu m : objects) {
            MAPjson = new HashMap();
            MAPjson.put("id", m.getMenuId());
            MAPjson.put("pId", m.getParentId());
            MAPjson.put("isParent", !m.getMenus().isEmpty());
            MAPjson.put("name", m.getMenuName());
            list.add(MAPjson);
        }
        request.setAttribute("jsonData", GsonUtil.GsonString(list));
        return "system/menu/ztree";
    }
}
