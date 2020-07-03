package com.toher.project.system.dict.controller;

import com.toher.common.dto.DataTableJson;
import com.toher.common.dto.ReturnJsonData;
import com.toher.framework.annotation.Log;
import com.toher.project.common.BaseController;
import com.toher.project.system.dict.entity.DictType;
import com.toher.project.system.dict.service.DictTypeService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.toher.common.utils.CommonUtils.parameterMapToMap;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/20 16:38
 */
@Controller
@RequestMapping("/system/dict/type")
public class DictTypeController extends BaseController {

    private final String templatePath = "system/dict/type/";

    @Resource
    private DictTypeService dictTypeServiceImpl;

    /**
     * 字典类型主页
     * @return
     */
    @RequiresPermissions("system:dict:index")
    @Log(title = "字典类型管理", action = "index")
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
        List<DictType> objects = dictTypeServiceImpl.getObjects(map);
        DataTableJson<DictType> result = new DataTableJson<DictType>(objects);
        return result;
    }

    /**
     * 字典类型添加视图
     *
     * @param request
     * @return
     */
    @RequiresPermissions("system:dict:add")
    @Log(title = "字典类型管理", action = "add")
    @RequestMapping("/add")
    public String addView(HttpServletRequest request) {
        return templatePath + "add";
    }

    /**
     * 字典类型修改视图
     *
     * @param request
     * @return
     */
    @RequiresPermissions("system:dict:edit")
    @Log(title = "字典类型管理", action = "edit")
    @RequestMapping("/edit/{dictId}")
    public String editView(HttpServletRequest request, @PathVariable("dictId") Integer dictId) {
        DictType dictType = dictTypeServiceImpl.findObjectByPrimaryKey(dictId);
        request.setAttribute("dictType",dictType);
        return templatePath + "edit";
    }

    /**
     * 字典类型更新及保存
     * @param request
     * @param dictType DictType对象
     * @return
     */
    @RequiresPermissions("system:dict:save")
    @Log(title = "字典类型管理", action = "save")
    @RequestMapping(value = "/save")
    @ResponseBody
    public ReturnJsonData save(HttpServletRequest request, DictType dictType) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = 0;
        if (dictType != null) {
            //判断是否是添加
            if (dictType.getDictId()!=null) {
                //如果dict_id存在 则更新
                dictType.setUpdateBy(getAuthUser().getRealName());
                dictType.setUpdateTime(new Date());
                success = dictTypeServiceImpl.editObjectSelective(dictType);
            } else {
                dictType.setCreateBy(getAuthUser().getRealName());
                success = dictTypeServiceImpl.saveObjectSelective(dictType);
            }
        }
        if (success > 0) {
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }

    /**
     * 更新属性 通用方法
     * @param dictType
     * @return
     */
    @RequiresPermissions("system:dict:update")
    @Log(title = "用户管理", action = "update")
    @RequestMapping("/updateProperty")
    @ResponseBody
    public ReturnJsonData updateProperty(DictType dictType) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = dictTypeServiceImpl.editObjectSelective(dictType);
        if (success > 0) {
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }

    /**
     * 字典类型删除
     * @param dictId
     * @return
     */
    @RequiresPermissions("system:dict:delete")
    @Log(title = "字典类型管理", action = "delete")
    @RequestMapping(value = "/delete/{dictId}")
    @ResponseBody
    public ReturnJsonData delete(@PathVariable("dictId") Integer dictId){
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = 0;
        if(dictId!=null){
            success = dictTypeServiceImpl.deleteObjectByPrimaryKey(dictId);
        }
        if (success > 0) {
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }

    /**
     * 字典类型批量删除
     * @param request
     * @return json
     */
    @RequiresPermissions("system:dict:delete")
    @RequestMapping("/deleteBatch")
    @ResponseBody
    public ReturnJsonData deleteBatch(HttpServletRequest request, Integer[] dictIds) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = 0;
        if(ArrayUtils.isNotEmpty(dictIds)) {
            List<Integer> data = Arrays.asList(dictIds);
            success = dictTypeServiceImpl.deleteBatchObject(data);
        }
        if (success > 0) {
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }

}
