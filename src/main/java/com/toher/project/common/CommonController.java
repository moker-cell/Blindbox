package com.toher.project.common;

import com.toher.common.dto.ReturnJsonData;
import com.toher.common.utils.file.FileuploadUtil;
import com.toher.framework.configurer.ReadApplicationYmlUtil;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.toher.common.utils.GsonUtil.GsonString;


/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/4 11:57
 */
@Controller
@RequestMapping("/common")
public class CommonController {

    /** 注入自定义配置 */
    @Resource
    private ReadApplicationYmlUtil readApplicationYmlUtil;

    /**
     * 通用图标选择
     * @return
     */
    @RequestMapping("/icon")
    public String icon(){
        return "system/common/icon";
    }

    /**
     * 统一错误AJAX处理 - 作用于异常处理 当请求是AJAX时候 由页面转化JSON数据输出
     */
    @RequestMapping("/error.json")
    @ResponseBody
    public ReturnJsonData errorJson(HttpServletRequest request){
        String code = request.getAttribute("code").toString();
        String message = request.getAttribute("title").toString();
        ReturnJsonData returnJsonData = new ReturnJsonData();
        returnJsonData.setCode(code);
        returnJsonData.setMessage(message);
        return returnJsonData;
    }

    /**
     * 通用上传文件
     * @param file 上传文件
     * @param folderName 文件夹
     * @param resultType 返回数据类型（默认：ReturnJsonData；layedit：layui富文本格式）
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file, String folderName, String resultType) {
        String resultStr = null;

        // 上传文件硬盘根路径
        String diskRootPath = readApplicationYmlUtil.getUploadPath();
        // 上传文件访问根路径
        String urlRootPath = "/upload/";
        if (StringUtils.isNotBlank(folderName)) {
            diskRootPath += folderName;
            urlRootPath += folderName + "/";
        }
        // 根据类型调用
        if (StringUtils.isBlank(resultType)) {
            // 默认
            resultStr = uploadDefault(file, diskRootPath, urlRootPath);
        } else if (resultType.equals("layedit")) {
            // layui富文本
            resultStr = uploadLayedit(file, diskRootPath, urlRootPath);
        }

        return resultStr;
    }

    private String uploadDefault(MultipartFile file, String diskRootPath, String urlRootPath){
        ReturnJsonData json = new ReturnJsonData("error", "上传失败");
        try {
            if (!file.isEmpty()) {
                String url = urlRootPath + FileuploadUtil.upload(diskRootPath, file);
                if (StringUtils.isNotBlank(url)) {
                    Map<String, String> map = new HashMap();
                    map.put("url", url);
                    json.setParams(map);
                    json.setCode("success");
                    json.setMessage("上传成功");
                }
            }
        } catch (FileUploadBase.FileSizeLimitExceededException e) {
            json.setMessage("上传文件超出限定大小");
        } catch (IOException ioe) {
            json.setMessage("上传文件写入错误");
        } catch (Exception e) {
            json.setMessage("系统错误");
        }
        return GsonString(json);
    }

    private String uploadLayedit(MultipartFile file, String diskRootPath, String urlRootPath){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "error");
        resultMap.put("msg", "上传失败");
        try {
            if (!file.isEmpty()) {
                String url = urlRootPath + FileuploadUtil.upload(diskRootPath, file);
                if (StringUtils.isNotBlank(url)) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("src", url);
                    resultMap.put("code", "0");
                    resultMap.put("msg", "上传成功");
                    resultMap.put("data", data);
                }
            }
        } catch (FileUploadBase.FileSizeLimitExceededException e) {
            resultMap.put("msg", "上传文件超出限定大小");
        } catch (IOException ioe) {
            resultMap.put("msg", "上传文件写入错误");
        } catch (Exception e) {
            resultMap.put("msg", "系统错误");
        }
        return GsonString(resultMap);
    }
}
