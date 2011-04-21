package cz.poptavka.sample.client.common.messages;

import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

@Events(startView = MessageView.class, module = MessageModule.class)
public interface MessageEventBus extends EventBus {

    @Event(handlers = MessagePresenter.class)
    void reply();

    @Event(handlers = MessagePresenter.class)
    void replyToAll();

    @Event(handlers = MessagePresenter.class)
    void forward();

    @Event(handlers = MessagePresenter.class)
    void send();

    @Event(handlers = MessagePresenter.class)
    void discard();

    @Event(handlers = MessagePresenter.class)
    void displayBody();
}
