package com.toher.project.system.user.controller;

import com.toher.common.dto.ReturnJsonData;
import com.toher.common.utils.file.FileuploadUtil;
import com.toher.common.utils.shiro.ShiroUtils;
import com.toher.framework.annotation.Log;
import com.toher.framework.configurer.ReadApplicationYmlUtil;
import com.toher.project.common.BaseController;
import com.toher.project.system.user.entity.User;
import com.toher.project.system.user.service.UserService;
import org.apache.commons.fileupload.FileUploadBase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/17 10:43
 */
@Controller
@RequestMapping("/system/user/center")
public class UserCenterController extends BaseController {

    private final String templatePath = "system/user/center";

    @Resource
    private UserService userServiceImpl;
    @Resource
    private ReadApplicationYmlUtil readApplicationYmlUtil;

    /**
     * 修改头像
     */
    @Log(title = "个人中心", action = "view")
    @GetMapping("/avatar")
    public String avatar(HttpServletRequest request) {
        User user = ShiroUtils.getAuthUser();
        request.setAttribute("user", user);
        return templatePath + "/avatar";
    }

    /**
     * 保存头像
     */
    @Log(title = "个人信息", action = "update")
    @PostMapping("/updateAvatar")
    @ResponseBody
    public ReturnJsonData updateAvatar(@RequestParam("avatarfile") MultipartFile file) {
        ReturnJsonData json = new ReturnJsonData("error", "操作失败");
        //获取认证用户
        User user = ShiroUtils.getAuthUser();
        //判断保存的图片地址
        String imagePath = "avatar";
        //获取classPath路径
        String filePath = readApplicationYmlUtil.getUploadPath() + imagePath;
        try {
            if (!file.isEmpty()) {
                String avatar = "/upload/"+imagePath + File.separator + FileuploadUtil.upload(filePath,file);
                //保存用户头像
                user.setAvatar(avatar);
                int success = userServiceImpl.editObjectSelective(user);
                if(success>0){
                    ShiroUtils.setAuthUser(user);
                }
                json.setCode("success");
                //将结果返回JSON 作用于刷新用户头像
                Map<String,String> map = new HashMap();
                map.put("avatar",avatar);
                json.setParams(map);
                json.setMessage("上传成功");
            }
        } catch (FileUploadBase.FileSizeLimitExceededException e) {
            //捕捉文件过大错误
            json.setMessage("上传文件超出限定大小");
            //logger.error("updateAvatar failed!", e);
        }catch(IOException ioe){
            //捕捉IO错误
            json.setMessage("文件写入错误");
        }
        return json;
    }

}


