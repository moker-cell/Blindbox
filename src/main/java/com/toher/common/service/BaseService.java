package com.toher.common.service;

import java.util.List;
import java.util.Map;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/4 15:54
 */
public interface BaseService<T,V> {

    T findObjectByPrimaryKey(V o);
    T findObjectByEntity(T t);
    List<T> getObjects(Map<String,Object> map);
    List<T> getObjectsByEntity(T t);
    int saveObject(T t);
    int editObject(T t);
    int saveObjectSelective(T t);
    int editObjectSelective(T t);
    int deleteObjectByPrimaryKey(V o);
    int deleteBatchObject(List<V> data);

}
