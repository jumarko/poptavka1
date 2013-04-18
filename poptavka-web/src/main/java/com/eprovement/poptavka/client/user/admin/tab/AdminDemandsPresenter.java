/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.common.ChangeMonitor;
import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.category.CategorySelectorView;
import com.eprovement.poptavka.client.common.locality.LocalitySelectorView;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.admin.AdminEventBus;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.type.ClientDemandType;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
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
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ivan.vlcek, edited by Martin Slavkovsky
 */
@Presenter(view = AdminDemandsView.class)
public class AdminDemandsPresenter
        extends LazyPresenter<AdminDemandsPresenter.AdminDemandsInterface, AdminEventBus> {

    private int changeCount = 0;
    //history of changes
    private HashMap<Long, ArrayList<ChangeDetail>> updatedFields = new HashMap<Long, ArrayList<ChangeDetail>>();
    //need to remember for asynchDataProvider if asking for more data
    private SearchModuleDataHolder searchDataHolder;
    //detail related
    private Boolean detailDisplayed = false;

    /**
     * Interface for widget AdminMessagesView.
     */
    public interface AdminDemandsInterface extends LazyView {

        //TABLE
        UniversalAsyncGrid<FullDemandDetail> getDataGrid();

        Column<FullDemandDetail, String> getIdColumn();

        Column<FullDemandDetail, String> getCidColumn();

        Column<FullDemandDetail, String> getDemandTitleColumn();

        Column<FullDemandDetail, String> getDemandTypeColumn();

        Column<FullDemandDetail, String> getDemandStatusColumn();

        Column<FullDemandDetail, Date> getDemandExpirationColumn();

        Column<FullDemandDetail, Date> getDemandEndColumn();

        // PAGER
        SimplePager getPager();

        ListBox getPageSizeCombo();

        int getPageSize();

        // BUTTONS
        Button getCommitBtn();

        Button getRollbackBtn();

        Button getRefreshBtn();

        Label getChangesLabel();

        // DETAIL
        AdminDemandInfoView getAdminDemandDetail();

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
    public void onInitDemands(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_DEMANDS);
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
    public void onDisplayAdminTabDemands(List<FullDemandDetail> demands) {
        view.getDataGrid().getDataProvider().updateRowData(
                view.getDataGrid().getStart(), demands);
        eventBus.loadingHide();
    }

    //*************************************************************************/
    //                          ACTION HANDLERS                               */
    //*************************************************************************/
    /**
     * Register handlers for widget actions.
     */
    @Override
    public void bindView() {
        addPageChangeHandler();
        //
        setIdColumnUpdater();
        setCidColumnUpdater();
        setDemandTitleColumnUpdater();
        setDemandTypeColumnUpdater();
        setDemandStatusColumnUpdater();
        setDemandExpirationColumnUpdater();
        setDemandEndColumnUpdater();
        //
        addCommitButtonHandler();
        addRollbackButtonHandler();
        addRefreshButtonHandler();
        view.getAdminDemandDetail().getEditCatBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.initCategoryWidget(
                        view.getAdminDemandDetail().getSelectorWidgetPopup(),
                        Constants.WITH_CHECK_BOXES,
                        CategoryCell.DISPLAY_COUNT_DISABLED,
                        view.getAdminDemandDetail().getCategories(), true);
                view.getAdminDemandDetail().getSelectorWidgetPopup().center();
            }
        });
        view.getAdminDemandDetail().getEditLocBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.initLocalityWidget(
                        view.getAdminDemandDetail().getSelectorWidgetPopup(),
                        Constants.WITH_CHECK_BOXES,
                        CategoryCell.DISPLAY_COUNT_DISABLED,
                        view.getAdminDemandDetail().getLocalities());
                view.getAdminDemandDetail().getSelectorWidgetPopup().center();
            }
        });
        view.getAdminDemandDetail().getSelectorWidgetPopup().addCloseHandler(
                new CloseHandler<PopupPanel>() {
                    @Override
                    public void onClose(CloseEvent<PopupPanel> event) {
                        if (view.getAdminDemandDetail().getSelectorWidgetPopup()
                                .getWidget() instanceof CategorySelectorView) {
                            view.getAdminDemandDetail().setCategories(
                                    ((CategorySelectorView) view.getAdminDemandDetail()
                                        .getSelectorWidgetPopup().getWidget())
                                        .getCellListDataProvider().getList());
                        } else if (view.getAdminDemandDetail().getSelectorWidgetPopup()
                                .getWidget() instanceof LocalitySelectorView) {
                            view.getAdminDemandDetail().setLocalities(
                                    ((LocalitySelectorView) view.getAdminDemandDetail()
                                        .getSelectorWidgetPopup().getWidget())
                                        .getCellListDataProvider().getList());
                        }
                    }
                });
        view.getAdminDemandDetail().setChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                addChange((ChangeMonitor) event.getSource());
            }
        });
        view.getDataGrid().setRowStyles(new RowStyles<FullDemandDetail>() {
            @Override
            public String getStyleNames(FullDemandDetail rowObject, int rowIndex) {
//                DataGrid.Resources resource = GWT.create(AsyncDataGrid.class);
                //alebo nejaky novy attribute v FullDemandDetail - nieco ako read
                if (updatedFields.containsKey(rowObject.getDemandId())) {
                    return Storage.RSCS.common().changed();
                }
                return Storage.RSCS.common().emptyStyle();
            }
        });
    }

    /**
     * TABLE PAGE CHANGER.
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

    /**
     * COLUMN UPDATER - END COLUMN.
     */
    private void setDemandEndColumnUpdater() {
        view.getDemandEndColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, Date>() {
            @Override
            public void update(int index, FullDemandDetail object, Date value) {
                if (!object.getEndDate().equals(value)) {
                    manageUpdatedFieldsOfColumns(FullDemandDetail.DemandField.END_DATE,
                            object.getDemandId(), object.getEndDate(), value);
                    object.setEndDate(value);
                }
            }
        });
    }

    /**
     * COLUMN UPDATER - EXPIRATION.
     */
    private void setDemandExpirationColumnUpdater() {
        view.getDemandExpirationColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, Date>() {
            @Override
            public void update(int index, FullDemandDetail object, Date value) {
                if (!object.getValidTo().equals(value)) {
                    manageUpdatedFieldsOfColumns(FullDemandDetail.DemandField.VALID_TO_DATE,
                            object.getDemandId(), object.getValidTo(), value);
                    object.setValidTo(value);
                }
            }
        });
    }

    /**
     * COLUMN UPDATER - STATUS.
     */
    private void setDemandStatusColumnUpdater() {
        view.getDemandStatusColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {
            @Override
            public void update(int index, FullDemandDetail object, String value) {
                for (DemandStatus demandStatusType : DemandStatus.values()) {
                    if (demandStatusType.getValue().equals(value)) {
                        if (!object.getDemandStatus().equals(demandStatusType)) {
                            manageUpdatedFieldsOfColumns(FullDemandDetail.DemandField.DEMAND_STATUS,
                                    object.getDemandId(), object.getDemandStatus(), value);
                            object.setDemandStatus(DemandStatus.valueOf(demandStatusType.name()));
                        }
                    }
                }
            }
        });
    }

    /**
     * COLUMN UPDATER - TYPE.
     */
    private void setDemandTypeColumnUpdater() {
        view.getDemandTypeColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {
            @Override
            public void update(int index, FullDemandDetail object, String value) {
                for (ClientDemandType clientDemandType : ClientDemandType.values()) {
                    if (clientDemandType.getValue().equals(value)) {
                        if (!object.getDemandType().equals(clientDemandType.name())) {
                            manageUpdatedFieldsOfColumns(FullDemandDetail.DemandField.DEMAND_TYPE,
                                    object.getDemandId(), object.getDemandType(), value);
                            object.setDemandType(clientDemandType.name());
                        }
                    }
                }
            }
        });
    }

    /**
     * COLUMN UPDATER - TITLE.
     */
    private void setDemandTitleColumnUpdater() {
        view.getDemandTitleColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {
            @Override
            public void update(int index, FullDemandDetail object, String value) {
                if (!object.getTitle().equals(value)) {
                    manageUpdatedFieldsOfColumns(FullDemandDetail.DemandField.TITLE,
                            object.getDemandId(), object.getTitle(), value);
                    object.setTitle(value);
                }
            }
        });
    }

    private void manageUpdatedFieldsOfColumns(FullDemandDetail.DemandField demandField,
            long demandId, Object originalValue, Object value) {
        if (!updatedFields.containsKey(demandId)) {
            ChangeDetail changeDetail = new ChangeDetail(demandField.getValue());
            changeDetail.setOriginalValue(originalValue);
            changeDetail.setValue(value);
            if (updatedFields.containsKey(demandId)) {
                updatedFields.get(demandId).add(changeDetail);
                changeCount++;
            } else {
                ArrayList<ChangeDetail> changes = new ArrayList<ChangeDetail>();
                changes.add(changeDetail);
                updatedFields.put(demandId, changes);
                changeCount++;
            }
        }
        view.getDataGrid().redraw();
        view.getChangesLabel().setText(Integer.toString(changeCount));
    }

    /**
     * COLUMN UPDATER - CID.
     */
    private void setCidColumnUpdater() {
        view.getCidColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {
            @Override
            public void update(int index, FullDemandDetail object, String value) {
//                view.getAdminDemandDetail().setDemandDetail(object);
                displayDemandDetail(object);
            }
        });
    }

    /**
     * COLUMN UPDATER - ID.
     */
    private void setIdColumnUpdater() {
        view.getIdColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {
            @Override
            public void update(int index, FullDemandDetail object, String value) {
//                view.getAdminDemandDetail().setDemandDetail(object);
                displayDemandDetail(object);
            }
        });
    }

    private void displayDemandDetail(FullDemandDetail demand) {
        view.getAdminDemandDetail().setDemandDetail(demand);
        if (updatedFields.containsKey(demand.getDemandId())) {
            view.getAdminDemandDetail().setFieldChanges(updatedFields.get(demand.getDemandId()));
        }
    }

    /**
     * COMMIT.
     */
    private void addCommitButtonHandler() {
        view.getCommitBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (Window.confirm("Realy commit changes?")) {
                    eventBus.loadingShow("Commiting...");
                    eventBus.updateDemands(updatedFields);
                    updatedFields.clear();
                    view.getAdminDemandDetail().resetChangeMonitors();
                    changeCount = 0;
                    view.getChangesLabel().setText("0");
                    view.getDataGrid().redraw();
                }
            }
        });
    }

    /**
     * ROLLBACK.
     */
    private void addRollbackButtonHandler() {
        view.getRollbackBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (Long demandId : updatedFields.keySet()) {
                    for (ChangeDetail change : updatedFields.get(demandId)) {
                        change.revert();
                    }
                    updatedFields.clear();
                    view.getAdminDemandDetail().revertChangeMonitors();
                    changeCount = 0;
                    view.getChangesLabel().setText("0");
                    view.getDataGrid().redraw();
                }
            }
        });
    }

    /**
     * REFRESH.
     */
    private void addRefreshButtonHandler() {
        view.getRefreshBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (updatedFields.isEmpty()) {
                    view.getPager().startLoading();
//                    eventBus.getDataCount(view.getDataGrid(), new SearchDefinition(searchDataHolder));
                } else {
                    Window.alert("You have some uncommited data. Do commit or rollback first");
                }
            }
        });
    }

//    public ArrayList<FullDemandDetail> getDataToUpdate(
//            Map<FullDemandDetail, List<DemandChangeMonitor>> updatedFields) {
//        ArrayList<FullDemandDetail> demands = new ArrayList<FullDemandDetail>();
//        for (FullDemandDetail demand : updatedFields.keySet()) {
//            for (DemandChangeMonitor change : updatedFields.get(demand)) {
//                demand = demand.updateDemandFieldValue(change.getDemandField(), change.getValue());
//            }
//            demands.add(demand);
//        }
//        return demands;
//    }
    public void onResponseUpdateDemands(Boolean result) {
        eventBus.loadingHide();
        if (result) {
            Window.alert("Changes commited");
        } else {
            Window.alert("Error while commiting");
        }
    }

    private void addChange(ChangeMonitor source) {
        source.getChangeDetail().setValue(source.getValue());
        FullDemandDetail demand = view.getAdminDemandDetail().getDemandDetail();
        ArrayList<ChangeDetail> changes = (ArrayList<ChangeDetail>) updatedFields.get(demand.getDemandId());
        if (source.isModified()) {
            if (updatedFields.containsKey(demand.getDemandId())) {
                //if contains already - remove before adding new
                if (changes.contains(source.getChangeDetail())) {
                    changes.remove(source.getChangeDetail());
                    changeCount--;
                }
                changes.add(source.getChangeDetail());
            } else {
                ArrayList<ChangeDetail> changesNew = new ArrayList<ChangeDetail>();
                changesNew.add(source.getChangeDetailCopy());
                updatedFields.put(demand.getDemandId(), changesNew);
            }
            changeCount++;
        } else {
            if (updatedFields.containsKey(demand.getDemandId())) {
                changes.remove(source.getChangeDetail());
                if (changes.isEmpty()) {
                    updatedFields.remove(demand.getDemandId());
                    changeCount--;
                }
            }
        }
        view.getDataGrid().redraw();
        view.getChangesLabel().setText(Integer.toString(changeCount));
    }
}
