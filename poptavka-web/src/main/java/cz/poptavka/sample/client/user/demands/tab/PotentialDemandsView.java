package cz.poptavka.sample.client.user.demands.tab;

import java.util.Comparator;
import java.util.Set;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;

import cz.poptavka.sample.client.main.common.OverflowComposite;
import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.demand.PotentialDemandDetail;

/**
 * View representing potential demands for supplier. Supplier can list them,
 * reply to selected, cancel displaying and other magic tricks
 *
 * @author Beho
 *
 */
public class PotentialDemandsView extends OverflowComposite implements
        PotentialDemandsPresenter.IPotentialDemands {

    private static PotentialDemandsViewUiBinder uiBinder = GWT
            .create(PotentialDemandsViewUiBinder.class);

    private static final StyleResource RSCS = GWT.create(StyleResource.class);

    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    interface PotentialDemandsViewUiBinder extends
            UiBinder<Widget, PotentialDemandsView> {
    }

    @UiField(provided = true)
    CellTable<PotentialDemandDetail> cellTable;
    @UiField(provided = true)
    SimplePager pager;
    @UiField
    Button replyBtn, deleteBtn, moreActionsBtn, refreshBtn;

    @UiField
    SimplePanel detailSection;

    // TODO remove then
    @UiField
    SimplePanel develPanel;

    // @UiField ToggleButton moreActionsBtn;

    private ListDataProvider<PotentialDemandDetail> dataProvider = new ListDataProvider<PotentialDemandDetail>();

    @Override
    public void createView() {
        GWT.log("LOAD");
        initCellWidget();
        initWidget(uiBinder.createAndBindUi(this));
        setParentOverflow(detailSection, Overflow.AUTO);

        // Element newHeadDiv = header.getElement();
        // GWT.log(newHeadDiv.getNodeName());
        // Element newTable = DOM.createTable();
        // GWT.log(newTable.getNodeName());
        // Element tableHead = (Element)
        // cellTable.getElement().getFirstChildElement();
        // GWT.log(tableHead.getNodeName());
        //
        // DOM.appendChild((Element) newHeadDiv, newTable);
        // DOM.appendChild(newTable, tableHead);
    }

    private void initCellWidget() {
        // Init table
        cellTable = new CellTable<PotentialDemandDetail>(KEY_PROVIDER);
        // cellTable.setPageSize(5);
        cellTable.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
        cellTable
                .setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);

        ListHandler<PotentialDemandDetail> sorHandler = new ListHandler<PotentialDemandDetail>(
                dataProvider.getList());
        cellTable.addColumnSortHandler(sorHandler);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT
                .create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
                true);
        pager.setDisplay(cellTable);

        final MultiSelectionModel<PotentialDemandDetail> selectionModel =
                new MultiSelectionModel<PotentialDemandDetail>(KEY_PROVIDER);
        cellTable.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<PotentialDemandDetail>createCheckboxManager());

        initTableColumns(selectionModel, sorHandler);

        // dataProvider
        dataProvider.addDataDisplay(cellTable);
    }

    private void initTableColumns(
            final SelectionModel<PotentialDemandDetail> tableSelectionModel,
            ListHandler<PotentialDemandDetail> sortHandler) {
        // MultipleSelection Checkbox
        Column<PotentialDemandDetail, Boolean> checkBoxColumn = new Column<PotentialDemandDetail, Boolean>(
                new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(PotentialDemandDetail object) {
                return tableSelectionModel.isSelected(object);
            }
        };

        // Demand Title Column
        Column<PotentialDemandDetail, String> titleColumn = (new Column<PotentialDemandDetail, String>(
                new TextCell()) {
            @Override
            public String getValue(PotentialDemandDetail object) {
                return object.getDemandTitle();
            }
        });

        // Demand Price Column
        Column<PotentialDemandDetail, String> priceColumn = (new Column<PotentialDemandDetail, String>(
                new TextCell()) {
            @Override
            public String getValue(PotentialDemandDetail object) {
                return (object.getPrice().intValue() < 0 ? "none" : object.getPrice().toString());
            }
        });

        final DateTimeFormat dateFormat = DateTimeFormat
                .getFormat(PredefinedFormat.DATE_MEDIUM);

        // Demand Finish Column
        Column<PotentialDemandDetail, String> endDateColumn = (new Column<PotentialDemandDetail, String>(
                new TextCell()) {
            @Override
            public String getValue(PotentialDemandDetail object) {
                return dateFormat.format(object.getEndDate());
            }
        });

        // Demand sent Date column
        Column<PotentialDemandDetail, String> sentDateColumn = (new Column<PotentialDemandDetail, String>(
                new TextCell()) {
            @Override
            public String getValue(PotentialDemandDetail object) {
                return dateFormat.format(object.getSent());
            }
        });

        // sort methods
        titleColumn.setSortable(true);
        sortHandler.setComparator(titleColumn,
                new Comparator<PotentialDemandDetail>() {
                    @Override
                    public int compare(PotentialDemandDetail o1,
                            PotentialDemandDetail o2) {
                        if (o1 == o2) {
                            return 0;
                        }

                        // Compare the name columns.
                        if (o1 != null) {
                            return (o2 != null) ? o1.getDemandTitle()
                                    .compareTo(o2.getDemandTitle()) : 1;
                        }
                        return -1;
                    }
                });
        priceColumn.setSortable(true);
        sortHandler.setComparator(priceColumn,
                new Comparator<PotentialDemandDetail>() {
                    @Override
                    public int compare(PotentialDemandDetail o1,
                            PotentialDemandDetail o2) {
                        return o1.getPrice().compareTo(o2.getPrice());
                    }
                });

        // add columns into table
        cellTable.addColumn(checkBoxColumn);
        cellTable.addColumn(titleColumn, MSGS.title());
        cellTable.addColumn(priceColumn, MSGS.price());
        cellTable.addColumn(endDateColumn, MSGS.endDate());
        cellTable.addColumn(sentDateColumn, MSGS.expireDate());

    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public MultiSelectionModel<PotentialDemandDetail> getSelectionModel() {
        return (MultiSelectionModel<PotentialDemandDetail>) cellTable.getSelectionModel();
    }

    private static final ProvidesKey<PotentialDemandDetail> KEY_PROVIDER = new ProvidesKey<PotentialDemandDetail>() {
        @Override
        public Object getKey(PotentialDemandDetail item) {
            return item == null ? null : item.getDemandId();
        }
    };

    @Override
    public ListDataProvider<PotentialDemandDetail> getDataProvider() {
        return dataProvider;
    }

    @Override
    public Button getReplyButton() {
        return replyBtn;
    }

    @Override
    public Button getDeleteButton() {
        return deleteBtn;
    }

    @Override
    public Button getActionButton() {
        return moreActionsBtn;
    }

    @Override
    public Button getRefreshButton() {
        return refreshBtn;
    }

    @Override
    public Set<PotentialDemandDetail> getSelectedSet() {
        return getSelectionModel().getSelectedSet();
    }

    @Override
    public SimplePanel getDetailSection() {
        return detailSection;
    }

}
