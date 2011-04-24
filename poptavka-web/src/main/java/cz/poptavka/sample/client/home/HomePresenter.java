package cz.poptavka.sample.client.home;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = HomeView.class)
public class HomePresenter extends LazyPresenter<HomePresenter.HomeInterface, HomeEventBus> {

    private static final Logger LOGGER = Logger.getLogger("  HomePresenter");

    public enum AnchorEnum {
        FIRST, SECOND, THIRD
    }

    public interface HomeInterface extends LazyView {

        void setHomeToken(String token);

        void setCreateDemandToken(String token);

        void setDisplayDemandsToken(String token);

        void setContent(AnchorEnum anchor, Widget content, boolean clearOthers);

        Widget getWidgetView();

        HasClickHandlers getButton3Btn();
    }

    public void bindView() {
        view.setHomeToken(getTokenGenerator().atHome());

        view.setCreateDemandToken(getTokenGenerator().atCreateDemand(true));

        view.setDisplayDemandsToken(getTokenGenerator().atDemands());

        view.getButton3Btn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.displayMyProblems();
            }
        });
    }

    public void onAtHome() {
        LOGGER.info(" on init view ...");
        eventBus.setBodyHolderWidget(view.getWidgetView());
        SimplePanel panel = new SimplePanel();
        eventBus.setHomeWidget(AnchorEnum.FIRST, panel, true);
        eventBus.initLocalityWidget(panel);
//        TODO uncomment below
//        eventBus.initCategoryDisplay(AnchorEnum.FIRST);
    }

    public void onDisplayMenu() {
        eventBus.setBodyHolderWidget(view.getWidgetView());
    }

    /**
     * Set content widget to selected part of page.
     *
     * @param anchor place where to place widget
     * @param body widget to be placed
     */
    public void onSetHomeWidget(AnchorEnum anchor, Widget content, boolean clearOthers) {
        view.setContent(anchor, content, clearOthers);
    }

}
