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
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
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
import com.mvp4g.client.view.ReverseViewInterface;

import cz.poptavka.sample.client.main.common.OverflowComposite;
import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;

/**
 * View representing potential demands for supplier. Supplier can list them,
 * reply to selected, cancel displaying and other magic tricks
 *
 * @author Beho
 *
 */
public class PotentialDemandsView extends OverflowComposite implements
        PotentialDemandsPresenter.IPotentialDemands, ReverseViewInterface<PotentialDemandsPresenter> {

    private static PotentialDemandsViewUiBinder uiBinder = GWT
            .create(PotentialDemandsViewUiBinder.class);

    private static final StyleResource RSCS = GWT.create(StyleResource.class);

    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    interface PotentialDemandsViewUiBinder extends
            UiBinder<Widget, PotentialDemandsView> {
    }

    @UiField(provided = true)
    CellTable<BaseDemandDetail> cellTable;
    @UiField(provided = true)
    SimplePager pager;
    @UiField
    Button replyBtn, deleteBtn, markBtn, moreActionsBtn, refreshBtn;

    @UiField
    SimplePanel detailSection;

    // TODO remove then
    @UiField
    SimplePanel develPanel;

    // @UiField ToggleButton moreActionsBtn;

    private ListDataProvider<BaseDemandDetail> dataProvider = new ListDataProvider<BaseDemandDetail>();

    private PotentialDemandsPresenter presenter;

    // Global read status for selected messages
    private boolean readStatus = false;

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
        cellTable = new CellTable<BaseDemandDetail>(KEY_PROVIDER);
        // cellTable.setPageSize(5);
        cellTable.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
        cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);

        ListHandler<BaseDemandDetail> sorHandler = new ListHandler<BaseDemandDetail>(
                dataProvider.getList());
        cellTable.addColumnSortHandler(sorHandler);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT
                .create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
                true);
        pager.setDisplay(cellTable);

        final MultiSelectionModel<BaseDemandDetail> selectionModel =
                new MultiSelectionModel<BaseDemandDetail>(KEY_PROVIDER);
        cellTable.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<BaseDemandDetail>createCheckboxManager());

        initTableColumns(selectionModel, sorHandler);

        // dataProvider
        dataProvider.addDataDisplay(cellTable);
    }

    private void initTableColumns(final SelectionModel<BaseDemandDetail> tableSelectionModel,
            ListHandler<BaseDemandDetail> sortHandler) {

        // for EVERY text display
        TextCell tableTextCell = new TextCell(new SafeHtmlRenderer<String>() {
            @Override
            public SafeHtml render(String object) {
                return SafeHtmlUtils.fromTrustedString(object);
            }
            @Override
            public void render(String object, SafeHtmlBuilder builder) {
                builder.appendHtmlConstant(object);
            }
        });

        // MultipleSelection Checkbox
        Column<BaseDemandDetail, Boolean> checkBoxColumn = new Column<BaseDemandDetail, Boolean>(
                new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(BaseDemandDetail object) {
                return tableSelectionModel.isSelected(object);
            }
        };

        // Demand Title Column
        Column<BaseDemandDetail, String> titleColumn = (new Column<BaseDemandDetail, String>(
                tableTextCell) {
            @Override
            public String getValue(BaseDemandDetail object) {
                GWT.log("Object is read? " + object.isRead());
                return object.displayTitle();
            }
        });

        // TODO not implemented
//        // Demand Price Column
//        Column<BaseDemandDetail, String> priceColumn = new Column<BaseDemandDetail, String>(tableTextCell) {
//            @Override
//            public String getValue(BaseDemandDetail object) {
//                return (object.getPrice().intValue() < 0
//                        ? object.htmlDisplay("none") : object.htmlDisplay(object.getPrice().toString()));
//            }
//        };

        final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);

        // Demand Finish Column
        Column<BaseDemandDetail, String> endDateColumn
            = new Column<BaseDemandDetail, String>(tableTextCell) {
                @Override
                public String getValue(BaseDemandDetail object) {
                    return object.displayFinishDate();
                }
            };

            // TODO not implemented
//        // Demand sent Date column
//        Column<BaseDemandDetail, String> sentDateColumn
//            =
//            new Column<BaseDemandDetail, String>(tableTextCell) {
//                @Override
//                public String getValue(BaseDemandDetail object) {
//                    return object.htmlDisplay(dateFormat.format(object.getSent()));
//                }
//            };

        // sort methods
        titleColumn.setSortable(true);
        sortHandler.setComparator(titleColumn,
                new Comparator<BaseDemandDetail>() {
                    @Override
                    public int compare(BaseDemandDetail o1,
                            BaseDemandDetail o2) {
                        if (o1 == o2) {
                            return 0;
                        }

                        // Compare the name columns.
                        if (o1 != null) {
                            return (o2 != null) ? o1.getTitle()
                                    .compareTo(o2.getTitle()) : 1;
                        }
                        return -1;
                    }
                });
        // TODO not implemented
//        priceColumn.setSortable(true);
//        sortHandler.setComparator(priceColumn,
//                new Comparator<BaseDemandDetail>() {
//                    @Override
//                    public int compare(BaseDemandDetail o1,
//                            BaseDemandDetail o2) {
//                        return o1.getPrice().compareTo(o2.getPrice());
//                    }
//                });

        // add columns into table
        cellTable.addColumn(checkBoxColumn);
        cellTable.addColumn(titleColumn, MSGS.title());
        // TODO not implemented
//        cellTable.addColumn(priceColumn, MSGS.price());
        cellTable.addColumn(endDateColumn, MSGS.endDate());
        // TODO not implemented
//        cellTable.addColumn(sentDateColumn, MSGS.expireDate());

    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public MultiSelectionModel<BaseDemandDetail> getSelectionModel() {
        return (MultiSelectionModel<BaseDemandDetail>) cellTable.getSelectionModel();
    }

    private static final ProvidesKey<BaseDemandDetail> KEY_PROVIDER = new ProvidesKey<BaseDemandDetail>() {
        @Override
        public Object getKey(BaseDemandDetail item) {
            return item == null ? null : item.getDemandId();
        }
    };

    @Override
    public ListDataProvider<BaseDemandDetail> getDataProvider() {
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
    public Set<BaseDemandDetail> getSelectedSet() {
        return getSelectionModel().getSelectedSet();
    }

    @Override
    public SimplePanel getDetailSection() {
        return detailSection;
    }

    @Override
    public CellTable<BaseDemandDetail> getDemandTable() {
        return cellTable;
    }

    @Override
    public void setPresenter(PotentialDemandsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public PotentialDemandsPresenter getPresenter() {
        return presenter;
    }

    @Override
    public Button getMarkButton() {
        return markBtn;
    }

    @Override
    public boolean getReadValueForMarkedMessages() {
        return readStatus;
    }

}
