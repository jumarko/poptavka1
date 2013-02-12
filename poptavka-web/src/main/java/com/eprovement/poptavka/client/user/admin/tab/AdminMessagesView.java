/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.GetValue;
import com.eprovement.poptavka.domain.enums.MessageState;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.type.MessageType;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminMessagesView extends Composite implements AdminMessagesPresenter.AdminMessagesInterface {

    private static AdminMessagesViewUiBinder uiBinder = GWT.create(AdminMessagesViewUiBinder.class);

    interface AdminMessagesViewUiBinder extends UiBinder<Widget, AdminMessagesView> {
    }
    //*************************************************************************/
    //                              ATTRIBUTES                                */
    //*************************************************************************/
    //Table constants
    private static final String BODY_COL_WIDTH = "200px";
    private static final String SENT_COL_WIDTH = "60px";
    private static final String CREATED_COL_WIDTH = "60px";
    private static final String STATE_COL_WIDTH = "150px";
    private static final String TYPE_COL_WIDTH = "150px";
    private static final String SUBJECT_COL_WIDTH = "150px";
    //
    @UiField
    Button commit, rollback, refresh;
    @UiField
    Label changesLabel;
    // PAGER
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSizeCombo;
    // TABLE
    @UiField(provided = true)
    UniversalAsyncGrid<MessageDetail> dataGrid;
    // Editable Columns
    private Column<MessageDetail, String> subjectColumn;
    private Column<MessageDetail, String> bodyColumn;
    private Column<MessageDetail, String> stateColumn;
    private Column<MessageDetail, String> typeColumn;
    private Column<MessageDetail, Date> createdColumn;
    private Column<MessageDetail, Date> sentColumn;
    private List<String> gridColumns = Arrays.asList(
            new String[]{
                "id", "demand.id", "parent.id", "sender.id",
                "subject", "messageState", "", "sent", "body"
            });
    // The key provider that provides the unique ID of a MessageDetail.
    private static final ProvidesKey<MessageDetail> KEY_PROVIDER = new ProvidesKey<MessageDetail>() {

        @Override
        public Object getKey(MessageDetail item) {
            return item == null ? null : item.getMessageId();
        }
    };

    //*************************************************************************/
    //                          INITIALIZATOIN                                */
    //*************************************************************************/
    /**
     * creates WIDGET view.
     */
    @Override
    public void createView() {
        pageSizeCombo = new ListBox();
        pageSizeCombo.addItem("10");
        pageSizeCombo.addItem("15");
        pageSizeCombo.addItem("20");
        pageSizeCombo.addItem("25");
        pageSizeCombo.addItem("30");
        pageSizeCombo.setSelectedIndex(1);
        initDataGrid();
        initWidget(uiBinder.createAndBindUi(this));
        changesLabel.setText("0");
    }

    /**
     * Creates table with accessories - columns, pager, selection model.
     */
    private void initDataGrid() {
        // Create a dataGrid.
        GWT.log("initDataGrid initialized");
        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        dataGrid = new UniversalAsyncGrid<MessageDetail>(KEY_PROVIDER, gridColumns);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        // Initialize the columns.
        initTableColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns() {
        addIdColumn();
        addDemandIdColumn();
        addParentIdColumn();
        addSenderIdColumn();
        addMessageTitleColumn();
        addMessageStateColumn();
        addMessageTypeColumn();
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
        addCreatedDateColumn(dateFormat);
        addSentDateColumn(dateFormat);
        addBodyColumn();
    }

    private void addBodyColumn() {
        bodyColumn = dataGrid.addColumn(new EditTextCell(), "Body", true, BODY_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((MessageDetail) object).getBody();
                    }
                });
    }

    private void addSentDateColumn(DateTimeFormat dateFormat) {
        sentColumn = dataGrid.addColumn(new DatePickerCell(dateFormat), "Sent", true, SENT_COL_WIDTH,
                new GetValue<Date>() {

                    @Override
                    public Date getValue(Object object) {
                        return ((MessageDetail) object).getSent();
                    }
                });
    }

    private void addCreatedDateColumn(DateTimeFormat dateFormat) {
        createdColumn = dataGrid.addColumn(new DatePickerCell(dateFormat), "Created", false, CREATED_COL_WIDTH,
                new GetValue<Date>() {

                    @Override
                    public Date getValue(Object object) {
                        return ((MessageDetail) object).getCreated();
                    }
                });
    }

    private void addMessageStateColumn() {
        List<String> msgStates = new ArrayList<String>();
        for (MessageState msgState : MessageState.values()) {
            msgStates.add(msgState.name());
        }
        stateColumn = dataGrid.addColumn(new SelectionCell(msgStates), "State", true, STATE_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((MessageDetail) object).getMessageState();
                    }
                });
    }

    private void addMessageTypeColumn() {
        List<String> msgTypes = new ArrayList<String>();
        for (MessageType msgState : MessageType.values()) {
            msgTypes.add(msgState.name());
        }
        typeColumn = dataGrid.addColumn(
                new SelectionCell(msgTypes), Storage.MSGS.columnType(), true, TYPE_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((MessageDetail) object).getMessageType();
                    }
                });
    }

    private void addMessageTitleColumn() {
        subjectColumn = dataGrid.addColumn(
                new EditTextCell(), Storage.MSGS.columnSubject(), true, SUBJECT_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((MessageDetail) object).getSubject();
                    }
                });
    }

    private void addSenderIdColumn() {
        dataGrid.addColumn(new TextCell(), Storage.MSGS.columnSenderID(),
                true, Constants.COL_WIDTH_ID,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return String.valueOf(((MessageDetail) object).getSenderId());
                    }
                });
    }

    private void addParentIdColumn() {
        dataGrid.addColumn(new TextCell(), Storage.MSGS.columnPID(),
                true, Constants.COL_WIDTH_ID,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return String.valueOf(((MessageDetail) object).getParentId());
                    }
                });
    }

    private void addDemandIdColumn() {
        dataGrid.addColumn(new TextCell(), Storage.MSGS.columnDID(),
                true, Constants.COL_WIDTH_ID,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return String.valueOf(((MessageDetail) object).getMessageId());
                    }
                });
    }

    private void addIdColumn() {
        dataGrid.addColumn(new TextCell(), Storage.MSGS.columnID(),
                true, Constants.COL_WIDTH_ID,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return String.valueOf(((MessageDetail) object).getMessageId());
                    }
                });
    }
    //*************************************************************************/
    //                      GETTER METHODS (defined by interface)             */
    //*************************************************************************/
    //                          *** TABLE ***

    /**
     * @return TABLE (DataGrid)
     */
    @Override
    public UniversalAsyncGrid<MessageDetail> getDataGrid() {
        return dataGrid;
    }

    /**
     * @return table column: STATE
     */
    @Override
    public Column<MessageDetail, String> getStateColumn() {
        return stateColumn;
    }

    /**
     * @return table column: SUBJECT
     */
    @Override
    public Column<MessageDetail, String> getSubjectColumn() {
        return subjectColumn;
    }

    /**
     * @return table column: BODY
     */
    @Override
    public Column<MessageDetail, String> getBodyColumn() {
        return bodyColumn;
    }

    /**
     * @return table column: TYPE
     */
    @Override
    public Column<MessageDetail, String> getTypeColumn() {
        return typeColumn;
    }

    /**
     * @return table column: CREATED
     */
    @Override
    public Column<MessageDetail, Date> getCreatedColumn() {
        return createdColumn;
    }

    /**
     * @return table column: SENT
     */
    @Override
    public Column<MessageDetail, Date> getSentColumn() {
        return sentColumn;
    }

    //                         *** PAGER ***
    /*
     * @return pager
     */
    @Override
    public SimplePager getPager() {
        return pager;
    }

    /**
     * @return table/pager size: COMBO
     */
    @Override
    public ListBox getPageSizeCombo() {
        return pageSizeCombo;
    }

    /**
     * @return table/pager size: VALUE
     */
    @Override
    public int getPageSize() {
        return Integer.valueOf(pageSizeCombo.getItemText(pageSizeCombo.getSelectedIndex()));
    }
    //                          *** BUTTONS ***
    /*
     * @return COMMIT button
     */

    @Override
    public Button getCommitBtn() {
        return commit;
    }

    /**
     * @return ROLLBACK button
     */
    @Override
    public Button getRollbackBtn() {
        return rollback;
    }

    /**
     * @return REFRESH button
     */
    @Override
    public Button getRefreshBtn() {
        return refresh;
    }
    //                          *** OTHER ***

    /**
     * @return label for displaying informations for user
     */
    @Override
    public Label getChangesLabel() {
        return changesLabel;
    }

    /**
     * @return this widget as it is
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}