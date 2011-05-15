package cz.poptavka.sample.messaging.sample;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Sample tests of synchronous methods used for messaging.
 *
 * @author Juraj Martinka
 *         Date: 5.5.11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:messaging-config.xml")
// Messaging is not completely ready
@Ignore
public class RabbitMQClientSynchronousTest {

    private static final String COMPLEX_MESSAGE_TITLE = "New complex message";
    private static final String TEST_QUEUE_NAME = "test.queue";
    @Autowired
    private AmqpAdmin admin;
    @Autowired
    private AmqpTemplate template;


    @Before
    public void setUp() {
        admin.declareQueue(new Queue(TEST_QUEUE_NAME));
    }

    @Test
    public void simpleProducerConsumerTest() {
        try {
            String sent = "Catch the rabbit! " + new Date();

            // write message
            template.convertAndSend(sent);
            // read message
            String received = (String) template.receiveAndConvert();

            System.out.println("Msg: " + received);

            Assert.assertEquals(sent, received);

        } catch (AmqpException e) {
            Assert.fail("Test failed: " + e.getLocalizedMessage());
        }
    }


    /**
     * Tests sending and receiving (i.e. conversion) of complex DTO objects, not just simple strings.
     * <p>
     *     More portable {@link org.springframework.amqp.support.converter.JsonMessageConverter} is used instead of
     * Java serialization -> see configuration in <code>messaging-config.xml</code>.
     */
    @Test
    public void testComplexDtoObjects() {
        try {

            // write message - twice
            template.convertAndSend(createComplexMessage());
            template.convertAndSend(createComplexMessage());

            // read message
            Message received = (Message) template.receiveAndConvert();
            Assert.assertEquals(COMPLEX_MESSAGE_TITLE, received.getTitle());

            // simple read second message from queue - you can manually check if message has been removed from
            // queue by issuing the "rabbitmqctl status" command
            template.receiveAndConvert();

        } catch (AmqpException e) {
            Assert.fail("Test failed: " + e.getLocalizedMessage());
        }
    }


    /**
     * Tests sending and receiving (i.e. conversion) of complex DTO objects, not just simple strings.
     *
     */

    //---------------------------------------------- HELPER METHODS ---------------------------------------------------
    private Message createComplexMessage() {

        final Message message = new Message();

        message.setTitle(COMPLEX_MESSAGE_TITLE);
        message.setTemp(true);
        message.setRetriesCount(3);
        message.setMessageItems(Arrays.asList(
                new MessageItem<String>("Content of first complex message's item"),
                new MessageItem<String>("Content of second complex message's item"),
                new MessageItem<String>("Content of third complex message's item")));
        message.setCreated(new Date());

        return message;
    }


    //----------------------------------  SAMPLE DOMAIN CLASSES FOR TESTS ----------------------------------------------
    public static class Message implements Serializable {
        private String title;

        private Date created;

        private boolean temp = false;

        private Integer retriesCount;

        private List<MessageItem<String>> messageItems = new ArrayList<MessageItem<String>>();


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Date getCreated() {
            return created;
        }

        public void setCreated(Date created) {
            this.created = created;
        }

        public boolean isTemp() {
            return temp;
        }

        public void setTemp(boolean temp) {
            this.temp = temp;
        }

        public Integer getRetriesCount() {
            return retriesCount;
        }

        public void setRetriesCount(Integer retriesCount) {
            this.retriesCount = retriesCount;
        }

        public List<MessageItem<String>> getMessageItems() {
            return messageItems;
        }

        public void setMessageItems(List<MessageItem<String>> messageItems) {
            this.messageItems = messageItems;
        }
    }

    public static class MessageItem<T> implements Serializable {
        private T content;

        private Long timestamp = System.currentTimeMillis();

        public MessageItem() {
            // default constructor for json converter
        }

        public MessageItem(T content) {
            this.content = content;
        }

        public T getContent() {
            return content;
        }

        public void setContent(T content) {
            this.content = content;
        }


        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }
    }


}
