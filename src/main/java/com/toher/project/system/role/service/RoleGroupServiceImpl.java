package com.toher.project.system.role.service;

import com.toher.project.system.role.entity.Role;
import com.toher.project.system.role.entity.RoleGroup;
import com.toher.project.system.role.mapper.RoleGroupMapper;
import com.toher.project.system.role.mapper.RoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/4 15:57
 */
@Service
@Transactional
//    @Transactional(propagation = Propagation.REQUIRED)
public class RoleGroupServiceImpl implements RoleGroupService {

    @Resource
    private RoleGroupMapper roleGroupMapper;
    @Resource
    private RoleMapper roleMapper;

    @Override
    public RoleGroup findObjectByPrimaryKey(Integer o) {
        return roleGroupMapper.selectByPrimaryKey(o);
    }

    @Override
    public RoleGroup findObjectByEntity(RoleGroup roleGroup) {
        return roleGroupMapper.selectOne(roleGroup);
    }

    @Override
    public List<RoleGroup> getObjects(Map<String, Object> map) {
        return roleGroupMapper.selectAll();
    }

    @Override
    public List<RoleGroup> getObjectsByEntity(RoleGroup roleGroup) {
        return roleGroupMapper.select(roleGroup);
    }

    @Override
    public int saveObject(RoleGroup roleGroup) {
        return roleGroupMapper.insert(roleGroup);
    }

    @Override
    public int editObject(RoleGroup roleGroup) {
        return roleGroupMapper.updateByPrimaryKey(roleGroup);
    }

    @Override
    public int saveObjectSelective(RoleGroup roleGroup) {
        return roleGroupMapper.insertSelective(roleGroup);
    }

    @Override
    public int editObjectSelective(RoleGroup roleGroup) {
        return roleGroupMapper.updateByPrimaryKeySelective(roleGroup);
    }

    @Override
    public int deleteObjectByPrimaryKey(Integer o) {
        //删除角色组需要清空角色
        Example example = new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();
        if(o!= null) {
            criteria.andEqualTo("groupRoleId", o);
        }
        roleMapper.deleteByExample(example);
        int success = roleGroupMapper.deleteByPrimaryKey(o);
        return success;
    }

    @Override
    public int deleteBatchObject(List<Integer> data) {
        int success = 0;
        if(data!=null && data.size()>0) {
            Example example = new Example(Role.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("groupRoleId",data);
            success = roleGroupMapper.deleteByExample(example);
        }
        return roleGroupMapper.deleteByExample(data);
    }
}
