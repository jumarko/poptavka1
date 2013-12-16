package com.eprovement.poptavka.messaging.sample;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Message sender
 */
public class MessageSender {

    @Autowired
    private AmqpTemplate template;

    public void send(String text) {
        template.convertAndSend(text);
    }
}
