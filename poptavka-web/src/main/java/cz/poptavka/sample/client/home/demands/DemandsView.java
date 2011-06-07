package cz.poptavka.sample.client.home.demands;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import cz.poptavka.sample.client.home.demands.demand.DemandView;

import cz.poptavka.sample.client.main.common.OverflowComposite;
import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.demand.ClientDemandDetail;

import java.util.Date;

/**
 *
 * @author Martin Slavkovsky
 * TODO Lokalizacia.
 */
public class DemandsView extends OverflowComposite implements DemandsPresenter.DemandsViewInterface {

    private static DemandsUiBinder uiBinder = GWT.create(DemandsUiBinder.class);

    interface DemandsUiBinder extends UiBinder<Widget, DemandsView> {
    }
    @UiField
    VerticalPanel verticalContent;
    @UiField
    ListBox category;
    @UiField
    ListBox locality;
    @UiField
    DemandView demandView;
    @UiField
    Label demandDetailLabel;
    @UiField(provided = true)
    CellTable<ClientDemandDetail> cellTable;
    @UiField(provided = true)
    SimplePager pager;
    private AsyncDataProvider<ClientDemandDetail> dataProvider;

    public DemandsView() {
        initCellTable();
        initWidget(uiBinder.createAndBindUi(this));
        demandView.setVisible(false);
        demandDetailLabel.setVisible(false);
        StyleResource.INSTANCE.layout().ensureInjected();
    }

    @Override
    public DemandView getDemandView() {
        return this.demandView;
    }

    @Override
    public ListBox getCategoryList() {
        return category;
    }

    @Override
    public ListBox getLocalityList() {
        return locality;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public AsyncDataProvider<ClientDemandDetail> getDataProvider() {
        return dataProvider;
    }

    @Override
    public void setDataProvider(AsyncDataProvider<ClientDemandDetail> dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public CellTable<ClientDemandDetail> getCellTable() {
        return cellTable;
    }

    @Override
    public SimplePager getPager() {
        return pager;
    }

    @Override
    public Label getDemandDetailLabel() {
        return demandDetailLabel;
    }

    /**
     * Initialize this example.
     */
    private void initCellTable() {
        // Create a CellTable.
        cellTable = new CellTable<ClientDemandDetail>();
        cellTable.setWidth("100%", true);
        cellTable.setRowCount(2, true);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(cellTable);

        initTableColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns() {
        // Title
        Column<ClientDemandDetail, String> demandTitle = new Column<ClientDemandDetail, String>(
                new TextCell()) {

            @Override
            public String getValue(ClientDemandDetail object) {
                return object.getTitle();
            }
        };
        cellTable.addColumn(demandTitle, "Názov poptávky");
        cellTable.setColumnWidth(demandTitle, 320, Unit.PX);

        // Date
        Column<ClientDemandDetail, Date> demandDate = new Column<ClientDemandDetail, Date>(
                new DateCell()) {

            @Override
            public Date getValue(ClientDemandDetail object) {
                return object.getEndDate();
            }
        };
        cellTable.addColumn(demandDate, "Dátum");
        cellTable.setColumnWidth(demandDate, 80, Unit.PX);
    }
}
