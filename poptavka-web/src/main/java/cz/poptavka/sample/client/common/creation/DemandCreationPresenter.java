package cz.poptavka.sample.client.common.creation;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.common.CommonEventBus;
import cz.poptavka.sample.client.common.creation.DemandCreationView.TopPanel;
import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;

@Presenter(view = DemandCreationView.class)
public class DemandCreationPresenter
    extends LazyPresenter<DemandCreationPresenter.CreationViewInterface, CommonEventBus> {

    private final static Logger LOGGER = Logger.getLogger("    DemandCreationPresenter");

    /** View interface methods. **/
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

        SimplePanel getLocalityHolder();

        SimplePanel getCategoryHolder();

        Widget getWidgetView();
    }

    private boolean initLocality = true;
    private boolean initCategory = true;

    public void bindView() {
        view.firstNextButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                view.toggleVisiblePanel(TopPanel.SECOND);
                if (initLocality) {
                    eventBus.initLocalityWidget(view.getLocalityHolder());
                    initLocality = false;
                }
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
                if (initCategory) {
                    eventBus.initCategoryWidget(view.getCategoryHolder());
                    initCategory = false;
                }
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

    public void onInitDemandCreation(boolean homeSection) {
        LOGGER.info("Initializing Demand Creation View Widget ... ");
        eventBus.setAnchorWidget(homeSection, AnchorEnum.THIRD, view.getWidgetView(), true);
    }

}
