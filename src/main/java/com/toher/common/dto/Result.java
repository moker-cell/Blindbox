package com.toher.common.dto;

import com.toher.common.constants.ConstantsCommon;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口返回数据格式
 *
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/20 11:52
 */
@Data
@ApiModel(value = "接口返回对象", description = "接口返回对象")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    @ApiModelProperty(value = "成功标志")
    private boolean success;

    /**
     * 返回处理消息
     */
    @ApiModelProperty(value = "返回处理消息")
    private String message;

    /**
     * 返回代码
     */
    @ApiModelProperty(value = "返回代码")
    private Integer code;

    /**
     * 返回数据对象 data
     */
    @ApiModelProperty(value = "返回数据对象")
    private T result;

    /**
     * 时间戳
     */
    @ApiModelProperty(value = "时间戳")
    private long timestamp = System.currentTimeMillis();

    public Result() {

    }

    //定义成功方法
    public static Result<Object> success() {
        Result<Object> r = new Result<Object>();
        r.setSuccess(true);
        r.setCode(ConstantsCommon.CODE_OK_200);
        r.setMessage(ConstantsCommon.MESSAGE_OK_200);
        return r;
    }

    public static Result<Object> success(String msg) {
        Result<Object> r = new Result<Object>();
        r.setSuccess(true);
        r.setCode(ConstantsCommon.CODE_OK_200);
        r.setMessage(msg);
        return r;
    }

    public static <T> Result<T> success(T data) {
        return success(ConstantsCommon.CODE_OK_200, ConstantsCommon.MESSAGE_OK_200, data);
    }

    public static <T> Result<T> success(String msg, T data) {
        return success(ConstantsCommon.CODE_OK_200, msg, data);
    }

    /** 定义成功方法(data指定类型) */
    private static <T> Result<T> success(Integer code, String msg, T data) {
        Result<T> r = new Result();
        r.setCode(code);
        r.setSuccess(true);
        r.setMessage(msg);
        r.setResult(data);
        return r;
    }

    public static Result<Object> error(String msg) {
        return error(ConstantsCommon.CODE_SERVER_ERROR_500, msg);
    }

    public static Result<Object> error(Integer code, String msg) {
        Result<Object> r = new Result<Object>();
        r.setCode(code);
        r.setMessage(msg);
        r.setSuccess(false);
        return r;
    }

    public static <T> Result<T> error(String msg, T data) {
        return error(ConstantsCommon.CODE_SERVER_ERROR_500, msg, data);
    }

    /** 定义错误方法（data指定类型） */
    public static <T> Result<T> error(Integer code, String msg, T data) {
        Result<T> r = new Result();
        r.setCode(code);
        r.setSuccess(false);
        r.setMessage(msg);
        r.setResult(data);
        return r;
    }

    /** 无权限访问返回结果 */
    public static Result<Object> noauth(String msg) {
        return error(ConstantsCommon.CODE_NO_AUTHZ, msg);
    }

}