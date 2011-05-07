package cz.poptavka.sample.messaging.sample;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @see
 */
public class MessageSender {

    @Autowired
    private AmqpTemplate template;

    public void send(String text) {
        template.convertAndSend(text);
    }
}
