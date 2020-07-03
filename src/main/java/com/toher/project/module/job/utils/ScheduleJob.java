package com.toher.project.module.job.utils;


import com.toher.common.constants.ConstantsUtil;
import com.toher.common.dto.ReturnJsonData;
import com.toher.common.utils.BeanUtils;
import com.toher.common.utils.spring.SpringContextHolder;
import com.toher.project.module.job.entity.QuartzTimer;
import com.toher.project.module.job.entity.QuartzTimerLog;
import com.toher.project.module.job.service.QuartzTimerLogService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 定时任务
 *
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/20 11:06
 */
public class ScheduleJob extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(ScheduleJob.class);

    private ExecutorService service = Executors.newSingleThreadExecutor();

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        QuartzTimer job = new QuartzTimer();
        //将传递过来的QuartzTimer拷贝
        BeanUtils.copyBeanProp(job, context.getMergedJobDataMap().get(ConstantsUtil.JOB_PARAM));
        //获取任务日志Service
        QuartzTimerLogService QuartzTimerLogServiceImpl = (QuartzTimerLogService) SpringContextHolder.getBean(QuartzTimerLogService.class);
        QuartzTimerLog jobLog = new QuartzTimerLog();
        jobLog.setJobName(job.getJobName());
        jobLog.setJobGroup(job.getJobGroup());
        jobLog.setMethodName(job.getMethodName());
        jobLog.setMethodParams(job.getMethodParams());
        jobLog.setCreateTime(new Date());
        long startTime = System.currentTimeMillis();
        String statusCode = "";
        try {
            // 执行任务
            log.info("任务开始执行 - 名称：{} 方法：{}", job.getJobName(), job.getMethodName());
            ScheduleCallable task = new ScheduleCallable(job.getJobName(), job.getMethodName(), job.getMethodParams());
            Future<?> future = service.submit(task);
            //任务状态 true：成功 false：失败
            jobLog.setStatus(true);
            //设置30秒的超时 30秒还没获取到回调则抛出TimeoutException
            Object o = future.get(30, TimeUnit.SECONDS);
            //根据回调 判断返回类型，读取存储的MAP值
            if (o != null && o instanceof ReturnJsonData) {
                ReturnJsonData jsonDate = (ReturnJsonData) o;
                Map<String, Object> map = jsonDate.getParams();
                //获取返回状态码
                statusCode = jsonDate.getCode();
                if(map!=null) {
                    //测试读取
                    if (map.containsKey("account")) {
                        System.out.println("读取回调数据account："+map.get("account"));
                    }
                }
                //凡是充值不返回SUCCESS都认定为失败
                if (!statusCode.equals("SUCCESS")) {
                    jobLog.setStatus(false);
                }
            }

            long times = System.currentTimeMillis() - startTime;
            jobLog.setJobMessage(times + "毫秒");
            log.info("任务执行结束 - 名称：{} 耗时：{} 毫秒", job.getJobName(), times);
        } catch (TimeoutException ex) {
            log.info("任务执行超时 - 名称：{} 方法：{}", job.getJobName(), job.getMethodName());
            long times = System.currentTimeMillis() - startTime;
            jobLog.setStatus(false);
            jobLog.setExceptionInfo(ex.toString());
        } catch (Exception e) {
            log.info("任务执行失败 - 名称：{} 方法：{}", job.getJobName(), job.getMethodName());
            log.error("任务执行异常  - ：", e);
            long times = System.currentTimeMillis() - startTime;
            jobLog.setJobMessage(times + "毫秒");
            // 任务状态 0：成功 1：失败
            jobLog.setStatus(false);
            jobLog.setExceptionInfo(e.toString());
        } finally {
            //过滤查无订单的信息回调，避免数据一直插入日志
            if (!statusCode.equals("FIND_ORDER_ERROR") && !statusCode.equals("FIND_CHANNEL_ERROR")) {
                QuartzTimerLogServiceImpl.saveObjectSelective(jobLog);
            }
        }
    }
}
