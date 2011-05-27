package cz.poptavka.sample.client.user.demands.tab;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.main.common.StatusIconLabel;
import cz.poptavka.sample.client.main.common.category.CategorySelectorPresenter.CategorySelectorInterface;
import cz.poptavka.sample.client.main.common.creation.FormDemandAdvPresenter.FormDemandAdvViewInterface;
import cz.poptavka.sample.client.main.common.creation.FormDemandBasicPresenter.FormDemandBasicInterface;
import cz.poptavka.sample.client.main.common.creation.ProvidesValidate;
import cz.poptavka.sample.client.main.common.locality.LocalitySelectorPresenter.LocalitySelectorInterface;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.DemandDetail;

@Presenter(view = NewDemandView.class)
public class NewDemandPresenter extends LazyPresenter<NewDemandPresenter.NewDemandInterface, UserEventBus> {

    private final static Logger LOGGER = Logger.getLogger("    DemandCreationPresenter");
    private static final LocalizableMessages MSGS = GWT
    .create(LocalizableMessages.class);
    public interface NewDemandInterface extends LazyView {

        StackLayoutPanel getMainPanel();

        StatusIconLabel getStatusLabel(int order);

        SimplePanel getHolderPanel(int order);

        HasClickHandlers getCreateDemandButton();

        Widget getWidgetView();
    }

    public void bindView() {
        view.getMainPanel().addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
                int eventItem = event.getItem();
                if (view.getMainPanel().getVisibleIndex() < eventItem) {
                    boolean result = canContinue(eventItem);
                    if (!result) {
                        // TODO change to global status changer eventBus call
                        event.cancel();
                    } else {
                        // TODO change to global status changer eventBus call
                        view.getStatusLabel(eventItem).setPassedSmall(result);
                    }
                }
            }
        });
        view.getCreateDemandButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (canContinue(ADVANCED)) {
                    eventBus.requestClientId();
                }
            }
        });
    }

    /**
     * Init method call.
     * TODO decide when other parts should be built.
     */
    public void onInvokeNewDemand() {
        LOGGER.fine("DemandCreation Widget");

        view.getMainPanel().showWidget(0);
        eventBus.initDemandBasicForm(view.getHolderPanel(BASIC));
        eventBus.initCategoryWidget(view.getHolderPanel(CATEGORY));

        eventBus.displayContent(view.getWidgetView());

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                eventBus.initLocalityWidget(view.getHolderPanel(LOCALITY));
                eventBus.initDemandAdvForm(view.getHolderPanel(ADVANCED));
            }
        });
    }

    public void onResponseClientId(Long id) {
        eventBus.loadingShow(MSGS.progressGettingDemandData());

        DemandDetail demand = new DemandDetail();

        demand.setBasicInfo(((FormDemandBasicInterface)
                view.getHolderPanel(BASIC).getWidget()).getValues());
        demand.setCategories(((CategorySelectorInterface)
                view.getHolderPanel(CATEGORY).getWidget()).getSelectedCategoryCodes());
        demand.setLocalities(((LocalitySelectorInterface)
                view.getHolderPanel(LOCALITY).getWidget()).getSelectedLocalityCodes());
        demand.setAdvInfo(((FormDemandAdvViewInterface)
                view.getHolderPanel(ADVANCED).getWidget()).getValues());

        eventBus.createDemand(demand, id);
        eventBus.loadingShow(MSGS.progressCreatingDemand());
    }

    private static final int BASIC = 1;
    private static final int CATEGORY = 2;
    private static final int LOCALITY = 3;
    private static final int ADVANCED = 4;

    private boolean canContinue(int step) {
        ProvidesValidate widget = (ProvidesValidate) view.getHolderPanel(step).getWidget();
        return widget.isValid();
    }
}
