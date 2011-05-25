package cz.poptavka.sample.client.user.demands.tab;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.DemandDetail;

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

        void setMyDemandOperatorDetail(String name);

        SingleSelectionModel<DemandDetail> getSelectionModel();
    }

    public void onInvokeMyDemandsOperator() {
        eventBus.displayContent(view.getWidgetView());
    }
}
