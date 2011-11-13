/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.homedemands;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import java.util.Date;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.client.main.common.OverflowComposite;
import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.user.demands.widget.DemandDetailView;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;

/**
 * This view is to replace DemandsView.java.
 * @author praso
 */
public class HomeDemandsView extends OverflowComposite implements HomeDemandsPresenter.HomeDemandsViewInterface {

    private static HomeDemandsViewUiBinder uiBinder = GWT.create(HomeDemandsViewUiBinder.class);

    interface HomeDemandsViewUiBinder extends UiBinder<Widget, HomeDemandsView> {
    }
    @UiField(provided = true)
    ListBox pageSize;
//    @UiField
//    ListBox category, locality;
    @UiField
    Label bannerLabel;
    @UiField(provided = true)
    DataGrid<FullDemandDetail> dataGrid;
    @UiField(provided = true)
    SimplePager pager;
    @UiField
    DemandDetailView demandDetail;
    @UiField
    SimplePanel demandDetailPanel;
    private LocalizableMessages bundle = (LocalizableMessages) GWT.create(LocalizableMessages.class);
    private final SingleSelectionModel<FullDemandDetail> selectionModel =
            new SingleSelectionModel<FullDemandDetail>();

    public HomeDemandsView() {
        pageSize = new ListBox();
        pageSize.addItem("5");
        pageSize.addItem("10");
        pageSize.addItem("15");
        pageSize.addItem("20");
        pageSize.addItem("25");
        pageSize.addItem("30");
        pageSize.setSelectedIndex(2);
        initCellTable();
        initWidget(uiBinder.createAndBindUi(this));
        demandDetailPanel.setVisible(false);
        StyleResource.INSTANCE.layout().ensureInjected();
    }

//    @Override
//    public ListBox getCategoryList() {
//        return category;
//    }
//
//    @Override
//    public ListBox getLocalityList() {
//        return locality;
//    }
    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public int getPageSize() {
        return Integer.valueOf(pageSize.getItemText(pageSize.getSelectedIndex()));
    }

    @Override
    public DataGrid<FullDemandDetail> getDataGrid() {
        return dataGrid;
    }

    @Override
    public SimplePager getPager() {
        return pager;
    }

    @Override
    public ListBox getPageSizeCombo() {
        return pageSize;
    }

    /**
     * Initialize this example.
     */
    private void initCellTable() {
        // Create a CellTable.
        dataGrid = new DataGrid<FullDemandDetail>();
        dataGrid.setWidth("800px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));
        dataGrid.setRowCount(Integer.valueOf(pageSize.getItemText(pageSize.getSelectedIndex())), true);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setSelectionModel(selectionModel);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        initGridColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initGridColumns() {
        // Date of creation
        addColumn(new TextCell(), bundle.createdDate(), true, 35, new GetValue<String>() {

            public String getValue(FullDemandDetail demandDetail) {
                if (demandDetail.getCreated() == null) {
                    return "not defined";
                } else {
                    Date now = new Date();
                    long millis = now.getTime() - demandDetail.getCreated().getTime();
                    if (millis < 86400000) {
                        return DateTimeFormat.getFormat("hh:mm").format(demandDetail.getCreated());
//                        return "dnes";
                    } else if (86400000 <= millis && millis < 172800000) {
                        return "vcera";
                    } else {
                        return DateTimeFormat.getFormat("dd.MM.yyyy").format(demandDetail.getCreated());
                    }
                }
            }
        });

        // Root category info
        addColumn(new TextCell(), bundle.category(), false, 60, new GetValue<String>() {

            @Override
            public String getValue(FullDemandDetail demandDetail) {
                StringBuilder str = new StringBuilder();
                for (String cat : demandDetail.getCategories().values()) {
                    str.append(cat);
                    str.append(",\n");
                }
                str.delete(str.length() - 2, str.length());
                return str.toString();
            }
        });

        // Demand Info
        addColumn(new TextCell(), bundle.demand(), true, 100, new GetValue<String>() {

            @Override
            public String getValue(FullDemandDetail demandDetail) {
                return demandDetail.getTitle();
            }
        });

        // Locality
        addColumn(new TextCell(), bundle.locality(), false, 60, new GetValue<String>() {

            @Override
            public String getValue(FullDemandDetail demandDetail) {
                StringBuilder str = new StringBuilder();
                for (String cat : demandDetail.getLocalities().values()) {
                    str.append(cat);
                    str.append(",\n");
                }
                str.delete(str.length() - 2, str.length());
                return str.toString();
            }
        });

        // Cena
        addColumn(new TextCell(), bundle.price(), true, 40, new GetValue<String>() {

            @Override
            public String getValue(FullDemandDetail demandDetail) {
                return demandDetail.getPriceString();
            }
        });

        // Urgencia
        addColumn(new ImageStatus(), "", true, 20, new GetValue<Date>() {

            @Override
            public Date getValue(FullDemandDetail object) {
                return object.getEndDate();
            }
        });
    }

    @Override
    public SingleSelectionModel<FullDemandDetail> getSelectionModel() {
        return selectionModel;
    }

    @Override
    public Label getBannerLabel() {
        return bannerLabel;
    }

    @Override
    public DemandDetailView getDemandDetail() {
        return demandDetail;
    }

    @Override
    public SimplePanel getDemandDetailPanel() {
        return demandDetailPanel;
    }

    /**
     * The Cell used to render Urgent image with text.
     */
    private static class ImageStatus extends AbstractCell<Date> {

        @Override
        public void render(Context context, Date value, SafeHtmlBuilder sb) {
            if (value == null) {
                return;
            }

            String imageHtml = null;
            String text = "";

            long diffSec = value.getTime() - (new Date()).getTime();
            long diffDays = diffSec / (1000 * 60 * 60 * 24);

            //TODO Martin - i18
            if ((int) diffDays <= 4) { //(0-4) velmi specha
//                text = "velmi specha";
                imageHtml = AbstractImagePrototype.create(StyleResource.INSTANCE.images().urgent()).getHTML();

            } else if ((int) diffDays <= 8) { //(5-8) specha
//                text = "specha";
                imageHtml = AbstractImagePrototype.create(StyleResource.INSTANCE.images().lessUrgent()).getHTML();

            } else if ((int) diffDays <= 12) { //(9-12) nespecha
//                text = "nespecha";
                imageHtml = AbstractImagePrototype.create(StyleResource.INSTANCE.images().normal()).getHTML();

            } else if (12 < (int) diffDays) { //(13-oo) vobec nespecha
//                text = "vobec nespecha";
                imageHtml = AbstractImagePrototype.create(StyleResource.INSTANCE.images().lessNormal()).getHTML();
            }
            sb.appendHtmlConstant("<table>");
            // Add the contact image.
            sb.appendHtmlConstant("<tr><td>");
            sb.appendHtmlConstant(imageHtml);
            sb.appendHtmlConstant("</td>");
            // Add text
            sb.appendHtmlConstant("<td style='font-size:60%;'>");
            sb.appendEscaped(text);
            sb.appendHtmlConstant("</td></tr></table>");
        }
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C>
     *            the cell type
     */
    private static interface GetValue<C> {

        C getValue(FullDemandDetail contact);
    }

    /**
     * Add a column with a header.
     *
     * @param <C>
     *            the cell type
     * @param cell
     *            the cell used to render the column
     * @param headerText
     *            the header string
     * @param getter
     *            the value getter for the cell
     */
    private <C> Column<FullDemandDetail, C> addColumn(Cell<C> cell,
            String headerText, boolean sort, int width, final GetValue<C> getter) {
        Column<FullDemandDetail, C> column = new Column<FullDemandDetail, C>(cell) {

            @Override
            public C getValue(FullDemandDetail demand) {
                return getter.getValue(demand);
            }
        };
        if (sort) {
            column.setSortable(true);
        }
        dataGrid.addColumn(column, headerText);
        dataGrid.setColumnWidth(column, width, Unit.PX);
        return column;
    }
}
