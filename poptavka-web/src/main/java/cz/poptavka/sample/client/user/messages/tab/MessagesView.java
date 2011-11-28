/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.messages.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.message.UserMessageDetail;
import java.util.Set;

/**
 *
 * @author Martin Slavkovsky
 */
public class MessagesView extends Composite implements MessagesPresenter.MessagesInterface {

    private static MessagesViewUiBinder uiBinder = GWT.create(MessagesViewUiBinder.class);
    @UiField
    Button replyBtn, deleteBtn, moreActionsBtn, refreshBtn;
    //working buttons
    @UiField
    Button markReadBtn, markUnreadBtn;
    @UiField
    SimplePanel detailSection;

    @Override
    public SimplePanel getDetailSection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    interface MessagesViewUiBinder extends UiBinder<Widget, MessagesView> {
    }
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    MessageTable dataGrid;
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSizeCombo;
    /**
     * Data provider that will cell table with data.
     */
    private SingleSelectionModel<UserMessageDetail> selectionModel;
    /** Editable Columns in dataGrid. **/
    private Column<UserMessageDetail, String> nameColumn;
    private Column<UserMessageDetail, String> descriptionColumn;
    private Column<UserMessageDetail, String> permissionsColumn;

    public MessagesView() {
        pageSizeCombo = new ListBox();
        pageSizeCombo.addItem("10");
        pageSizeCombo.addItem("15");
        pageSizeCombo.addItem("20");
        pageSizeCombo.addItem("25");
        pageSizeCombo.addItem("30");
        pageSizeCombo.setSelectedIndex(1);
        initDataGrid();
        initWidget(uiBinder.createAndBindUi(this));
    }
    private static final StyleResource RSCS = GWT.create(StyleResource.class);
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    private void initDataGrid() {
        // Create a dataGrid.
        GWT.log("initDataGrid initialized");
        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        dataGrid = new MessageTable(MSGS, RSCS); // DataGrid<UserMessageDetail>();
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("500px");
        dataGrid.setHeight("400px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public MessageTable getDataGrid() {
        return dataGrid;
    }

    @Override
    public MultiSelectionModel<UserMessageDetail> getSelectionModel() {
        return (MultiSelectionModel<UserMessageDetail>) dataGrid.getSelectionModel();
    }

    @Override
    public Set<UserMessageDetail> getSelectedSet() {
        return getSelectionModel().getSelectedSet();
    }

    @Override
    public ListDataProvider<UserMessageDetail> getDataProvider() {
        return dataGrid.getDataProvider();
    }

    @Override
    public SimplePager getPager() {
        return pager;
    }

    @Override
    public ListBox getPageSizeCombo() {
        return pageSizeCombo;
    }

    @Override
    public int getPageSize() {
        return Integer.valueOf(pageSizeCombo.getItemText(pageSizeCombo.getSelectedIndex()));
    }

    /** view buttons **/
    @Override
    public Button getReplyButton() {
        return replyBtn;
    }

    @Override
    public Button getDeleteButton() {
        return deleteBtn;
    }

    @Override
    public Button getActionButton() {
        return moreActionsBtn;
    }

    @Override
    public Button getRefreshButton() {
        return refreshBtn;
    }

    @Override
    public Button getMarkReadButton() {
        return markReadBtn;
    }

    @Override
    public Button getMarkUnreadButton() {
        return markUnreadBtn;
    }
}