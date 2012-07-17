/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.homedemands;

import com.eprovement.poptavka.client.common.OverflowComposite;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.client.user.widget.detail.DemandDetailView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * This view is to replace DemandsView.java.
 *
 * @author praso
 */
public class HomeDemandsView extends OverflowComposite implements HomeDemandsPresenter.HomeDemandsViewInterface {

    private static HomeDemandsViewUiBinder uiBinder = GWT.create(HomeDemandsViewUiBinder.class);

    interface HomeDemandsViewUiBinder extends UiBinder<Widget, HomeDemandsView> {
    }
    //Table constants
    private static final int CREATED_DATE_COL_WIDTH = 35;
    private static final int CATEGORY_COL_WIDTH = 60;
    private static final int TITLE_COL_WIDTH = 100;
    private static final int LOCALITY_COL_WIDTH = 60;
    private static final int PRICE_WIDTH = 40;
    private static final int URGENCY_COL_WIDTH = 20;
    // Table definitions
    @UiField(provided = true)
    UniversalAsyncGrid<FullDemandDetail> dataGrid;
    private List<String> gridColumns = Arrays.asList(
            new String[]{
                "createdDate", "category", "title", "locality", "price"
            });
    // Pager
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSize;
    // Others
    @UiField
    Label bannerLabel;
    @UiField
    DemandDetailView demandDetail;
    @UiField
    SimplePanel demandDetailPanel;
    @UiField
    Button offerBtn;
    private LocalizableMessages bundle = (LocalizableMessages) GWT.create(LocalizableMessages.class);
    private NumberFormat currencyFormat = NumberFormat.getFormat(bundle.currencyFormat());

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

    @Override
    public Button getOfferBtn() {
        return offerBtn;
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
    public UniversalAsyncGrid<FullDemandDetail> getDataGrid() {
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
        dataGrid = new UniversalAsyncGrid<FullDemandDetail>(gridColumns);
        dataGrid.setEmptyTableWidget(new Label(Storage.MSGS.noData()));
        dataGrid.setWidth("800px");
        dataGrid.setHeight("500px");
        // Selection handler
        dataGrid.setSelectionModel(new SingleSelectionModel<FullDemandDetail>());

        dataGrid.setRowCount(Integer.valueOf(pageSize.getItemText(pageSize.getSelectedIndex())), true);
        dataGrid.setPageSize(this.getPageSize());

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
        dataGrid.addColumn(new TextCell(), bundle.createdDate(), true, CREATED_DATE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    public String getValue(Object object) {
                        FullDemandDetail demandDetail = (FullDemandDetail) object;
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
        dataGrid.addColumn(new TextCell(), bundle.category(), false, CATEGORY_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        StringBuilder str = new StringBuilder();
                        for (String cat : ((FullDemandDetail) object).getCategories().values()) {
                            str.append(cat);
                            str.append(",\n");
                        }
                        str.delete(str.length() - 2, str.length());
                        return str.toString();
                    }
                });

        // Demand Info
        dataGrid.addColumn(new TextCell(), bundle.demand(), true, TITLE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((FullDemandDetail) object).getTitle();
                    }
                });

        // Locality
        dataGrid.addColumn(new TextCell(), bundle.locality(), false, LOCALITY_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        StringBuilder str = new StringBuilder();
                        for (String cat : ((FullDemandDetail) object).getLocalities().values()) {
                            str.append(cat);
                            str.append(",\n");
                        }
                        str.delete(str.length() - 2, str.length());
                        return str.toString();
                    }
                });

        // Cena
        dataGrid.addColumn(new TextCell(), bundle.price(), true, PRICE_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return currencyFormat.format(((FullDemandDetail) object).getPrice());
                    }
                });

        // Urgencia
        dataGrid.addColumn(new ImageStatus(), "", true, URGENCY_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<Date>() {

                    @Override
                    public Date getValue(Object object) {
                        return ((FullDemandDetail) object).getEndDate();
                    }
                });
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
}
