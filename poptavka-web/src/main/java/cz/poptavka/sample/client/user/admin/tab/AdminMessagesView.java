/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.domain.message.MessageState;
import cz.poptavka.sample.shared.domain.message.MessageDetail;

import cz.poptavka.sample.shared.domain.type.MessageType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminMessagesView extends Composite implements AdminMessagesPresenter.AdminMessagesInterface {

    private static AdminMessagesViewUiBinder uiBinder = GWT.create(AdminMessagesViewUiBinder.class);
    @UiField
    Button commit, rollback, refresh;
    @UiField
    Label changesLabel;

    /**
     * @return the message stateColumn
     */
    @Override
    public Column<MessageDetail, String> getStateColumn() {
        return stateColumn;
    }

    /**
     * @return the message subjectColumn
     */
    @Override
    public Column<MessageDetail, String> getSubjectColumn() {
        return subjectColumn;
    }

    /**
     * @return the message bodyColumn
     */
    @Override
    public Column<MessageDetail, String> getBodyColumn() {
        return bodyColumn;
    }

    /**
     * @return the message TypeColumn
     */
    @Override
    public Column<MessageDetail, String> getTypeColumn() {
        return typeColumn;
    }

    /**
     * @return the message createdColumn
     */
    @Override
    public Column<MessageDetail, Date> getCreatedColumn() {
        return createdColumn;
    }

    /**
     * @return the message sentColumn
     */
    @Override
    public Column<MessageDetail, Date> getSentColumn() {
        return sentColumn;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public DataGrid<MessageDetail> getDataGrid() {
        return dataGrid;
    }

    /**
     * @return the selectionModel
     */
    @Override
    public SingleSelectionModel<MessageDetail> getSelectionModel() {
        return selectionModel;
    }

    interface AdminMessagesViewUiBinder extends UiBinder<Widget, AdminMessagesView> {
    }
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    DataGrid<MessageDetail> dataGrid;
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
    private SingleSelectionModel<MessageDetail> selectionModel;
    /** Editable Columns in dataGrid. **/
    private Column<MessageDetail, String> subjectColumn;
    private Column<MessageDetail, String> bodyColumn;
    private Column<MessageDetail, String> stateColumn;
    private Column<MessageDetail, String> typeColumn;
    private Column<MessageDetail, Date> createdColumn;
    private Column<MessageDetail, Date> sentColumn;

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

    private void initDataGrid() {
        // Create a dataGrid.
        GWT.log("initDataGrid initialized");
        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        dataGrid = new DataGrid<MessageDetail>(KEY_PROVIDER);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("1000px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        selectionModel = new SingleSelectionModel<MessageDetail>(KEY_PROVIDER);
        dataGrid.setSelectionModel(getSelectionModel(),
                DefaultSelectionEventManager.<MessageDetail>createCheckboxManager());

        // Initialize the columns.
        initTableColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns() {

        // ID
        addColumn(new TextCell(), "ID", true, 50, new GetValue<String>() {

            @Override
            public String getValue(MessageDetail object) {
                return String.valueOf(object.getMessageId());
            }
        });

        // Demand ID
        addColumn(new TextCell(), "DID", true, 50, new GetValue<String>() {

            @Override
            public String getValue(MessageDetail object) {
                return String.valueOf(object.getMessageId());
            }
        });

        // Parent ID
        addColumn(new TextCell(), "PID", true, 50, new GetValue<String>() {

            @Override
            public String getValue(MessageDetail object) {
                return String.valueOf(object.getParentId());
            }
        });

        // Sender ID
        addColumn(new TextCell(), "SID", true, 50, new GetValue<String>() {

            @Override
            public String getValue(MessageDetail object) {
                return String.valueOf(object.getSenderId());
            }
        });

        // Receiver ID
        addColumn(new TextCell(), "RID", true, 50, new GetValue<String>() {

            @Override
            public String getValue(MessageDetail object) {
                return String.valueOf(object.getReceiverId());
            }
        });

        // MessageTitle
        subjectColumn = addColumn(new EditTextCell(), "Subject", true, 150, new GetValue<String>() {

            @Override
            public String getValue(MessageDetail object) {
                return object.getSubject();
            }
        });

        // State
        List<String> msgStates = new ArrayList<String>();
        for (MessageState msgState : MessageState.values()) {
            msgStates.add(msgState.name());
        }
        stateColumn = addColumn(new SelectionCell(msgStates), "State", true, 150, new GetValue<String>() {

            @Override
            public String getValue(MessageDetail object) {
                return object.getMessageState();
            }
        });

        // type.
        List<String> msgTypes = new ArrayList<String>();
        for (MessageType msgType : MessageType.values()) {
            msgTypes.add(msgType.getValue());
        }
        typeColumn = addColumn(new SelectionCell(msgTypes), "Type", true, 120, new GetValue<String>() {

            @Override
            public String getValue(MessageDetail object) {
                return object.getMessageType();
            }
        });

        // created date.
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
        createdColumn = addColumn(new DatePickerCell(dateFormat), "Created", false, 60,
                new GetValue<Date>() {

                    @Override
                    public Date getValue(MessageDetail messageDetail) {
                        return messageDetail.getCreated();
                    }
                });

        // sent date.
        sentColumn = addColumn(new DatePickerCell(dateFormat), "Sent", true, 60,
                new GetValue<Date>() {

                    @Override
                    public Date getValue(MessageDetail messageDetail) {
                        return messageDetail.getSent();
                    }
                });

        // body
        bodyColumn = addColumn(new EditTextCell(), "Body", true, 200, new GetValue<String>() {

            @Override
            public String getValue(MessageDetail object) {
                return object.getBody();
            }
        });
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private static interface GetValue<C> {

        C getValue(MessageDetail messageDetail);
    }

    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> Column<MessageDetail, C> addColumn(Cell<C> cell, String headerText, boolean sort, int width,
            final GetValue<C> getter) {
        Column<MessageDetail, C> column = new Column<MessageDetail, C>(cell) {

            @Override
            public C getValue(MessageDetail object) {
                return getter.getValue(object);
            }
        };
        if (sort) {
            column.setSortable(true);
        }
        dataGrid.addColumn(column, headerText);
        dataGrid.setColumnWidth(column, width, Unit.PX);
        return column;
    }
    /**
     * The key provider that provides the unique ID of a MessageDetail.
     */
    private static final ProvidesKey<MessageDetail> KEY_PROVIDER = new ProvidesKey<MessageDetail>() {

        @Override
        public Object getKey(MessageDetail item) {
            return item == null ? null : item.getMessageId();
        }
    };

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

    @Override
    public Button getCommitBtn() {
        return commit;
    }

    @Override
    public Button getRollbackBtn() {
        return rollback;
    }

    @Override
    public Button getRefreshBtn() {
        return refresh;
    }

    @Override
    public Label getChangesLabel() {
        return changesLabel;
    }
}