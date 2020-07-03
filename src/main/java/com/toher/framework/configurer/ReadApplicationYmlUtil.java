package com.toher.framework.configurer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/18 10:05
 */
@Data
@Component
@ConfigurationProperties(prefix = "toher.system")
public class ReadApplicationYmlUtil {

    private String systemPath;

    private String sessionUser;

    private String sessionVrifycode;

    private String salt;
    /** 文件上传根路径 */
    private String uploadPath;
    /** 跨域白名单 */
    private List<String> whiteList;

    /*private List<String> templateId;*/
}
