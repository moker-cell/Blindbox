package com.toher.project.system.role.controller;

import com.toher.common.dto.DataTableJson;
import com.toher.common.dto.ReturnJsonData;
import com.toher.common.utils.CommonUtils;
import com.toher.framework.redis.RedisUtil;
import com.toher.project.system.menu.entity.Menu;
import com.toher.project.system.menu.service.MenuService;
import com.toher.project.system.role.entity.Role;
import com.toher.project.system.role.entity.RoleGroup;
import com.toher.project.system.role.entity.RolePermission;
import com.toher.project.system.role.service.RoleGroupService;
import com.toher.project.system.role.service.RoleService;
import com.toher.project.system.user.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.toher.common.utils.CommonUtils.parameterMapToMap;
import static com.toher.common.utils.GsonUtil.GsonString;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/6 18:44
 */
@Controller
@RequestMapping("/system/role")
public class RoleController {

    @Resource
    private RoleGroupService roleGroupServiceImpl;
    @Resource
    private RoleService roleServiceImpl;
    @Resource
    private MenuService menuServiceImpl;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisCacheManager cacheManager;

    /**
     * 用户角色列表
     * @param request
     * @return
     */
    @RequiresPermissions("system:role:index")
    @RequestMapping("/index")
    public String index(HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("session_user");
        List<RoleGroup> roleGroups = roleGroupServiceImpl.getObjects(null);
        request.setAttribute("roleGroups", roleGroups);
        request.setAttribute("user", user);
        return "system/role/list";
    }

    /**
     * 角色列表JSON数据
     * @param request
     * @return
     */
    @RequestMapping("/data")
    @ResponseBody
    public DataTableJson dataJson(HttpServletRequest request) {
        Map<String, Object> map = parameterMapToMap(request);
        List<Role> objects = roleServiceImpl.getObjects(map);
        DataTableJson<Role> result = new DataTableJson<Role>(objects);
        return result;
    }

    /**
     * 角色组添加视图
     * @param request
     * @return
     */
    @RequiresPermissions("system:role:addGroup")
    @RequestMapping("/addGroup")
    public String addGroupView(HttpServletRequest request) {
        return "system/role/roleGroup_add_edit";
    }

    /**
     * 角色组修改视图
     * @param request
     * @return
     */
    @RequiresPermissions("system:role:editGroup")
    @RequestMapping("/editGroup/{roleGroupId}")
    public String editGroupView(HttpServletRequest request,@PathVariable("roleGroupId") Integer roleGroupId) {
        RoleGroup roleGroup = roleGroupServiceImpl.findObjectByPrimaryKey(roleGroupId);
        request.setAttribute("roleGroup", roleGroup);
        return "system/role/roleGroup_add_edit";
    }

    /**
     * 保存角色组 根据角色组ID 判断添加修改
     * @param request
     * @param roleGroup 角色组
     * @return
     */
    @RequiresPermissions("system:role:saveGroup")
    @RequestMapping(value = "/saveGroup")
    @ResponseBody
    public ReturnJsonData saveGroup(HttpServletRequest request, RoleGroup roleGroup) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = 0;
        if (roleGroup != null) {
            //判断是否是添加
            if (roleGroup.getGroupRoleId()==null) {
                success = roleGroupServiceImpl.saveObjectSelective(roleGroup);
            } else {
                //如果getGroupRoleId存在 则更新
                success = roleGroupServiceImpl.editObjectSelective(roleGroup);
            }
        }
        if (success > 0) {
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }

    /**
     * 删除角色组 Service 中同步删除 角色 + 角色权限
     * @param request
     * @param roleGroupId
     * @return
     */
    @RequiresPermissions("system:role:deleteGroup")
    @RequestMapping(value = "/deleteGroup/{roleGroupId}")
    @ResponseBody
    public ReturnJsonData deleteGroup(HttpServletRequest request, @PathVariable("roleGroupId") Integer roleGroupId) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = roleGroupServiceImpl.deleteObjectByPrimaryKey(roleGroupId);
        if (success > 0) {
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }


    /**
     * 角色添加视图
     * @param request
     * @return
     */
    @RequiresPermissions("system:role:add")
    @RequestMapping("/addRole")
    public String addRoleView(HttpServletRequest request) {
        //查询角色组列表
        List<RoleGroup> roleGroups = roleGroupServiceImpl.getObjects(null);
        request.setAttribute("roleGroups",roleGroups);
        //如果已经进入对应组别 则默认选择对应组别
        String parent_id = request.getParameter("parent_id");
        if(StringUtils.isNotEmpty(parent_id)) {
            RoleGroup roleGroup = roleGroupServiceImpl.findObjectByPrimaryKey(Integer.parseInt(parent_id));
            request.setAttribute("roleGroup",roleGroup);
        }
        return "system/role/role_add_edit";
    }

    /**
     * 保存角色 根据角色ID 判断添加修改
     * @param request
     * @param role 角色组
     * @return
     */
    @RequiresPermissions("system:role:save")
    @RequestMapping(value = "/saveRole")
    @ResponseBody
    public ReturnJsonData saveRole(HttpServletRequest request, Role role) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = 0;
        if (role != null) {
            //判断是否是添加
            if (StringUtils.isEmpty(role.getRoleId())) {
                role.setRoleId(CommonUtils.snGen("RO"));
                success = roleServiceImpl.saveObjectSelective(role);
            } else {
                //如果getGroupRoleId存在 则更新
                success = roleServiceImpl.editObjectSelective(role);
            }
        }
        if (success > 0) {
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }

    /**
     * 角色修改视图
     * @param request
     * @return
     */
    @RequiresPermissions("system:role:edit")
    @RequestMapping("/editRole/{roleId}")
    public String editRoleView(HttpServletRequest request, @PathVariable String roleId) {
        //查询角色组列表
        List<RoleGroup> roleGroups = roleGroupServiceImpl.getObjects(null);
        request.setAttribute("roleGroups",roleGroups);

        Role role = roleServiceImpl.findObjectByPrimaryKey(roleId);
        request.setAttribute("role",role);
        Integer parent_id = null;
        if(role!=null){
            parent_id = role.getGroupRoleId();
        }
        //如果已经进入对应组别 则默认选择对应组别
        if(parent_id!=null) {
            RoleGroup roleGroup = roleGroupServiceImpl.findObjectByPrimaryKey(parent_id);
            request.setAttribute("roleGroup",roleGroup);
        }
        return "system/role/role_add_edit";
    }

    /**
     * 单个角色删除
     * @param request
     * @return Role
     */
    @RequiresPermissions("system:role:delete")
    @RequestMapping("/deleteRole/{roleId}")
    @ResponseBody
    public ReturnJsonData deleteRole(HttpServletRequest request, @PathVariable String roleId) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = roleServiceImpl.deleteObjectByPrimaryKey(roleId);
        if (success > 0) {
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }
    /**
     * 批量角色删除
     * @param request
     * @return Role
     */
    @RequiresPermissions("system:role:delete")
    @RequestMapping("/deleteBatchRole")
    @ResponseBody
    public ReturnJsonData deleteBatchRole(HttpServletRequest request, String[] roleIds) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = 0;
        if(roleIds!=null && roleIds.length>0) {
            List<String> data = Arrays.asList(roleIds);
            success = roleServiceImpl.deleteBatchObject(data);
        }
        if (success > 0) {
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }

    /**
     * 角色权限设置显示
     * @param request
     * @param roleId 角色ID
     * @return
     */
    @RequiresPermissions("system:role:authority")
    @RequestMapping(value = "/authority/{roleId}")
    public String authority(HttpServletRequest request,@PathVariable("roleId") String roleId) {
        Map<String, Object> map = parameterMapToMap(request);
        //查询角色的所有权限
        Role role = roleServiceImpl.getRoleAndPermission(roleId);
        List<RolePermission> rolePermissions = role.getRolePermissions();
        //将结果集转换JSON
        List<Map<String, Object>> list = new ArrayList();
        Map<String, Object> MAPjson = new HashMap();
        List<Menu> objects = menuServiceImpl.getObjects(map);
        for (Menu m : objects) {
            MAPjson = new HashMap();
            MAPjson.put("id", m.getMenuId());
            MAPjson.put("pId", m.getParentId());
            Boolean isParent = false;
            if(m.getMenus()!=null && m.getMenus().size()>0){
                isParent = true;
            }
            MAPjson.put("isParent", isParent);
            MAPjson.put("name", m.getMenuName());
            for(RolePermission p : rolePermissions){
                Menu menu = p.getMenu();
                //如果角色权限存在该菜单ID 则 默认勾选
                if(menu!=null && menu.getMenuId().equals(m.getMenuId())){
                    MAPjson.put("checked", true);
                    break;
                }
            }
            list.add(MAPjson);
        }
        request.setAttribute("json",GsonString(list));
        request.setAttribute("roleId",roleId);
        System.out.println("role:" + GsonString(role));
        return "system/role/authority";
    }

    /**
     * 角色权限设置保存
     * @param AuthorityIds 勾选的权限ID数组
     * @param roleId 角色ID
     * @return
     */
    @RequestMapping(value = "/saveAuthority/{roleId}")
    @ResponseBody
    public String saveAuthority(String[] AuthorityIds, @PathVariable("roleId") String roleId){
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = 0;
        List<RolePermission> rolePermissions = new ArrayList();
        for (int i=0; i<AuthorityIds.length ; i++) {
            //封装保存对象
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setMenuId(AuthorityIds[i]);
            rolePermissions.add(rolePermission);
        }
        success = roleServiceImpl.savePermission(rolePermissions);
        if (success > 0) {
            // 清除redis中shiro权限缓存
            redisUtil.deleteByPrex(cacheManager.getKeyPrefix() + "*");
            json.setCode("success");
            json.setMessage("添加成功");
        }
        return GsonString(json);
    }
}
