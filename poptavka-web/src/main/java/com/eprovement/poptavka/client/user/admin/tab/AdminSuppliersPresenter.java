/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.category.CategorySelectorView;
import com.eprovement.poptavka.client.common.locality.LocalitySelectorView;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.admin.AdminEventBus;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.domain.enums.BusinessType;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminSuppliersView.class)
public class AdminSuppliersPresenter
        extends LazyPresenter<AdminSuppliersPresenter.AdminSuppliersInterface, AdminEventBus> {

    //history of changes
    private Map<Long, FullSupplierDetail> dataToUpdate = new HashMap<Long, FullSupplierDetail>();
    private Map<Long, FullSupplierDetail> originalData = new HashMap<Long, FullSupplierDetail>();
    //need to remember for asynchDataProvider if asking for more data
    private SearchModuleDataHolder searchDataHolder;
    //detail related
    private Boolean detailDisplayed = false;

    /**
     * Interface for widget AdminSuppliersView.
     */
    public interface AdminSuppliersInterface extends LazyView {

        // TABLE
        UniversalAsyncGrid<FullSupplierDetail> getDataGrid();

        Column<FullSupplierDetail, String> getSupplierIdColumn();

        Column<FullSupplierDetail, String> getSupplierNameColumn();

        Column<FullSupplierDetail, String> getSupplierTypeColumn();

        Column<FullSupplierDetail, Boolean> getCertifiedColumn();

        Column<FullSupplierDetail, String> getVerificationColumn();

        // PAGER
        SimplePager getPager();

        ListBox getPageSizeCombo();

        int getPageSize();

        // BUTTONS
        Button getCommitBtn();

        Button getRollbackBtn();

        Button getRefreshBtn();

        Label getChangesLabel();

        // WIDGETS
        AdminSupplierInfoView getAdminSupplierDetail();

        Widget getWidgetView();
    }

    //*************************************************************************/
    //                          INITIALIZATOIN                                */
    //*************************************************************************/
    /**
     * Initial methods for handling starting.
     *
     * @param filter
     */
    public void onInitSuppliers(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_SUPPLIERS);
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
     * @param accessRoles -- list to display
     */
    public void onDisplayAdminTabSuppliers(List<FullSupplierDetail> suppliers) {
        if (suppliers == null) {
            GWT.log("suppliers are null");
        }
        view.getDataGrid().getDataProvider().updateRowData(
                view.getDataGrid().getStart(), suppliers);
        eventBus.loadingHide();
    }

    //*************************************************************************/
    //                              DATA CHANGE                               */
    //*************************************************************************/
    /**
     * Store changes made in table data.
     */
    public void onAddSupplierToCommit(FullSupplierDetail data) {
        dataToUpdate.remove(data.getSupplierId());
        dataToUpdate.put(data.getSupplierId(), data);
        if (detailDisplayed) {
            view.getAdminSupplierDetail().setSupplierDetail(data);
        }
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
        setSupplierIdColumnUpdater();
        setNameColumnUpdater();
        setTypeColumnUpdater();
        setCertifiedColumnUpdater();
        setVerificationColumnUpdater();
        //
        addCommitButtonHandler();
        addRollbackButtonHandler();
        addRefreshButtonHandler();
        addDetailUpdateBtnHandler();
        view.getAdminSupplierDetail().getEditCatBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.initCategoryWidget(
                        view.getAdminSupplierDetail().getSelectorWidgetPopup(),
                        Constants.WITH_CHECK_BOXES,
                        CategoryCell.DISPLAY_COUNT_DISABLED,
                        view.getAdminSupplierDetail().getCategories(), true);
                view.getAdminSupplierDetail().getSelectorWidgetPopup().center();
            }
        });
        view.getAdminSupplierDetail().getEditLocBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.initLocalityWidget(
                        view.getAdminSupplierDetail().getSelectorWidgetPopup(),
                        Constants.WITH_CHECK_BOXES,
                        CategoryCell.DISPLAY_COUNT_DISABLED,
                        view.getAdminSupplierDetail().getLocalities(), true);
                view.getAdminSupplierDetail().getSelectorWidgetPopup().center();
            }
        });
        view.getAdminSupplierDetail().getSelectorWidgetPopup().addCloseHandler(
                new CloseHandler<PopupPanel>() {
                    @Override
                    public void onClose(CloseEvent<PopupPanel> event) {
                        if (view.getAdminSupplierDetail().getSelectorWidgetPopup()
                                .getWidget() instanceof CategorySelectorView) {
                            view.getAdminSupplierDetail().setCategories(
                                    ((CategorySelectorView) view.getAdminSupplierDetail()
                                        .getSelectorWidgetPopup().getWidget())
                                        .getCellListDataProvider().getList());
                        } else if (view.getAdminSupplierDetail().getSelectorWidgetPopup()
                                .getWidget() instanceof LocalitySelectorView) {
                            view.getAdminSupplierDetail().setLocalities(
                                    ((LocalitySelectorView) view.getAdminSupplierDetail()
                                        .getSelectorWidgetPopup().getWidget())
                                        .getCellListDataProvider().getList());
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

    /*
     * COLUMN UPDATER - VERIFICATION
     */
    private void setVerificationColumnUpdater() {
        view.getVerificationColumn().setFieldUpdater(new FieldUpdater<FullSupplierDetail, String>() {
            @Override
            public void update(int index, FullSupplierDetail object, String value) {
                for (Verification verification : Verification.values()) {
                    if (!verification.name().equals(object.getUserData().getVerification())) {
                        if (!originalData.containsKey(object.getSupplierId())) {
                            originalData.put(object.getSupplierId(), object);
                        }
                        object.getUserData().setVerification(value);
                        eventBus.addSupplierToCommit(object);
                    }
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - CERTIFIED
     */
    private void setCertifiedColumnUpdater() {
        view.getCertifiedColumn().setFieldUpdater(new FieldUpdater<FullSupplierDetail, Boolean>() {
            @Override
            public void update(int index, FullSupplierDetail object, Boolean value) {
                if (object.isCertified() != value) {
                    if (!originalData.containsKey(object.getSupplierId())) {
                        originalData.put(object.getSupplierId(), object);
                    }
                    object.setCertified(value);
                    eventBus.addSupplierToCommit(object);
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - TYPE
     */
    private void setTypeColumnUpdater() {
        view.getSupplierTypeColumn().setFieldUpdater(new FieldUpdater<FullSupplierDetail, String>() {
            @Override
            public void update(int index, FullSupplierDetail object, String value) {
                for (BusinessType businessType : BusinessType.values()) {
                    if (businessType.getValue().equals(value)) {
                        if (!object.getUserData().getBusinessType().equals(businessType.name())) {
                            if (!originalData.containsKey(object.getSupplierId())) {
                                originalData.put(object.getSupplierId(), object);
                            }
                            object.getUserData().setBusinessType(value);
                            eventBus.addSupplierToCommit(object);
                        }
                    }
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - NAME
     */
    private void setNameColumnUpdater() {
        view.getSupplierNameColumn().setFieldUpdater(new FieldUpdater<FullSupplierDetail, String>() {
            @Override
            public void update(int index, FullSupplierDetail object, String value) {
                if (!object.getUserData().getCompanyName().equals(value)) {
                    if (!originalData.containsKey(object.getSupplierId())) {
                        originalData.put(object.getSupplierId(), object);
                    }
                }
                object.getUserData().setCompanyName(value);
                eventBus.addSupplierToCommit(object);
            }
        });
    }

    /*
     * COLUMN UPDATER - SUPPLIER ID
     */
    private void setSupplierIdColumnUpdater() {
        view.getSupplierIdColumn().setFieldUpdater(new FieldUpdater<FullSupplierDetail, String>() {
            @Override
            public void update(int index, FullSupplierDetail object, String value) {
                view.getAdminSupplierDetail().setSupplierDetail(object);
            }
        });
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
                    eventBus.loadingShow(Storage.MSGS.adminCommonBtnCommit());
                    for (Long idx : dataToUpdate.keySet()) {
                        eventBus.updateSupplier(dataToUpdate.get(idx));
                    }
                    eventBus.loadingHide();
                    dataToUpdate.clear();
                    originalData.clear();
                    Window.alert("Changes commited");
                }
            }
        });
    }

    /*
     * ROLBACK
     */
    private void addRollbackButtonHandler() {
        view.getRollbackBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dataToUpdate.clear();
                view.getDataGrid().setFocus(true);
                int idx = 0;
                for (FullSupplierDetail data : originalData.values()) {
                    idx = view.getDataGrid().getVisibleItems().indexOf(data);
//                    view.getDataGrid().getVisibleItem(idx).updateWholeSupplier(data);
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
                    view.getPager().startLoading();
                    eventBus.getDataCount(view.getDataGrid(), new SearchDefinition(searchDataHolder));
                } else {
                    Window.alert("You have some uncommited data. Do commit or rollback first");
                }
            }
        });
    }

    /*
     * UPDATE (in detail widget)
     */
    private void addDetailUpdateBtnHandler() {
        view.getAdminSupplierDetail().getUpdateBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.addSupplierToCommit(view.getAdminSupplierDetail().getUpdatedSupplierDetail());
            }
        });
    }
}