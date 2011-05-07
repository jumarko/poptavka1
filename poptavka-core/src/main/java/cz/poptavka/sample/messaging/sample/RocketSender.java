package cz.poptavka.sample.messaging.sample;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 5.5.11
 */
public class RocketSender {

    public void sendRockets() throws IOException {
        List<String> rocketsWithRoutings = new RocketRouter().build();

        Connection connection = new ConnectionFactory().newConnection();
        Channel channel = connection.createChannel();

        String rocketExchange = "rockets.launched";
        channel.exchangeDeclare(rocketExchange, "topic");
        String rocketQueue = channel.queueDeclare().getQueue();
        channel.queueBind(rocketQueue, rocketExchange, "galaxies.*.planets.*");

        for (String rocketTo : rocketsWithRoutings) {
            channel.basicPublish(rocketExchange, "galaxies.*.planets." + rocketTo, null, rocketTo.getBytes());
        }

        channel.close();
        connection.close();
    }

}
