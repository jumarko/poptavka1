package cz.poptavka.sample.client.user.demands.tab;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
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

        void setMyDemandDetail(String name);

        ListDataProvider<DemandDetail> getDataProvider();
    }

    private ArrayList<DemandDetail> demandList = new ArrayList<DemandDetail>();

    public void onInvokeMyDemands() {

        eventBus.displayContent(view.getWidgetView());
        eventBus.getClientsDemands(1);
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

    public void onResponseDemands(ArrayList<DemandDetail> demands) {
        demandList = new ArrayList<DemandDetail>(demands);
        List<DemandDetail> demandDetails = new ArrayList<DemandDetail>();
        for (DemandDetail demand : demandList) {
            DemandDetail d = new DemandDetail();
            d.setClientId(demand.getId());
            d.setDescription(demand.getDescription());
            d.setTitle(demand.getTitle());
            d.setPrice(demand.getPrice());
            demandDetails.add(d);
        }
        // Add the data to the data provider, which automatically pushes it to
        // the widget.
        view.getDataProvider().getList().addAll(demandDetails);
        refreshDisplays();
    }

    /**
     * Refresh all displays.
     */
    public void refreshDisplays() {
        view.getDataProvider().refresh();
    }

    public void onGetDemandDetail(String name) {
        view.setMyDemandDetail(name);
    }
}
