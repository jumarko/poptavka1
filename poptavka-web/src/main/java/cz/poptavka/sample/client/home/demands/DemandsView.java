package cz.poptavka.sample.client.home.demands;

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
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.client.main.common.OverflowComposite;
import cz.poptavka.sample.client.resources.StyleResource;
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
    @UiField(provided = true)
    ListBox pageSize;
    @UiField
    ListBox category, locality;
    @UiField
    HTMLPanel demandView;
    @UiField
    Label bannerLabel;
    @UiField(provided = true)
    CellTable<FullDemandDetail> cellTable;
    @UiField(provided = true)
    SimplePager pager;

    @UiField Hyperlink linkAttachment, linkLogin, linkRegisterClient, linkRegisterSupplier;
    @UiField FlexTable infoTable;
    @UiField Label textArea;

    private LocalizableMessages bundle = (LocalizableMessages) GWT.create(LocalizableMessages.class);
    private final SingleSelectionModel<FullDemandDetail> selectionModel =
            new SingleSelectionModel<FullDemandDetail>();
    private AsyncDataProvider dataProvider;

    @Override
    public void setRegisterSupplierToken(String token) {
        linkRegisterSupplier.setTargetHistoryToken(token);
    }

    //TODO Martin dorobit register client

    @Override
    public void setAttachmentToken(String token) {
        linkAttachment.setTargetHistoryToken(token);
    }

    @Override
    public void setLoginToken(String token) {
        linkLogin.setTargetHistoryToken(token);
    }

    public DemandsView() {
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
        demandView.setVisible(false);
        StyleResource.INSTANCE.layout().ensureInjected();
    }

    @Override
    public HTMLPanel getDemandView() {
        return demandView;
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
    public int getPageSize() {
        return Integer.valueOf(pageSize.getItemText(pageSize.getSelectedIndex()));
    }

    @Override
    public CellTable<FullDemandDetail> getCellTable() {
        return cellTable;
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
        cellTable = new CellTable<FullDemandDetail>();
        cellTable.setWidth("100%", true);
        cellTable.setRowCount(Integer.valueOf(pageSize.getItemText(pageSize.getSelectedIndex())), true);

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
        // Date of creation
        // TODO Martin - opravit ak bude dostupny datum vlozenia
        addColumn(new TextCell(), bundle.createdDate(), 30, new GetValue<String>() {

            public String getValue(FullDemandDetail demandDetail) {
                //TODO Martin dorobit rozdelenie casu a datumu podla niecoho
                //if (...) {
                //DateTimeFormat.getFormat("hh:mm").format(demandDetail.getEndDate());
                //}
                return DateTimeFormat.getFormat("dd.MM.yyyy").format(demandDetail.getCreated());
            }
        });

        // Root category info
        addColumn(new TextCell(), bundle.category(), 40, new GetValue<String>() {

            public String getValue(FullDemandDetail demandDetail) {
                if (demandDetail.getCategories() != null
                        && demandDetail.getCategories().size() != 0) {
                    return demandDetail.getCategories().get(0);
                } else {
                    return "";
                }
            }
        });

        // Demand Info
        addColumn(new TextCell(), bundle.demand(), 100, new GetValue<String>() {

            public String getValue(FullDemandDetail demandDetail) {
                return demandDetail.getTitle();
            }
        });

        // Mesto
        addColumn(new TextCell(), bundle.locality(), 40, new GetValue<String>() {

            public String getValue(FullDemandDetail demandDetail) {
                if (demandDetail.getLocalities() != null
                        && demandDetail.getLocalities().size() != 0) {
                    return demandDetail.getLocalities().get(0);
                } else {
                    return "";
                }
            }
        });

        // Urgencia
        addColumn(new ImageStatus(), bundle.urgency(), 40, new GetValue<Date>() {

            public Date getValue(FullDemandDetail object) {
                return object.getEndDate();
            }
        });

        // Cena
        addColumn(new TextCell(), bundle.price(), 30, new GetValue<String>() {

            public String getValue(FullDemandDetail demandDetail) {
                return String.valueOf(demandDetail.getPrice());
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

    /**
     * The Cell used to render Urgent image with text
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
//            diffDays = rnd.nextInt(15); //TODO Martin - docasne, potom vymazat

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
    private <C> Column<FullDemandDetail, C> addColumn(Cell<C> cell,
            String headerText, int width, final GetValue<C> getter) {
        Column<FullDemandDetail, C> column = new Column<FullDemandDetail, C>(cell) {

            @Override
            public C getValue(FullDemandDetail demand) {
                return getter.getValue(demand);
            }
        };
        cellTable.addColumn(column, headerText);
        cellTable.setColumnWidth(column, width, Unit.PX);
        return column;
    }

    @Override
    public void setDemand(FullDemandDetail demand) {
        infoTable.clear();
        textArea.setText("");

        textArea.getElement().getStyle().setProperty("whiteSpace", "pre");
        linkAttachment.setVisible(false);

        int row = 0;

        if (demand.getDescription() != null) {
            textArea.setText(demand.getDescription());
        }

        if (demand.getPrice() != null) {
            infoTable.setWidget(row, 0, new Label(bundle.title() + ":"));
            infoTable.setWidget(row++, 1, new Label(demand.getTitle().toString()));
        }

        if (demand.getPrice() != null) {
            infoTable.setWidget(row, 0, new Label(bundle.price() + ":"));
            infoTable.setWidget(row++, 1, new Label(demand.getPrice().toPlainString()));
        }

        if (demand.getEndDate() != null) {
            infoTable.setWidget(row, 0, new Label(bundle.endDate() + ":"));
            infoTable.setWidget(row++, 1, new Label(demand.getEndDate().toString()));
        }

        if (demand.getValidToDate() != null) {
            infoTable.setWidget(row, 0, new Label(bundle.validTo() + ":"));
            infoTable.setWidget(row++, 1, new Label(demand.getValidToDate().toString()));
        }

        if (demand.getDemandType() != null) {
            infoTable.setWidget(row, 0, new Label(bundle.type() + ":"));
            infoTable.setWidget(row++, 1, new Label(demand.getDemandType()));
        }

        if (demand.getCategories() != null) {
            infoTable.setWidget(row, 0, new Label(bundle.category() + ":"));
            infoTable.setWidget(row++, 1, new Label(demand.getCategories().toString()
                    .substring(1, demand.getCategories().toString().length() - 1)));
        }

        if (demand.getLocalities() != null) {
            infoTable.setWidget(row, 0, new Label(bundle.locality() + ":"));
            infoTable.setWidget(row++, 1, new Label(demand.getLocalities().toString()
                    .substring(1, demand.getLocalities().toString().length() - 1)));
        }

        if (demand.getPrice() != null) {
            infoTable.setWidget(row++, 0, new Label(bundle.attachment() + ":"));
            infoTable.setWidget(row, 1, new Label(demand.getTitle().toString()));
            linkAttachment.setVisible(true);
        }
    }
}
