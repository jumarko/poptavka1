package cz.poptavka.sample.client.user.demands.tab;

import java.util.Comparator;
import java.util.Set;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;

import cz.poptavka.sample.shared.domain.DemandDetail;

/**
 * View representing potential demands for supplier. Supplier can list them, reply to selected,
 * cancel displaying and other magic tricks
 *
 * @author Beho
 *
 */
public class PotentialDemandsView extends Composite implements PotentialDemandsPresenter.IPotentialDemands {

    private static PotentialDemandsViewUiBinder uiBinder = GWT.create(PotentialDemandsViewUiBinder.class);

    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    interface PotentialDemandsViewUiBinder extends UiBinder<Widget, PotentialDemandsView> {
    }

    @UiField(provided = true) CellTable<DemandDetail> cellTable;
    @UiField(provided = true) SimplePager pager;
    @UiField Button replyBtn, deleteBtn, moreActionsBtn, refreshBtn;
    @UiField SimplePanel detailSection;

//    @UiField ToggleButton moreActionsBtn;

    private ListDataProvider<DemandDetail> dataProvider = new ListDataProvider<DemandDetail>();
    private MultiSelectionModel<DemandDetail> selectionModel;

    @Override
    public void createView() {
        GWT.log("LOAD");
        initCellWidget();
        initWidget(uiBinder.createAndBindUi(this));

//        Element newHeadDiv = header.getElement();
//        GWT.log(newHeadDiv.getNodeName());
//        Element newTable = DOM.createTable();
//        GWT.log(newTable.getNodeName());
//        Element tableHead = (Element) cellTable.getElement().getFirstChildElement();
//        GWT.log(tableHead.getNodeName());
//
//        DOM.appendChild((Element) newHeadDiv, newTable);
//        DOM.appendChild(newTable, tableHead);
    }

    private void initCellWidget() {

        // Init table
        cellTable = new CellTable<DemandDetail>(KEY_PROVIDER);
//        cellTable.setPageSize(5);
        cellTable.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
        cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);

        // dataProvider
        dataProvider.addDataDisplay(cellTable);

        ListHandler<DemandDetail> sorHandler = new ListHandler<DemandDetail>(
                dataProvider.getList());
        cellTable.addColumnSortHandler(sorHandler);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(cellTable);

        selectionModel = new MultiSelectionModel<DemandDetail>(KEY_PROVIDER);
        cellTable.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<DemandDetail>createCheckboxManager());

        initTableColumns(selectionModel, sorHandler);
    }

    private void initTableColumns(final SelectionModel<DemandDetail> selectionModel,
            ListHandler<DemandDetail> sortHandler) {
        // MultipleSelection Checkbox
        Column<DemandDetail, Boolean> checkBoxColumn = new Column<DemandDetail,
            Boolean>(new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(DemandDetail object) {
                return selectionModel.isSelected(object);
            }
        };

        // Demand Title Column
        Column<DemandDetail, String> titleColumn = (new Column<DemandDetail, String>(new TextCell()) {
            @Override
            public String getValue(DemandDetail object) {
                return object.getTitle();
            }
        });

        // Demand Price Column
        Column<DemandDetail, String> priceColumn = (new Column<DemandDetail, String>(new TextCell()) {
            @Override
            public String getValue(DemandDetail object) {
                return object.getPrice().toString();
            }
        });

        final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);

        // Demand Finish Column
        Column<DemandDetail, String> endDateColumn = (new Column<DemandDetail, String>(new TextCell()) {
            @Override
            public String getValue(DemandDetail object) {
                return dateFormat.format(object.getEndDate());
            }
        });

        // Demand Finish Column
        Column<DemandDetail, String> expireDateColumn = (new Column<DemandDetail, String>(new TextCell()) {
            @Override
            public String getValue(DemandDetail object) {
                return dateFormat.format(object.getExpireDate());
            }
        });

        // sort methods
        titleColumn.setSortable(true);
        sortHandler.setComparator(titleColumn, new Comparator<DemandDetail>() {
            @Override
            public int compare(DemandDetail o1, DemandDetail o2) {
                if (o1 == o2) {
                    return 0;
                }

                // Compare the name columns.
                if (o1 != null) {
                    return (o2 != null) ? o1.getTitle().compareTo(o2.getTitle()) : 1;
                }
                return -1;
            }
        });
        priceColumn.setSortable(true);
        sortHandler.setComparator(priceColumn, new Comparator<DemandDetail>() {
            @Override
            public int compare(DemandDetail o1, DemandDetail o2) {
                return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
            }
        });

        // add columns into table
        cellTable.addColumn(checkBoxColumn);
        cellTable.addColumn(titleColumn, MSGS.title());
        cellTable.addColumn(priceColumn, MSGS.price());
        cellTable.addColumn(endDateColumn, MSGS.endDate());
        cellTable.addColumn(expireDateColumn, MSGS.expireDate());
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public MultiSelectionModel<DemandDetail> getSelectionModel() {
        return selectionModel;
    }

    private static final ProvidesKey<DemandDetail> KEY_PROVIDER = new ProvidesKey<DemandDetail>() {
        @Override
        public Object getKey(DemandDetail item) {
            return item == null ? null : item.getId();
        }
    };

    @Override
    public ListDataProvider<DemandDetail> getDataProvider() {
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
    public Set<DemandDetail> getSelectedSet() {
        return selectionModel.getSelectedSet();
    }

    @Override
    public SimplePanel getDetailSection() {
        return detailSection;
    }

}
