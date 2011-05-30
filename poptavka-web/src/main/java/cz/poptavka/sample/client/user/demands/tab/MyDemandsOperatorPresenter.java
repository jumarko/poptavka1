package cz.poptavka.sample.client.user.demands.tab;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.demands.widgets.DetailWrapperPresenter;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.demand.DetailType;

@Presenter(view = MyDemandsOperatorView.class)
public class MyDemandsOperatorPresenter extends
    LazyPresenter<MyDemandsOperatorPresenter.MyDemandsOperatorViewInterface, UserEventBus> {

    public interface MyDemandsOperatorViewInterface extends LazyView {
        Widget getWidgetView();

        Button getAnswerBtn();

        Button getEditBtn();

        Button getCloseBtn();

        Button getCancelBtn();

        Button getRefreshBtn();

        Button getActivateBtn();

        Button getRejectBtn();

        CellTable<DemandDetail> getCellTable();

        SimplePanel getDetailSection();

        SingleSelectionModel<DemandDetail> getSelectionModel();

        ListDataProvider<DemandDetail> getDataProvider();
    }

    private DetailWrapperPresenter detailPresenter = null;

    public void onInvokeMyDemandsOperator() {
        eventBus.displayContent(view.getWidgetView());

     // Init DetailWrapper for this view
        if (detailPresenter  == null) {
            detailPresenter = eventBus.addHandler(DetailWrapperPresenter.class);
            detailPresenter.setType(DetailType.OPERATOR);
            detailPresenter.initDetailWrapper(view.getDetailSection());
        }

    }

    /**
     * Refresh all displays.
     */
    public void refreshDisplays() {
        view.getDataProvider().refresh();
    }

    public void onResponseDemandDetail(Widget widget) {
        view.getDetailSection().setWidget(widget);
    }

    public void onResponseClientDemands(ArrayList<DemandDetail> demands) {
        GWT.log("Demands are on the way.    demands.size = " + demands.size());

        // Add the data to the data provider, which automatically pushes it to
        // the widget.
        view.getDataProvider().getList().clear();
        view.getDataProvider().getList().addAll(demands);
        refreshDisplays();
    }
}
