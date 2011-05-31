package cz.poptavka.sample.client.user.demands.tab;

import java.util.Comparator;
import java.util.Set;

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

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.OfferDemandDetail;

public class OffersView extends Composite implements OffersPresenter.OffersInterface {

    private static OffersViewUiBinder uiBinder = GWT.create(OffersViewUiBinder.class);
    interface OffersViewUiBinder extends UiBinder<Widget, OffersView> { }

    private static final StyleResource RSCS = GWT.create(StyleResource.class);

    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    @UiField(provided = true)
    CellTable<OfferDemandDetail> demandTable;
    @UiField(provided = true)
    SimplePager demandPager;
    @UiField
    Button replyBtn, deleteBtn, moreActionsBtn, refreshBtn;

    @UiField
    SimplePanel detailSection;

    // TODO remove then
    @UiField
    SimplePanel develPanel;

    private ListDataProvider<OfferDemandDetail> dataProvider
        = new ListDataProvider<OfferDemandDetail>();

    @Override
    public void createView() {
        GWT.log("LOAD");
        initCellWidget();
        initWidget(uiBinder.createAndBindUi(this));

        // Element newHeadDiv = header.getElement();
        // GWT.log(newHeadDiv.getNodeName());
        // Element newTable = DOM.createTable();
        // GWT.log(newTable.getNodeName());
        // Element tableHead = (Element)
        // demandTable.getElement().getFirstChildElement();
        // GWT.log(tableHead.getNodeName());
        //
        // DOM.appendChild((Element) newHeadDiv, newTable);
        // DOM.appendChild(newTable, tableHead);
    }

    private void initCellWidget() {
        // Init table
        demandTable = new CellTable<OfferDemandDetail>(KEY_PROVIDER);
        // TODO setPageSize
        // demandTable.setPageSize(5);

        ListHandler<OfferDemandDetail> sorHandler = new ListHandler<OfferDemandDetail>(
                dataProvider.getList());
        demandTable.addColumnSortHandler(sorHandler);

        // Create a demandPager to control the table.
        SimplePager.Resources demandPagerResources = GWT.create(SimplePager.Resources.class);
        demandPager = new SimplePager(TextLocation.CENTER, demandPagerResources, false, 0,
                true);
        demandPager.setDisplay(demandTable);

        final MultiSelectionModel<OfferDemandDetail> selectionModel = new MultiSelectionModel<OfferDemandDetail>(
                KEY_PROVIDER);
        demandTable.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<OfferDemandDetail>createDefaultManager());

        initTableColumns(selectionModel, sorHandler);

        // dataProvider
        dataProvider.addDataDisplay(demandTable);
    }

    private void initTableColumns(final SelectionModel<OfferDemandDetail> tableSelectionModel,
            ListHandler<OfferDemandDetail> sortHandler) {
        // MultipleSelection Checkbox
//        Column<OfferDemandDetail, Boolean> checkBoxColumn = new Column<OfferDemandDetail, Boolean>(
//                new CheckboxCell(true, false)) {
//            @Override
//            public Boolean getValue(OfferDemandDetail object) {
//                return tableSelectionModel.isSelected(object);
//            }
//        };

        // Demand Title Column
        Column<OfferDemandDetail, String> titleColumn = (new Column<OfferDemandDetail, String>(
                new TextCell()) {
            @Override
            public String getValue(OfferDemandDetail object) {
                return object.getTitle() + "(" + object.getNumberOfOffers()
                    + "/" + object.getMaxOffers() + ")";
            }
        });

        // Demand Price Column
        Column<OfferDemandDetail, String> priceColumn = (new Column<OfferDemandDetail, String>(
                new TextCell()) {
            @Override
            public String getValue(OfferDemandDetail object) {
                // TODO add 'none' value into Localizable resources
                return (object.getPrice() == null ? "None" : object.getPrice().toString());
            }
        });

        final DateTimeFormat dateFormat = DateTimeFormat
                .getFormat(PredefinedFormat.DATE_MEDIUM);

        // Demand Finish Column
        Column<OfferDemandDetail, String> endDateColumn = (new Column<OfferDemandDetail, String>(
                new TextCell()) {
            @Override
            public String getValue(OfferDemandDetail object) {
                return dateFormat.format(object.getEndDate());
            }
        });

        // Demand sent Date column
        Column<OfferDemandDetail, String> validToDateColumn = (new Column<OfferDemandDetail, String>(
                new TextCell()) {
            @Override
            public String getValue(OfferDemandDetail object) {
                return dateFormat.format(object.getFinishDate());
            }
        });

        // sort methods ****************************
        titleColumn.setSortable(true);
        sortHandler.setComparator(titleColumn,
                new Comparator<OfferDemandDetail>() {
                    @Override
                    public int compare(OfferDemandDetail o1,
                            OfferDemandDetail o2) {
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
        sortHandler.setComparator(priceColumn,
                new Comparator<OfferDemandDetail>() {
                    @Override
                    public int compare(OfferDemandDetail o1,
                            OfferDemandDetail o2) {
                        return o1.getPrice().compareTo(o2.getPrice());
                    }
                });

        endDateColumn.setSortable(true);
        validToDateColumn.setSortable(true);
        Comparator<OfferDemandDetail> dateComparator = new Comparator<OfferDemandDetail>() {
            @Override
            public int compare(OfferDemandDetail o1, OfferDemandDetail o2) {
                // TODO Auto-generated method stub
                return o1.getEndDate().compareTo(o2.getEndDate());
            }
        };
        sortHandler.setComparator(endDateColumn, dateComparator);
        sortHandler.setComparator(validToDateColumn, dateComparator);
        // add columns into table
//        demandTable.addColumn(checkBoxColumn);
        demandTable.addColumn(titleColumn, MSGS.title());
        demandTable.addColumn(priceColumn, MSGS.price());
        demandTable.addColumn(endDateColumn, MSGS.endDate());
        demandTable.addColumn(validToDateColumn, MSGS.expireDate());

    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public MultiSelectionModel<OfferDemandDetail> getSelectionModel() {
        return (MultiSelectionModel<OfferDemandDetail>) demandTable.getSelectionModel();
    }

    private static final ProvidesKey<OfferDemandDetail> KEY_PROVIDER = new ProvidesKey<OfferDemandDetail>() {
        @Override
        public Object getKey(OfferDemandDetail item) {
            return item == null ? null : item.getDemandId();
        }
    };

    @Override
    public ListDataProvider<OfferDemandDetail> getDataProvider() {
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
    public Set<OfferDemandDetail> getSelectedSet() {
        return getSelectionModel().getSelectedSet();
    }

    @Override
    public SimplePanel getDetailSection() {
        return detailSection;
    }

}
