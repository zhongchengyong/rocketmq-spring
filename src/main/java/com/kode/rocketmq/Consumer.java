package com.kode.rocketmq;

import com.kode.rocketmq.service.RocketMqMessageWrapper;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 消息消费者
 * Created by zhongcy on 2017-02-22.
 */
@Component
public class Consumer {
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private DefaultMQPushConsumer defaultMQPushConsumer;

    @Value("${namesrvAddr}")
    private String namesrvAddr;

    @Value("${consumerGroup}")
    private String consumerGroup;

    @Value("${topic}")
    private String topic;

    @Value("${subExpression}")
    private String subExpression;

    @Value("${instanceName}")
    private String instanceName;

    @Autowired
    private RocketMqMessageWrapper rocketMqMessageWrapper;

    @PostConstruct
    public void init() throws MQClientException {
        defaultMQPushConsumer = new DefaultMQPushConsumer(consumerGroup);
        defaultMQPushConsumer.setNamesrvAddr(namesrvAddr);
        defaultMQPushConsumer.setInstanceName(instanceName);

        //设置订阅tag下的subExpression
        defaultMQPushConsumer.subscribe(topic, subExpression);

        // 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
        // 如果非第一次启动，那么按照上次消费的位置继续消费
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        //设置为集群消费(区别于广播消费)
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);

        //注册监听
        defaultMQPushConsumer.registerMessageListener(rocketMqMessageWrapper);

        //关闭VIP通道，避免接收不了消息
        defaultMQPushConsumer.setVipChannelEnabled(false);

        defaultMQPushConsumer.start();
        logger.info("rocketMq Client start success");
    }

    @PreDestroy
    public void destroy(){
        defaultMQPushConsumer.shutdown();
    }

}
