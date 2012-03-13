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
import cz.poptavka.sample.shared.domain.demandsModule.ClientDemandDetail;
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
    UniversalGrid<ClientDemandDetail> demandGrid;
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
        demandGrid = new UniversalGrid<ClientDemandDetail>(ClientDemandDetail.KEY_PROVIDER);
        // Add a selection model so we can select cells.
        final SelectionModel<ClientDemandDetail> selectionModel =
            new MultiSelectionModel<ClientDemandDetail>(ClientDemandDetail.KEY_PROVIDER);
        demandGrid.setSelectionModel(selectionModel, DefaultSelectionEventManager
            .<ClientDemandDetail>createCheckboxManager());

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
    public UniversalGrid<ClientDemandDetail> getGrid() {
        return demandGrid;
    }

    @Override
    public ListDataProvider<ClientDemandDetail> getDataProvider() {
        return demandGrid.getDataProvider();
    }

    /**
     * Create all columns to the grid and define click actions.
     */
    public void initTableColumns(final SelectionModel<ClientDemandDetail> selectionModel) {
        //init column factory
        ColumnFactory<ClientDemandDetail> factory = new ColumnFactory<ClientDemandDetail>();

// **** definition of all needed FieldUpdaters
        //TEXT FIELD UPDATER create common demand display fieldUpdater for demand and related conversation display
        FieldUpdater<ClientDemandDetail, String> action = new FieldUpdater<ClientDemandDetail, String>() {

            @Override
            public void update(int index, ClientDemandDetail object,
                    String value) {
                TableDisplay obj = (TableDisplay) object;
                obj.setRead(true);
                demandGrid.redraw();
//presenter.displayDetailContent(object.getDemandId(), object.getMessageId(), object.getUserMessageId());
            }
        };

        //DATE FIELD UPDATER displaying of demand detail. The fieldUpdater 'action' cannot be used,
        //because this is working with Date instead of String
        FieldUpdater<ClientDemandDetail, Date> dateAction = new FieldUpdater<ClientDemandDetail,
            Date>() {

            @Override
            public void update(int index, ClientDemandDetail object,
                    Date value) {
                //for pure display detail action
//presenter.displayDetailContent(object.getDemandId(), object.getMessageId(), object.getUserMessageId());
            }
        };

        Column<ClientDemandDetail, DemandStatus> statusColumn =
            factory.createStatusColumn(demandGrid.getSortHandler());
        demandGrid.addColumn(statusColumn, Storage.MSGS.status());

// **** demand title column
        Column<ClientDemandDetail, String> titleCol =
            factory.createTitleColumn(demandGrid.getSortHandler(), true);
        titleCol.setFieldUpdater(action);
        demandGrid.addColumn(titleCol, Storage.MSGS.title());

// **** demand price column
        Column<ClientDemandDetail, String> priceCol = factory.createPriceColumn(demandGrid.getSortHandler());
        priceCol.setFieldUpdater(action);
        demandGrid.addColumn(priceCol, Storage.MSGS.price());

 // **** finishDate column
        Column<ClientDemandDetail, Date> finishCol =
            factory.createDateColumn(demandGrid.getSortHandler(), ColumnFactory.DATE_FINISHED);
        finishCol.setFieldUpdater(dateAction);
        demandGrid.addColumn(finishCol, Storage.MSGS.finnishDate());

// **** expireDate column
        //Martin uncoment if ColumnFactory.DATE_VALIDTO is implemented
        Column<ClientDemandDetail, Date> expireCol =
            factory.createDateColumn(demandGrid.getSortHandler(), ColumnFactory.DATE_EXPIRE);
        expireCol.setFieldUpdater(dateAction);
        demandGrid.addColumn(expireCol, Storage.MSGS.validTo());
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
        Set<ClientDemandDetail> set = getSelectedMessageList();
        Iterator<ClientDemandDetail> it = set.iterator();
        while (it.hasNext()) {
//            idList.add(it.next().getUserMessageId());
        }
        return idList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<ClientDemandDetail> getSelectedMessageList() {
        MultiSelectionModel<ClientDemandDetail> model
            = (MultiSelectionModel<ClientDemandDetail>) demandGrid.getSelectionModel();
        return model.getSelectedSet();
    }

    @Override
    public SimplePanel getWrapperPanel() {
        return wrapperPanel;
    }
}
