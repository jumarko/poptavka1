package cz.poptavka.sample.client.user.demands.tab;

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
import cz.poptavka.sample.client.user.demands.tab.SupplierListPresenter.IList;
import cz.poptavka.sample.client.user.widget.grid.ColumnFactory;
import cz.poptavka.sample.client.user.widget.grid.UniversalGrid;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;
import cz.poptavka.sample.shared.domain.message.TableDisplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * IMPORTANT NOTE: This view is ReverseView. Because of eventBus calls from dataGrid table and these event calls are
 * defined in view, not in presenter.
 *
 * @author beho
 *
 */
public class SupplierList extends Composite implements //IList {
    ReverseViewInterface<SupplierListPresenter>, IList {

    private static DemandListUiBinder uiBinder = GWT.create(DemandListUiBinder.class);
    interface DemandListUiBinder extends UiBinder<Widget, SupplierList> {
    }

    //attrribute preventing repeated loading of demand detail, when clicked on the same demand
    private long lastOpenedDemand = -1;


    //table handling buttons
    @UiField Button readBtn, unreadBtn, starBtn, unstarBtn;

    //DataGridattributes
    @UiField(provided = true)
    UniversalGrid<PotentialDemandMessage> demandGrid;
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
        demandGrid = new UniversalGrid<PotentialDemandMessage>(PotentialDemandMessage.KEY_PROVIDER);
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
    public UniversalGrid<PotentialDemandMessage> getGrid() {
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

// **** definition of all needed FieldUpdaters
        //TEXT FIELD UPDATER create common demand display fieldUpdater for demand and related conversation display
        FieldUpdater<PotentialDemandMessage, String> action = createCommonDemandUpdater();

        //DATE FIELD UPDATER displaying of demand detail. The fieldUpdater 'action' cannot be used,
        //because this is working with Date instead of String
        FieldUpdater<PotentialDemandMessage, Date> dateAction = createDemandDateUpdater();

        //STAR COLUMN FIELD UPDATER
        FieldUpdater<PotentialDemandMessage, Boolean> starUpdater = createDemandStarredUpdater();

// **** ROW selection column and set it's width to 40px.
        //contains custom header providing selecting all visible items
        final Header<Boolean> header = factory.createCheckBoxHeader();
        setHeaderUpdater(selectionModel, header);

        demandGrid.addColumn(factory.createCheckboxColumn(selectionModel), header);
        demandGrid.setColumnWidth(demandGrid.getColumn(ColumnFactory.COL_ZERO), ColumnFactory.WIDTH_40, Unit.PX);

// **** Star collumn with defined valueUpdater and custom style
        Column<PotentialDemandMessage, Boolean> starColumn = factory.createStarColumn();
        starColumn.setFieldUpdater(starUpdater);
        //TODO
        //testing if assigning style in columnFactory works - works well 7.11.11 Beho
        //but keep here for reference
        //starColumn.setCellStyleNames(Storage.RSCS.grid().cellTableHandCursor());
        demandGrid.setColumnWidth(starColumn, ColumnFactory.WIDTH_40, Unit.PX);
        demandGrid.addColumn(starColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));

// **** client column
        setClientColumnUpdater(factory, action);

// **** demand title column
        setTitleColumnUpdater(factory, action);

// **** urgent column
        Column<PotentialDemandMessage, Date> urgentCol = setUrgentColumnUpdater(factory, dateAction);
        //TODO
        //example width, can be different
        //widths shall be set automatically in
        demandGrid.setColumnWidth(urgentCol, 60, Unit.PX);
        demandGrid.addColumn(urgentCol, Storage.MSGS.urgency());

// **** client rating column
        Column<PotentialDemandMessage, String> ratingCo = setRatingUpdater(factory, action);
        //TODO
        //implement img header
        demandGrid.addColumn(ratingCo, "img");

// **** demand price column
        setDemandPriceUpdater(factory, action);

// **** creationDate column
        setCreationDateUpdater(factory, dateAction);

    }

    private void setHeaderUpdater(final SelectionModel<PotentialDemandMessage> selectionModel, Header<Boolean> header) {
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
    }

    private void setClientColumnUpdater(ColumnFactory<PotentialDemandMessage> factory,
                                        FieldUpdater<PotentialDemandMessage, String> action) {
        Column<PotentialDemandMessage, String> clientCol = factory.createClientColumn(null, true);
        clientCol.setFieldUpdater(action);
        demandGrid.addColumn(clientCol, Storage.MSGS.client());
    }

    private void setTitleColumnUpdater(ColumnFactory<PotentialDemandMessage> factory,
                                       FieldUpdater<PotentialDemandMessage, String> action) {
        Column<PotentialDemandMessage, String> titleCol = factory.createTitleColumn(demandGrid.getSortHandler(), false);
        titleCol.setFieldUpdater(action);
        demandGrid.addColumn(titleCol, Storage.MSGS.title());
    }

    private Column<PotentialDemandMessage, Date> setUrgentColumnUpdater(ColumnFactory<PotentialDemandMessage> factory,
                FieldUpdater<PotentialDemandMessage, Date> dateAction) {
        Column<PotentialDemandMessage, Date> urgentCol = factory.createUrgentColumn(demandGrid.getSortHandler());
        urgentCol.setFieldUpdater(dateAction);
        return urgentCol;
    }

    private Column<PotentialDemandMessage, String> setRatingUpdater(ColumnFactory<PotentialDemandMessage> factory,
                FieldUpdater<PotentialDemandMessage, String> action) {
        Column<PotentialDemandMessage, String> ratingCo = factory.createRatingColumn(
                demandGrid.getSortHandler());
        ratingCo.setFieldUpdater(action);
        return ratingCo;
    }

    private void setDemandPriceUpdater(ColumnFactory<PotentialDemandMessage> factory,
                                       FieldUpdater<PotentialDemandMessage, String> action) {
        Column<PotentialDemandMessage, String> priceCol = factory.createPriceColumn(demandGrid.getSortHandler());
        priceCol.setFieldUpdater(action);
        demandGrid.addColumn(priceCol, Storage.MSGS.price());
    }

    private void setCreationDateUpdater(ColumnFactory<PotentialDemandMessage> factory,
                                        FieldUpdater<PotentialDemandMessage, Date> dateAction) {
        Column<PotentialDemandMessage, Date> creationCol =
            factory.createDateColumn(demandGrid.getSortHandler(), ColumnFactory.DATE_CREATED);
        creationCol.setFieldUpdater(dateAction);
        demandGrid.addColumn(creationCol, Storage.MSGS.createdDate());
    }

    private FieldUpdater<PotentialDemandMessage, Boolean> createDemandStarredUpdater() {
        return new FieldUpdater<PotentialDemandMessage, Boolean>()
            {
                @Override
                public void update(int index, PotentialDemandMessage object, Boolean value) {
                    TableDisplay obj = (TableDisplay) object;
                    obj.setStarred(!value);
                    demandGrid.redraw();
                    Long[] item = new Long[] {object.getUserMessageId()};
                    presenter.updateStarStatus(Arrays.asList(item), !value);
                }
            };
    }

    private FieldUpdater<PotentialDemandMessage, Date> createDemandDateUpdater() {
        return new FieldUpdater<PotentialDemandMessage,
                Date>() {

                @Override
                public void update(int index, PotentialDemandMessage object, Date value) {
                    //for pure display detail action
                    presenter.displayDetailContent(object.getDemandId(),
                            object.getMessageId(), object.getUserMessageId());
                }
            };
    }

    private FieldUpdater<PotentialDemandMessage, String> createCommonDemandUpdater() {
        return new FieldUpdater<PotentialDemandMessage, String>() {

                @Override
                public void update(int index, PotentialDemandMessage object, String value) {
                    TableDisplay obj = object;
                    obj.setRead(true);
                    demandGrid.redraw();
                    presenter.displayDetailContent(object.getDemandId(), object.getMessageId(),
                            object.getUserMessageId());
                }
            };
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
