package cz.poptavka.sample.client.user.problems;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;

/**
 *
 * @author Martin Slavkovsky
 *
 */
@Presenter(view = MyProblemsView.class)
public class MyProblemsPresenter
        extends
        BasePresenter<MyProblemsPresenter.MyProblemsViewInterface, MyProblemsEventBus> {

    private static final Logger LOGGER = Logger
            .getLogger(MyProblemsPresenter.class.getName());

    public interface MyProblemsViewInterface {
        Widget getWidgetView();

        void createView();
    }

    public void bind() {

    }

    public void onDisplayMyProblems() {
        LOGGER.info("TUTUTUTUTU");
        //Initialize Problems
        eventBus.setHomeWidget(AnchorEnum.FIRST, view.getWidgetView(), true);
       //Initialize Messages
        LOGGER.info("AJAJAJAJAJA");
        eventBus.displayMessages();
    }

}
