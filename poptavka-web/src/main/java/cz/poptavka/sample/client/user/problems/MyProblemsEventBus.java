package cz.poptavka.sample.client.user.problems;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;
import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
/**
 *
 * @author Martin Slavkovsky
 *
 */
@Events(startView = MyProblemsView.class, module = MyProblemsModule.class)
public interface MyProblemsEventBus extends EventBus {

    @Event(handlers = MyProblemsPresenter.class)
    void displayMyProblems();

    @Event(handlers = MyProblemsPresenter.class)
    void displayMessages();

    @Event(forwardToParent = true)
    void setHomeWidget(AnchorEnum anchor, Widget content, boolean clearOthers);
}
