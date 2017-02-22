package com.kode.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 生产者
 * Created by zhongcy on 2017-02-22.
 */
@Component
public class Producer {
    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    private DefaultMQProducer defaultMQProducer;

    @Value("${producerGroup}")
    private String producerGroup;

    @Value("${namesrvAddr}")
    private String namesrvAddr;

    @Value("${instanceName}")
    private String instanceName;

    @PostConstruct
    public void init() throws MQClientException {
        this.defaultMQProducer = new DefaultMQProducer(producerGroup);
        defaultMQProducer.setNamesrvAddr(namesrvAddr);
        defaultMQProducer.setInstanceName(instanceName);
        //关闭VIP通道，避免出现connect to <:10909> failed导致消息发送失败
        defaultMQProducer.setVipChannelEnabled(false);
        defaultMQProducer.start();
        logger.info("RocketMq Producer start success");
    }

    @PreDestroy
    public void destroy() {
        defaultMQProducer.shutdown();
    }

    public DefaultMQProducer getDefaultMQProducer() {
        return defaultMQProducer;
    }
}
