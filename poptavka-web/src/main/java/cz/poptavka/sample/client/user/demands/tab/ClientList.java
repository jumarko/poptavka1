package cz.poptavka.sample.client.user.demands.tab;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
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
import cz.poptavka.sample.client.user.demands.tab.ClientListPresenter.IList;
import cz.poptavka.sample.client.user.widget.grid.ColumnFactory;
import cz.poptavka.sample.client.user.widget.grid.UniversalGrid;
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;
import cz.poptavka.sample.shared.domain.message.TableDisplay;

/**
 * IMPORTANT NOTE: This view is ReverseView. Because of eventBus calls from dataGrid table and these event calls are
 * defined in view, not in presenter.
 *
 * @author beho
 *
 */
public class ClientList extends Composite implements ReverseViewInterface<ClientListPresenter>, IList {

    private static DemandListUiBinder uiBinder = GWT.create(DemandListUiBinder.class);
    interface DemandListUiBinder extends UiBinder<Widget, ClientList> {
    }

    //attrribute preventing repeated loading of demand detail, when clicked on the same demand
    private long lastOpenedDemand = -1;


    //table handling buttons
    @UiField Button readBtn, unreadBtn, starBtn, unstarBtn;

    //DataGridattributes
    @UiField(provided = true)
    UniversalGrid<ClientDemandMessageDetail> demandGrid;
    @UiField(provided = true)
    SimplePager pager;

    //presenter
    private ClientListPresenter presenter;

    //detailWrapperPanel
    @UiField SimplePanel wrapperPanel;

    @Override
    public void setPresenter(ClientListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public ClientListPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();
        //demandGrid init
        demandGrid = new UniversalGrid<ClientDemandMessageDetail>(ClientDemandMessageDetail.KEY_PROVIDER);
        // Add a selection model so we can select cells.
        final SelectionModel<ClientDemandMessageDetail> selectionModel =
            new MultiSelectionModel<ClientDemandMessageDetail>(ClientDemandMessageDetail.KEY_PROVIDER);
        demandGrid.setSelectionModel(selectionModel, DefaultSelectionEventManager
            .<ClientDemandMessageDetail>createCheckboxManager());

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
    public UniversalGrid<ClientDemandMessageDetail> getGrid() {
        return demandGrid;
    }

    @Override
    public ListDataProvider<ClientDemandMessageDetail> getDataProvider() {
        return demandGrid.getDataProvider();
    }

    /**
     * Create all columns to the grid and define click actions.
     */
    public void initTableColumns(final SelectionModel<ClientDemandMessageDetail> selectionModel) {
        //init column factory
        ColumnFactory<ClientDemandMessageDetail> factory = new ColumnFactory<ClientDemandMessageDetail>();

// **** definition of all needed FieldUpdaters
        //TEXT FIELD UPDATER create common demand display fieldUpdater for demand and related conversation display
        FieldUpdater<ClientDemandMessageDetail, String> action = new FieldUpdater<ClientDemandMessageDetail, String>() {

            @Override
            public void update(int index, ClientDemandMessageDetail object,
                    String value) {
                TableDisplay obj = (TableDisplay) object;
                obj.setRead(true);
                demandGrid.redraw();
                /*
                presenter.displayDetailContent(object.getDemandId(), object.getMessageId(), object.getUserMessageId());
                */
            }
        };

        //DATE FIELD UPDATER displaying of demand detail. The fieldUpdater 'action' cannot be used,
        //because this is working with Date instead of String
        FieldUpdater<ClientDemandMessageDetail, Date> dateAction = new FieldUpdater<ClientDemandMessageDetail,
            Date>() {

            @Override
            public void update(int index, ClientDemandMessageDetail object,
                    Date value) {
                //for pure display detail action
                /*
                presenter.displayDetailContent(object.getDemandId(), object.getMessageId(), object.getUserMessageId());
                */
            }
        };

        Column<ClientDemandMessageDetail, DemandStatus> statusColumn =
            factory.createStatusColumn(demandGrid.getSortHandler());
        demandGrid.addColumn(statusColumn, Storage.MSGS.status());

// **** demand title column
        Column<ClientDemandMessageDetail, String> titleCol =
            factory.createTitleColumn(demandGrid.getSortHandler(), true);
        titleCol.setFieldUpdater(action);
        demandGrid.addColumn(titleCol, Storage.MSGS.title());

// **** demand price column
        Column<ClientDemandMessageDetail, String> priceCol = factory.createPriceColumn(demandGrid.getSortHandler());
        priceCol.setFieldUpdater(action);
        demandGrid.addColumn(priceCol, Storage.MSGS.price());

 // **** finishDate column
        Column<ClientDemandMessageDetail, Date> finishCol =
            factory.createDateColumn(demandGrid.getSortHandler(), ColumnFactory.DATE_FINISHED);
        finishCol.setFieldUpdater(dateAction);
        demandGrid.addColumn(finishCol, Storage.MSGS.finnishDate());

// **** expireDate column
        Column<ClientDemandMessageDetail, Date> expireCol =
            factory.createDateColumn(demandGrid.getSortHandler(), ColumnFactory.DATE_EXPIRE);
        expireCol.setFieldUpdater(dateAction);
        demandGrid.addColumn(expireCol, Storage.MSGS.expireDate());
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
        Set<ClientDemandMessageDetail> set = getSelectedMessageList();
        Iterator<ClientDemandMessageDetail> it = set.iterator();
        while (it.hasNext()) {
//            idList.add(it.next().getUserMessageId());
        }
        return idList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<ClientDemandMessageDetail> getSelectedMessageList() {
        MultiSelectionModel<ClientDemandMessageDetail> model
            = (MultiSelectionModel<ClientDemandMessageDetail>) demandGrid.getSelectionModel();
        return model.getSelectedSet();
    }

    @Override
    public SimplePanel getWrapperPanel() {
        return wrapperPanel;
    }
}
