/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toher.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author 李怀明 封装工具类
 */
public class CommonUtils {

    /**
     * 通过UUID生成32位唯一数
     *
     * @return
     */
    public static String get32UUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }

    /**
     * 通过时间+随机数生成唯一数
     * @return
     */
    public static String snGen(){
        String SnPostfix = System.currentTimeMillis() + (int) (Math.random() * 1000000000) + "";
        return SnPostfix;
    }

    /**
     * 自定义前缀的时间+随机数生成唯一数
     * @param Prefix
     * @return
     */
    public static String snGen(String Prefix){
        String SnPostfix = System.currentTimeMillis() + (int) (Math.random() * 1000000000) + "";
        return Prefix + SnPostfix;
    }

    /**
     * 随机生成六位数验证码
     *
     * @return
     */
    public static int getRandomNum() {
        Random r = new Random();
        return r.nextInt(900000) + 100000;//(Math.random()*(999999-100000)+100000)
    }

    /**
     * 考虑到接收如：复选框多个值的时候，对于MYbatis我们还是直接存数组
     *
     * @param request
     * @return request.getParameterMap() 以 Map<String,String[]> 我们需要将其转化成普通Map
     */
    public static Map<String, Object> parameterMapToMap(HttpServletRequest request) {
        Map<String, Object> map = new HashMap();
        Map<String, String[]> requestMap = request.getParameterMap();
        Iterator<Entry<String, String[]>> it = requestMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String[]> entry = it.next();
            //普通类型
            if (entry.getValue().length == 1) {
                map.put(entry.getKey(), entry.getValue()[0]);
            } //数组类型
            else {
                String[] values = entry.getValue();
//                String value = "";
//                for (int i = 0; i < values.length; i++) {
//                    value = values[i] + ",";
//                }
//                value = value.substring(0, value.length() - 1);
                map.put(entry.getKey(), values);
            }
        }
        return map;
    }

    /**
     * 专用于接收 editTable 参数数据并封装MAP
     *
     * @param request
     * @return map
     */
    public static Map<String, Object> editTableDataToMap(HttpServletRequest request, String pkKey) {
        Map<String, Object> map = new HashMap();
        Map<String, String[]> requestMap = request.getParameterMap();
        Iterator<Entry<String, String[]>> it = requestMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String[]> entry = it.next();
            //获取参数名
            String paramName = entry.getKey();
            //确定属于可编辑表格数据时候才进行相应赋值
            if (paramName.contains("data[")) {
                paramName = paramName.replace("data[", "");
                Object id = StringUtils.substringBefore(paramName, "]");
                //赋值pkKey值
                map.put(pkKey, id);
                paramName = StringUtils.substringAfter(paramName, "[");
                paramName = paramName.replace("]", "");
                map.put(paramName, entry.getValue()[0]);
                break;
            }
        }
        return map;
    }

    /**
     * 使用org.apache.commons.beanutils.BeanUtils 将Map转换成对象
     *
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null) {
            return null;
        }
        Object obj = beanClass.newInstance();
        org.apache.commons.beanutils.BeanUtils.populate(obj, map);
        return obj;
    }

    /**
     * 获得用户远程地址
     *
     * @param request
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        } else if (StringUtils.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        } else if (StringUtils.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }

    /**
     * 将日期格式日期转换为字符串格式
     *
     * @param date
     * @param dateformat
     * @return
     */
    public static String dateToString(Date date, String dateformat) {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(dateformat);
        datestr = df.format(date);
        return datestr;
    }

    /**
     * 将字符串日期转换为日期格式
     *
     * @param datestr
     * @return
     */
    public static Date stringToDate(String datestr, String dateformat) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(dateformat);
        try {
            date = df.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 判断Object 对象不为null 或者 ""
     * @param str
     * @return
     */
    public static boolean objectIsEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    /**
     * 获取两个List的不同元素
     * @param list1
     * @param list2
     * @return
     */
    public static List<String> getDiffrentList(List<String> list1, List<String> list2) {

        if(list1 == null || list2 == null){
            return null;
        }
        Map<String,Integer> map = new HashMap<>(list1.size() + list2.size());
        List<String> diff = new ArrayList<>();
        List<String> maxList = list1;
        List<String> minList = list2;
        if(list2.size()>list1.size())
        {
            maxList = list2;
            minList = list1;
        }
        for (String string : maxList) {
            map.put(string, 1);
        }
        for (String string : minList) {
            Integer cc = map.get(string);
            if(cc!=null)
            {
                map.put(string, ++cc);
                continue;
            }
            map.put(string, 1);
        }
        for(Map.Entry<String, Integer> entry:map.entrySet())
        {
            if(entry.getValue()==1)
            {
                diff.add(entry.getKey());
            }
        }
        return diff;

    }
}
