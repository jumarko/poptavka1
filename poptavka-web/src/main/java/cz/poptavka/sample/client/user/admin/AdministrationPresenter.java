/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.DemandStatusDetail;
import cz.poptavka.sample.shared.domain.DemandTypeDetail;

import java.util.Date;
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

        Column<DemandDetail, String> getClientIdColumn();

        Column<DemandDetail, String> getDemandTypeColumn();

        Column<DemandDetail, String> getDemandStatusColumn();

        Column<DemandDetail, Date> getDemandExpirationColumn();

        Column<DemandDetail, Date> getDemandEndColumn();

        DemandTypeDetail[] getDemandTypes();

        DemandStatusDetail[] getDemandStatuses();

        SingleSelectionModel<DemandDetail> getSelectionModel();

        SimplePanel getAdminDemandDetail();
    }

//    private ArrayList<Demand> demands = new ArrayList<Demand>();
    public void onInvokeAdministration() {
        // TODO ivlcek - ktoru event mam volat skor? Je v tom nejaky rozdiel?
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

        // TODO ivlcek - try to remove refreshDispalys
        refreshDisplays();
    }

    public void onRefreshUpdatedDemand(DemandDetail demand) {
//        view.getCellTable().setSize("10%", "10%");
    }

    public void onResponseAdminDemandDetail(Widget widget) {
        view.getAdminDemandDetail().setWidget(widget);
    }

    /**
     * Refresh all displays.
     */
    public void refreshDisplays() {
        view.getDataProvider().refresh();
    }

    @Override
    public void bindView() {
        view.getClientIdColumn().setFieldUpdater(new FieldUpdater<DemandDetail, String>() {
            @Override
            public void update(int index, DemandDetail object, String value) {
                object.setClientId(Long.valueOf(value));
                eventBus.updateDemand(object);
                refreshDisplays();
            }
        });
        view.getDemandTypeColumn().setFieldUpdater(new FieldUpdater<DemandDetail, String>() {
            @Override
            public void update(int index, DemandDetail object, String value) {
                for (DemandTypeDetail demandTypeDetail : view.getDemandTypes()) {
                    if (demandTypeDetail.name().equals(value)) {
                        object.setDemandType(demandTypeDetail.name());
                        eventBus.updateDemand(object);
                    }
                }
                refreshDisplays();
            }
        });
        view.getDemandStatusColumn().setFieldUpdater(new FieldUpdater<DemandDetail, String>() {
            @Override
            public void update(int index, DemandDetail object, String value) {
                for (DemandStatusDetail demandStatusDetail : view.getDemandStatuses()) {
                    if (demandStatusDetail.name().equals(value)) {
                        object.setDemandType(demandStatusDetail.name());
                        eventBus.updateDemand(object);
                    }
                }
                refreshDisplays();
            }
        });
        view.getDemandExpirationColumn().setFieldUpdater(new FieldUpdater<DemandDetail, Date>() {
            @Override
            public void update(int index, DemandDetail object, Date value) {
                object.setExpireDate(value);
                eventBus.updateDemand(object);
                refreshDisplays();
            }
        });
        view.getDemandEndColumn().setFieldUpdater(new FieldUpdater<DemandDetail, Date>() {
            @Override
            public void update(int index, DemandDetail object, Date value) {
                object.setEndDate(value);
                eventBus.updateDemand(object);
                refreshDisplays();
            }
        });
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
//                contactForm.setContact(selectionModel.getSelectedObject());
//                eventBus.displayContent(view.getWidgetView());
//                eventBus.getAllDemands();
                eventBus.showAdminDemandDetail(view.getSelectionModel().getSelectedObject());

            }
        });
    }
}
