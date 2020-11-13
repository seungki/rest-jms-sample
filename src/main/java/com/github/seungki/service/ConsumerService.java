package com.github.seungki.service;

import com.github.seungki.converter.ObjectConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.util.Map;

@Slf4j
@Component
public class ConsumerService {

    /**
     * 비동기용 Queue consume 
     * @param message
     * @throws Exception
     */
    @JmsListener(destination = "${default.async.queue.name}", containerFactory = "jmsListenerContainerFactory")
    public void receiveASyncMessage(ActiveMQObjectMessage message) throws Exception {
        Map<String, Object> dataMap = ObjectConverter.convertFromAMQ(message.getObject());
        log.info("2. RECEIVE FROM JMS : {} ",dataMap.toString());
    }

    /**
     * 동기용 Queue consume 후 수신 데이터 그대로 리턴
     * @param message
     * @param session
     * @throws Exception
     */
    @JmsListener(destination = "${default.sync.queue.name}", containerFactory = "jmsListenerContainerFactory")
    public void receiveSyncMessage(ActiveMQObjectMessage message, Session session) throws Exception {

        Map<String, Object> dataMap = ObjectConverter.convertFromAMQ(message.getObject());
        log.info("2. RECEIVE FROM JMS : {} ",dataMap.toString());

        final ObjectMessage responseMessage = new ActiveMQObjectMessage();
        responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());
        responseMessage.setObject(message.getObject());
        log.info("3. REPLY TO : {} ",message.getObject().toString());

        final MessageProducer producer = session.createProducer(message.getJMSReplyTo());
        producer.send(responseMessage);

    }

}
