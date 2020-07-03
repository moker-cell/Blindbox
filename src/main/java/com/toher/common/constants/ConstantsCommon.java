package com.toher.common.constants;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/20 10:57
 */
public class ConstantsCommon {
    //任务器参数MAP name
    public static final String JOB_PARAM = "JOB_PARAM";
    //UTF-8 字符集
    public static final String UTF8 = "UTF-8";
    //成功标识
    public static final String SUCCESS = "success";
    //失败标识
    public static final String FAIL = "fail";
    //错误标识
    public static final String ERROR = "error";

    //正常状态
    public static final Integer STATUS_NORMAL = 1;
    //禁用状态
    public static final Integer STATUS_DISABLE = 0;
    //删除标志
    public static final Integer STATUS_DELETE = -1;

    //系统日志类型： 登录
    public static final int LOG_TYPE_LOGIN = 1;
    //系统日志类型： 操作
    public static final int LOG_TYPE_HANDLE = 2;
    //系统日志类型： 定时任务
    public static final int LOG_TYPE_TIMER = 3;

    //操作日志类型： 查询
    public static final int OPERATE_TYPE_QUERY = 1;
    //操作日志类型： 添加
    public static final int OPERATE_TYPE_ADD = 2;
    //操作日志类型： 更新
    public static final int OPERATE_TYPE_UPDATE = 3;
    //操作日志类型： 删除
    public static final int OPERATE_TYPE_DELETE = 4;
    //操作日志类型： 导出
    public static final int OPERATE_TYPE_EXPORT = 5;
    //操作日志类型： 导入
    public static final int OPERATE_TYPE_IMPORT = 6;

    //定义HTTP返回码
    public static final Integer CODE_SERVER_ERROR_500 = 500;
    public static final Integer CODE_OK_200 = 200;
    public static final Integer CODE_NO_AUTHZ = 510;
    public static final Integer CODE_NO_FIND = 404;

    //定义HTTP返回信息
    public static final String MESSAGE_OK_200 = "操作成功！";

    //定义JWT header存储得KEY
    public static final String HEADER_KEY = "ACCESS-TOKEN";
    /** 登录用户Shiro权限缓存KEY前缀 */
    public static String PREFIX_USER_SHIRO_CACHE  = "shiro:cache:cm.toher.boot.authorization:";
    /** 登录用户Token令牌缓存KEY前缀 */
    public static final String PREFIX_USER_TOKEN  = "prefix_user_token_";
    /** Token缓存时间：3600秒即一小时 */
    public static final int  TOKEN_EXPIRE_TIME  = 3600;
    /** Token缓存KEY前缀 */
    public static final String PREFIX_TOKEN_CACHE = "api:cache:token:";
}
