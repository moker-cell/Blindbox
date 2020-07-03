package com.toher.project.module.job.controller;

import com.toher.common.dto.DataTableJson;
import com.toher.common.dto.ReturnJsonData;
import com.toher.common.utils.quartz.QuartzManager;
import com.toher.project.module.job.entity.QuartzTimer;
import com.toher.project.module.job.service.QuartzTimerService;
import com.toher.project.module.job.utils.ScheduleJob;
import com.toher.project.system.user.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.toher.common.utils.CommonUtils.parameterMapToMap;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/19 10:22
 */
@Controller
@RequestMapping("/system/job")
public class QuartzTimerController {

    private static final Logger logger = LoggerFactory.getLogger(QuartzTimerController.class);
    private final String templatePath = "module/job/";

    @Value("${toher.system.sessionUser}")
    private String SESSION_USER;

    @Resource
    private QuartzTimerService quartzTimerServiceImpl;

    /**
     * 项目启动 执行开启的定时器
     * 服务器加载Servlet的时候运行，并且只会被服务器调用一次，类似于Serclet的inti()方法
     */
    @PostConstruct
    public void init(){
        //查询所有已开启的任务
        QuartzTimer qt = new QuartzTimer();
        qt.setStatus(true);
        List<QuartzTimer> quartzTimers = quartzTimerServiceImpl.getObjectsByEntity(qt);
        for(QuartzTimer quartzTimer : quartzTimers){
            try {
                QuartzManager.addJob(quartzTimer.getJobName(), quartzTimer.getJobGroup(),ScheduleJob.class, quartzTimer.getCronExpression(),quartzTimer);
            }catch (Exception e){
                //启动失败则关闭任务
                quartzTimer.setStatus(false);
                quartzTimerServiceImpl.editObjectSelective(quartzTimer);
                //并删除该任务
                QuartzManager.removeJob(quartzTimer.getJobName());
            }
        }
    }

    /**
     * 注销调用： @PreDestroy 类似于Servlet的destroy()方法
     */
    @PreDestroy
    public void destroy(){
        //查询所有已开启的任务
        QuartzTimer qt = new QuartzTimer();
        qt.setStatus(true);
        List<QuartzTimer> quartzTimers = quartzTimerServiceImpl.getObjectsByEntity(qt);
        for(QuartzTimer quartzTimer : quartzTimers){
            try {
                QuartzManager.removeJob(quartzTimer.getJobName(), quartzTimer.getJobGroup());
            }catch (Exception e){
                logger.error("destroy：关闭任务失败", e);
            }
        }
    }

    /**
     * 任务器主页
     * @return
     */
    @RequiresPermissions("system:job:index")
    @RequestMapping("/index")
    public String index(){
        return templatePath + "index";
    }

    /**
     * 获取dataTable JSON数据
     * @param request
     * @return
     */
    @RequestMapping("/dataJson")
    @ResponseBody
    public DataTableJson dataJson(HttpServletRequest request){
        Map<String, Object> map = parameterMapToMap(request);
        List<QuartzTimer> objects = quartzTimerServiceImpl.getObjects(map);
        DataTableJson<QuartzTimer> result = new DataTableJson<QuartzTimer>(objects);
        return result;
    }

    /**
     * 任务添加视图
     * @param request
     * @return
     */
    @RequiresPermissions("system:job:add")
    @RequestMapping("/add")
    public String addView(HttpServletRequest request){
        return templatePath + "add_edit";
    }

    /**
     * 任务添加视图
     * @param request
     * @return
     */
    @RequiresPermissions("system:job:edit")
    @RequestMapping("/edit/{jobId}")
    public String editView(HttpServletRequest request, @PathVariable("jobId") Integer jobId){
        QuartzTimer quartzTimer = quartzTimerServiceImpl.findObjectByPrimaryKey(jobId);
        request.setAttribute("timer",quartzTimer);
        return templatePath + "add_edit";
    }

    @RequiresPermissions("system:job:save")
    @RequestMapping("/save")
    @ResponseBody
    public ReturnJsonData save(HttpServletRequest request, QuartzTimer quartzTimer){
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = 0;
        if (quartzTimer != null) {
            //存在主键则更新 否则插入
            if(quartzTimer.getJobId()!=null){
                //获取Session
                Subject subject = SecurityUtils.getSubject();
                Session session = subject.getSession();
                User user = (User) session.getAttribute(SESSION_USER);
                quartzTimer.setUpdateBy(user.getRealName());
                quartzTimer.setUpdateTime(new Date());
                success = quartzTimerServiceImpl.editObject(quartzTimer);
            }else{
                success = quartzTimerServiceImpl.saveObjectSelective(quartzTimer);
            }
        }
        if(success>0){
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }

    /**
     * 删除任务计划
     * @param request
     * @return
     */
    @RequiresPermissions("system:job:delete")
    @RequestMapping("/delete/{jobId}")
    @ResponseBody
    public ReturnJsonData delete(HttpServletRequest request, @PathVariable("jobId") Integer jobId){
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = 0;
            if (jobId!=null) {
                //删除前需要将当前任务清除
                QuartzTimer quartzTimer = quartzTimerServiceImpl.findObjectByPrimaryKey(jobId);
                try {
                    QuartzManager.removeJob(quartzTimer.getJobName());
                }catch (Exception e){
                    json.setMessage("清除任务失败");
                    return json;
                }
                //最后删除当前选中
                success = quartzTimerServiceImpl.deleteObjectByPrimaryKey(jobId);
            }

        if (success > 0) {
            json.setCode("success");
            json.setMessage("成功删除 " + success + " 条记录");
        }
        return json;
    }

    /**
     * 切换状态
     *
     * @param request
     * @param qz 任务对象
     * @throws Exception
     */
    @RequiresPermissions("system:job:update")
    @RequestMapping(value = "/updateProperty")
    @ResponseBody
    public ReturnJsonData changeStatus(HttpServletRequest request, QuartzTimer qz) throws Exception {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");

        String status = request.getParameter("status");
        String timer_id = request.getParameter("timer_id");
        //获取Session
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user = (User) session.getAttribute(SESSION_USER);

        //根据ID读取对应任务对象
        QuartzTimer quartzTimer = quartzTimerServiceImpl.findObjectByPrimaryKey(qz.getJobId());
        //查询不到任务信息则 返回
        if(quartzTimer==null){
            json.setMessage("查询不到任务信息");
            return json;
        }
        if (qz.getStatus()==false) {
            quartzTimer.setStatus(false);
            //删除任务
            QuartzManager.removeJob(quartzTimer.getJobName(),quartzTimer.getJobGroup());
        } else {
            quartzTimer.setStatus(true);
            //添加任务 并传递参数
            //补抓异常 启动任务失败 不执行更新操作
            try {
                QuartzManager.addJob(quartzTimer.getJobName(), quartzTimer.getJobGroup(),ScheduleJob.class, quartzTimer.getCronExpression(),quartzTimer);
            }catch (Exception e){
                json.setMessage("任务置行失败");
                return json;
            }
        }
        int success = quartzTimerServiceImpl.editObjectSelective(quartzTimer);
        if(success > 0 ){
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }
}
