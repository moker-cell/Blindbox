package com.toher.project.module.job.task;

import com.toher.common.dto.ReturnJsonData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/19 17:59
 */
@Component
public class TestJob{
    private static final Logger log = LoggerFactory.getLogger(TestJob.class);

    public void isParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void isNoParams()
    {
        System.out.println("执行无参方法");
    }

    public ReturnJsonData isReturnFuture()
    {
        System.out.println("执行无参方法 Future回调");
        ReturnJsonData json = new ReturnJsonData("SUCCESS","下单成功");
        Map<String,Object> map = new HashMap();
        map.put("account","account");
        map.put("orderNo","NO123");
        json.setParams(map);
        return json;
    }
}
