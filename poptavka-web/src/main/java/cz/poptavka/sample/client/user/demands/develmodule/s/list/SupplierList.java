package cz.poptavka.sample.client.user.demands.develmodule.s.list;

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
import cz.poptavka.sample.client.user.demands.develmodule.s.list.SupplierListPresenter.IList;
import cz.poptavka.sample.client.user.demands.widget.table.ColumnFactory;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;
import cz.poptavka.sample.shared.domain.message.TableDisplay;

/**
 * IMPORTANT NOTE: This view is ReverseView. Because of eventBus calls from dataGrid table and these event calls are
 * defined in view, not in presenter.
 *
 * @author beho
 *
 */
public class SupplierList extends Composite implements ReverseViewInterface<SupplierListPresenter>, IList {

    private static DemandListUiBinder uiBinder = GWT.create(DemandListUiBinder.class);
    interface DemandListUiBinder extends UiBinder<Widget, SupplierList> {
    }

    //attrribute preventing repeated loading of demand detail, when clicked on the same demand
    private long lastOpenedDemand = -1;


    //table handling buttons
    @UiField Button readBtn, unreadBtn, starBtn, unstarBtn;

    //DataGridattributes
    @UiField(provided = true)
    SupplierListGrid<PotentialDemandMessage> demandGrid;
    @UiField(provided = true)
    SimplePager pager;

    //presenter
    private SupplierListPresenter presenter;

    //detailWrapperPanel
    @UiField SimplePanel wrapperPanel;

    @Override
    public void setPresenter(SupplierListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public SupplierListPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();
        //demandGrid init
        demandGrid = new SupplierListGrid<PotentialDemandMessage>(PotentialDemandMessage.KEY_PROVIDER);
        // Add a selection model so we can select cells.
        final SelectionModel<PotentialDemandMessage> selectionModel =
            new MultiSelectionModel<PotentialDemandMessage>(PotentialDemandMessage.KEY_PROVIDER);
        demandGrid.setSelectionModel(selectionModel, DefaultSelectionEventManager
            .<PotentialDemandMessage>createCheckboxManager());

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
    public SupplierListGrid<PotentialDemandMessage> getGrid() {
        return demandGrid;
    }

    @Override
    public ListDataProvider<PotentialDemandMessage> getDataProvider() {
        return demandGrid.getDataProvider();
    }

    /**
     * Create all columns to the grid and define click actions.
     */
    public void initTableColumns(final SelectionModel<PotentialDemandMessage> selectionModel) {
        //init column factory
        ColumnFactory<PotentialDemandMessage> factory = new ColumnFactory<PotentialDemandMessage>();

        //ROW selection column and set it's width to 40px.
        //contains custom header providing selecting all visible items
        final Header<Boolean> header = factory.createCheckBoxHeader();
        //select
        header.setUpdater(new ValueUpdater<Boolean>() {

            @Override
            public void update(Boolean value) {
                List<PotentialDemandMessage> rows = demandGrid.getVisibleItems();
                for (PotentialDemandMessage row : rows) {
                    selectionModel.setSelected(row, value);
                }

            }
        });
        demandGrid.addColumn(factory.createCheckboxColumn(selectionModel), header);
        demandGrid.setColumnWidth(demandGrid.getColumn(ColumnFactory.COL_ZERO), ColumnFactory.WIDTH_40, Unit.PX);

        //Star collumn with defined valueUpdater and custom style
        Column<PotentialDemandMessage, Boolean> starColumn = factory.createStarColumn();
        starColumn.setCellStyleNames(Storage.RSCS.grid().cellTableHandCursor());
        starColumn.setFieldUpdater(new FieldUpdater<PotentialDemandMessage, Boolean>() {

            @Override
            public void update(int index, PotentialDemandMessage object, Boolean value) {
                TableDisplay obj = (TableDisplay) object;
                obj.setStarred(!value);
                demandGrid.redraw();
                Long[] item = new Long[] {object.getUserMessageId()};
                presenter.updateStarStatus(Arrays.asList(item), !value);
            }
        });
        demandGrid.setColumnWidth(starColumn, ColumnFactory.WIDTH_40, Unit.PX);
        demandGrid.addColumn(starColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));

        //create common demand display fieldUpdater for demand and related conversation display
        FieldUpdater<PotentialDemandMessage, String> action = new FieldUpdater<PotentialDemandMessage, String>() {

            @Override
            public void update(int index, PotentialDemandMessage object,
                    String value) {
                TableDisplay obj = object;
                obj.setRead(true);
                demandGrid.redraw();
                presenter.displayDetailContent(object.getDemandId(), object.getMessageId(), object.getUserMessageId());
            }
        };

        //just for displaying of demand detail. The fieldUpdater 'action' cannot be used, because this is working
        //with Date instead of String
        FieldUpdater<PotentialDemandMessage, Date> dateAction = new FieldUpdater<PotentialDemandMessage,
            Date>() {

            @Override
            public void update(int index, PotentialDemandMessage object,
                    Date value) {
                //for pure display detail action
                presenter.displayDetailContent(object.getDemandId(), object.getMessageId(), object.getUserMessageId());
            }
        };

        Column<PotentialDemandMessage, String> clientCol = factory.createClientColumn(null);
        demandGrid.addColumn(clientCol, Storage.MSGS.client());

        //demand title column
        Column<PotentialDemandMessage, String> titleCol = factory.createTitleColumn(demandGrid.getSortHandler());
        titleCol.setFieldUpdater(action);
        demandGrid.addColumn(titleCol, Storage.MSGS.title());

        //urgent column
        Column<PotentialDemandMessage, Date> urgentCol = factory.createUrgentColumn(demandGrid.getSortHandler());
        urgentCol.setFieldUpdater(dateAction);
        //example width, can be different
        demandGrid.setColumnWidth(urgentCol, 60, Unit.PX);
        demandGrid.addColumn(urgentCol, Storage.MSGS.urgency());

        //demand price column
        Column<PotentialDemandMessage, String> priceCol = factory.createPriceColumn(demandGrid.getSortHandler());
        priceCol.setFieldUpdater(action);
        demandGrid.addColumn(priceCol, Storage.MSGS.price());

        //creationDate column
        Column<PotentialDemandMessage, Date> creationCol =
            factory.createCreationDateColumn(demandGrid.getSortHandler());
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
        Set<PotentialDemandMessage> set = getSelectedMessageList();
        Iterator<PotentialDemandMessage> it = set.iterator();
        while (it.hasNext()) {
            idList.add(it.next().getUserMessageId());
        }
        return idList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<PotentialDemandMessage> getSelectedMessageList() {
        MultiSelectionModel<PotentialDemandMessage> model
            = (MultiSelectionModel<PotentialDemandMessage>) demandGrid.getSelectionModel();
        return model.getSelectedSet();
    }

    @Override
    public SimplePanel getWrapperPanel() {
        return wrapperPanel;
    }
}
