package com.toher.project.system.dict.controller;

import com.toher.common.dto.DataTableJson;
import com.toher.common.dto.ReturnJsonData;
import com.toher.framework.annotation.Log;
import com.toher.project.common.BaseController;
import com.toher.project.system.dict.entity.DictData;
import com.toher.project.system.dict.entity.DictType;
import com.toher.project.system.dict.service.DictDataService;
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
@RequestMapping("/system/dict/data")
public class DictDataController extends BaseController {

    private final String templatePath = "system/dict/data/";

    @Resource
    private DictDataService dictDataServiceImpl;
    @Resource
    private DictTypeService dictTypeServiceImpl;

    /**
     * 字典类型主页
     * @return
     */
    @RequiresPermissions("system:dict:index")
    @Log(title = "字典数据管理", action = "index")
    @RequestMapping("/index/{dictId}")
    public String index(HttpServletRequest request , @PathVariable("dictId") Integer dictId){
        Map<String, Object> map = parameterMapToMap(request);
        List<DictType> dictTypes = dictTypeServiceImpl.getObjects(map);
        request.setAttribute("dictId",dictId);
        request.setAttribute("dictTypes",dictTypes);
        DictData dictData = new DictData();
        dictData.setDictType("sys_common_status");
        List<DictData> objectsByEntity = dictDataServiceImpl.getObjectsByEntity(dictData);
        System.out.println(objectsByEntity);
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
        List<DictData> objects = dictDataServiceImpl.getObjects(map);
        DataTableJson<DictData> result = new DataTableJson<DictData>(objects);
        return result;
    }

    /**
     * 字典类型添加视图
     * @param request
     * @return
     */
    @RequiresPermissions("system:dict:add")
    @Log(title = "字典数据管理", action = "add")
    @RequestMapping("/add/{dictId}")
    public String addView(HttpServletRequest request,@PathVariable("dictId") Integer dictId) {
        DictType dictType = dictTypeServiceImpl.findObjectByPrimaryKey(dictId);
        request.setAttribute("dictType",dictType);
        return templatePath + "add";
    }

    /**
     * 字典类型修改视图
     * @param request
     * @return
     */
    @RequiresPermissions("system:dict:edit")
    @Log(title = "字典数据管理", action = "edit")
    @RequestMapping("/edit/{dataId}")
    public String editView(HttpServletRequest request, @PathVariable("dataId") Integer dataId) {
        DictData dictData = dictDataServiceImpl.findObjectByPrimaryKey(dataId);
        request.setAttribute("dictData",dictData);
        return templatePath + "edit";
    }

    /**
     * 字典类型更新及保存
     * @param request
     * @param dictData DictData对象
     * @return
     */
    @RequiresPermissions("system:dict:save")
    @Log(title = "字典数据管理", action = "save")
    @RequestMapping(value = "/save")
    @ResponseBody
    public ReturnJsonData save(HttpServletRequest request, DictData dictData) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        logger.info("保存数据：{}",dictData.getDictLabel());
        int success = 0;
        if (dictData != null) {
            //判断是否是添加
            if (dictData.getDataId()!=null) {
                //如果dict_id存在 则更新同时保存修改人 和修改时间
                dictData.setUpdateBy(getAuthUser().getRealName());
                dictData.setUpdateTime(new Date());
                success = dictDataServiceImpl.editObjectSelective(dictData);
            } else {
                //添加插入添加人
                dictData.setCreateBy(getAuthUser().getRealName());
                success = dictDataServiceImpl.saveObjectSelective(dictData);
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
     * @param dictData
     * @return
     */
    @RequiresPermissions("system:dict:update")
    @Log(title = "字典数据管理", action = "update")
    @RequestMapping("/updateProperty")
    @ResponseBody
    public ReturnJsonData updateProperty(DictData dictData) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = dictDataServiceImpl.editObjectSelective(dictData);
        if (success > 0) {
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }

    /**
     * 字典数据删除
     * @param dataId
     * @return
     */
    @RequiresPermissions("system:dict:delete")
    @Log(title = "字典数据管理", action = "delete")
    @RequestMapping(value = "/delete/{dataId}")
    @ResponseBody
    public ReturnJsonData delete(@PathVariable("dataId") Integer dataId){
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = 0;
        if(dataId!=null){
            success = dictDataServiceImpl.deleteObjectByPrimaryKey(dataId);
        }
        if (success > 0) {
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }

    /**
     * 字典数据批量删除
     * @param request
     * @return json
     */
    @RequiresPermissions("system:dict:delete")
    @RequestMapping("/deleteBatch")
    @ResponseBody
    public ReturnJsonData deleteBatch(HttpServletRequest request, Integer[] dataIds) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        int success = 0;
        if(ArrayUtils.isNotEmpty(dataIds)) {
            List<Integer> data = Arrays.asList(dataIds);
            success = dictDataServiceImpl.deleteBatchObject(data);
        }
        if (success > 0) {
            json.setCode("success");
            json.setMessage("操作成功");
        }
        return json;
    }

}
