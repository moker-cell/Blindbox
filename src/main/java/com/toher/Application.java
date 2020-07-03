package com.toher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/1 11:15
 */
@EnableScheduling//表示开启定时任务
@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = "com.toher.project.**.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
