/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

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
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.admin.AdminEventBus;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.adminModule.ClientDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminClientsView.class)
public class AdminClientsPresenter
        extends LazyPresenter<AdminClientsPresenter.AdminClientsInterface, AdminEventBus> {

    //history of changes
    private Map<Long, ClientDetail> dataToUpdate = new HashMap<Long, ClientDetail>();
    private Map<Long, ClientDetail> originalData = new HashMap<Long, ClientDetail>();
    //need to remember for asynchDataProvider if asking for more data
    private SearchModuleDataHolder searchDataHolder;
    //detail related
    private Boolean detailDisplayed = false;

    /**
     * Interface for widget AdminClientsView.
     */
    public interface AdminClientsInterface extends LazyView {

        // TABLE
        UniversalAsyncGrid<ClientDetail> getDataGrid();

        Column<ClientDetail, String> getIdColumn();

        Column<ClientDetail, String> getCompanyColumn();

        Column<ClientDetail, String> getFirstNameColumn();

        Column<ClientDetail, String> getLastNameColumn();

        Column<ClientDetail, String> getRatingColumn();

        // PAGET
        ListBox getPageSizeCombo();

        SimplePager getPager();

        int getPageSize();

        // BUTTONS
        Button getCommitBtn();

        Button getRollbackBtn();

        Button getRefreshBtn();

        Label getChangesLabel();

        // WIDGETS
        AdminClientInfoView getAdminClientDetail();

        Widget getWidgetView();
    }

    /**
     * Interface for widget AdminClientsInfoView.
     */
    public interface AdminClientInfoInterface { //extends LazyView {

        void setClientDetail(ClientDetail contact);

        ClientDetail getUpdatedClientDetail();

        Button getUpdateBtn();

        Button getCreateBtn();

        Widget getWidgetView();
    }

    //*************************************************************************/
    //                          INITIALIZATOIN                                */
    //*************************************************************************/
    /**
     * Initial methods for handling starting.
     * @param filter
     */
    public void onInitClients(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_CLIENTS);
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
     * @param accessRoles -- list to display
     */
    public void onDisplayAdminTabClients(List<ClientDetail> clients) {
        view.getDataGrid().updateRowData(clients);
        Storage.hideLoading();
    }

    //*************************************************************************/
    //                              DATA CHANGE                               */
    //*************************************************************************/
    /**
     * Store changes made in table data.
     */
    public void onAddClientToCommit(ClientDetail data) {
        dataToUpdate.remove(data.getId());
        dataToUpdate.put(data.getId(), data);
        if (detailDisplayed) {
            view.getAdminClientDetail().setClientDetail(data);
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
        setIdColumntUpdater();
        setCompanyColumnUpdater();
        setFirstNameColumnUpdater();
        setLastNameColumnUpdater();
        setRatingColumnUpdater();
        //
        addCommitButtonHandler();
        addRollbackButtonHandler();
        addRefreshButtonHandler();
        addDetailUpdateBtnHandler();
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
     * COLUMN UPDATER - ID
     */
    private void setIdColumntUpdater() {
        view.getIdColumn().setFieldUpdater(new FieldUpdater<ClientDetail, String>() {

            @Override
            public void update(int index, ClientDetail object, String value) {
                view.getAdminClientDetail().setClientDetail(object);
                view.getAdminClientDetail().setVisible(true);
                detailDisplayed = true;
            }
        });
    }

    /*
     * COLUMN UPDATER - RATING
     */
    private void setRatingColumnUpdater() {
        view.getRatingColumn().setFieldUpdater(new FieldUpdater<ClientDetail, String>() {

            @Override
            public void update(int index, ClientDetail object, String value) {
                if (!Integer.toString(object.getOveralRating()).equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new ClientDetail(object));
                    }
                    object.setOveralRating(Integer.valueOf(value));
                    eventBus.addClientToCommit(object);
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - LAST NAME
     */
    private void setLastNameColumnUpdater() {
        view.getLastNameColumn().setFieldUpdater(new FieldUpdater<ClientDetail, String>() {

            @Override
            public void update(int index, ClientDetail object, String value) {
                if (!object.getUserDetail().getLastName().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new ClientDetail(object));
                    }
                    object.getUserDetail().setLastName(value);
                    eventBus.addClientToCommit(object);
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - FIRST NAME
     */
    private void setFirstNameColumnUpdater() {
        view.getFirstNameColumn().setFieldUpdater(new FieldUpdater<ClientDetail, String>() {

            @Override
            public void update(int index, ClientDetail object, String value) {
                if (!object.getUserDetail().getFirstName().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new ClientDetail(object));
                    }
                    object.getUserDetail().setFirstName(value);
                    eventBus.addClientToCommit(object);
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - COMPANY
     */
    private void setCompanyColumnUpdater() {
        view.getCompanyColumn().setFieldUpdater(new FieldUpdater<ClientDetail, String>() {

            @Override
            public void update(int index, ClientDetail object, String value) {
                if (!object.getUserDetail().getCompanyName().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new ClientDetail(object));
                    }
                    object.getUserDetail().setCompanyName(value);
                    eventBus.addClientToCommit(object);
                }
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
                    Storage.showLoading(Storage.MSGS.commit());
                    for (Long idx : dataToUpdate.keySet()) {
                        eventBus.updateClient(dataToUpdate.get(idx));
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
                for (ClientDetail data : originalData.values()) {
                    idx = view.getDataGrid().getVisibleItems().indexOf(data);
                    view.getDataGrid().getVisibleItem(idx).updateWholeClient(data);
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
                    view.getDataGrid().updateRowData(new ArrayList<ClientDetail>());
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
        view.getAdminClientDetail().getUpdateBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.addClientToCommit(view.getAdminClientDetail().getUpdatedClientDetail());
            }
        });
    }
}