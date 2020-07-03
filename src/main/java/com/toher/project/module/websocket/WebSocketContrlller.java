package com.toher.project.module.websocket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2019/1/14 17:15
 */
@Controller
@RequestMapping("/socket")
public class WebSocketContrlller {

    @RequestMapping("/push")
    @ResponseBody
    public void index(WebSocketBean webSocketBean){
        try {
            WebSocketServer.sendMessage(webSocketBean);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
