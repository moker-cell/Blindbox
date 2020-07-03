package com.toher.project.module.websocket;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2019/1/14 15:11
 */
public class WebSocketBean {
    //1、站内信 2、IM
    private int sendType;
    //接收用户
    private String toUsers;
    //发送的消息
    private String message;

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public String getToUsers() {
        return toUsers;
    }

    public void setToUsers(String toUsers) {
        this.toUsers = toUsers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
