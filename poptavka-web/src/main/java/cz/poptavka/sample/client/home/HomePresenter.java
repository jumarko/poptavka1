package cz.poptavka.sample.client.home;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = HomeView.class)
public class HomePresenter extends LazyPresenter<HomePresenter.HomeInterface, HomeEventBus> {

    private static final Logger LOGGER = Logger.getLogger(HomePresenter.class.getName());

    public enum AnchorEnum {
        FIRST, SECOND, THIRD
    }

    public interface HomeInterface extends LazyView {

        void setContent(AnchorEnum anchor, Widget content);

        Widget getWidgetView();
    }

    public void bindView() {

    }

    public void onInitHome() {
        LOGGER.info("on init home");
        eventBus.setBodyHolderWidget(view.getWidgetView());

        //locality selector testing
        eventBus.initLocalitySelector(AnchorEnum.FIRST);

    }

    /**
     * Set content widget to selected part of page
     *
     * @param anchor place where to place widget
     * @param body widget to be placed
     */
    public void onSetAnchorWidget(AnchorEnum anchor, Widget content) {
        view.setContent(anchor, content);
    }

}
