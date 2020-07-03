package com.toher.project.module.job.service;

import com.github.pagehelper.PageHelper;
import com.toher.project.module.job.entity.QuartzTimer;
import com.toher.project.module.job.mapper.QuartzTimerMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/19 10:06
 */
@Service
@Transactional
public class QuartzTimerServiceImpl implements QuartzTimerService {
    @Resource
    private QuartzTimerMapper quartzTimerMapper;

    @Override
    public QuartzTimer findObjectByPrimaryKey(Integer o) {
        return quartzTimerMapper.selectByPrimaryKey(o);
    }

    @Override
    public QuartzTimer findObjectByEntity(QuartzTimer quartzTimer) {
        return quartzTimerMapper.selectOne(quartzTimer);
    }

    @Override
    public List<QuartzTimer> getObjects(Map<String, Object> map) {
        if (map.containsKey("length") && map.containsKey("start")) {
            PageHelper.offsetPage(Integer.parseInt(map.get("start").toString()), Integer.parseInt(map.get("length").toString()));
        }
        Example example = new Example(QuartzTimer.class);
        Example.Criteria criteria = example.createCriteria();
        String dictName = (String)map.get("dictName");
        String methodName = (String)map.get("methodName");
        Boolean status = (Boolean)map.get("status");
        //判读是否传递了任务名搜索
        if(StringUtils.isNotEmpty(dictName)){
            criteria.andEqualTo("dictName",dictName);
        }
        //判断是否传递了方法名搜索
        if(StringUtils.isNotEmpty(methodName)){
            criteria.andEqualTo("methodName",methodName);
        }
        //判断是否传递了状态搜索
        if(status!=null){
            criteria.andEqualTo("status",status);
        }
        example.setOrderByClause("job_id desc");
        return quartzTimerMapper.selectByExample(example);
    }

    @Override
    public List<QuartzTimer> getObjectsByEntity(QuartzTimer quartzTimer) {
        return quartzTimerMapper.select(quartzTimer);
    }

    @Override
    public int saveObject(QuartzTimer quartzTimer) {
        return quartzTimerMapper.insert(quartzTimer);
    }

    @Override
    public int editObject(QuartzTimer quartzTimer) {
        return quartzTimerMapper.updateByPrimaryKey(quartzTimer);
    }

    @Override
    public int saveObjectSelective(QuartzTimer quartzTimer) {
        return quartzTimerMapper.insertSelective(quartzTimer);
    }

    @Override
    public int editObjectSelective(QuartzTimer quartzTimer) {
        return quartzTimerMapper.updateByPrimaryKeySelective(quartzTimer);
    }

    @Override
    public int deleteObjectByPrimaryKey(Integer o) {
        return quartzTimerMapper.deleteByPrimaryKey(o);
    }

    @Override
    public int deleteBatchObject(List<Integer> data) {
        int success = 0;
        if(data!=null && data.size()>0) {
            Example example = new Example(QuartzTimer.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("jobId",data);
            success = quartzTimerMapper.deleteByExample(example);
        }
        return success;
    }
}

