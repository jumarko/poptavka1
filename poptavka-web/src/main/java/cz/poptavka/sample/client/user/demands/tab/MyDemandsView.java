package cz.poptavka.sample.client.user.demands.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.NoSelectionModel;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.user.demands.widget.table.GlobalDemandConversationTable;
import cz.poptavka.sample.client.user.demands.widget.table.SingleDemandConversationTable;
import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;

public class MyDemandsView extends Composite implements
        MyDemandsPresenter.MyDemandsInterface {

    private static MyDemandsViewUiBinder uiBinder = GWT
            .create(MyDemandsViewUiBinder.class);

    interface MyDemandsViewUiBinder extends UiBinder<Widget, MyDemandsView> {
    }

    private static final StyleResource RSCS = GWT.create(StyleResource.class);

    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    @UiField(provided = true) GlobalDemandConversationTable demandTable;
    @UiField(provided = true) SimplePager demandPager;

    @UiField(provided = true) SingleDemandConversationTable conversationTable;
    @UiField(provided = true) SimplePager conversationPager;

    @UiField
    Button answerBtn, editBtn, closeBtn, cancelBtn;
    @UiField Anchor backToDemandsBtn;

    @UiField
    SimplePanel detailHolder;

    private boolean conversationTableVisible = false;

    @Override
    public void createView() {
        demandTable = new GlobalDemandConversationTable(MSGS, RSCS);
        conversationTable = new SingleDemandConversationTable(MSGS, RSCS);

        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        demandPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        demandPager.setDisplay(demandTable);
        conversationPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        conversationPager.setDisplay(conversationTable);

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public Button getAnswerBtn() {
        return answerBtn;
    }

    @Override
    public Button getEditBtn() {
        return editBtn;
    }

    @Override
    public Button getCloseBtn() {
        return closeBtn;
    }

    @Override
    public Button getCancelBtn() {
        return cancelBtn;
    }

    @Override
    public SimplePanel getDetailSection() {
        return detailHolder;
    }

    @Override
    public void swapTables() {
        if (conversationTableVisible) {
            demandTable.getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
            backToDemandsBtn.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
        } else {
            demandTable.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
            backToDemandsBtn.getElement().getParentElement().getStyle().setDisplay(Display.BLOCK);
        }
        conversationTableVisible = !conversationTableVisible;
    }

    @Override
    public Anchor getBackToDemandsButton() {
        return backToDemandsBtn;
    }

    @Override
    public GlobalDemandConversationTable getDemandTable() {
        return demandTable;
    }

    @Override
    public ListDataProvider<ClientDemandMessageDetail> getDemandProvider() {
        return demandTable.getDataProvider();
    }

    @Override
    public NoSelectionModel<ClientDemandMessageDetail> getDemandTableModel() {
        return (NoSelectionModel<ClientDemandMessageDetail>) demandTable.getSelectionModel();
    }

    @Override
    public SingleDemandConversationTable getConversationTable() {
        return conversationTable;
    }

    @Override
    public ListDataProvider<MessageDetail> getConversationProvider() {
        return conversationTable.getDataProvider();
    }

    @Override
    public MultiSelectionModel<MessageDetail> getConversationTableModel() {
        return (MultiSelectionModel<MessageDetail>) conversationTable.getSelectionModel();
    }

}
