package cz.poptavka.sample.client.user.demands.tab;

import java.util.ArrayList;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.DemandDetail;

@Presenter(view = MyDemandsView.class)
public class MyDemandsPresenter extends
        LazyPresenter<MyDemandsPresenter.MyDemandsInterface, UserEventBus> {

    public interface MyDemandsInterface extends LazyView {
        Widget getWidgetView();

        Button getAnswerBtn();

        Button getEditBtn();

        Button getCloseBtn();

        Button getCancelBtn();

        CellTable<DemandDetail> getCellTable();

        void setMyDemandDetail(String name);

        SingleSelectionModel<DemandDetail> getSelectionModel();
    }

    public void onInvokeMyDemands() {
        eventBus.displayContent(view.getWidgetView());
    }

    public void bindView() {
        view.getCellTable().getSelectionModel()
                .addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                    public void onSelectionChange(SelectionChangeEvent event) {
                        DemandDetail selected = view.getSelectionModel()
                                .getSelectedObject();
                        if (selected != null) {
                            eventBus.getDemandDetail(selected.getTitle());
                        }
                    }
                });

    }

    public void onGetDemandDetail(String name) {
        view.setMyDemandDetail(name);
    }

    public void onResponseDemands(ArrayList<DemandDetail> demands) {
        // TODO
    }
}
