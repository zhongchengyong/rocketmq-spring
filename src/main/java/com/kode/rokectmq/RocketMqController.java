package com.kode.rokectmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller
 * Created by zhongcy on 2017-02-22.
 */
@RestController
@RequestMapping("/rocketmq")
public class RocketMqController {

    private static final String SUCCESS = "success";

    private static final Logger logger = LoggerFactory.getLogger(RocketMqController.class);

    @RequestMapping("send")
    public String sendMessage(String msg) {
        logger.info("send msg:{}",msg);
        return SUCCESS;
    }
}
