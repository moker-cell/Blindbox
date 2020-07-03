package com.toher.project.module.server.controller;

import com.toher.project.common.BaseController;
import com.toher.project.module.server.entity.Server;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/25 15:03
 */

@Controller
@RequestMapping("/system/server")
public class ServerController extends BaseController {

    private String templatePath = "module/server";

    @GetMapping("/index")
    public String server(ModelMap mmap) throws Exception {
        Server server = new Server();
        server.copyTo();
        mmap.put("server", server);
        return templatePath + "/index";
    }
}