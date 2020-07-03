package com.toher.project.swagger;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/27 9:45
 */
@Controller
@RequestMapping("/system/api")
public class SwaggerController {

    @RequiresPermissions("system:api:index")
    @GetMapping("/index")
    public String index() {
        return "redirect:/swagger-ui.html";
    }
}
