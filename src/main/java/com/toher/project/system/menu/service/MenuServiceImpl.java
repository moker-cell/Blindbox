package com.toher.project.system.menu.service;

import com.toher.project.system.menu.entity.Menu;
import com.toher.project.system.menu.mapper.MenuMapper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/10 10:25
 */
@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    private MenuMapper MenuMapper;

    @Override
    public Menu findObjectByPrimaryKey(String o) {
        return MenuMapper.selectByPrimaryKey(o);
    }

    @Override
    public Menu findObjectByEntity(Menu menu) {
        return MenuMapper.selectOne(menu);
    }

    @Override
    public List<Menu> getObjects(Map<String, Object> map) {
        if(map!=null) {
            return MenuMapper.getObjects(map);
        }else{
            return MenuMapper.selectAll();
        }
    }

    @Override
    public List<Menu> getObjectsByEntity(Menu menu) {
        return MenuMapper.select(menu);
    }

    @Override
    public int saveObject(Menu menu) {
        return MenuMapper.insert(menu);
    }

    @Override
    public int editObject(Menu menu) {
        return MenuMapper.updateByPrimaryKey(menu);
    }

    @Override
    public int saveObjectSelective(Menu menu) {
        return MenuMapper.insertSelective(menu);
    }

    @Override
    public int editObjectSelective(Menu menu) {
        return MenuMapper.updateByPrimaryKeySelective(menu);
    }

    @Override
    public int deleteObjectByPrimaryKey(String o) {
        return MenuMapper.deleteByPrimaryKey(o);
    }

    @Override
    public int deleteBatchObject(List<String> data) {
        int success = 0;
        if(data!=null && data.size()>0) {
            Example example = new Example(Menu.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("menuId",data);
            success = MenuMapper.deleteByExample(example);
        }
        return 0;
    }
}
