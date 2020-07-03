package com.toher.project.system.user.service;

import com.github.pagehelper.PageHelper;
import com.toher.common.utils.shiro.ShiroUtils;
import com.toher.project.system.user.entity.User;
import com.toher.project.system.user.entity.UserRole;
import com.toher.project.system.user.mapper.UserMapper;
import com.toher.project.system.user.mapper.UserRoleMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.UnknownAccountException;
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
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public User findObjectByPrimaryKey(String o) {
        return userMapper.selectUserAndRolesInXML(o);
    }

    @Override
    public User findObjectByEntity(User user) {
        return userMapper.selectOne(user);
    }

    @Override
    public List<User> getObjects(Map<String, Object> map) {
        if (map.containsKey("length") && map.containsKey("start")) {
            PageHelper.offsetPage(Integer.parseInt(map.get("start").toString()), Integer.parseInt(map.get("length").toString()));
        }
        return userMapper.getObjectsInXML(map);
    }

    @Override
    public List<User> getObjectsByEntity(User user) {
        return userMapper.select(user);
    }

    @Override
    public int saveObject(User user) {
        return userMapper.insert(user);
    }

    @Override
    public int editObject(User user) {
        return userMapper.updateByPrimaryKey(user);
    }

    @Override
    public int saveObjectSelective(User user) { return userMapper.insertSelective(user); }

    @Override
    public int insertUserRole(UserRole userRole) {
        return userRoleMapper.insertSelective(userRole);
    }

    @Override
    public int deleteUserRole(String userId) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        return userRoleMapper.deleteByExample(example);
    }

    @Override
    public int editObjectSelective(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Transactional
    @Override
    public int deleteObjectByPrimaryKey(String o) {
//        RechargeOrder rechargeOrder = new RechargeOrder();
//        rechargeOrder.setOrderid(41368);
//        rechargeOrder.setMobile("18723971000");
//        rechargeOrderServiceImpl.editObjectSelective(rechargeOrder);
//        int i = 1/0;
        return userMapper.deleteByPrimaryKey(o);
    }

    @Override
    public User login(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new UnknownAccountException("未知账户验证未通过");
        }
        Example e = new Example(User.class);
        Example.Criteria c = e.createCriteria();
        c.orEqualTo("username", username).orEqualTo("phone", username);
        Example.Criteria criteria = e.createCriteria();
        criteria.andEqualTo("password", password);
        e.and(criteria);
        return userMapper.selectOneByExample(e);
    }

    @Override
    public int deleteBatchObject(List<String> data) {
        int success = 0;
        if(data!=null && data.size()>0) {
            Example example = new Example(User.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("userId",data);
            success = userMapper.deleteByExample(example);
        }
        return success;
    }

    @Override
    public int selectCountByOnly(String username, String phone) {
        Example e = new Example(User.class);
        Example.Criteria c = e.createCriteria();
        c.orEqualTo("username", username).orEqualTo("phone", phone);
        return userMapper.selectCountByExample(e);
    }

    @Override
    public User selectOneByOnly(String username, String phone) {
        Example e = new Example(User.class);
        Example.Criteria c = e.createCriteria();
        c.orEqualTo("username", username).orEqualTo("phone", phone);
        return userMapper.selectOneByExample(e);
    }
}
