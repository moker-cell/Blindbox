package com.toher.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2019/1/9 17:53
 */
public class RegexValidateUtil {
    /**
     * 邮箱校验
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();

        } catch(Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 手机号校验
     * @param mobileNumber
     * @return
     */
    public static boolean checkMobileNumber(String mobileNumber){
        boolean flag = false;
        try {
            Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        } catch(Exception e) {
            flag = false;
        }
        return flag;
    }
}
