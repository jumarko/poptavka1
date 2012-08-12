/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.admin.AdminEventBus;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminOurPaymentDetailsView.class)
public class AdminOurPaymentDetailsPresenter
        extends LazyPresenter<AdminOurPaymentDetailsPresenter.AdminOurPaymentDetailsInterface, AdminEventBus> {

    //history of changes
    private Map<Long, PaymentDetail> dataToUpdate = new HashMap<Long, PaymentDetail>();
    private Map<Long, PaymentDetail> originalData = new HashMap<Long, PaymentDetail>();
    //need to remember for asynchDataProvider if asking for more data
    private SearchModuleDataHolder searchDataHolder;

    public interface AdminOurPaymentDetailsInterface extends LazyView {

        Widget getWidgetView();

        UniversalAsyncGrid<PaymentDetail> getDataGrid();

        Column<PaymentDetail, String> getBankAccountColumn();

        Column<PaymentDetail, String> getBankCodeColumn();

        Column<PaymentDetail, String> getIbanColumn();

        Column<PaymentDetail, String> getSwiftCodeColumn();

        SimplePager getPager();

        int getPageSize();

        Button getCommitBtn();

        Button getRollbackBtn();

        Button getRefreshBtn();

        Label getChangesLabel();

        ListBox getPageSizeCombo();
    }

    //*************************************************************************/
    //                          INITIALIZATOIN                                */
    //*************************************************************************/
    /**
     * Initial methods for handling starting.
     *
     * @param filter
     */
    public void onInitOurPaymentDetails(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_OUR_PAYMENT_DETAILS);
        eventBus.clearSearchContent();
        searchDataHolder = filter;
        view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
    }

    //*************************************************************************/
    //                              DISPLAY                                   */
    //*************************************************************************/
    /**
     * Displays retrieved data.
     *
     * @param paymentDetailList -- list to display
     */
    public void onDisplayAdminTabOurPaymentDetails(List<PaymentDetail> paymentDetailList) {
        view.getDataGrid().updateRowData(paymentDetailList);
        Storage.hideLoading();
    }

    //*************************************************************************/
    //                              DATA CHANGE                               */
    //*************************************************************************/
    /**
     * Store changes made in table data
     */
    public void onAddOurPaymentDetailToCommit(PaymentDetail data) {
        dataToUpdate.remove(data.getId());
        dataToUpdate.put(data.getId(), data);
        view.getChangesLabel().setText(Integer.toString(dataToUpdate.size()));
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
    }

    //*************************************************************************/
    //                           ACTION HANDLERS                              */
    //*************************************************************************/
    /**
     * Register handlers for widget actions.
     */
    @Override
    public void bindView() {
        addPageChangeHandler();
        //
        setBankAccountColumnUpdater();
        setBankCodeColumnUpdater();
        setIbanColumnUpdater();
        setSwiftCodeColumnUpdater();
        //
        addCommitButtonHandler();
        addRollbackButtonHandler();
        addRefreshButtonHandler();
    }

    /*
     * COMMIT
     */
    private void addCommitButtonHandler() {
        view.getCommitBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (Window.confirm("Realy commit changes?")) {
                    view.getDataGrid().setFocus(true);
                    Storage.showLoading(Storage.MSGS.commit());
                    for (Long idx : dataToUpdate.keySet()) {
                        eventBus.updateOurPaymentDetail(dataToUpdate.get(idx));
                    }
                    Storage.hideLoading();
                    dataToUpdate.clear();
                    originalData.clear();
                    Window.alert("Changes commited");
                }
            }
        });
    }

    /*
     * ROLLBACK
     */
    private void addRollbackButtonHandler() {
        view.getRollbackBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dataToUpdate.clear();
                view.getDataGrid().setFocus(true);
                int idx = 0;
                for (PaymentDetail data : originalData.values()) {
                    idx = view.getDataGrid().getVisibleItems().indexOf(data);
                    view.getDataGrid().getVisibleItem(idx).updateWholeOurPaymentDetail(data);
                }
                view.getDataGrid().flush();
                view.getDataGrid().redraw();
                Window.alert(view.getChangesLabel().getText() + " changes rolledback.");
                view.getChangesLabel().setText("0");
                originalData.clear();
            }
        });
    }

    /*
     * REFRESH
     */
    private void addRefreshButtonHandler() {
        view.getRefreshBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (dataToUpdate.isEmpty()) {
                    view.getDataGrid().updateRowData(new ArrayList<PaymentDetail>());
                    eventBus.getDataCount(view.getDataGrid(), new SearchDefinition(searchDataHolder));
                } else {
                    Window.alert("You have some uncommited data. Do commit or rollback first");
                }
            }
        });
    }

    /*
     * TABLE PAGE CHANGER
     */
    private void addPageChangeHandler() {
        view.getPageSizeCombo().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                int page = view.getPager().getPageStart() / view.getPageSize();
                view.getPager().setPageStart(page * view.getPageSize());
                view.getPager().setPageSize(view.getPageSize());
            }
        });
    }

    private void setSwiftCodeColumnUpdater() {
        view.getSwiftCodeColumn().setFieldUpdater(new FieldUpdater<PaymentDetail, String>() {

            @Override
            public void update(int index, PaymentDetail object, String value) {
                if (!object.getSwiftCode().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new PaymentDetail(object));
                    }
                    object.setSwiftCode(value);
                    eventBus.addOurPaymentDetailToCommit(object);
                }
            }
        });
    }

    private void setIbanColumnUpdater() {
        view.getIbanColumn().setFieldUpdater(new FieldUpdater<PaymentDetail, String>() {

            @Override
            public void update(int index, PaymentDetail object, String value) {
                if (!object.getIban().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new PaymentDetail(object));
                    }
                    object.setIban(value);
                    eventBus.addOurPaymentDetailToCommit(object);
                }
            }
        });
    }

    private void setBankCodeColumnUpdater() {
        view.getBankCodeColumn().setFieldUpdater(new FieldUpdater<PaymentDetail, String>() {

            @Override
            public void update(int index, PaymentDetail object, String value) {
                if (!object.getBankCode().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new PaymentDetail(object));
                    }
                    object.setBankCode(value);
                    eventBus.addOurPaymentDetailToCommit(object);
                }
            }
        });
    }

    private void setBankAccountColumnUpdater() {
        view.getBankAccountColumn().setFieldUpdater(new FieldUpdater<PaymentDetail, String>() {

            @Override
            public void update(int index, PaymentDetail object, String value) {
                if (!object.getBankAccount().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new PaymentDetail(object));
                    }
                    object.setBankAccount(value);
                    eventBus.addOurPaymentDetailToCommit(object);
                }
            }
        });
    }
}
