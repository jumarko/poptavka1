package com.eprovement.poptavka.client.user.messages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.messages.toolbar.MessagesToolbarView;
import com.eprovement.poptavka.client.user.widget.detail.MessageDetailView;
import com.eprovement.poptavka.client.user.widget.grid.TableDisplayUserMessage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author martin slavkovsky
 */
public class MessageListView extends Composite implements MessageListPresenter.MessageListViewInterface {

    private static MessageListUiBinder uiBinder = GWT.create(MessageListUiBinder.class);

    interface MessageListUiBinder extends UiBinder<Widget, MessageListView> {
    }

    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) UniversalAsyncGrid table;
    @UiField SimplePanel footerContainer, detailPanel;
    @UiField MessageDetailView messageDetailView;
    /** Constants. **/
    @Inject
    private MessagesToolbarView toolbar;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        Storage.GRSCS.dataGridStyle().ensureInjected();
    }

    /**
     * Initialize this example.
     */
    @Override
    public void initTable(UniversalAsyncGrid table) {
        this.table = table;
        this.toolbar.getPager().setVisible(true);
        this.toolbar.bindPager(this.table);
    }

    /**************************************************************************/
    /* Getter                                                                 */
    /**************************************************************************/
    @Override
    public List<Long> getSelectedUserMessageIds() {
        List<Long> idList = new ArrayList<Long>();
        Set<TableDisplayUserMessage> set = getSelectedObjects();
        Iterator<TableDisplayUserMessage> it = set.iterator();
        while (it.hasNext()) {
            idList.add(it.next().getUserMessageId());
        }
        return idList;
    }

    @Override
    public Set getSelectedObjects() {
        MultiSelectionModel model = (MultiSelectionModel) table.getSelectionModel();
        return model.getSelectedSet();
    }

    /** Table related. **/
    @Override
    public UniversalAsyncGrid<MessageDetail> getTable() {
        return table;
    }

    /** Others. **/
    @Override
    public MessageDetailView getMessageDetailView() {
        return messageDetailView;
    }

    @Override
    public MessagesToolbarView getToolbar() {
        return toolbar;
    }

    @Override
    public SimplePanel getFooterContainer() {
        return footerContainer;
    }

    @Override
    public SimplePanel getWrapperPanel() {
        return detailPanel;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}