package com.toher.project.system.menu.mapper;

import com.toher.framework.mapper.MyMapper;
import com.toher.project.system.menu.entity.Menu;

import java.util.List;
import java.util.Map;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/10 10:23
 */
public interface MenuMapper extends MyMapper<Menu> {

    List<Menu> getObjects(Map<String, Object> param);

    List<Menu> getObjectByParentId(String parentId);
}
