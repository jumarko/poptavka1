package cz.poptavka.sample.client.user.problems;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

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
    }

    public void bind() {

    }

}
