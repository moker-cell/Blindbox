package com.toher.framework.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/18 11:14
 */
@Component
public class ShiroSessionListener implements SessionListener {

    private  Logger logger= LoggerFactory.getLogger(ShiroSessionListener.class);
    @Override
    public void onStart(Session session) {
        logger.info("sesion onStart");
    }

    @Override
    public void onStop(Session session) {
        logger.info("sesion onStop");
    }

    @Override
    public void onExpiration(Session session) {
        logger.info("sesion onExpiration");
    }
}
