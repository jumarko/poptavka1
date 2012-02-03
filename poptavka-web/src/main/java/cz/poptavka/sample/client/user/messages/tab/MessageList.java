package cz.poptavka.sample.client.user.messages.tab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;

import com.mvp4g.client.view.ReverseViewInterface;
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.user.messages.tab.MessageListPresenter.IListM;
import cz.poptavka.sample.shared.domain.message.UserMessageDetail;

/**
 * IMPORTANT NOTE: This view is ReverseView. Because of eventBus calls from dataGrid table and these event calls are
 * defined in view, not in presenter.
 *
 * @author beho
 *
 */
public class MessageList extends Composite implements
        ReverseViewInterface<MessageListPresenter>, IListM {

    private static MessageListUiBinder uiBinder = GWT.create(MessageListUiBinder.class);

    interface MessageListUiBinder extends UiBinder<Widget, MessageList> {
    }
    //attrribute preventing repeated loading of demand detail, when clicked on the same demand
    private long lastOpenedDemand = -1;
    //table handling buttons
    @UiField
    Button readBtn, unreadBtn, starBtn, unstarBtn, deleteBtn;
    //DataGridattributes
    @UiField(provided = true)
    MessageListGrid<UserMessageDetail> messageGrid;
    @UiField(provided = true)
    SimplePager pager;
    //presenter
    private MessageListPresenter presenter;

    @Override
    public void setPresenter(MessageListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public MessageListPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();
        //demandGrid init
        messageGrid = new MessageListGrid<UserMessageDetail>(UserMessageDetail.KEY_PROVIDER);
        // Add a selection model so we can select cells.
        final SelectionModel<UserMessageDetail> selectionModel =
                new MultiSelectionModel<UserMessageDetail>(UserMessageDetail.KEY_PROVIDER);
        messageGrid.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<UserMessageDetail>createCheckboxManager());

        //init table
        initTableColumns(selectionModel);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(messageGrid);

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
//**** GRID related methods - START
    private Column<UserMessageDetail, Boolean> starColumn;
    private Column<UserMessageDetail, String> userCol;
    private Column<UserMessageDetail, String> subjectCol;
    private Column<UserMessageDetail, String> creationCol;

    @Override
    public MessageListGrid<UserMessageDetail> getGrid() {
        return messageGrid;
    }

    @Override
    public ListDataProvider<UserMessageDetail> getDataProvider() {
        return messageGrid.getDataProvider();
    }

    @Override
    public Column<UserMessageDetail, String> getCreationCol() {
        return creationCol;
    }

    @Override
    public Column<UserMessageDetail, Boolean> getStarColumn() {
        return starColumn;
    }

    @Override
    public Column<UserMessageDetail, String> getSubjectCol() {
        return subjectCol;
    }

    @Override
    public Column<UserMessageDetail, String> getUserCol() {
        return userCol;
    }

    /**
     * Create all columns to the grid and define click actions.
     */
    public void initTableColumns(final SelectionModel<UserMessageDetail> selectionModel) {
        //init column factory
        MessageColumnFactory<UserMessageDetail> factory = new MessageColumnFactory<UserMessageDetail>();

// **** definition of all needed FieldUpdaters
        //TEXT FIELD UPDATER create common demand display fieldUpdater for demand and related conversation display


// **** ROW selection column and set it's width to 40px.
        //contains custom header providing selecting all visible items
        final Header<Boolean> header = factory.createCheckBoxHeader();
        //select
        header.setUpdater(new ValueUpdater<Boolean>() {

            @Override
            public void update(Boolean value) {
                List<UserMessageDetail> rows = messageGrid.getVisibleItems();
                for (UserMessageDetail row : rows) {
                    selectionModel.setSelected(row, value);
                }

            }
        });
        messageGrid.addColumn(factory.createCheckboxColumn(selectionModel), header);
        messageGrid.setColumnWidth(messageGrid.getColumn(MessageColumnFactory.COL_ZERO),
                MessageColumnFactory.WIDTH_40, Unit.PX);

// **** Star collumn with defined valueUpdater and custom style
        starColumn = factory.createStarColumn();
        //TODO
        //testing if assigning style in MessageColumnFactory works - works well 7.11.11 Beho
        //but keep here for reference
        //starColumn.setCellStyleNames(Storage.RSCS.grid().cellTableHandCursor());
        messageGrid.setColumnWidth(starColumn, MessageColumnFactory.WIDTH_40, Unit.PX);
        messageGrid.addColumn(starColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));

// **** user column
        userCol = factory.createUserColumn(messageGrid.getSortHandler());
        //TODO i18
//        demandGrid.addColumn(userCol, Storage.MSGS.title());
        messageGrid.addColumn(userCol, "Od koho");

// **** subject column
        subjectCol = factory.createSubjectColumn(messageGrid.getSortHandler(), true);
//        demandGrid.addColumn(subjectCol, Storage.MSGS.client());
        messageGrid.addColumn(subjectCol, "Predmet");

// **** creationDate column
        creationCol = factory.createDateColumn(messageGrid.getSortHandler(), MessageColumnFactory.DATE_CREATED);
        messageGrid.addColumn(creationCol, Storage.MSGS.createdDate());

    }

//**** GRID related methods - END
    @Override
    public Button getReadBtn() {
        return readBtn;
    }

    @Override
    public Button getUnreadBtn() {
        return unreadBtn;
    }

    @Override
    public Button getStarBtn() {
        return starBtn;
    }

    @Override
    public Button getUnstarBtn() {
        return unstarBtn;
    }

    @Override
    public Button getDeleteBtn() {
        return deleteBtn;
    }

    @Override
    public List<Long> getSelectedIdList() {
        List<Long> idList = new ArrayList<Long>();
        Set<UserMessageDetail> set = getSelectedMessageList();
        Iterator<UserMessageDetail> it = set.iterator();
        while (it.hasNext()) {
            idList.add(it.next().getId());
        }
        return idList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<UserMessageDetail> getSelectedMessageList() {
        MultiSelectionModel<UserMessageDetail> model =
                (MultiSelectionModel<UserMessageDetail>) messageGrid.getSelectionModel();
        return model.getSelectedSet();
    }
}
