package com.eprovement.poptavka.client.user.messages.tab;

import com.google.gwt.cell.client.FieldUpdater;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.messages.MessagesEventBus;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;
import java.util.Arrays;

@Presenter(view = MessageList.class)
public class MessageListPresenter extends LazyPresenter<MessageListPresenter.IListM, MessagesEventBus> {

    public interface IListM extends LazyView, IsWidget {

        Widget getWidgetView();

        //control buttons getters
        Button getReadBtn();

        Button getUnreadBtn();

        Button getStarBtn();

        Button getUnstarBtn();

        Button getDeleteBtn();

        //table getters
        MessageListGrid<UserMessageDetail> getGrid();

        ListDataProvider<UserMessageDetail> getDataProvider();

        Column<UserMessageDetail, String> getCreationCol();

        Column<UserMessageDetail, Boolean> getStarColumn();

        Column<UserMessageDetail, String> getSubjectCol();

        Column<UserMessageDetail, String> getUserCol();

        List<Long> getSelectedIdList();

        Set<UserMessageDetail> getSelectedMessageList();
    }

    @Override
    public void bindView() {
        view.getReadBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                internalReadUpdate(true);
                updateReadStatus(view.getSelectedIdList(), true);
            }
        });
        view.getUnreadBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                internalReadUpdate(false);
                updateReadStatus(view.getSelectedIdList(), false);
            }
        });
        view.getStarBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                internalStarUpdate(true);
                updateStarStatus(view.getSelectedIdList(), true);
            }
        });
        view.getUnstarBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                internalStarUpdate(false);
                updateStarStatus(view.getSelectedIdList(), false);
            }
        });
        view.getDeleteBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.deleteMessages(view.getSelectedIdList());
            }
        });
        view.getUserCol().setFieldUpdater(new FieldUpdater<UserMessageDetail, String>() {

            @Override
            public void update(int index, UserMessageDetail object, String value) {
                object.setRead(true);
                eventBus.requestReadStatusUpdate(Arrays.asList(object.getId()), true);
                view.getGrid().redraw();
//                eventBus.displayConversation(
//                        object.getMessageDetail().getThreadRootId(), object.getMessageDetail().getMessageId());
            }
        });
        view.getSubjectCol().setFieldUpdater(new FieldUpdater<UserMessageDetail, String>() {

            @Override
            public void update(int index, UserMessageDetail object, String value) {
                object.setRead(true);
                eventBus.requestReadStatusUpdate(Arrays.asList(object.getId()), true);
//                eventBus.displayConversation(
//                        object.getMessageDetail().getThreadRootId(), object.getMessageDetail().getMessageId());
            }
        });
        view.getCreationCol().setFieldUpdater(new FieldUpdater<UserMessageDetail, String>() {

            @Override
            public void update(int index, UserMessageDetail object,
                    String value) {
                object.setRead(true);
                eventBus.requestReadStatusUpdate(Arrays.asList(object.getId()), true);
//                eventBus.displayConversation(
//                        object.getMessageDetail().getThreadRootId(), object.getMessageDetail().getMessageId());
            }
        });

        view.getStarColumn().setFieldUpdater(new FieldUpdater<UserMessageDetail, Boolean>() {

            @Override
            public void update(int index, UserMessageDetail object, Boolean value) {
                object.setStarred(!value);
                view.getGrid().redraw();
                Long[] item = new Long[]{object.getId()};
                eventBus.requestStarStatusUpdate(Arrays.asList(item), !value);
            }
        });
    }

    /**
     * Init view and fetch new supplier's demands. Demand request
     * is sent ONLY for the first time - when view is loaded.
     *
     * Associated DetailWrapper widget is created and initialized.
     */
    public void onInitInbox(SearchModuleDataHolder filter) {
        this.init();
        Storage.setCurrentlyLoadedView(Constants.MESSAGES_INBOX);
        eventBus.getInboxMessages(Storage.getUser().getUserId(), filter);
    }

    public void onInitSent(SearchModuleDataHolder filter) {
        this.init();
        Storage.setCurrentlyLoadedView(Constants.MESSAGES_SENT);
        eventBus.getSentMessages(Storage.getUser().getUserId(), filter);
    }

    public void onInitDraft(SearchModuleDataHolder filter) {
        this.init();
        Storage.setCurrentlyLoadedView(Constants.MESSAGES_DRAFT);
        //TODO LATER Martin: getDraftMessages
    }

    public void onInitTrash(SearchModuleDataHolder filter) {
        this.init();
        Storage.setCurrentlyLoadedView(Constants.MESSAGES_TRASH);
        eventBus.getDeletedMessages(Storage.getUser().getUserId(), filter);
    }

    private void init() {
        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayMain(view.getWidgetView());
    }

    public void onDisplayMessages(List<UserMessageDetail> messages) {
        List<UserMessageDetail> list = view.getDataProvider().getList();
        list.clear();
        for (UserMessageDetail d : messages) {
            list.add(d);
        }

        view.getDataProvider().refresh();
    }

    //call eventBus to update READ status
    //called in ClickEvent of action button.
    public void updateReadStatus(List<Long> selectedIdList, boolean newStatus) {
        eventBus.requestReadStatusUpdate(selectedIdList, newStatus);
    }

    /**
     * Triggered by action button: read/unread button
     * When changing read of multiple demands by action button. Visible change has to be done manually;
     *
     * @param isRead
     */
    private void internalReadUpdate(boolean isRead) {
        Iterator<UserMessageDetail> it = view.getSelectedMessageList().iterator();
        while (it.hasNext()) {
            UserMessageDetail message = it.next();
            message.setRead(isRead);
        }
        view.getDataProvider().refresh();
        view.getGrid().redraw();
    }

    /**
     * Call eventBus to update STARRED status.
     * T
     * his method is called by clicking star image on single demand by default. Also is called in ClickEvent of
     * action button.
     * @param list
     * @param newStatus
     */
    public void updateStarStatus(List<Long> list, boolean newStatus) {
        eventBus.requestStarStatusUpdate(list, newStatus);
    }

    /**
     * Triggered by action button: star/unstar buttons
     * when starring multiple demands by action button. Visible change has to be done manually;
     *
     * @param isStared
     */
    private void internalStarUpdate(boolean isStared) {
        Iterator<UserMessageDetail> it = view.getSelectedMessageList().iterator();
        while (it.hasNext()) {
            UserMessageDetail message = it.next();
            message.setStarred(isStared);
        }
        view.getDataProvider().refresh();
        view.getGrid().redraw();
    }
}
