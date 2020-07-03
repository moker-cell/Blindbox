package com.toher.project.system.user.controller;

import com.github.crab2died.ExcelUtils;
import com.toher.common.dto.DataTableJson;
import com.toher.common.dto.ReturnJsonData;
import com.toher.common.utils.CommonUtils;
import com.toher.framework.annotation.Log;
import com.toher.framework.configurer.ReadApplicationYmlUtil;
import com.toher.framework.redis.RedisUtil;
import com.toher.project.common.BaseController;
import com.toher.project.system.role.entity.Role;
import com.toher.project.system.role.entity.RoleGroup;
import com.toher.project.system.role.service.RoleGroupService;
import com.toher.project.system.role.service.RoleService;
import com.toher.project.system.user.entity.User;
import com.toher.project.system.user.entity.UserRole;
import com.toher.project.system.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

import static com.toher.common.utils.CommonUtils.*;
import static com.toher.common.utils.GsonUtil.GsonStringDate;
import static com.toher.common.utils.file.FileUtil.createDir;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/17 10:43
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

    private final String templatePath = "system/user/";

    //注入自定义配置
    @Resource
    private ReadApplicationYmlUtil readApplicationYmlUtil;

    @Resource
    private UserService userServiceImpl;
    @Resource
    private RoleService roleServiceimpl;
    @Resource
    private RoleGroupService roleGroupServiceimpl;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisCacheManager cacheManager;

    @RequiresPermissions("system:user:index")
    @Log(title = "用户管理", action = "view")
    @RequestMapping("/index")
    public String index(HttpServletRequest request) {
        List<Role> roles = roleServiceimpl.getObjectsByEntity(null);
        request.setAttribute("roles", roles);
        return templatePath + "index";
    }

    /**
     * 用户列表JSON数据
     *
     * @param request
     * @return
     */
    @RequestMapping("/dataJson")
    @ResponseBody
    public String dataJson(HttpServletRequest request) {
        Map<String, Object> map = parameterMapToMap(request);
        List<User> objects = userServiceImpl.getObjects(map);
        DataTableJson<User> result = new DataTableJson<User>(objects);
        return GsonStringDate(result, "yyyy-MM-dd HH:mm");
    }

    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", action = "edit")
    @RequestMapping("/editDataJson")
    @ResponseBody
    public String editDataJson(HttpServletRequest request) {
        Map<String, Object> editDataJsonMap = new HashMap();
        User user = null;
        int success = 0;
        try {
            Map<String, Object> map = editTableDataToMap(request, "userId");
            //将可编辑表格封装数据转换对象
            User u = (User) mapToObject(map, User.class);
            success = userServiceImpl.editObjectSelective(u);
            if (success > 0) {
                user = userServiceImpl.findObjectByPrimaryKey(u.getUserId());
                editDataJsonMap.put("data", user);
            }
        } catch (Exception ex) {
            logger.error("用户管理可编辑表格数据错误", ex);
        }
        return GsonStringDate(editDataJsonMap, "yyyy-MM-dd HH:mm");
    }

    /**
     * 用户导出
     *
     * @param request
     * @return
     */
    @RequiresPermissions("system:user:export")
    @Log(title = "用户管理", action = "export")
    @RequestMapping("/export")
    @ResponseBody
    public ReturnJsonData export(HttpServletRequest request) {
        ReturnJsonData json = new ReturnJsonData("error", "导出失败");
        Map<String, Object> map = parameterMapToMap(request);
        List<User> objects = userServiceImpl.getObjects(map);
        try {
            //获取classPath路径
            String path = readApplicationYmlUtil.getUploadPath() + File.separator + "execl" + File.separator;
            System.out.println(path);
            //设置文件名称
            String filename = dateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            //硬盘创建保存目录
            createDir(path + filename);
            ExcelUtils.getInstance().exportObjects2Excel(objects, User.class, true, null, true, path + filename);
            json.setCode("success");
            json.setMessage("导出成功");
            Map<String, Object> param = new HashMap();
            param.put("name", "upload/execl/" + filename);
            json.setParams(param);
        } catch (Exception ex) {
            logger.error("用户导出", ex);
        }
        return json;
    }

    /**
     * 用户添加视图
     *
     * @param request
     * @return
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "用户管理", action = "add")
    @RequestMapping("/add")
    public String addView(HttpServletRequest request) {
        request.setAttribute("roleGroups", getRoleList(request));
        return templatePath + "add";
    }

    /**
     * 用户添加视图
     *
     * @param request
     * @return
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", action = "edit")
    @RequestMapping("/edit/{userId}")
    public String editView(HttpServletRequest request, @PathVariable("userId") String userId) {
        User user = userServiceImpl.findObjectByPrimaryKey(userId);
        request.setAttribute("user", user);
        request.setAttribute("roleGroups", getRoleList(request));
        //获取List<String> 用户角色组ID集合 作用于option选中
        request.setAttribute("roleIds", user.getRoleIds());
        return templatePath + "edit";
    }


    /**
     * 用户更新及保存
     *
     * @param request
     * @param user
     * @return
     */
    @RequiresPermissions("system:user:save")
    @Log(title = "用户管理", action = "save")
    @RequestMapping(value = "/save")
    @ResponseBody
    public ReturnJsonData save(HttpServletRequest request, User user, String[] rolesIds) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        if(user == null || StringUtils.isAnyBlank(user.getUsername(), user.getPhone())){
            json.setMessage("用户名、手机号不能为空");
            return json;
        }
        // 判断用户名和手机号唯一
        int count = userServiceImpl.selectCountByOnly(user.getUsername(), user.getPhone());
        if(count > 0){
            if(StringUtils.isNotBlank(user.getUserId()) && count == 1){
                // 更新操作并且只有一条数据，判断是不是本身
                User one = userServiceImpl.selectOneByOnly(user.getUsername(), user.getPhone());
                if(!user.getUserId().equals(one.getUserId())){
                    json.setMessage("存在用户名或手机号");
                    return json;
                }
            }else{
                json.setMessage("存在用户名或手机号");
                return json;
            }
        }
        int success;
        if (StringUtils.isNotBlank(user.getPassword())) {
            // 将密码进行加密
            String password = new SimpleHash("SHA-1",readApplicationYmlUtil.getSalt(), user.getPassword()).toString();
            user.setPassword(password);
        }
        //判断是否是添加
        if (StringUtils.isBlank(user.getUserId())) {
            user.setUserId(CommonUtils.snGen("UU"));
            success = userServiceImpl.saveObjectSelective(user);
        } else {
            //如果user_id存在 则更新
            success = userServiceImpl.editObjectSelective(user);
            // 清除redis中shiro权限缓存
            redisUtil.deleteByPrex(cacheManager.getKeyPrefix() + "*");
        }
        if (success > 0) {
            commonSaveUserRole(rolesIds, user.getUserId());
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }


    /**
     * 编辑角色页面
     *
     * @param request
     * @param userId  用户ID
     * @return json
     */
    @RequiresPermissions("system:user:editRole")
    @RequestMapping(value = "/editRole/{userId}")
    public String editRoleView(HttpServletRequest request, @PathVariable("userId") String userId) {
        request.setAttribute("roleGroups", getRoleList(request));
        if (StringUtils.isNotBlank(userId)) {
            User user = userServiceImpl.findObjectByPrimaryKey(userId);
            request.setAttribute("user", user);
            //获取List<String> 用户角色组ID集合 作用于option选中
            request.setAttribute("roleIds", user.getRoleIds());
        }
        return templatePath + "edit_role";
    }

    /**
     * 用户设置角色操作
     *
     * @param request
     * @param rolesIds
     * @return
     */
    @RequiresPermissions("system:user:editRole")
    @RequestMapping(value = "/saveRole")
    @ResponseBody
    public ReturnJsonData saveUserRole(HttpServletRequest request, String[] rolesIds) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        String userId = request.getParameter("userId");
        int success = commonSaveUserRole(rolesIds, userId);
        if (success > 0) {
            // 清除redis中shiro权限缓存
            redisUtil.deleteByPrex(cacheManager.getKeyPrefix() + "*");
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }

    /**
     * 删除用户方法
     *
     * @param request
     * @return JSON 作为前端AJAX回调判断
     */
    @RequiresPermissions("system:user:delete")
    @Log(title = "用户管理", action = "delete")
    @RequestMapping("/delete")
    @ResponseBody
    public ReturnJsonData delete(HttpServletRequest request, String[] userIds) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = 0;
        for (String userId : userIds) {
            if (StringUtils.isNotBlank(userId)) {
                //最后删除当前选中
                success = success + userServiceImpl.deleteObjectByPrimaryKey(userId);
                //一并删除用户的角色
                userServiceImpl.deleteUserRole(userId);
            }
        }
        if (success > 0) {
            json.setCode("success");
            json.setMessage("成功删除 " + success + " 条记录");
        }
        return json;
    }

    /**
     * 更新用户属性 通用方法
     * @param user
     * @return
     */
    @RequiresPermissions("system:user:update")
    @Log(title = "用户管理", action = "update")
    @RequestMapping("/updateProperty")
    @ResponseBody
    public ReturnJsonData updateProperty(User user) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = userServiceImpl.editObjectSelective(user);
        if (success > 0) {
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }

    /**
     * 通用保存用户角色方法
     *
     * @param roles  用户多角色传递值
     * @param userId 用户ID
     * @return
     */
    public int commonSaveUserRole(String[] roles, String userId) {
        int success = 0;
        for (int i = 0; i < roles.length; i++) {
            //判断是否空值
            if (!StringUtils.isBlank(roles[i])) {
                //第一次将原数据清除再插入
                if (i == 0) {
                    userServiceImpl.deleteUserRole(userId);
                }
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roles[i]);
                success = success + userServiceImpl.insertUserRole(userRole);
            }
        }
        return success;
    }

    /**
     * 通用获取角色分组及其角色列表
     *
     * @param request
     * @return
     */
    public List<RoleGroup> getRoleList(HttpServletRequest request) {
        Map<String, Object> map = parameterMapToMap(request);
        List<RoleGroup> roleGroups = roleGroupServiceimpl.getObjects(map);
        //查询角色分组下的所有角色
        List<RoleGroup> newRoleGroups = new ArrayList();
        Role role = new Role();
        for (RoleGroup roleGroup : roleGroups) {
            role.setGroupRoleId(roleGroup.getGroupRoleId());
            roleGroup.setRoles(roleServiceimpl.getObjectsByEntity(role));
            newRoleGroups.add(roleGroup);
        }
        return newRoleGroups;
    }

}


