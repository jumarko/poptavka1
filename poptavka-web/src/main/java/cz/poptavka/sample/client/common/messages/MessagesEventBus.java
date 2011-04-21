/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.common.messages;

import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

/**
 *
 * @author Martin Slavkovsky
 */
@Events(startView = MessagesView.class, module = MessagesModule.class)
public interface MessagesEventBus extends EventBus {

    @Event(handlers = MessagesPresenter.class)
    void displayMessages();
}
