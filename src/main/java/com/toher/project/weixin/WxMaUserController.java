package com.toher.project.weixin;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.toher.common.constants.ConstantsCommon;
import com.toher.framework.configurer.ReadApplicationYmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.toher.common.utils.BeanUtils.copyBeanProp;


/**
 * 微信小程序用户接口
 *
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Api(tags = "微信登录")
@RestController
@RequestMapping("/api/wx/user")
public class WxMaUserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 使用静态Map存储session */
    private static Map<String,String> sessionKeyMap = new HashMap<>();

    @Resource
    private ReadApplicationYmlUtil readApplicationYmlUtil;

    /*@Resource
    UserWechatService userWechatService;//微信用户

    @Resource
    UserLoginService userLoginService;//员工用户

    @Resource
    SystemConfigureService systemConfigureService;//系统配置

    *//**
     * 用户登录接口
     * @param code
     * @return
     *//*
    @ApiOperation("登录")
    @GetMapping("/login/{code}")
    public com.toher.common.dto.ReturnJsonData login(@PathVariable String code){
        com.toher.common.dto.ReturnJsonData json = new com.toher.common.dto.ReturnJsonData("400","登录失败！");

        SystemConfigure systemConfigure = systemConfigureService.findObjectByPrimaryKey(1);
        final WxMaService wxService = WxMaConfiguration.getMaService(systemConfigure.getAppid());

        try{
            //获取临时登录凭证code到微信服务器换取openId和session_key
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            //用静态map存储sessionkey
            sessionKeyMap.put(session.getOpenid(),session.getSessionKey());
            //查询openId是否绑定用户
            Map<String,Object> map = new HashMap<>();
            map.put("wechatOpenId",session.getOpenid());
            UserWechat userWechat = userWechatService.customGetUserInXml(map);
            if (userWechat == null){
                //封装参数
                Map<String, Object> params = new HashMap<>();
                params.put("openId",session.getOpenid());
                json.setParams(params);
                json.setCode("401");
                json.setMessage("进行绑定");
                return json;
            }

            //查询员工账号
            UserLogin userLogin = userLoginService.findObjectByPrimaryKey(userWechat.getLoginUserId());
            if (userLogin == null){
                //封装参数
                Map<String, Object> params = new HashMap<>();
                params.put("openId",session.getOpenid());
                json.setParams(params);
                json.setCode("401");
                json.setMessage("找不到绑定账号");
                return json;
            }else if(userLogin.getLoginVersion() == 0){
                json.setMessage("该账号已被禁用");
                return json;
            }

            //封装参数
            Map<String, Object> params = new HashMap<>();
            params.put("openId",userWechat.getLoginUserId());
            params.put("scanState",userWechat.getUserLogin().getScanState());
            params.put("loginemployees",userLogin.getLoginEmployees());
            json.setParams(params);
            json.setCode("200");
            json.setMessage("登录成功");
            return json;
        }catch (Exception e){
            logger.info("系统错误",e);
            json.setCode("500");
            json.setMessage("系统错误");
            return json;
        }
    }

    *//**
     * 授权绑定
     * @param request
     * @param userLogin
     * @return
     *//*
    @RequestMapping("/bangding")
    public com.toher.common.dto.ReturnJsonData bangding(HttpServletRequest request, @RequestBody UserLogin userLogin){
        com.toher.common.dto.ReturnJsonData json = new com.toher.common.dto.ReturnJsonData("400","登录失败");

        int success = 0;
        try {
            //获取openId
            String cookie = request.getHeader(ConstantsCommon.HEADER_KEY);
            if (StringUtils.isBlank(cookie)){
                json.setMessage("");
                return json;
            }

            //判断账号、密码不能为空
            if (StringUtils.isBlank(userLogin.getLoginUsername()) || StringUtils.isBlank(userLogin.getLoginPassword())){
                json.setMessage("登录账号或密码不能为空");
                return json;
            }

            //将密码进行加密
            String password = new SimpleHash("SHA-1",readApplicationYmlUtil.getSalt(),userLogin.getLoginPassword()).toString();
            //查询登录账号是否存在
            UserLogin user = new UserLogin();
            user.setLoginUsername(userLogin.getLoginUsername());
            user.setLoginPassword(password);
            UserLogin userByUsername = userLoginService.findObjectByEntity(user);
            if (userByUsername == null){
                json.setMessage("账号或密码错误");
                return json;
            }else if(userByUsername.getLoginVersion() == 0 ){
                json.setMessage("该账号已被禁用");
                return json;
            }

            //查询登录账号是否被绑定
            UserWechat userWechat = new UserWechat();
            userWechat.setLoginUserId(userByUsername.getLoginUserId());
            Map<String,Object> map = new HashMap<>();
            map.put("loginUserId",userByUsername.getLoginUserId());
            UserWechat userWechatByUsername = userWechatService.customGetUserInXml(map);
            if (userWechatByUsername != null){
                json.setMessage("该账号已被绑定");
                return json;
            }

            //判断openid是否被绑定，没有绑定直接添加用户，绑定过就更新用户
            UserWechat openId = userWechatService.findObjectByPrimaryKey(cookie);
            userWechat.setWechatOpenId(cookie);
            if (openId == null){
                success = userWechatService.saveObjectSelective(userWechat);

            }else {
                success = userWechatService.editObjectSelective(userWechat);
            }

            if (success > 0){
                //封装参数
                Map<String, Object> params = new HashMap<>();
                params.put("openId",userWechat.getLoginUserId());
                json.setParams(params);
                json.setCode("200");
                json.setMessage("绑定成功");
            }
        }catch (Exception e){
            json.setCode("500");
            json.setMessage("系统错误");
            return json;
        }
        return json;
    }

    *//**
     * 获取微信用户信息接口
     * @return
     *//*
    @PostMapping("/info")
    public com.toher.common.dto.ReturnJsonData info(HttpServletRequest request,@RequestBody UserWechat requestParams){
        com.toher.common.dto.ReturnJsonData json = new com.toher.common.dto.ReturnJsonData("400","获取失败");

        SystemConfigure systemConfigure = systemConfigureService.findObjectByPrimaryKey(1);
        final WxMaService wxService = WxMaConfiguration.getMaService(systemConfigure.getAppid());

        int success = 0;
        try {
            //获取请求头信息
            String cookie = request.getHeader(ConstantsCommon.HEADER_KEY);
            UserWechat userWechat = new UserWechat();
            userWechat.setLoginUserId(cookie);
            UserWechat userWechatServiceObjectByEntity = userWechatService.findObjectByEntity(userWechat);
            userWechat.setWechatOpenId(userWechatServiceObjectByEntity.getWechatOpenId());
            if (userWechatServiceObjectByEntity == null){
                json.setMessage("获取用户失败");
                return json;
            }
            String sessionKey = sessionKeyMap.get(userWechatServiceObjectByEntity.getWechatOpenId());
            //用户信息校验
            if (!wxService.getUserService().checkUserInfo(sessionKey,requestParams.getRawData(),requestParams.getSignature())){
                json.setMessage("用户信息校验失败");
                return json;
            }
            //解密用户信息
            WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, requestParams.getEncryptedData(), requestParams.getIv());
            //更新用户信息
            userWechat.setWechatName(userInfo.getNickName());
            userWechat.setWechatUrl(userInfo.getAvatarUrl());
            userWechat.setWechatGender(userInfo.getGender());
            userWechat.setWechatCountry(userInfo.getCountry());
            userWechat.setWechatProvince(userInfo.getProvince());
            userWechat.setWechatCity(userInfo.getCity());
            success = userWechatService.editObjectSelective(userWechat);
            if (success > 0){
                json.setCode("200");
                json.setMessage("更新成功");
            }
        } catch (Exception e){
            json.setCode("500");
            json.setMessage("系统错误");
            return json;
        }

        return json;
    }*/
}
