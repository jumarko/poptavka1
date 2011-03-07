package cz.poptavka.sample.client.common.creation;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.common.creation.CreationView.TopPanel;
import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;

@Presenter(view = CreationView.class)
public class CreationPresenter extends LazyPresenter<CreationPresenter.CreationViewInterface, CreationEventBus> {

    private final static Logger LOGGER = Logger.getLogger(CreationPresenter.class.getName());

    public interface CreationViewInterface extends LazyView {

        //click handlers
        HasClickHandlers firstNextButton();
        HasClickHandlers secondBackButton();
        HasClickHandlers secondNextButton();
        HasClickHandlers thirdBackButton();
        HasClickHandlers thirdNextButton();
        HasClickHandlers fourthBackButton();
        HasClickHandlers createDemandButton();
        //nav methods
        void toggleVisiblePanel(TopPanel newPanel);

        Widget getWidgetView();
    }

    public void bindView() {
        view.firstNextButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                view.toggleVisiblePanel(TopPanel.SECOND);
            }
        });
        view.secondBackButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                view.toggleVisiblePanel(TopPanel.REMOVE);
            }
        });
        view.secondNextButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                view.toggleVisiblePanel(TopPanel.THIRD);
            }
        });
        view.thirdBackButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                view.toggleVisiblePanel(TopPanel.REMOVE);
            }
        });

        view.thirdNextButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                view.toggleVisiblePanel(TopPanel.FOURTH);
            }
        });
        view.fourthBackButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                view.toggleVisiblePanel(TopPanel.REMOVE);
            }
        });
    }

    public void onInitDemandCreation() {
        LOGGER.info("Initializing View Widget...");
        eventBus.setAnchorWidget(AnchorEnum.SECOND, view.getWidgetView());
    }

}
