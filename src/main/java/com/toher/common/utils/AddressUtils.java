package com.toher.common.utils;

import com.toher.common.utils.http.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/4 11:15
 *
 * 根据IP获取地理位置,通过抓取ip138页面数据获取
 */
public class AddressUtils {

    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    public static final String IP_URL = "http://www.ip138.com/ips138.asp?action=2&ip=";

    public static String getRealAddressByIP(String ip) {
        String address = "";
        try {
            HttpClientUtil httpClientUtil = new HttpClientUtil();
            //设置读取的字符编码
            httpClientUtil.setDEFALUT_ENCODE("gb2312");
            String addressHtml = httpClientUtil.post(IP_URL + ip);
            Document doc = Jsoup.parse(addressHtml);
            Elements select = doc.select(".ul1 > li:eq(1)");
            //找到结果标识才进行处理
            if(select!=null){
                String addressString = select.text();
                if(StringUtils.isNotEmpty(addressString)) {
                    addressString = StringUtils.substringAfter(addressString,"：");
                    System.out.println("addressString:" + addressString);
                }
            }
        } catch (Exception e) {
            log.error("根据IP获取所在位置----------错误消息：" + e.getMessage());
        }
        return address;
    }
}
