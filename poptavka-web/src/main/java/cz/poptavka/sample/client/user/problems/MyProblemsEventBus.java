package cz.poptavka.sample.client.user.problems;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;
/**
 *
 * @author Martin Slavkovsky
 *
 */
@Events(startView = MyProblemsView.class, module = MyProblemsModule.class)
public interface MyProblemsEventBus extends EventBus {

    @Event(handlers = MyProblemsPresenter.class)
    void displayProblems();

    @Event(handlers = MyProblemsPresenter.class)
    void displayMessages(Problem problem);

    @Event(forwardToParent = true)
    void setBodyWidget(Widget content);
}
