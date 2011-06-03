package cz.poptavka.sample.client.home.demands.demand;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.home.demands.DemandsEventBus;

@Presenter(view = DemandView.class)
public class DemandPresenter extends BasePresenter<DemandPresenter.DemandViewInterface, DemandsEventBus> {

    public interface DemandViewInterface {

        HasClickHandlers getButtonAttachments();

        HasClickHandlers getButtonLogin();

        HasClickHandlers getButtonRegister();
    }

    /**
     * Bind objects and theirs action handlers.
     */
    @Override
    public void bind() {
        view.getButtonAttachments().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.attachement();
            }
        });
        view.getButtonLogin().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.login();
            }
        });
        view.getButtonRegister().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.register();
            }
        });
    }

    public void onAttachement() {
    }

    public void onLogin() {
    }

    public void onRegister() {
    }
}
