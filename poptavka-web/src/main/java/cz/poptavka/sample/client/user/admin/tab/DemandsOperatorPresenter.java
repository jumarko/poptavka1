package cz.poptavka.sample.client.user.admin.tab;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.demands.widgets.DetailWrapperPresenter;
import cz.poptavka.sample.shared.domain.demand.ClientDemandDetail;
import cz.poptavka.sample.shared.domain.type.ViewType;

@Presenter(view = DemandsOperatorView.class, multiple = true)
public class DemandsOperatorPresenter extends
    LazyPresenter<DemandsOperatorPresenter.MyDemandsOperatorViewInterface, UserEventBus> {

    public interface MyDemandsOperatorViewInterface extends LazyView {
        Widget getWidgetView();

        Button getAnswerBtn();

        Button getEditBtn();

        Button getCloseBtn();

        Button getCancelBtn();

        Button getRefreshBtn();

        Button getActivateBtn();

        Button getRejectBtn();

        CellTable<ClientDemandDetail> getCellTable();

        SimplePanel getDetailSection();

        SingleSelectionModel<ClientDemandDetail> getSelectionModel();

        ListDataProvider<ClientDemandDetail> getDataProvider();
    }

    private DetailWrapperPresenter detailPresenter = null;

    public void onInvokeDemandsOperator() {
        eventBus.displayContent(view.getWidgetView());

     // Init DetailWrapper for this view
        if (detailPresenter  == null) {
            detailPresenter = eventBus.addHandler(DetailWrapperPresenter.class);
            detailPresenter.initDetailWrapper(view.getDetailSection(), ViewType.OPERATOR);
        }

        // TODO Beho - just for information, delete when implementing
        PopupPanel p = new PopupPanel(true);
        p.setWidget(new HTML("NO EVENT CALL, SO NO DATA WILL BE VISIBLE. YOu HAVE TO IMPLEMENT IT"));
        p.center();
        p.show();
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

    public void onResponseClientDemands(ArrayList<ClientDemandDetail> demands) {
        GWT.log("Demands are on the way.    demands.size = " + demands.size());

        // Add the data to the data provider, which automatically pushes it to
        // the widget.
        view.getDataProvider().getList().clear();
        view.getDataProvider().getList().addAll(demands);
        refreshDisplays();
    }

    // TODO delete, just devel tool
    public void cleanDetailWrapperPresenterForDevelopment() {
        if (detailPresenter != null) {
            eventBus.removeHandler(detailPresenter);
        }
    }
}
