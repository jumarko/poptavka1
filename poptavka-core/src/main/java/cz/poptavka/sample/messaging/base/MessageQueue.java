package cz.poptavka.sample.messaging.base;

import org.springframework.amqp.core.Queue;

import java.util.Map;

/**
 * Handy subclass of {@link org.springframework.amqp.core.Queue}.
 * <p>
 * This class does not add any new functionality.
 * The builder pattern is used to replace awkward constructors of original {@link org.springframework.amqp.core.Queue}.
 *
 * @author Juraj Martinka
 *         Date: 6.5.11
 */
public class MessageQueue extends Queue {

    public MessageQueue(String name) {
        super(name);
    }

    public static class Builder {
        // Required parameters
        private String queueName;

        // Optional parameters
        private boolean durable;
        private boolean exclusive;
        private boolean autoDelete;
        private Map<String, Object> arguments;


        public Builder(String queueName) {
            this.queueName = queueName;
        }

        public Builder durable(boolean durable) {
            this.durable = durable;
            return this;
        }

        public Builder exclusive(boolean exclusive) {
            this.exclusive = exclusive;
            return this;
        }

        public Builder autoDelete(boolean autoDelete) {
            this.autoDelete = autoDelete;
            return this;
        }

        public Builder arguments(Map<String, Object> arguments) {
            this.arguments = arguments;
            return this;
        }

        // method for creating final object
        public MessageQueue build() {
            return new MessageQueue(this);
        }
    }

    private MessageQueue(Builder builder) {
        super(builder.queueName, builder.durable, builder.exclusive, builder.autoDelete, builder.arguments);
    }
}
