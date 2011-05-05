package cz.poptavka.sample.client.user.demands;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.user.UserEventBus;

/**
 *
 * Just for user, not operator functionality implemented
 *
 * @author Beho
 *
 */

@Presenter(view = DemandsLayoutView.class)
public class DemandsLayoutPresenter extends BasePresenter<DemandsLayoutPresenter.DemandsLayoutInterface, UserEventBus> {


    private static final Logger LOGGER = Logger
            .getLogger(DemandsLayoutPresenter.class.getName());

    public interface DemandsLayoutInterface {

        HasClickHandlers getMyDemandsBtn();

        HasClickHandlers getOffersBtn();

        HasClickHandlers getCreateDemandBtn();

        Widget getWidgetView();

        void setContent(Widget contentWidget);
    }

    public void bind() {
        /** bind menu operations **/
        view.getMyDemandsBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.invokeMyDemands();
            }
        });
        view.getOffersBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                LOGGER.info("invoke offers");
                eventBus.invokeOffers();
            }
        });
        view.getCreateDemandBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.invokeDemandCreation();
            }
        });
    }

    public void onAtAccount() {
        eventBus.setTabWidget(view.getWidgetView());
    }

    public void onDisplayContent(Widget contentWidget) {
        LOGGER.fine("display content");
        view.setContent(contentWidget);
    }

}
