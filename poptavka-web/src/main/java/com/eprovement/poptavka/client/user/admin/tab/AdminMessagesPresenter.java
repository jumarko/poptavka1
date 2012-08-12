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
import com.eprovement.poptavka.domain.enums.MessageState;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.type.MessageType;
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
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminMessagesView.class)
public class AdminMessagesPresenter
        extends LazyPresenter<AdminMessagesPresenter.AdminMessagesInterface, AdminEventBus> {

    //history of changes
    private Map<Long, MessageDetail> dataToUpdate = new HashMap<Long, MessageDetail>();
    private Map<Long, MessageDetail> originalData = new HashMap<Long, MessageDetail>();
    //need to remember for asynchDataProvider if asking for more data
    private SearchModuleDataHolder searchDataHolder;

    /**
     * Interface for widget AdminMessagesView.
     */
    public interface AdminMessagesInterface extends LazyView {

        // TABLE
        UniversalAsyncGrid<MessageDetail> getDataGrid();

        Column<MessageDetail, String> getStateColumn();

        Column<MessageDetail, String> getSubjectColumn();

        Column<MessageDetail, String> getBodyColumn();

        Column<MessageDetail, String> getTypeColumn();

        Column<MessageDetail, Date> getCreatedColumn();

        Column<MessageDetail, Date> getSentColumn();

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
     *
     * @param filter
     */
    public void onInitMessages(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_MESSAGES);
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
    public void onDisplayAdminTabMessages(List<MessageDetail> messages) {
        view.getDataGrid().updateRowData(messages);
        Storage.hideLoading();
    }

    //*************************************************************************/
    //                              DATA CHANGE                               */
    //*************************************************************************/
    /**
     * Store changes made in table data.
     */
    public void onAddMessageToCommit(MessageDetail data) {
        dataToUpdate.remove(data.getMessageId());
        dataToUpdate.put(data.getMessageId(), data);
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
        setSubjectColumnUpdater();
        setBodyColumnUpdater();
        setStateColumnUpdater();
        setTypeColumnUpdater();
        setCreatedColumnUpdater();
        setSentColumnUpdater();
        //
        addCommitButtonHandler();
        addRollbackButtonHandler();
        addRefreshButtonHandler();
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
     * COLUMN UPDATER - SENT
     */
    private void setSentColumnUpdater() {
        view.getSentColumn().setFieldUpdater(new FieldUpdater<MessageDetail, Date>() {

            @Override
            public void update(int index, MessageDetail object, Date value) {
                if (!object.getSent().equals(object)) {
                    if (!originalData.containsKey(object.getMessageId())) {
                        originalData.put(object.getMessageId(), new MessageDetail(object));
                    }
                    object.setSent(value);
                    eventBus.addMessageToCommit(object);
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - CREATED
     */
    private void setCreatedColumnUpdater() {
        view.getCreatedColumn().setFieldUpdater(new FieldUpdater<MessageDetail, Date>() {

            @Override
            public void update(int index, MessageDetail object, Date value) {
                if (!object.getCreated().equals(value)) {
                    if (!originalData.containsKey(object.getMessageId())) {
                        originalData.put(object.getMessageId(), new MessageDetail(object));
                    }
                    object.setCreated(value);
                    eventBus.addMessageToCommit(object);
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - TYPE
     */
    private void setTypeColumnUpdater() {
        view.getTypeColumn().setFieldUpdater(new FieldUpdater<MessageDetail, String>() {

            @Override
            public void update(int index, MessageDetail object, String value) {
                for (MessageType msgType : MessageType.values()) {
                    if (msgType.getValue().equals(value)) {
                        if (!object.getMessageType().equals(msgType.name())) {
                            if (!originalData.containsKey(object.getMessageId())) {
                                originalData.put(object.getMessageId(), new MessageDetail(object));
                            }
                            object.setMessageType(msgType.name());
                            eventBus.addMessageToCommit(object);
                        }
                    }
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - STATE
     */
    private void setStateColumnUpdater() {
        view.getStateColumn().setFieldUpdater(new FieldUpdater<MessageDetail, String>() {

            @Override
            public void update(int index, MessageDetail object, String value) {
                for (MessageState msgState : MessageState.values()) {
                    if (msgState.name().equals(value)) {
                        if (!object.getMessageState().equals(msgState.name())) {
                            if (!originalData.containsKey(object.getMessageId())) {
                                originalData.put(object.getMessageId(), new MessageDetail(object));
                            }
                            object.setMessageState(msgState.name());
                            eventBus.addMessageToCommit(object);
                        }
                    }
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - BODY
     */
    private void setBodyColumnUpdater() {
        view.getBodyColumn().setFieldUpdater(new FieldUpdater<MessageDetail, String>() {

            @Override
            public void update(int index, MessageDetail object, String value) {
                if (!object.getBody().equals(value)) {
                    if (!originalData.containsKey(object.getMessageId())) {
                        originalData.put(object.getMessageId(), new MessageDetail(object));
                    }
                    object.setBody(value);
                    eventBus.addMessageToCommit(object);
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - SUBJECT
     */
    private void setSubjectColumnUpdater() {
        view.getSubjectColumn().setFieldUpdater(new FieldUpdater<MessageDetail, String>() {

            @Override
            public void update(int index, MessageDetail object, String value) {
                if (!object.getSubject().equals(value)) {
                    if (!originalData.containsKey(object.getMessageId())) {
                        originalData.put(object.getMessageId(), new MessageDetail(object));
                    }
                    object.setSubject(value);
                    eventBus.addMessageToCommit(object);
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
                        eventBus.updateMessage(dataToUpdate.get(idx));
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
                for (MessageDetail data : originalData.values()) {
                    idx = view.getDataGrid().getVisibleItems().indexOf(data);
                    view.getDataGrid().getVisibleItem(idx).updateWholeMessage(data);
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
                    view.getDataGrid().updateRowData(new ArrayList<MessageDetail>());
                    eventBus.getDataCount(view.getDataGrid(), new SearchDefinition(searchDataHolder));
                } else {
                    Window.alert("You have some uncommited data. Do commit or rollback first");
                }
            }
        });
    }
}
