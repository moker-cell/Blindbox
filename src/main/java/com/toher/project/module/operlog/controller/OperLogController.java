package com.toher.project.module.operlog.controller;

import com.toher.common.dto.DataTableJson;
import com.toher.common.dto.ReturnJsonData;
import com.toher.project.module.operlog.entity.OperLog;
import com.toher.project.module.operlog.service.OperLogService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.toher.common.utils.CommonUtils.parameterMapToMap;
import static com.toher.common.utils.GsonUtil.GsonStringDate;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/14 16:00
 */
@Controller
@RequestMapping("/system/operlog")
public class OperLogController {

    private final String templatePath = "module/operlog/";
    @Resource
    private OperLogService operLogServiceImpl;

    /**
     * 进入日志列表页
     * @return
     */
    @RequiresPermissions("system:operlog:index")
    @RequestMapping("/index")
    public String index(){
        return templatePath + "index";
    }

    /**
     * OperLog列表JSON数据
     * @param request
     * @return
     */
    @RequestMapping("/dataJson")
    @ResponseBody
    public String dataJson(HttpServletRequest request) {
        Map<String, Object> map = parameterMapToMap(request);
        List<OperLog> objects = operLogServiceImpl.getObjects(map);
        DataTableJson<OperLog> result = new DataTableJson<OperLog>(objects);
        return GsonStringDate(result, "yyyy-MM-dd HH:mm");
    }

    /**
     * 删除单条日志
     * @return
     */
    @RequiresPermissions("system:operlog:delete")
    @RequestMapping("/delete/{id}")
    @ResponseBody
    public ReturnJsonData delete(@PathVariable("id") Integer id){
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success =  operLogServiceImpl.deleteObjectByPrimaryKey(id);
        if(success>0){
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }

    /**
     * 日志批量删除
     * @param request
     * @return json
     */
    @RequiresPermissions("system:operlog:delete")
    @RequestMapping("/deleteBatch")
    @ResponseBody
    public ReturnJsonData deleteBatch(HttpServletRequest request, Integer[] ids) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = 0;
        if(ArrayUtils.isNotEmpty(ids)) {
            List<Integer> data = Arrays.asList(ids);
            success = operLogServiceImpl.deleteBatchObject(data);
        }
        if (success > 0) {
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }

}
