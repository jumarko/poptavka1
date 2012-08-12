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
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.type.ClientDemandType;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ivan.vlcek, edited by Martin Slavkovsky
 */
@Presenter(view = AdminDemandsView.class)
public class AdminDemandsPresenter
        extends LazyPresenter<AdminDemandsPresenter.AdminDemandsInterface, AdminEventBus> {

    //history of changes
    private Map<Long, FullDemandDetail> dataToUpdate = new HashMap<Long, FullDemandDetail>();
    private Map<Long, FullDemandDetail> originalData = new HashMap<Long, FullDemandDetail>();
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
     * @param accessRoles -- list to display
     */
    public void onDisplayAdminTabDemands(List<FullDemandDetail> demands) {
        view.getDataGrid().updateRowData(demands);
        Storage.hideLoading();
    }

    //*************************************************************************/
    //                              DATA CHANGE                               */
    //*************************************************************************/
    /**
     * Store changes made in table data.
     */
    public void onAddDemandToCommit(FullDemandDetail data) {
        dataToUpdate.remove(data.getDemandId());
        dataToUpdate.put(data.getDemandId(), data);
        if (detailDisplayed) {
            view.getAdminDemandDetail().setDemandDetail(data);
        }
        view.getChangesLabel().setText(Integer.toString(dataToUpdate.size()));
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
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
                    if (!originalData.containsKey(object.getDemandId())) {
                        originalData.put(object.getDemandId(), new FullDemandDetail(object));
                    }
                    object.setEndDate(value);
                    eventBus.addDemandToCommit(object);
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
                if (!object.getValidToDate().equals(value)) {
                    if (!originalData.containsKey(object.getDemandId())) {
                        originalData.put(object.getDemandId(), new FullDemandDetail(object));
                    }
                    object.setValidToDate(value);
                    eventBus.addDemandToCommit(object);
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
                        if (!object.getDemandStatus().equals(demandStatusType.name())) {
                            if (!originalData.containsKey(object.getDemandId())) {
                                originalData.put(object.getDemandId(), new FullDemandDetail(object));
                            }
                            object.setDemandStatus(DemandStatus.valueOf(demandStatusType.name()));
                            eventBus.addDemandToCommit(object);
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
                            if (!originalData.containsKey(object.getDemandId())) {
                                originalData.put(object.getDemandId(), new FullDemandDetail(object));
                            }
                            object.setDemandType(clientDemandType.name());
                            eventBus.addDemandToCommit(object);
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
                    if (!originalData.containsKey(object.getDemandId())) {
                        originalData.put(object.getDemandId(), new FullDemandDetail(object));
                    }
                    object.setTitle(value);
                    eventBus.addDemandToCommit(object);
                }
            }
        });
    }

    /**
     * COLUMN UPDATER - CID.
     */
    private void setCidColumnUpdater() {
        view.getCidColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {

            @Override
            public void update(int index, FullDemandDetail object, String value) {
                view.getAdminDemandDetail().setDemandDetail(object);
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
                view.getAdminDemandDetail().setDemandDetail(object);
            }
        });
    }

    /**
     * COMMIT.
     */
    private void addCommitButtonHandler() {
        view.getCommitBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (Window.confirm("Realy commit changes?")) {
                    view.getDataGrid().setFocus(true);
                    Storage.showLoading(Storage.MSGS.commit());
                    for (Long idx : dataToUpdate.keySet()) {
                        eventBus.updateDemand(dataToUpdate.get(idx));
                    }
                    Storage.hideLoading();
                    dataToUpdate.clear();
                    originalData.clear();
                    Window.alert("Changes commited");
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
                dataToUpdate.clear();
                view.getDataGrid().setFocus(true);
                int idx = 0;
                for (FullDemandDetail data : originalData.values()) {
                    idx = view.getDataGrid().getVisibleItems().indexOf(data);
                    view.getDataGrid().getVisibleItem(idx).updateWholeDemand(data);
                }
                view.getDataGrid().flush();
                view.getDataGrid().redraw();
                Window.alert(view.getChangesLabel().getText() + " changes rolledback.");
                view.getChangesLabel().setText("0");
                originalData.clear();
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
                if (dataToUpdate.isEmpty()) {
                    view.getDataGrid().updateRowData(new ArrayList<FullDemandDetail>());
                    eventBus.getDataCount(view.getDataGrid(), new SearchDefinition(searchDataHolder));
                } else {
                    Window.alert("You have some uncommited data. Do commit or rollback first");
                }
            }
        });
    }
}
