package com.toher.project.system.dict.service;

import com.github.pagehelper.PageHelper;
import com.toher.project.system.dict.entity.DictType;
import com.toher.project.system.dict.mapper.DictTypeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/20 16:11
 */
@Service
@Transactional
public class DictTypeServiceImpl implements DictTypeService {
    @Resource
    private DictTypeMapper dictTypeMapper;
    @Override
    public DictType findObjectByPrimaryKey(Integer o) {
        return dictTypeMapper.selectByPrimaryKey(o);
    }

    @Override
    public DictType findObjectByEntity(DictType dictType) {
        return dictTypeMapper.selectOne(dictType);
    }

    @Override
    public List<DictType> getObjects(Map<String, Object> map) {
        if (map.containsKey("length") && map.containsKey("start")) {
            PageHelper.offsetPage(Integer.parseInt(map.get("start").toString()), Integer.parseInt(map.get("length").toString()));
        }
        Example example = new Example(DictType.class);
        Example.Criteria criteria = example.createCriteria();
        String startTime = (String)map.get("startTime");
        String endTime = (String)map.get("endTime");
        String dictName = (String)map.get("dictName");
        String dictType = (String)map.get("dictType");
        String status = (String)map.get("status");
        //判读是否传递了起始时间搜索
        if(StringUtils.isNotEmpty(startTime)){
            criteria.andGreaterThanOrEqualTo("createTime",startTime);
        }
        //判断结束时间是否存在
        if(StringUtils.isNotBlank(endTime)){
            criteria.andLessThanOrEqualTo("createTime",endTime + " 23:59:59");
        }
        //判断是否传递了字典名搜索
        if(StringUtils.isNotEmpty(dictName)){
            criteria.andEqualTo("dictName",dictName);
        }
        //判断是否传递了字典名搜索
        if(StringUtils.isNotEmpty(dictType)){
            criteria.andEqualTo("dictType",dictType);
        }
        //判断是否传递了状态搜索
        if(StringUtils.isNotEmpty(status)){
            criteria.andEqualTo("status",Boolean.parseBoolean(status));
        }
        example.setOrderByClause("dict_id desc");
        return dictTypeMapper.selectByExample(example);
    }

    @Override
    public List<DictType> getObjectsByEntity(DictType dictType) {
        return dictTypeMapper.select(dictType);
    }

    @Override
    public int saveObject(DictType dictType) {
        return dictTypeMapper.insert(dictType);
    }

    @Override
    public int editObject(DictType dictType) {
        return dictTypeMapper.updateByPrimaryKey(dictType);
    }

    @Override
    public int saveObjectSelective(DictType dictType) {
        return dictTypeMapper.insertSelective(dictType);
    }

    @Override
    public int editObjectSelective(DictType dictType) {
        return dictTypeMapper.updateByPrimaryKeySelective(dictType);
    }

    @Override
    public int deleteObjectByPrimaryKey(Integer o) {
        return dictTypeMapper.deleteByPrimaryKey(o);
    }

    @Override
    public int deleteBatchObject(List<Integer> data) {
        int success = 0;
        if(data!=null && data.size()>0) {
            Example example = new Example(DictType.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("dictId",data);
            success = dictTypeMapper.deleteByExample(example);
        }
        return success;
    }


}
