package cz.poptavka.sample.client.user.problems;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.module.ChildModule;
import com.mvp4g.client.annotation.module.ChildModules;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.common.messages.MessagesModule;
import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
/**
 *
 * @author Martin Slavkovsky
 *
 */
@Events(startView = MyProblemsView.class, module = MyProblemsModule.class)
@ChildModules({
        @ChildModule(moduleClass = MessagesModule.class, autoDisplay = false, async = true)
})
public interface MyProblemsEventBus extends EventBus {

    @Event(handlers = MyProblemsPresenter.class)
    void displayMyProblems();

    @Event(modulesToLoad = MessagesModule.class)
    void displayMessages();

    @Event(forwardToParent = true)
    void setHomeWidget(AnchorEnum anchor, Widget content, boolean clearOthers);
}
