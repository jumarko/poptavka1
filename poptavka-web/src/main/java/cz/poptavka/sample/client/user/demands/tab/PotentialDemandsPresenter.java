package cz.poptavka.sample.client.user.demands.tab;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
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

        ListDataProvider<DemandDetail> getProvider();

        SingleSelectionModel<DemandDetail> getSelectionModel();
    }

    public void bindView() {
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
//                Window.alert(view.getSelectionModel().getSelectedObject().getTitle());
            }
        });
    }

    public void onInvokePotentialDemands() {
        // TODO call real method to get potential demands
        eventBus.requestClientDemands();
    }

    public void onResponseClientDemands(ArrayList<DemandDetail> data) {
        GWT.log(" ** ** ** PotentialDemands Init ** ** ** ");
        // TODO fill the view
        view.getProvider().setList(data);

        // widget display
        eventBus.displayContent(view.getWidgetView());
    }

}
