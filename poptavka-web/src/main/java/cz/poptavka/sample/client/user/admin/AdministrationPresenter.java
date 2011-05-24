/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.DemandDetail;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author ivan.vlcek
 */
@Presenter(view = AdministrationView.class)
public class AdministrationPresenter
        extends LazyPresenter<AdministrationPresenter.AdministrationInterface, UserEventBus> {

    public interface AdministrationInterface extends LazyView {

        Widget getWidgetView();

//        SimplePager getPager();
        CellTable<DemandDetail> getCellTable();

        ListDataProvider<DemandDetail> getDataProvider();
    }

    private ArrayList<Demand> demands = new ArrayList<Demand>();

    public void onInvokeAdministration() {
        eventBus.displayContent(view.getWidgetView());
        eventBus.getAllDemands();
    }

    public void onSetAllDemands(List<Demand> allDemands) {
        demands = new ArrayList<Demand>(allDemands);
        List<DemandDetail> demandDetails = new ArrayList<DemandDetail>();
        for (Demand demand : demands) {
            DemandDetail d = new DemandDetail();
            d.setClientId(demand.getId());
            d.setDemandType(demand.getType().getCode());
            d.setDescription(demand.getDescription());
            d.setTitle(demand.getTitle());
            demandDetails.add(d);
        }
        // Add the data to the data provider, which automatically pushes it to the widget.
        view.getDataProvider().getList().addAll(demandDetails);
        refreshDisplays();
//        view.getCellTable().setRowData(demandDetails);
    }

    /**
     * Refresh all displays.
     */
    public void refreshDisplays() {
        view.getDataProvider().refresh();
    }
}
