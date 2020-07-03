package com.toher.project.module.job.service;

import com.toher.common.service.BaseService;
import com.toher.project.module.job.entity.QuartzTimerLog;

import java.util.Map;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/19 10:04
 */
public interface QuartzTimerLogService extends BaseService<QuartzTimerLog,Integer> {
    int deleteBatchParamObject(Map<String, Object> map);
}
