package cz.poptavka.sample.client.user.admin.tab;

import java.util.Date;
import java.util.List;

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
import cz.poptavka.sample.shared.domain.demand.DemandDetail;
import cz.poptavka.sample.shared.domain.offer.FullOfferDetail;
import cz.poptavka.sample.shared.domain.type.OfferStateType;

@Presenter(view = AdminOffersView.class)
public class AdminOffersPresenter
        extends LazyPresenter<AdminOffersPresenter.AdminOffersInterface, UserEventBus>
        implements HasValueChangeHandlers<String> {
    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public interface AdminOffersInterface extends LazyView {
        Widget getWidgetView();

//        SimplePager getPager();
        CellTable<FullOfferDetail> getCellTable();

        ListDataProvider<FullOfferDetail> getDataProvider();

        Column<FullOfferDetail, String> getSupplierIdColumn();

        Column<FullOfferDetail, String> getOfferStatusColumn();

        Column<FullOfferDetail, Date> getOfferFinishColumn();

        OfferStateType[] getOfferStatuses();

        SingleSelectionModel<FullOfferDetail> getSelectionModel();

        SimplePanel getAdminOfferDetail();
    }

//    private ArrayList<Demand> demands = new ArrayList<Demand>();
    public void onInvokeAdminOffers() {
        // TODO ivlcek - ktoru event mam volat skor? Je v tom nejaky rozdiel?
//        eventBus.getAllDemands();
        //eventBus.displayContent(view.getWidgetView());
        //eventBus.getAllDemands();
    }

    public void onSetAllOffer(List<FullOfferDetail> offerDetails) {
        // Add the data to the data provider, which automatically pushes it to the widget.
        // TODO ivlcek - try to set list in for cycle. Maybe it depends on how you populate
        // data into ListProvider. DONE - it realy depends on how you set data to list provider
//        view.getDataProvider().setList(demandDetails);

        List<FullOfferDetail> list = view.getDataProvider().getList();
        for (FullOfferDetail d : offerDetails) {
            list.add(d);
        }

        // TODO ivlcek - try to remove refreshDispalys
        refreshDisplays();
    }

    public void onRefreshUpdatedDemand(DemandDetail demand) {
//        view.getCellTable().setSize("10%", "10%");
    }

    public void onResponseAdminOfferDetail(Widget widget) {
        view.getAdminOfferDetail().setWidget(widget);
    }

    /**
     * Refresh all displays.
     */
    public void refreshDisplays() {
        view.getDataProvider().refresh();
    }

    @Override
    public void bindView() {
        view.getSupplierIdColumn().setFieldUpdater(new FieldUpdater<FullOfferDetail, String>() {
            @Override
            public void update(int index, FullOfferDetail object, String value) {
                object.setSupplierId(Long.valueOf(value));
                eventBus.updateOffer(object);
                refreshDisplays();
            }
        });
        view.getOfferStatusColumn().setFieldUpdater(new FieldUpdater<FullOfferDetail, String>() {
            @Override
            public void update(int index, FullOfferDetail object, String value) {
                for (OfferStateType offerStatusDetail : view.getOfferStatuses()) {
                    if (offerStatusDetail.name().equals(value)) {
                        object.setState(offerStatusDetail.name());
                        eventBus.updateOffer(object);
                    }
                }
                refreshDisplays();
            }
        });
        view.getOfferFinishColumn().setFieldUpdater(new FieldUpdater<FullOfferDetail, Date>() {
            @Override
            public void update(int index, FullOfferDetail object, Date value) {
                object.setFinishDate(value);
                eventBus.updateOffer(object);
                refreshDisplays();
            }
        });
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
//                contactForm.setContact(selectionModel.getSelectedObject());
//                eventBus.displayContent(view.getWidgetView());
//                eventBus.getAllDemands();
                eventBus.showAdminOfferDetail(view.getSelectionModel().getSelectedObject());

            }
        });
    }
}
