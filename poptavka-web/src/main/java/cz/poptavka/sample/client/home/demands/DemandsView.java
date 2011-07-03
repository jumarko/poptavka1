package cz.poptavka.sample.client.home.demands;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import java.util.Date;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.client.home.demands.demand.DemandView;
import cz.poptavka.sample.client.main.common.OverflowComposite;
import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.demand.DemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import java.util.Random;

/**
 *
 * @author Martin Slavkovsky
 */
public class DemandsView extends OverflowComposite implements DemandsPresenter.DemandsViewInterface {

    private static DemandsUiBinder uiBinder = GWT.create(DemandsUiBinder.class);

    interface DemandsUiBinder extends UiBinder<Widget, DemandsView> {
    }
//    @UiField
//    VerticalPanel verticalContent;
    @UiField
    ListBox category, locality;
    @UiField
    DemandView demandView;
    @UiField
    Label bannerLabel;
    @UiField(provided = true)
    CellTable<DemandDetail> cellTable;
    @UiField(provided = true)
    SimplePager pager;
    private final SingleSelectionModel<DemandDetail> selectionModel = new SingleSelectionModel<DemandDetail>();
    private AsyncDataProvider dataProvider;
    LocalizableMessages bundle = (LocalizableMessages) GWT.create(LocalizableMessages.class);

    public DemandsView() {
        initCellTable();
        initWidget(uiBinder.createAndBindUi(this));
        demandView.setVisible(false);
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
    public AsyncDataProvider<DemandDetail> getDataProvider() {
        return dataProvider;
    }

    @Override
    public void setDataProvider(AsyncDataProvider<DemandDetail> dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public CellTable<DemandDetail> getCellTable() {
        return cellTable;
    }

    @Override
    public SimplePager getPager() {
        return pager;
    }

    /**
     * Initialize this example.
     */
    private void initCellTable() {
        // Create a CellTable.
        cellTable = new CellTable<DemandDetail>();
        cellTable.setWidth("100%", true);
        cellTable.setRowCount(9, true);

        cellTable.setSelectionModel(selectionModel);

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
        // Date of creation TODO Martin - opravit ak bude dostupny datum vlozenia
        addColumn(new DateCell(DateTimeFormat.getFormat("MM.dd.yyyy")), bundle.date(), 30, new GetValue<Date>() {

            public Date getValue(FullDemandDetail fullDemandDetail) {
                return fullDemandDetail.getEndDate();
            }
        });

        // Time of creation TODO Martin - opravit ak bude dostupny cas vlozenia
        addColumn(new DateCell(DateTimeFormat.getFormat("hh:mm")), "time", 20, new GetValue<Date>() {

            public Date getValue(FullDemandDetail fullDemandDetail) {
                return fullDemandDetail.getEndDate();
            }
        });

        // Root category info
        addColumn(new TextCell(), "root category", 40, new GetValue<String>() {

            public String getValue(FullDemandDetail fullDemandDetail) {
                if (fullDemandDetail.getCategories() != null
                        && fullDemandDetail.getCategories().size() != 0) {
                    return fullDemandDetail.getCategories().get(0);
                } else {
                    return "";
                }
            }
        });

        // Demand Info
        addColumn(new TextCell(), bundle.demand(), 100, new GetValue<String>() {

            public String getValue(FullDemandDetail fullDemandDetail) {
                return fullDemandDetail.getTitle();
            }
        });

        // Mesto
        addColumn(new TextCell(), "lokalita", 40, new GetValue<String>() {

            public String getValue(FullDemandDetail fullDemandDetail) {
                if (fullDemandDetail.getLocalities() != null
                        && fullDemandDetail.getLocalities().size() != 0) {
                    return fullDemandDetail.getLocalities().get(0);
                } else {
                    return "";
                }
            }
        });

        // Urgencia
        addColumn(new ImageStatus(), "urgencia", 40, new GetValue<Date>() {

            public Date getValue(FullDemandDetail object) {
                return object.getEndDate();
            }
        });

        // Cena
        addColumn(new TextCell(), "mesto", 30, new GetValue<String>() {

            public String getValue(FullDemandDetail fullDemandDetail) {
                return String.valueOf(fullDemandDetail.getPrice());
            }
        });
    }

    @Override
    public SingleSelectionModel<DemandDetail> getSelectionModel() {
        return selectionModel;
    }

    @Override
    public Label getBannerLabel() {
        return bannerLabel;
    }

    /**
     * The Cell used to render a {@link ContactInfo}.
     */
    private static class ImageStatus extends AbstractCell<Date> {

        private Random rnd = new Random();

        @Override
        public void render(Context context, Date value, SafeHtmlBuilder sb) {
            if (value == null) {
                return;
            }

            String imageHtml = null;
            String text = null;

            long diffSec = value.getTime() - (new Date()).getTime();
            long diffDays = diffSec / (1000 * 60 * 60 * 24);
            diffDays = rnd.nextInt(15); //TODO Martin - docasne, potom vymazat

            //TODO Martin - i18
            if ((int) diffDays <= 4) { //(0-4) velmi specha
                text = "velmi specha";
                imageHtml = AbstractImagePrototype.create(StyleResource.INSTANCE.images().urgent()).getHTML();

            } else if ((int) diffDays <= 8) { //(5-8) specha
                text = "specha";
                imageHtml = AbstractImagePrototype.create(StyleResource.INSTANCE.images().lessUrgent()).getHTML();

            } else if ((int) diffDays <= 12) { //(9-12) nespecha
                text = "nespecha";
                imageHtml = AbstractImagePrototype.create(StyleResource.INSTANCE.images().normal()).getHTML();

            } else if (12 < (int) diffDays) { //(13-oo) vobec nespecha
                text = "vobec nespecha";
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
    private <C> Column<DemandDetail, C> addColumn(Cell<C> cell,
            String headerText, int width, final GetValue<C> getter) {
        Column<DemandDetail, C> column = new Column<DemandDetail, C>(cell) {

            @Override
            public C getValue(DemandDetail object) {
                return getter.getValue((FullDemandDetail) object);
            }
        };
        cellTable.addColumn(column, headerText);
        cellTable.setColumnWidth(column, width, Unit.PX);
        return column;
    }
}
