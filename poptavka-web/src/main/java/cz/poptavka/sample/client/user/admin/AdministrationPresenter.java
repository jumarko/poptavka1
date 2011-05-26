/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.DemandDetail;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
@Presenter(view = AdministrationView.class)
public class AdministrationPresenter
        extends LazyPresenter<AdministrationPresenter.AdministrationInterface, UserEventBus>
        implements HasValueChangeHandlers<String> {
    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public interface AdministrationInterface extends LazyView {
        Widget getWidgetView();

//        SimplePager getPager();
        CellTable<DemandDetail> getCellTable();

        ListDataProvider<DemandDetail> getDataProvider();

        void sortTitle();
    }

//    private ArrayList<Demand> demands = new ArrayList<Demand>();
    public void onInvokeAdministration() {
//        eventBus.getAllDemands();
        eventBus.displayContent(view.getWidgetView());
        eventBus.getAllDemands();
    }

    public void onSetAllDemands(List<DemandDetail> demandDetails) {
        // Add the data to the data provider, which automatically pushes it to the widget.
        // TODO ivlcek - try to set list in for cycle. Maybe it depends on how you populate
        // data into ListProvider. DONE - it realy depends on how you set data to list provider
//        view.getDataProvider().setList(demandDetails);

        List<DemandDetail> list = view.getDataProvider().getList();
        for (DemandDetail d : demandDetails) {
            list.add(d);
        }

        view.sortTitle();
        // TODO ivlcek - try to remove refreshDispalys
        refreshDisplays();
    }

    public void onRefreshUpdatedDemand(DemandDetail demand) {
//        view.getCellTable().setSize("10%", "10%");
    }

    /**
     * Refresh all displays.
     */
    public void refreshDisplays() {
        view.getDataProvider().refresh();
    }

    @Override
    public void bindView() {
//        view.getFirstNameColumn().setFieldUpdater(new FieldUpdater<DemandDetail, String>() {
//
//            @Override
//            public void update(int index, DemandDetail object, String value) {
//                // Called when the user changes the value.
//                object.setTitle(value);
//                eventBus.updateDemand(object);
//                view.getDataProvider().refresh();
//            }
//        });
    }
}
