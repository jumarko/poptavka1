package com.eprovement.poptavka.client.user.messages.tab;

import com.eprovement.poptavka.client.common.session.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.detail.MessageDetailView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.resources.datagrid.AsyncDataGrid;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail.MessageField;
import com.eprovement.poptavka.shared.search.SortDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author martin slavkovsky
 *
 */
public class MessageListView extends Composite implements MessageListPresenter.MessageListViewInterface {

    private static MessageListUiBinder uiBinder = GWT.create(MessageListUiBinder.class);

    interface MessageListUiBinder extends UiBinder<Widget, MessageListView> {
    }
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) UniversalAsyncGrid grid;
    @UiField(provided = true) UniversalPagerWidget pager;
    @UiField SimplePanel wrapperPanel, actionBox;
    @UiField MessageDetailView messageDetailView;
    @UiField HorizontalPanel toolBar;
    @UiField Button replyBtn;
    /** Class attributes. **/
    private MultiSelectionModel selectionModel;
    private Column<MessageDetail, Boolean> checkColumn;
    private Column<MessageDetail, String> senderColumn;
    private Column<MessageDetail, String> subjectColumn;
    private Column<MessageDetail, String> dateColumn;
    /** Constants. **/
    private static final String COL_WIDTH_SENDER = "100px";

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();

        initTableAndPager();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Initialize this example.
     */
    private void initTableAndPager() {
        // Create a Pager.
        pager = new UniversalPagerWidget();
        // Create a Table.
        DataGrid.Resources resource = GWT.create(AsyncDataGrid.class);
        grid = new UniversalAsyncGrid<MessageDetail>(initSort(), pager.getPageSize(), resource);
        grid.setWidth("100%");
        grid.setHeight("100%");
        // Selection Model - must define different from default which is used in UniversalAsyncGrid
        // Add a selection model so we can select cells.
        selectionModel = new MultiSelectionModel<MessageDetail>(MessageDetail.KEY_PROVIDER);
        grid.setSelectionModel(selectionModel, DefaultSelectionEventManager.<MessageDetail>createCheckboxManager());

        // Bind pager to demandGrid
        pager.setDisplay(grid);

        initDemandTableColumns();
    }

    private SortDataHolder initSort() {
        List<String> gridColumns = Arrays.asList(new String[]{
            "", //check box column
            MessageField.SENDER.getValue(),
            MessageField.SUBJECT.getValue(),
            MessageField.CREATED.getValue()
        });
        List<SortPair> sortPairs = Arrays.asList(
                new SortPair(MessageField.CREATED.getValue(), OrderType.DESC));
        return new SortDataHolder(sortPairs, gridColumns);
    }

    /**
     * Create all columns to the grid.
     */
    public void initDemandTableColumns() {
        // CheckBox column header - always create this header
        Header checkHeader = new Header<Boolean>(new CheckboxCell()) {
            @Override
            public Boolean getValue() {
                return false;
            }
        };
        // CheckBox column - always create this column
        grid.addCheckboxColumn(checkHeader);

        // Sender name column
        senderColumn = grid.addColumn(grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnSender(),
                true, COL_WIDTH_SENDER,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        return ((MessageDetail) object).getSender();
                    }
                });

        // Subject column
        subjectColumn = grid.addColumn(
                grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnSubject(),
                true, Constants.COL_WIDTH_TITLE,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        return ((MessageDetail) object).getSubject();
                    }
                });

        // Creation date column
        dateColumn = grid.addColumn(
                grid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnCreatedDate(),
                true, Constants.COL_WIDTH_DATE,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        return Storage.DATE_FORMAT_SHORT.format(((MessageDetail) object).getCreated());
                    }
                });
    }

    /**************************************************************************/
    /* Getter                                                                 */
    /**************************************************************************/
    /** Table related. **/
    @Override
    public UniversalAsyncGrid<MessageDetail> getGrid() {
        return grid;
    }

    @Override
    public MultiSelectionModel<MessageDetail> getSelectionModel() {
        return selectionModel;
    }

    @Override
    public UniversalPagerWidget getPager() {
        return pager;
    }

    /** Columns. **/
    @Override
    public Column<MessageDetail, Boolean> getCheckColumn() {
        return checkColumn;
    }

    @Override
    public Column<MessageDetail, String> getSenderColumn() {
        return senderColumn;
    }

    @Override
    public Column<MessageDetail, String> getSubjectColumn() {
        return subjectColumn;
    }

    @Override
    public Column<MessageDetail, String> getDateColumn() {
        return dateColumn;
    }

    /** Buttons. **/
    @Override
    public Button getReplyBtn() {
        return replyBtn;
    }

    /** Others. **/
    @Override
    public MessageDetailView getMessageDetailView() {
        return messageDetailView;
    }

    @Override
    public SimplePanel getActionBox() {
        return actionBox;
    }

    @Override
    public SimplePanel getWrapperPanel() {
        return wrapperPanel;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
