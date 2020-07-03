package com.toher.project.system;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.toher.common.dto.ReturnJsonData;
import com.toher.framework.configurer.ReadApplicationYmlUtil;
import com.toher.framework.redis.RedisUtil;
import com.toher.project.system.user.entity.User;
import com.toher.project.system.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import static com.toher.common.utils.CommonUtils.get32UUID;
import static com.toher.common.utils.RegexValidateUtil.checkMobileNumber;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/4 15:19
 */
@Controller
public class LoginController {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    DefaultKaptcha defaultKaptcha;
    //注入 shiro-redis 封装的redisSessionDAO 获取Session 前缀
    @Autowired
    RedisSessionDAO redisSessionDAO;
    @Resource
    private UserService userServiceImpl;

    //注入自定义配置
    @Resource
    private ReadApplicationYmlUtil readApplicationYmlUtil;

    private static final String TOKEN_KEY = "token:sms:";
    private static final String SMS_KEY = "sms:mobile:";
    /**
     * 登录请求方法
     *
     * @param httpSession
     * @param username
     * @param code
     * @return
     */
    @RequestMapping(value = "/login.json")
    @ResponseBody
    public ReturnJsonData login(HttpSession httpSession, String username, String password, String code) {

        String captcha = (String) httpSession.getAttribute(readApplicationYmlUtil.getSessionVrifycode());
        //封装返回参数
        ReturnJsonData json = new ReturnJsonData("error", "验证码不匹配");
        if(StringUtils.isBlank(captcha)){
            json.setMessage("验证码已失效");
            return json;
        }
        //equalsIgnoreCase 忽略大小写的比较
        if (captcha.equalsIgnoreCase(code)) {
            //shiro加入身份验证
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            try {
                subject.login(token);
                json.setCode("success");
                json.setMessage("登录成功");
                //清楚验证码Session
                httpSession.removeAttribute(readApplicationYmlUtil.getSessionVrifycode());
            } catch (UnknownAccountException uae) {
                json.setMessage("帐号或密码错误");
            } catch (IncorrectCredentialsException ice) {
                json.setMessage("验证未通过错误的凭证");
            } catch (LockedAccountException lae) {
                json.setMessage("验证未通过帐号已禁止登录");
            } catch (ExcessiveAttemptsException eae) {
                json.setMessage("验证未通过,错误次数过多");
            } catch (AuthenticationException e) {
                json.setMessage("用户验证失败");
                e.printStackTrace();
            }
        }
        return json;
    }

    @RequestMapping(value = "/logout.json")
    public String logout(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        if (session != null) {
            System.out.println("logout_session_id:" + session.getId());
            session.removeAttribute(readApplicationYmlUtil.getSessionUser());
            //再删除redis中的共享Session
            //Shiro默认KRY前缀
            String shiroKey = redisSessionDAO.getKeyPrefix();
            System.out.println(shiroKey);
            redisUtil.del(shiroKey + session.getId().toString());
        }
        return "redirect:/index.html" ;
    }

    @RequestMapping(value = "/forget.json")
    public String forget(HttpServletRequest request,String token) {
        request.setAttribute("token",token);
        return "system/forget_password.html" ;
    }

    @RequestMapping("/kaptcha.view")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        String type = httpServletRequest.getParameter("t");
        try {
            //生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            if(StringUtils.isBlank(type)) {
                httpServletRequest.getSession().setAttribute(readApplicationYmlUtil.getSessionVrifycode(), createText);
            }else if(type.equals("mail")){
                httpServletRequest.getSession().setAttribute("mailCode", createText);
            }
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    /**
     * 找回密码 手机发送短信验证码
     * @param request
     * @param mobile
     * @return
     */
    @RequestMapping("/sendSmsForGet.json")
    @ResponseBody
    public ReturnJsonData sendSms(HttpServletRequest request, String mobile){
        ReturnJsonData json = new ReturnJsonData("error","发送失败");
        if(StringUtils.isBlank(mobile)){
            json.setMessage("手机不允许为空");
            return json;
        }
        if(!checkMobileNumber(mobile)){
            json.setMessage("手机格式不正确");
            return json;
        }
        //判断用户表是否存在该邮箱
        User param = new User();
        param.setPhone(mobile);
        User user = userServiceImpl.findObjectByEntity(param);
        if(user==null){
            json.setMessage("不存在该手机账户");
            return json;
        }
        //redis中存在保存的随机数 则认定已经发送过短信等待 redis清除
        String redisValue = (String)redisUtil.get(SMS_KEY+mobile);
        if(StringUtils.isNotEmpty(redisValue)){
            Long expire = redisUtil.getExpire(SMS_KEY+mobile);
            System.out.println("expire:" + expire);
            //读取redis过期时间
            Long waitTime = 60 - (600L - expire);
            if(waitTime>0){
                json.setMessage(waitTime + "秒后获取验证码");
                return json;
            }
        }
        //生成随机6位数
        Integer random = (int)((Math.random() * 9 + 1) * 100000);
        //调用短信接口发送短信
        System.out.println("random:" + random);
        //发送成功后设置到Redis 有效时间10分钟
        redisUtil.set(SMS_KEY+mobile,random+"",600);
        return new ReturnJsonData("success","发送成功");
    }

    /**
     * 通过短信验证码获取TOKEN 作为可以修改的凭证
     * @param request
     * @param mobile
     * @param mobileCode
     * @return
     */
    @RequestMapping("/getSmsToken.json")
    @ResponseBody
    public ReturnJsonData getSmsToken(HttpServletRequest request, String mobile, String mobileCode){
        ReturnJsonData json = new ReturnJsonData("error","获取Token失败");
        String redisValue = (String)redisUtil.get(SMS_KEY+mobile);
        if(StringUtils.isBlank(redisValue)){
            json.setMessage("验证码已失效请重新获取");
            return json;
        }
        if(!redisValue.equals(mobileCode)){
            json.setMessage("验证码不正确");
            return json;
        }
        //判断用户表是否存在该邮箱
        User param = new User();
        param.setPhone(mobile);
        User user = userServiceImpl.findObjectByEntity(param);
        if(user==null){
            json.setMessage("不存在该手机账户");
            return json;
        }
        //生成UUID Token 将UserID存入Redis
        String tokenKey = get32UUID();
        redisUtil.set(TOKEN_KEY + tokenKey,user.getUserId(),1800);
        json.setCode("success");
        json.setMessage(tokenKey);
        return json;
    }

    /**
     * 重置密码
     * @param request
     * @param password
     * @param token
     * @return
     */
    @RequestMapping("/savePassword.json")
    @ResponseBody
    public ReturnJsonData savePassword(HttpServletRequest request, String password, String token){
        ReturnJsonData json = new ReturnJsonData("error","保存失败");
        if(StringUtils.isBlank(password)){
            json.setMessage("密码不能为空");
            return json;
        }
        String userId = (String)redisUtil.get(TOKEN_KEY + token);
        if(StringUtils.isBlank(userId)){
            json.setMessage("token无效");
            return json;
        }
        String passwordSHA = new SimpleHash("SHA-1",readApplicationYmlUtil.getSalt(), password).toString();
        User user = new User();
        user.setUserId(userId);
        user.setPassword(passwordSHA);
        int success = userServiceImpl.editObjectSelective(user);
        if(success>0){
            json.setCode("success");
            json.setMessage("保存成功");
            //成功后删除Redis Token
            redisUtil.del(TOKEN_KEY + token);
        }
        return json;
    }
}
