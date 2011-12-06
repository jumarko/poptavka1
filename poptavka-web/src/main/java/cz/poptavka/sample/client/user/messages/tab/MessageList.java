package cz.poptavka.sample.client.user.messages.tab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.FieldUpdater;
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
import com.google.gwt.user.client.ui.SimplePanel;
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
public class MessageList extends Composite implements ReverseViewInterface<MessageListPresenter>, IListM {

    private static MessageListUiBinder uiBinder = GWT.create(MessageListUiBinder.class);

    interface MessageListUiBinder extends UiBinder<Widget, MessageList> {
    }
    //attrribute preventing repeated loading of demand detail, when clicked on the same demand
    private long lastOpenedDemand = -1;
    //table handling buttons
    @UiField
    Button readBtn, unreadBtn, starBtn, unstarBtn;
    //DataGridattributes
    @UiField(provided = true)
    MessageListGrid<UserMessageDetail> demandGrid;
    @UiField(provided = true)
    SimplePager pager;
    //presenter
    private MessageListPresenter presenter;
    //detailWrapperPanel
    @UiField
    SimplePanel wrapperPanel;

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
        demandGrid = new MessageListGrid<UserMessageDetail>(UserMessageDetail.KEY_PROVIDER);
        // Add a selection model so we can select cells.
        final SelectionModel<UserMessageDetail> selectionModel =
                new MultiSelectionModel<UserMessageDetail>(UserMessageDetail.KEY_PROVIDER);
        demandGrid.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<UserMessageDetail>createCheckboxManager());

        //init table
        initTableColumns(selectionModel);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(demandGrid);

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public MessageListGrid<UserMessageDetail> getGrid() {
        return demandGrid;
    }

    @Override
    public ListDataProvider<UserMessageDetail> getDataProvider() {
        return demandGrid.getDataProvider();
    }

    /**
     * Create all columns to the grid and define click actions.
     */
    public void initTableColumns(final SelectionModel<UserMessageDetail> selectionModel) {
        //init column factory
        MessageColumnFactory<UserMessageDetail> factory = new MessageColumnFactory<UserMessageDetail>();

// **** definition of all needed FieldUpdaters
        //TEXT FIELD UPDATER create common demand display fieldUpdater for demand and related conversation display
        FieldUpdater<UserMessageDetail, String> action = new FieldUpdater<UserMessageDetail, String>() {
            @Override
            public void update(int index, UserMessageDetail object, String value) {
                MessageTableDisplay obj = (MessageTableDisplay) object;
                object.setRead(true);
                demandGrid.redraw();
                presenter.displayConversation(object.getMessageDetail()
                        .getThreadRootId(), object.getMessageDetail().getMessageId());
            }
        };
//        //DATE FIELD UPDATER displaying of demand detail. The fieldUpdater 'action' cannot be used,
//        //because this is working with Date instead of String
        FieldUpdater<UserMessageDetail, Date> dateAction = new FieldUpdater<UserMessageDetail, Date>() {

            @Override
            public void update(int index, UserMessageDetail object,
                    Date value) {
                //for pure display detail action
                presenter.displayConversation(object.getMessageDetail()
                        .getThreadRootId(), object.getMessageDetail().getMessageId());
            }
        };

        //STAR COLUMN FIELD UPDATER
        FieldUpdater<UserMessageDetail, Boolean> starUpdater = new FieldUpdater<UserMessageDetail, Boolean>() {

            @Override
            public void update(int index, UserMessageDetail object, Boolean value) {
                MessageTableDisplay obj = (MessageTableDisplay) object;
                object.setStarred(!value);
                demandGrid.redraw();
                Long[] item = new Long[]{object.getId()};
                presenter.updateStarStatus(Arrays.asList(item), !value);
            }
        };

// **** ROW selection column and set it's width to 40px.
        //contains custom header providing selecting all visible items
        final Header<Boolean> header = factory.createCheckBoxHeader();
        //select
        header.setUpdater(new ValueUpdater<Boolean>() {

            @Override
            public void update(Boolean value) {
                List<UserMessageDetail> rows = demandGrid.getVisibleItems();
                for (UserMessageDetail row : rows) {
                    selectionModel.setSelected(row, value);
                }

            }
        });
        demandGrid.addColumn(factory.createCheckboxColumn(selectionModel), header);
        demandGrid.setColumnWidth(demandGrid.getColumn(MessageColumnFactory.COL_ZERO),
                MessageColumnFactory.WIDTH_40, Unit.PX);

// **** Star collumn with defined valueUpdater and custom style
        Column<UserMessageDetail, Boolean> starColumn = factory.createStarColumn();
        starColumn.setFieldUpdater(starUpdater);
        //TODO
        //testing if assigning style in MessageColumnFactory works - works well 7.11.11 Beho
        //but keep here for reference
        //starColumn.setCellStyleNames(Storage.RSCS.grid().cellTableHandCursor());
        demandGrid.setColumnWidth(starColumn, MessageColumnFactory.WIDTH_40, Unit.PX);
        demandGrid.addColumn(starColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));

// **** user column
        Column<UserMessageDetail, String> userCol = factory.createUserColumn(demandGrid.getSortHandler());
        userCol.setFieldUpdater(action);
        //TODO i18
//        demandGrid.addColumn(userCol, Storage.MSGS.title());
        demandGrid.addColumn(userCol, "Od koho");

// **** subject column
        Column<UserMessageDetail, String> subjectCol = factory.createSubjectColumn(null, true);
        subjectCol.setFieldUpdater(action);
//        demandGrid.addColumn(subjectCol, Storage.MSGS.client());
        demandGrid.addColumn(userCol, "Predmet");


//// **** urgent column
//        Column<UserMessageDetail, Date> urgentCol = factory.createUrgentColumn(demandGrid.getSortHandler());
//        urgentCol.setFieldUpdater(dateAction);
//        //TODO
//        //example width, can be different
//        //widths shall be set automatically in
//        demandGrid.setColumnWidth(urgentCol, 60, Unit.PX);
//        demandGrid.addColumn(urgentCol, Storage.MSGS.urgency());
//
//// **** client rating column
//        Column<UserMessageDetail, String> ratingCo = factory.createClientRatingColumn(demandGrid.getSortHandler());
//        ratingCo.setFieldUpdater(action);
//        //TODO
//        //implement img header
//        demandGrid.addColumn(ratingCo, "img");

//// **** demand price column
//        Column<UserMessageDetail, String> priceCol = factory.createPriceColumn(demandGrid.getSortHandler());
//        priceCol.setFieldUpdater(action);
//        demandGrid.addColumn(priceCol, Storage.MSGS.price());

// **** creationDate column
        Column<UserMessageDetail, Date> creationCol =
                factory.createDateColumn(demandGrid.getSortHandler(), MessageColumnFactory.DATE_CREATED);
        creationCol.setFieldUpdater(dateAction);
        demandGrid.addColumn(creationCol, Storage.MSGS.createdDate());

    }

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
                (MultiSelectionModel<UserMessageDetail>) demandGrid.getSelectionModel();
        return model.getSelectedSet();
    }

    @Override
    public SimplePanel getWrapperPanel() {
        return wrapperPanel;
    }
}
