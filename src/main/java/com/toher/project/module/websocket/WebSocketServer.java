package com.toher.project.module.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2019/1/14 11:50
 */
@Component
@ServerEndpoint("/websocket/{userId}")
public class WebSocketServer {
    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    //定义连接总数
    private static int onlineCount = 0;
    //使用map对象，便于根据userId来获取对应的WebSocket
    private static ConcurrentHashMap<String,Session> websocketMap = new ConcurrentHashMap<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session socketSession;

    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId) {
        //判断当前map是否已经存在KEY 存在则不需要累加人数
        if(!websocketMap.containsKey(userId)){
            addOnlineCount();//在线数加1
        }
        websocketMap.put(userId,session);
        this.socketSession = session;
        logger.info("有新窗口开始监听:"+userId+",当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     *
     * @param session WebSocket会话
     */
    @OnClose
    public void onClose(Session session) {
        String userId = getKey(websocketMap, session);
        if (!StringUtils.isBlank(userId)) {
            //在线数减1
            subOnlineCount();
            System.out.println("用户" + userId + "退出！当前在线人数为" + onlineCount);
            websocketMap.remove(userId);
        }
    }

    /**
     *
     * @param message JSON封装需要发送的数据 类型 接收人等
     * @param session WebSocket会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        //如需要 此处可以封装对象 JSON TO Bean
        ObjectMapper mapper = new ObjectMapper();
        try {
            WebSocketBean webSocketBean = mapper.readValue(message, WebSocketBean.class);
            sendMessage(webSocketBean);
        } catch (IOException e) {
            logger.error("发生错误",e);
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("发生错误",error);
    }

    public static void sendMessage(WebSocketBean webSocketBean) throws IOException {
        if(webSocketBean==null){
            return;
        }
        switch (webSocketBean.getSendType()) {
            //站内信推送
            case 1:
                //如果未指定接收用户则群发
                if(StringUtils.isBlank(webSocketBean.getToUsers())){
                    //循环发给所有在线的人
                    for (Session s : websocketMap.values()) {
                        //转化Json待前端解析 决定处理
                        s.getAsyncRemote().sendText(webSocketBean.getSendType() + ":" + webSocketBean.getMessage());
                        System.out.println("发给系统所有在线的人" + s.getOpenSessions() + "：" + webSocketBean.getMessage());
                    }
                }else{
                    String[] users = webSocketBean.getToUsers().split(",");
                    for(String userId : users){
                        Session s = websocketMap.get(userId);
                        if (s != null) {
                            s.getAsyncRemote().sendText(webSocketBean.getSendType() + ":" + webSocketBean.getMessage());
                            System.out.println("发给" + userId + "：" + webSocketBean.getMessage());
                        }
                    }
                }
                break;
            //即时通讯推送
            case 2:
                //代码编辑
                break;
            default:
                break;
        }
    }

    /**
     * 根据value值获取到对应的一个key值
     *
     * @param map 存储的
     * @param value
     * @return
     */
    public static String getKey(ConcurrentHashMap<String, Session> map, Session value) {
        String key = null;
        for (String getKey : map.keySet()) {
            if (map.get(getKey).equals(value)) {
                key = getKey;
                break;
            }
        }
        return key;
    }

    //获取总在线人数
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    //每次上线 +1
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    //每次下线 -1
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
