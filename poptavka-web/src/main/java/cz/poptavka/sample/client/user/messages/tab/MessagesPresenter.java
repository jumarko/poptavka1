/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.messages.tab;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.demands.widget.DetailWrapperPresenter;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.message.UserMessageDetail;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = MessagesView.class)
public class MessagesPresenter
        extends BasePresenter<MessagesPresenter.MessagesInterface, UserEventBus>
        implements HasValueChangeHandlers<String> {

    private final static Logger LOGGER = Logger.getLogger("MessagesPresenter");

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public interface MessagesInterface {

        Widget getWidgetView();

        MessageTable getDataGrid();

        SimplePager getPager();

        int getPageSize();

        Button getReplyButton();

        Button getDeleteButton();

        Button getActionButton();

        Button getRefreshButton();

        Button getMarkReadButton();

        Button getMarkUnreadButton();

        ListDataProvider<UserMessageDetail> getDataProvider();

        MultiSelectionModel<UserMessageDetail> getSelectionModel();

        Set<UserMessageDetail> getSelectedSet();

        SimplePanel getDetailSection();

        ListBox getPageSizeCombo();
    }
    private final Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();

    public void onInvokeInbox() {
        orderColumns.clear();
        orderColumns.put("message.created", OrderType.ASC);
        eventBus.getMessages(Arrays.asList(new String[]{"SENT", "DELETED"}), orderColumns, true);
        eventBus.displayMessagesContent(view.getWidgetView());
    }

    public void onInvokeSent() {
        orderColumns.clear();
        orderColumns.put("message.created", OrderType.ASC);
        eventBus.getMessages(Arrays.asList(new String[]{"SENT"}), orderColumns, false);
        eventBus.displayMessagesContent(view.getWidgetView());
    }

    public void onInvokeDeleted() {
        orderColumns.clear();
        orderColumns.put("message.created", OrderType.ASC);
        eventBus.getMessages(Arrays.asList(new String[]{"DELETED"}), orderColumns, false);
        eventBus.displayMessagesContent(view.getWidgetView());
    }

    public void onDisplayMessages(List<UserMessageDetail> messages) {
        List<UserMessageDetail> list = view.getDataProvider().getList();
        list.clear();
        for (UserMessageDetail d : messages) {
            list.add(d);
        }

        view.getDataProvider().refresh();
    }

    @Override
    public void bind() {
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                if (view.getSelectedSet().size() == 1) {
                    Iterator<UserMessageDetail> iter = view.getSelectedSet().iterator();
                    UserMessageDetail selected = iter.next();

//eventBus.getDemandDetail(selected.getDemandId(), DETAIL_TYPE);
//eventBus.requestMessagesConversation(selected.getMessageDetail().getMessageId(),
//                    selected.getUserMessageId());

                    //default set this demand as read
                    // TODO verify, if this is automatically done on server side as well
                    markMessagesAsRead(selected, true);
                }
            }
        });
        view.getMarkReadButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Iterator<UserMessageDetail> it = view.getSelectedSet().iterator();
                ArrayList<Long> messages = new ArrayList<Long>();
                while (it.hasNext()) {
                    UserMessageDetail d = it.next();
                    markMessagesAsRead(d, true);
                    messages.add(d.getMessageDetail().getMessageId());
                }
                eventBus.requestPotentialDemandReadStatusChange(messages, true);
            }
        });
        view.getMarkUnreadButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Iterator<UserMessageDetail> it = view.getSelectedSet().iterator();
                ArrayList<Long> messages = new ArrayList<Long>();
                while (it.hasNext()) {
                    UserMessageDetail d = it.next();
                    markMessagesAsRead(d, false);
                    messages.add(d.getMessageDetail().getMessageId());
                }
                eventBus.requestPotentialDemandReadStatusChange(messages, false);
            }
        });
    }
    private DetailWrapperPresenter detailPresenter = null;
    private boolean loaded = false;
    // TODO delete, just devel tool

    public void cleanDetailWrapperPresenterForDevelopment() {
        if (detailPresenter != null) {
            eventBus.removeHandler(detailPresenter);
        }
    }

    public void markMessagesAsRead(UserMessageDetail detail, boolean isRead) {
        List<UserMessageDetail> list = view.getDataProvider().getList();
        list.get(list.indexOf(detail)).setIsRead(isRead);
        view.getDataProvider().refresh();
    }
}