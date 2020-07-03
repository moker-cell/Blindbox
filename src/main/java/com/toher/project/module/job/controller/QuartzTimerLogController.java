package com.toher.project.module.job.controller;

import com.toher.common.dto.DataTableJson;
import com.toher.common.dto.ReturnJsonData;
import com.toher.project.module.job.entity.QuartzTimerLog;
import com.toher.project.module.job.service.QuartzTimerLogService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.toher.common.utils.CommonUtils.parameterMapToMap;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2019/1/3 19:31
 */
@Controller
@RequestMapping("/system/job/log")
public class QuartzTimerLogController {

    private final String templatePath = "module/job/log/";
    @Resource
    private QuartzTimerLogService quartzTimerLogServiceImpl;
    /**
     * 任务器主页
     * @return
     */
    @RequiresPermissions("system:joblog:index")
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
        List<QuartzTimerLog> objects = quartzTimerLogServiceImpl.getObjects(map);
        DataTableJson<QuartzTimerLog> result = new DataTableJson<QuartzTimerLog>(objects);
        return result;
    }



    /**
     * 批量删除所选
     * @param request
     * @return json
     */
    @RequestMapping("/deleteBatch")
    @ResponseBody
    public ReturnJsonData deleteBatch(HttpServletRequest request, Integer[] logIds) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = 0;
        if(ArrayUtils.isNotEmpty(logIds)) {
            List<Integer> data = Arrays.asList(logIds);
            success = quartzTimerLogServiceImpl.deleteBatchObject(data);
        }
        if (success > 0) {
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }

    /**
     * 按条件批量删除
     * @param request
     * @return
     */
    @RequestMapping("/deleteBatchParam")
    @ResponseBody
    public ReturnJsonData deleteBatchParam(HttpServletRequest request) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = 0;
        Map<String, Object> map = parameterMapToMap(request);
        success = quartzTimerLogServiceImpl.deleteBatchParamObject(map);
        if (success > 0) {
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }
}
