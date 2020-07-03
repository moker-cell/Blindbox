package com.toher.project.module.job.service;

import com.github.pagehelper.PageHelper;
import com.toher.project.module.job.entity.QuartzTimerLog;
import com.toher.project.module.job.mapper.QuartzTimerLogMapper;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/20 11:06
 */
@Service
@Transactional
public class QuartzTimerLogServiceImpl implements QuartzTimerLogService {
    @Resource
    private QuartzTimerLogMapper quartzTimerLogMapper;
    @Override
    public QuartzTimerLog findObjectByPrimaryKey(Integer o) {
        return quartzTimerLogMapper.selectByPrimaryKey(o);
    }

    @Override
    public QuartzTimerLog findObjectByEntity(QuartzTimerLog quartzTimerLog) {
        return quartzTimerLogMapper.selectOne(quartzTimerLog);
    }

    @Override
    public List<QuartzTimerLog> getObjects(Map<String, Object> map) {
        if (map.containsKey("length") && map.containsKey("start")) {
            PageHelper.offsetPage(Integer.parseInt(map.get("start").toString()), Integer.parseInt(map.get("length").toString()));
        }
        Example example = queryExample(map);
        return quartzTimerLogMapper.selectByExample(example);
    }

    @Override
    public List<QuartzTimerLog> getObjectsByEntity(QuartzTimerLog quartzTimerLog) {
        return quartzTimerLogMapper.select(quartzTimerLog);
    }

    @Override
    public int saveObject(QuartzTimerLog quartzTimerLog) {
        return quartzTimerLogMapper.insert(quartzTimerLog);
    }

    @Override
    public int editObject(QuartzTimerLog quartzTimerLog) {
        return quartzTimerLogMapper.updateByPrimaryKey(quartzTimerLog);
    }

    @Override
    public int saveObjectSelective(QuartzTimerLog quartzTimerLog) {
        return quartzTimerLogMapper.insertSelective(quartzTimerLog);
    }

    @Override
    public int editObjectSelective(QuartzTimerLog quartzTimerLog) {
        return quartzTimerLogMapper.updateByPrimaryKeySelective(quartzTimerLog);
    }

    @Override
    public int deleteObjectByPrimaryKey(Integer o) {
        return quartzTimerLogMapper.deleteByPrimaryKey(o);
    }

    @Override
    public int deleteBatchObject(List<Integer> data) {
        int success = 0;
        if(data!=null && data.size()>0) {
            Example example = new Example(QuartzTimerLog.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("logId",data);
            success = quartzTimerLogMapper.deleteByExample(example);
        }
        return success;
    }

    @Override
    public int deleteBatchParamObject(Map<String, Object> map) {
        Example example = queryExample(map);
        int success = quartzTimerLogMapper.deleteByExample(example);
        return success;
    }

    public Example queryExample(Map<String, Object> map){
        Example example = new Example(QuartzTimerLog.class);
        if(MapUtils.isNotEmpty(map)){
            Example.Criteria criteria = example.createCriteria();
            //判读是否传递了起始时间搜索
            String startTime = (String)map.get("startTime");
            if(StringUtils.isNotEmpty(startTime)){
                criteria.andGreaterThanOrEqualTo("createTime",startTime);
            }
            //判断结束时间是否存在
            String endTime = (String)map.get("endTime");
            if(StringUtils.isNotBlank(endTime)){
                criteria.andLessThanOrEqualTo("createTime",endTime + " 23:59:59");
            }
            //判断是否传递了方法名称
            String methodName = (String)map.get("methodName");
            if(StringUtils.isNotBlank(methodName)){
                criteria.andEqualTo("methodName",methodName);
            }
            //判断是否传递了方法参数
            String methodParams = (String)map.get("methodParams");
            if(StringUtils.isNotBlank(methodParams)){
                criteria.andEqualTo("methodParams",methodParams);
            }
            //判断是否传递了remark2查询
            String remark2 = (String)map.get("remark2");
            if(StringUtils.isNotBlank(remark2)){
                criteria.andLike("remark2",remark2);
            }
            //判断是否传递了状态
            String status = (String)map.get("status");
            if(StringUtils.isNotBlank(status)){
                criteria.andEqualTo("status",Boolean.parseBoolean(status));
            }
        }
        example.setOrderByClause("log_id desc");
        return example;
    }
}
