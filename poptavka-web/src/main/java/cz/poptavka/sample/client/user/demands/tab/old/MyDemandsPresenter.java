package cz.poptavka.sample.client.user.demands.tab.old;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.widget.grid.unused.GlobalDemandConversationTable;
import cz.poptavka.sample.client.user.widget.grid.unused.SingleDemandConversationTable;
import cz.poptavka.sample.client.user.widget.unused.OldDetailWrapperPresenter;
import cz.poptavka.sample.shared.domain.demandsModule.ClientDemandDetail;
import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;
import cz.poptavka.sample.shared.domain.message.DemandMessageDetail;
import cz.poptavka.sample.shared.domain.type.ViewType;

@Presenter(view = MyDemandsView.class, multiple = true)
public class MyDemandsPresenter extends
        LazyPresenter<MyDemandsPresenter.MyDemandsInterface, UserEventBus> {

    public interface MyDemandsInterface extends LazyView {
        Widget getWidgetView();

        Button getAnswerBtn();

        Button getEditBtn();

        Button getCloseBtn();

        Button getCancelBtn();

        GlobalDemandConversationTable getDemandTable();
        ListDataProvider<ClientDemandMessageDetail> getDemandProvider();
        NoSelectionModel<ClientDemandMessageDetail> getDemandTableModel();

        // TODO maybe will be need type change
        SingleDemandConversationTable getConversationTable();
        ListDataProvider<DemandMessageDetail> getConversationProvider();
        NoSelectionModel<DemandMessageDetail> getConversationTableModel();

        SimplePanel getDetailSection();

        void swapTables();

        Anchor getBackToDemandsButton();
    }

    private OldDetailWrapperPresenter detailPresenter = null;

    public void bindView() {
        view.getDemandTableModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                view.swapTables();
                ClientDemandMessageDetail detail = view.getDemandTableModel().getLastSelectedObject();
                eventBus.requestDemandConversations(detail.getMessageId());
            }
        });
        view.getConversationTableModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                DemandMessageDetail detail = view.getConversationTableModel().getLastSelectedObject();
                eventBus.requestSingleConversation(detail.getThreadRootId(), detail.getMessageId());
            }
        });
        view.getBackToDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.swapTables();
                view.getConversationProvider().setList(new ArrayList<DemandMessageDetail>());
            }
        });
    }

    public void onInvokeMyDemands() {
        // Init DetailWrapper for this view
        if (detailPresenter == null) {
            detailPresenter = eventBus.addHandler(OldDetailWrapperPresenter.class);
            detailPresenter.initDetailWrapper(view.getDetailSection(),
                    ViewType.EDITABLE);
        }
        eventBus.requestDemandsWithConversationInfo();
    }

    // PARENT TABLE
    public void onSetClientDemandWithConversations(ArrayList<ClientDemandDetail> demandMessageList) {
//        List<ClientDemandMessageDetail> list = view.getDemandProvider().getList();
//        list.clear();
//        for (ClientDemandMessageDetail m : demandMessageList) {
//            list.add(m);
//        }
//        GWT.log("DATA SIZE: " + list.size());
//        refreshDisplays();

//        eventBus.displayContent(view.getWidgetView());
    }

    // CHILD TABLE
    public void onSetDemandConversations(ArrayList<DemandMessageDetail> conversations) {
        // TODO need to be implemented according to type of messageDetail
        List<DemandMessageDetail> list = view.getConversationProvider().getList();
        list.clear();
        for (DemandMessageDetail m : conversations) {
            list.add(m);
        }
        GWT.log("Conversation DATA SIZE: " + list.size());
        refreshDisplays();

    }

//    public void onResponseClientDemands(ArrayList<FullDemandDetail> demands) {
//        GWT.log("Demands are on the way.    demands.size = " + demands.size());
//
//        // Add the data to the data provider, which automatically pushes it to
//        // the widget.
//        view.getDataProvider().getList().clear();
//        view.getDataProvider().getList().addAll(demands);
//        refreshDisplays();
//    }

    /**
     * Refresh all displays.
     */
    public void refreshDisplays() {
        view.getDemandProvider().refresh();
        view.getConversationProvider().refresh();
    }

    public void onResponseDemandDetail(Widget widget) {
        view.getDetailSection().setWidget(widget);
    }

    // TODO delete, just devel tool
    public void cleanDetailWrapperPresenterForDevelopment() {
        if (detailPresenter != null) {
            eventBus.removeHandler(detailPresenter);
        }
    }

}
