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

        SingleSelectionModel<DemandDetail> getSelectionModel();

        SimplePanel getDetailSection();

        ListDataProvider<DemandDetail> getDataProvider();
    }


    public void onInvokeMyDemands() {
        GWT.log("display DEMANDS WIDGET");
        eventBus.displayContent(view.getWidgetView());
        GWT.log("Demands are on the way - getDemands!");
        eventBus.requestClientDemands();
    }

    public void bindView() {
        // view.getCellTable().getSelectionModel()
        // .addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
        // public void onSelectionChange(SelectionChangeEvent event) {
        // DemandDetail selected = view.getSelectionModel()
        // .getSelectedObject();
        // if (selected != null) {
        // eventBus.getDemandDetail(selected.getTitle());
        // }
        // }
        // });
    }

    public void onResponseClientDemands(ArrayList<DemandDetail> demands) {
        GWT.log("Demands are on the way.    demands.size = " + demands.size());

        // Add the data to the data provider, which automatically pushes it to
        // the widget.
        view.getDataProvider().getList().clear();
        view.getDataProvider().getList().addAll(demands);
        refreshDisplays();
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
}
