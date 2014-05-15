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
import com.eprovement.poptavka.shared.domain.adminModule.ActivationEmailDetail;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminEmailActivationsView.class)
public class AdminEmailActivationsPresenter
        extends LazyPresenter<AdminEmailActivationsPresenter.AdminEmailActivationsInterface, AdminEventBus> {

    //history of changes
    private Map<Long, ActivationEmailDetail> dataToUpdate = new HashMap<Long, ActivationEmailDetail>();
    private Map<Long, ActivationEmailDetail> originalData = new HashMap<Long, ActivationEmailDetail>();
    //need to remember for asynchDataProvider if asking for more data
    private SearchModuleDataHolder searchDataHolder;

    /**
     * Interface for widget AdminEmailActivationsView.
     */
    public interface AdminEmailActivationsInterface extends LazyView {

        // TABLE
        UniversalAsyncGrid<ActivationEmailDetail> getDataGrid();

        Column<ActivationEmailDetail, String> getActivationCodeColumn();

        Column<ActivationEmailDetail, Date> getTimeoutColumn();

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
        Widget getWidgetView();
    }

    //*************************************************************************/
    //                          INITIALIZATOIN                                */
    //*************************************************************************/
    /**
     * Initial methods for handling starting.
     * @param filter
     */
    public void onInitEmailsActivation(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_EMAILS_ACTIVATION);
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
    public void onDisplayAdminTabEmailsActivation(List<ActivationEmailDetail> list) {
        view.getDataGrid().getDataProvider().updateRowData(
                view.getDataGrid().getStart(), list);
        eventBus.loadingHide();
    }

    //*************************************************************************/
    //                              DATA CHANGE                               */
    //*************************************************************************/
    /**
     * Store changes made in table data.
     */
    public void onAddEmailActivationToCommit(ActivationEmailDetail data) {
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
        addPageChangedHandler();
        //
        setActivationCodeColumnUpdater();
        setTimeoutColumnUpdater();
        //
        addCommitButtonHandler();
        addRollbackButtonHandler();
        addRefreshButtonHandler();
    }

    /**
     * TABLE PAGE CHANGER.
     */
    private void addPageChangedHandler() {
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
     * COLUMN UPDATER - ACTIVATION CODE.
     */
    private void setActivationCodeColumnUpdater() {
        view.getActivationCodeColumn().setFieldUpdater(new FieldUpdater<ActivationEmailDetail, String>() {

            @Override
            public void update(int index, ActivationEmailDetail object, String value) {
                if (!object.getActivationCode().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new ActivationEmailDetail(object));
                    }
                    object.setActivationCode(value);
                    eventBus.addEmailActivationToCommit(object);
                }
            }
        });
    }

    /**
     * COLUMN UPDATER - TIMEOUT.
     */
    private void setTimeoutColumnUpdater() {
        view.getTimeoutColumn().setFieldUpdater(new FieldUpdater<ActivationEmailDetail, Date>() {

            @Override
            public void update(int index, ActivationEmailDetail object, Date value) {
                if (!object.getTimeout().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new ActivationEmailDetail(object));
                    }
                    object.setTimeout(value);
                    eventBus.addEmailActivationToCommit(object);
                }
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
                    eventBus.loadingShow(Storage.MSGS.adminCommonBtnCommit());
                    for (Long idx : dataToUpdate.keySet()) {
//                        eventBus.updateEmailActivation(dataToUpdate.get(idx));
                    }
                    eventBus.loadingHide();
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
                for (ActivationEmailDetail data : originalData.values()) {
                    idx = view.getDataGrid().getVisibleItems().indexOf(data);
                    view.getDataGrid().getVisibleItem(idx).updateWholeEmailActivation(data);
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
                    view.getPager().startLoading();
                    eventBus.getDataCount(view.getDataGrid(), new SearchDefinition(searchDataHolder));
                } else {
                    Window.alert("You have some uncommited data. Do commit or rollback first");
                }
            }
        });
    }
}
