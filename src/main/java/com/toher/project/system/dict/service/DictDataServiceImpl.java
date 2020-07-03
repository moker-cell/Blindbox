package com.toher.project.system.dict.service;

import com.github.pagehelper.PageHelper;
import com.toher.project.system.dict.entity.DictData;
import com.toher.project.system.dict.mapper.DictDataMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/20 16:18
 */
@Service
@Transactional
public class DictDataServiceImpl implements DictDataService {
    @Resource
    private DictDataMapper dictDataMapper;
    @Override
    public DictData findObjectByPrimaryKey(Integer o) {
        return dictDataMapper.selectByPrimaryKey(o);
    }

    @Override
    public DictData findObjectByEntity(DictData dictData) {
        return dictDataMapper.selectOne(dictData);
    }

    @Override
    public List<DictData> getObjects(Map<String, Object> map) {
        if (map.containsKey("length") && map.containsKey("start")) {
            PageHelper.offsetPage(Integer.parseInt(map.get("start").toString()), Integer.parseInt(map.get("length").toString()));
        }
        Example example = new Example(DictData.class);
        Example.Criteria criteria = example.createCriteria();
        String dictId = (String)map.get("dictId");
        String dictLabel = (String)map.get("dictLabel");
        String status = (String)map.get("status");
        if(StringUtils.isNotEmpty(dictId)){
            criteria.andEqualTo("dictId",Integer.parseInt(dictId));
        }
        if(StringUtils.isNotEmpty(dictLabel)){
            criteria.andEqualTo("dictLabel",dictLabel);
        }
        if(StringUtils.isNotEmpty(status)){
            criteria.andEqualTo("status",Boolean.parseBoolean(status));
        }
        example.setOrderByClause("data_id desc");
        return dictDataMapper.selectByExample(example);
    }

    @Override
    public List<DictData> getObjectsByEntity(DictData dictData) {
        return dictDataMapper.select(dictData);
    }

    @Override
    public int saveObject(DictData dictData) {
        return dictDataMapper.insert(dictData);
    }

    @Override
    public int editObject(DictData dictData) {
        return dictDataMapper.updateByPrimaryKey(dictData);
    }

    @Override
    public int saveObjectSelective(DictData dictData) {
        return dictDataMapper.insertSelective(dictData);
    }

    @Override
    public int editObjectSelective(DictData dictData) {
        return dictDataMapper.updateByPrimaryKeySelective(dictData);
    }

    @Override
    public int deleteObjectByPrimaryKey(Integer o) {
        return dictDataMapper.deleteByPrimaryKey(o);
    }

    @Override
    public int deleteBatchObject(List<Integer> data) {
        int success = 0;
        if(data!=null && data.size()>0) {
            Example example = new Example(DictData.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("dataId",data);
            success = dictDataMapper.deleteByExample(example);
        }
        return success;
    }
}
