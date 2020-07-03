package com.toher.project.module.operlog.service;

import com.github.pagehelper.PageHelper;
import com.toher.project.module.operlog.entity.OperLog;
import com.toher.project.module.operlog.mapper.OperLogMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/11 18:55
 */
@Service
public class OperLogServiceImpl implements OperLogService  {

    @Resource
    private OperLogMapper operLogMapper;

    @Override
    public OperLog findObjectByPrimaryKey(Integer o) {
        return operLogMapper.selectByPrimaryKey(o);
    }

    @Override
    public OperLog findObjectByEntity(OperLog operLog) {
        return operLogMapper.selectOne(operLog);
    }

    @Override
    public List<OperLog> getObjects(Map<String, Object> map) {
        Example example = new Example(OperLog.class);
        if(map!=null && map.size()>0){
            String startTime = (String)map.get("startTime");
            String endTime = (String)map.get("endTime");
            String method = (String)map.get("method");
            Example.Criteria criteria = example.createCriteria();
            //判断起始时间是否存在
            if(StringUtils.isNotBlank(startTime)){
                criteria.andGreaterThanOrEqualTo("operTime",startTime);
            }
            //判断结束时间是否存在
            if(StringUtils.isNotBlank(endTime)){
                criteria.andLessThanOrEqualTo("operTime",endTime + " 23:59:59");
            }
            //判断搜索类型是否存在
            if(StringUtils.isNotBlank(method)){
                criteria.andEqualTo("action",method);
            }
        }
        //判断分页
        if (map.containsKey("length") && map.containsKey("start")) {
            PageHelper.orderBy("id desc");
            PageHelper.offsetPage(Integer.parseInt(map.get("start").toString()), Integer.parseInt(map.get("length").toString()));
        }
        return operLogMapper.selectByExample(example);
    }

    @Override
    public List<OperLog> getObjectsByEntity(OperLog operLog) {
        return operLogMapper.select(operLog);
    }

    @Override
    public int saveObject(OperLog operLog) {
        return operLogMapper.insert(operLog);
    }

    @Override
    public int editObject(OperLog operLog) {
        return operLogMapper.updateByPrimaryKey(operLog);
    }

    @Override
    public int saveObjectSelective(OperLog operLog) {
        return operLogMapper.insertSelective(operLog);
    }

    @Override
    public int editObjectSelective(OperLog operLog) {
        return operLogMapper.updateByPrimaryKeySelective(operLog);
    }

    @Override
    public int deleteObjectByPrimaryKey(Integer o) {
        return operLogMapper.deleteByPrimaryKey(o);
    }

    @Override
    public int deleteBatchObject(List<Integer> data) {
        int success = 0;
        if(data!=null && data.size()>0) {
            Example example = new Example(OperLog.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("id",data);
            success = operLogMapper.deleteByExample(example);
        }
        return success;
    }
}
