package cz.poptavka.sample.client.home;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
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

//        void setDisplaySuppliersToken(String token);
        void setRegisterSupplierToken(String token);

        Widget getWidgetView();

        void setBody(Widget content);

        HasClickHandlers getDemandsButton();

        HasClickHandlers getSuppliersButton();
    }

    private DemandCreationPresenter demandCreation;

    public void bindView() {
        view.setHomeToken(getTokenGenerator().atHome());

        view.setCreateDemandToken(getTokenGenerator().atCreateDemand());
// TODO praso - remove TokenGenerator that doesn't support linking between async modules.
// normal buttons with HasClickHandler are used bellow in bind method

//        view.setDisplayDemandsToken(getTokenGenerator().atDemands());

//        view.setDisplaySuppliersToken(getTokenGenerator().atSuppliers());

        view.setRegisterSupplierToken(getTokenGenerator().atRegisterSupplier());

        view.getDemandsButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                eventBus.goToHomeDemands();
            }
        });
        view.getSuppliersButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                eventBus.goToHomeSuppliers();
            }
        });
    }

    public void onStart() {
        // for now do nothing...
    }

    public void onForward() {
        // for now do nothing...
    }

    public void onAtHome() {
        LOGGER.info("INIT Home Widget");
        onDisplayMenu();
        // TODO initial homepage widget compilation
    }

    public void onSetBodyWidget(Widget content) {
        view.setBody(content);
    }

    public void onDisplayMenu() {
        eventBus.setPublicLayout();
        eventBus.setBodyHolderWidget(view.getWidgetView());
    }

    /* Business events for child modules loading */
    public void onErrorOnLoad(Throwable reason) {
        // TODO praso - display error message if child modules doesn't load successfully
//view.displayErrorMessage( reason.getMessage() );
    }

    public void onBeforeLoad() {
        // TODO praso - display wait loop
//view.setWaitVisible( true );
    }

    public void onAfterLoad() {
        // TODO praso -  hide wait loop
//view.setWaitVisible( false );
    }
}
