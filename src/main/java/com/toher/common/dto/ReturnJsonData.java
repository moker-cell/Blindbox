package com.toher.common.dto;

import java.util.Map;

public class ReturnJsonData {
    private String code;
    private String message;
    //作为其他需要传递的参数
    private Map params;

    public ReturnJsonData() {
    }

    public ReturnJsonData(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }
}
