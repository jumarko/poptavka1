package cz.poptavka.sample.client.home;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.home.creation.DemandCreationPresenter;

@Presenter(view = HomeView.class)
public class HomePresenter extends LazyPresenter<HomePresenter.HomeInterface, HomeEventBus> {

    private static final Logger LOGGER = Logger.getLogger("  HomePresenter");

    public interface HomeInterface extends LazyView {

        void setHomeToken(String token);

        void setCreateDemandToken(String token);

        void setDisplayDemandsToken(String token);

        void setRegisterSupplierToken(String token);

        Widget getWidgetView();

        void setBody(Widget content);

    }

    private DemandCreationPresenter demandCreation;

    public void bindView() {
        view.setHomeToken(getTokenGenerator().atHome());

        view.setCreateDemandToken(getTokenGenerator().atCreateDemand());

        view.setDisplayDemandsToken(getTokenGenerator().atDemands());

        view.setRegisterSupplierToken(getTokenGenerator().atRegisterSupplier());
    }

    public void onAtHome() {
        LOGGER.info("INIT Home Widget");
        onDisplayMenu();
        // TODO initial homepage widget compilation
    }

    public void onDisplayMenu() {
        eventBus.setPublicLayout();
        eventBus.setBodyHolderWidget(view.getWidgetView());
    }

    public void onSetBodyWidget(Widget content) {
        view.setBody(content);
    }

}
