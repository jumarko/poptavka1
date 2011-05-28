package cz.poptavka.sample.client.user.demands.tab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
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
import cz.poptavka.sample.shared.domain.DemandDetail;

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

        ListDataProvider<DemandDetail> getDataProvider();

        MultiSelectionModel<DemandDetail> getSelectionModel();

        Set<DemandDetail> getSelectedSet();

        SimplePanel getDetailSection();
    }

    public void bindView() {
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                // TODO
                GWT.log("SIZE of result set: " + view.getSelectedSet().size());
                if (view.getSelectedSet().size() != 1) {
                    // do not show any demand detail
                    GWT.log("NOT ONE");
                    return;
                }
                GWT.log("ONE");
                Iterator<DemandDetail> iter = view.getSelectedSet().iterator();
                eventBus.requestDemandDetail(iter.next().getId());
//                Window.alert(view.getSelectionModel().getSelectedObject().getTitle());
            }
        });
    }

    public void onInvokePotentialDemands() {
        // TODO call real method to get potential demands
        eventBus.displayContent(view.getWidgetView());
        eventBus.requestClientDemands();
    }

    public void onResponseClientDemands(ArrayList<DemandDetail> data) {
        GWT.log(" ** ** ** PotentialDemands Init ** ** ** ");
        // TODO fill the view
        List<DemandDetail> list = view.getDataProvider().getList();
        list.clear();
        for (DemandDetail d : data) {
            list.add(d);
        }
        view.getDataProvider().refresh();
        // widget display

    }

    public void onResponseDemandDetail(Widget widget) {
        view.getDetailSection().setWidget(widget);
    }

}
