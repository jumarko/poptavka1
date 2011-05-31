package cz.poptavka.sample.client.user.demands.tab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.demands.widgets.DetailWrapperPresenter;
import cz.poptavka.sample.shared.domain.demand.DetailType;
import cz.poptavka.sample.shared.domain.demand.PotentialDemandDetail;

/**
 * Presenter for handling view actions.
 *
 * @author Beho
 *
 */
@Presenter(view = PotentialDemandsView.class, multiple = true)
public class PotentialDemandsPresenter extends
    LazyPresenter<PotentialDemandsPresenter.IPotentialDemands, UserEventBus> {

    public interface IPotentialDemands extends LazyView {
        Widget getWidgetView();

        Button getReplyButton();
        Button getDeleteButton();
        Button getActionButton();
        Button getRefreshButton();

        ListDataProvider<PotentialDemandDetail> getDataProvider();

        MultiSelectionModel<PotentialDemandDetail> getSelectionModel();

        Set<PotentialDemandDetail> getSelectedSet();

        SimplePanel getDetailSection();
    }

    private DetailWrapperPresenter detailPresenter = null;
    private boolean loaded = false;

    public void bindView() {
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
//                // TODO fix multiSelection
//                GWT.log("SIZE of result set: " + view.getSelectedSet().size());
//                if (view.getSelectedSet().size() != 1) {
//                    // do not show any demand detail
//                    return;
//                }
                Iterator<PotentialDemandDetail> iter = view.getSelectedSet().iterator();
                PotentialDemandDetail selected = iter.next();

                // event calls from the click
                eventBus.getDemandDetail(selected.getDemandId(), DetailType.POTENTIAL);
                eventBus.requestPotentialDemandConversation(selected.getMessageId(), Random.nextInt(6));
                detailPresenter.setMessageId(selected.getMessageId());
            }
        });
    }

    public void onInvokePotentialDemands() {
        if (loaded) {
            eventBus.displayContent(view.getWidgetView());
            return;
        }
        eventBus.requestPotentialDemands();
        loaded = true;
    }

    public void onResponsePotentialDemands(ArrayList<PotentialDemandDetail> data) {

        List<PotentialDemandDetail> list = view.getDataProvider().getList();
        list.clear();
        for (PotentialDemandDetail d : data) {
            list.add(d);
        }
        view.getDataProvider().refresh();

        // Init DetailWrapper for this view
        if (detailPresenter == null) {
            detailPresenter = eventBus.addHandler(DetailWrapperPresenter.class);
            detailPresenter.initDetailWrapper(view.getDetailSection(), DetailType.POTENTIAL);
        }

        // widget display
        eventBus.displayContent(view.getWidgetView());
    }

    public void cleanDetailWrapperPresenterForDevelopment() {
        GWT.log("WRAPPER REMOVED");
        eventBus.removeHandler(detailPresenter);
    }


}
