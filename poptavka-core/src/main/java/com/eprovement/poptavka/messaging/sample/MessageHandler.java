package com.eprovement.poptavka.messaging.sample;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Classic POJO for asynchronous receiving of messages.
 * <p>
 *     Decoupling from messaging infrastructure (i.e. in this case the
 *     {@link org.springframework.amqp.core.MessageListener} interface)
 *     is possible (BUT NOT THIS CASE)
 *     by using {@link org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter} in spring
 *     configuration file.
 *
 *
 * @author Juraj Martinka
 *         Date: 5.5.11
 */
public class MessageHandler implements MessageListener {

    private volatile AtomicLong totalNumberOfReceivedMessage = new AtomicLong(0);

    public void onMessage(Message message) {
        System.out.println("Received message: " + message);
        totalNumberOfReceivedMessage.incrementAndGet();
    }

    public long getTotalNumberOfReceivedMessage() {
        return totalNumberOfReceivedMessage.get();
    }

}
