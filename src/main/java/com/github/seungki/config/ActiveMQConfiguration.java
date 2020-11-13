package com.github.seungki.config;


import com.github.seungki.converter.ObjectMessageConverterErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;

@Slf4j
@EnableJms
@Configuration
public class ActiveMQConfiguration {

    @Value("${activemq.broker.url}")
    private String brokerUrl;

    @Value("${activemq.connection.max}")
    private int maxConnections;

    @Value("${activemq.listener.concurrency}")
    private String listenerConcurrency;


    /**
     * Producer 용 connection pool 생성
     * @return
     */
    public PooledConnectionFactory pooledConnectionFactory() {
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(connectionFactory());
        pooledConnectionFactory.setMaxConnections(maxConnections);
        pooledConnectionFactory.setCreateConnectionOnStartup(true);
        return pooledConnectionFactory;
    }

    /**
     * ActiveMQ Connection Factory 생성
     * @return
     */
    private ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(brokerUrl);
        activeMQConnectionFactory.setTrustAllPackages(true);
        activeMQConnectionFactory.setWatchTopicAdvisories(false);
        return activeMQConnectionFactory;
    }

    /**
     * Producer 용 JmsTemplate
     * @return
     */
    @Bean
    public JmsTemplate producerJmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        //DeliveryMode
        jmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT);
        //AcknowledgeMode
        jmsTemplate.setSessionAcknowledgeMode(JmsProperties.AcknowledgeMode.AUTO.getMode());
        //TimeOut - move to expiryQueue
        jmsTemplate.setTimeToLive(60000);
        jmsTemplate.setConnectionFactory(pooledConnectionFactory());
        return jmsTemplate;
    }

    /**
     * Consumer 용 listener
     * @return
     */
    @Bean
    public JmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory());
        factory.setErrorHandler(new ObjectMessageConverterErrorHandler());
        //core poll size=2 threads and max poll size 10 threads
        factory.setConcurrency(listenerConcurrency);
        return factory;
    }

}
