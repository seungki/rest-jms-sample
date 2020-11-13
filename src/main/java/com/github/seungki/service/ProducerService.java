package com.github.seungki.service;

import com.github.seungki.converter.ObjectConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ProducerService {

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${default.async.queue.name}")
    String aSyncQueueName;

    @Value("${default.sync.queue.name}")
    String syncQueueName;

    @Value("${activemq.timeToLive}")
    int timeToLive;

    @Value("${activemq.receiveTimeout}")
    int receiveTimeout;


    @Autowired
    JmsMessagingTemplate jmsMessagingTemplate;

    /**
     * 비동기 방식 - Queue 에 데이터 전송
     * @param dataMap
     * @return
     * @throws Exception
     */
    public Map<String, Object> send(Map<String, Object> dataMap) throws Exception{
        log.info("1. SEND TO JMS : {} ", dataMap.toString());

        jmsTemplate.setTimeToLive(timeToLive);
        jmsMessagingTemplate.setJmsTemplate(jmsTemplate);

        Session session = jmsMessagingTemplate.getConnectionFactory().createConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
        ObjectMessage objectMessage = session.createObjectMessage(ObjectConverter.convertToAMQ(dataMap));
        objectMessage.setJMSExpiration(timeToLive);

        jmsMessagingTemplate.convertAndSend(aSyncQueueName, objectMessage);

        return null;
    }


    /**
     * 동기방식 - Queue 에 데이터 전송 후 reply 데이터 리턴.
     * @param dataMap
     * @return
     * @throws Exception
     */
    public Map<String, Object> sendAndReceive(Map<String, Object> dataMap) throws Exception {
        log.info("1. SEND TO JMS : {} ", dataMap.toString());

        jmsTemplate.setTimeToLive(timeToLive);
        jmsTemplate.setReceiveTimeout(receiveTimeout);
        jmsMessagingTemplate.setJmsTemplate(jmsTemplate);

        Session session = jmsMessagingTemplate.getConnectionFactory().createConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
        ObjectMessage objectMessage = session.createObjectMessage(ObjectConverter.convertToAMQ(dataMap));
        objectMessage.setJMSExpiration(timeToLive);

        Destination tempDest = session.createTemporaryQueue();

        objectMessage.setJMSCorrelationID(UUID.randomUUID().toString());
        objectMessage.setJMSReplyTo(tempDest);

        return jmsMessagingTemplate.convertSendAndReceive(syncQueueName, objectMessage, Map.class);
    }
}
