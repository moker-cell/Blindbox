package com.toher.project.system.role.service;

import com.github.pagehelper.PageHelper;
import com.toher.project.system.role.entity.Role;
import com.toher.project.system.role.entity.RolePermission;
import com.toher.project.system.role.mapper.RoleMapper;
import com.toher.project.system.role.mapper.RolePermissionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/7 14:37
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService{

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public Role findObjectByPrimaryKey(String o) {
        return roleMapper.selectByPrimaryKey(o);
    }

    @Override
    public Role findObjectByEntity(Role role) {
        return roleMapper.selectOne(role);
    }

    @Override
    public Role getRoleAndPermission(String roleId) {
        return roleMapper.getRoleAndPermission(roleId);
    }

    @Override
    public List<Role> getObjects(Map<String, Object> map) {
        if (map.containsKey("length") && map.containsKey("start")) {
            PageHelper.orderBy("create_date desc");
            PageHelper.offsetPage(Integer.parseInt(map.get("start").toString()), Integer.parseInt(map.get("length").toString()));
        }
        return roleMapper.getObjects(map);
    }

    @Override
    public List<Role> getObjectsByEntity(Role role) {
        return roleMapper.select(role);
    }

    @Override
    public int saveObject(Role role) {
        return roleMapper.insert(role);
    }

    @Override
    public int editObject(Role role) {
        return roleMapper.updateByPrimaryKey(role);
    }

    @Override
    public int saveObjectSelective(Role role) {
        return roleMapper.insertSelective(role);
    }

    @Override
    public int editObjectSelective(Role role) {
        return roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public int deleteObjectByPrimaryKey(String o) {
        return roleMapper.deleteByPrimaryKey(o);
    }

    @Override
    public int deleteBatchObject(List<String> data) {
        int success = 0;
        if(data!=null && data.size()>0) {
            Example example = new Example(Role.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("roleId",data);
            success = roleMapper.deleteByExample(example);
        }
        return success;
    }

    @Override
    public int savePermission(List<RolePermission> rolePermissions) {
        int success = 0;
        for (int i=0; i<rolePermissions.size() ; i++) {
            RolePermission rolePermission = rolePermissions.get(i);
            //每次保存第一步首先清空原有权限
            if(i==0){
                //添加删除条件
                Example example = new Example(RolePermission.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("roleId",rolePermission.getRoleId());
                rolePermissionMapper.deleteByExample(example);
            }
            success = success + rolePermissionMapper.insertSelective(rolePermission);
        }
        return success;
    }
}
