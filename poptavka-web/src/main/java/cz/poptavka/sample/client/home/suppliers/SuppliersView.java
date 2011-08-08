package cz.poptavka.sample.client.home.suppliers;

import com.google.gwt.cell.client.AbstractCell;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import cz.poptavka.sample.client.main.common.OverflowComposite;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.SupplierDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import java.util.ArrayList;

public class SuppliersView extends OverflowComposite
        implements SuppliersPresenter.SuppliersViewInterface {

    private static SuppliersViewUiBinder uiBinder = GWT.create(SuppliersViewUiBinder.class);

    interface SuppliersViewUiBinder extends UiBinder<Widget, SuppliersView> {
    }
    private static final Logger LOGGER = Logger.getLogger("    SupplierCreationView");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    @UiField(provided = true)
    CellList suppliersList, categoriesList;
    @UiField(provided = true)
    SimplePager pager;
    @UiField
    FlowPanel path;
//    @UiField
//    SplitLayoutPanel split;
    @UiField(provided = true)
    ListBox pageSize;
    @UiField
    ListBox localityList;
    @UiField
    Label overallRating, certified, description, verification, localities,
    categories, services, bsuRoles, addresses, businessType, email, companyName,
    identificationNumber, firstName, lastName, phone, reklama;
    @UiField
    SimplePanel detail;
    private final SingleSelectionModel<CategoryDetail> selectionCategoryModel =
            new SingleSelectionModel<CategoryDetail>();
    private final SingleSelectionModel<CategoryDetail> selectionSupplierModel =
            new SingleSelectionModel<CategoryDetail>();
    private AsyncDataProvider dataProvider;

    public SuppliersView() {
        pageSize = new ListBox();
        pageSize.addItem("5");
        pageSize.addItem("10");
        pageSize.addItem("15");
        pageSize.addItem("20");
        pageSize.addItem("25");
        pageSize.addItem("30");
        pageSize.setSelectedIndex(2);
        initCellList();
        initWidget(uiBinder.createAndBindUi(this));
        reklama.setVisible(true);

        detail.setVisible(false);
        LOGGER.info("CreateView pre DisplaySuppliers");
    }

    @Override
    public AsyncDataProvider<SupplierDetail> getDataProvider() {
        return dataProvider;
    }

    @Override
    public void setDataProvider(AsyncDataProvider<SupplierDetail> dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public int getPageSize() {
        return Integer.valueOf(pageSize.getItemText(pageSize.getSelectedIndex()));
    }

    @Override
    public ListBox getPageSizeCombo() {
        return pageSize;
    }

    @Override
    public ListBox getLocalityList() {
        return localityList;
    }

    @Override
    public String getSelectedLocality() {
        if (localityList.getSelectedIndex() == 0) {
            return null;
        } else {
            return localityList.getValue(localityList.getSelectedIndex());
        }
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public FlowPanel getPath() {
        return path;
    }

    //removes last one
    @Override
    public void removePath() {
        path.remove(path.getWidgetCount() - 1);
    }

    @Override
    public void addPath(Widget widget) {
        path.add(widget);
    }

    @Override
    public CellList getSuppliersList() {
        return suppliersList;
    }

    @Override
    public SimplePager getPager() {
        return pager;
    }

    @Override
    public SingleSelectionModel getSelectionCategoryModel() {
        return selectionCategoryModel;
    }

    @Override
    public SingleSelectionModel getSelectionSupplierModel() {
        return selectionSupplierModel;
    }

    @Override
    public SplitLayoutPanel getSplitter() {
        return null; //split;
    }

    private void initCellList() {
        // Use the cell in a CellList.
        suppliersList = new CellList<UserDetail>(new SupplierCell());
        suppliersList.setSelectionModel(selectionSupplierModel);
        categoriesList = new CellList<CategoryDetail>(new SubCategoryCell());
        categoriesList.setSelectionModel(selectionCategoryModel);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setPageSize(Integer.valueOf(pageSize.getItemText(pageSize.getSelectedIndex())));
        pager.setDisplay(suppliersList);
    }

    @Override
    public void displaySubCategories(int columns, ArrayList<CategoryDetail> subCategories) {
        categoriesList.setRowCount(subCategories.size(), true);
        categoriesList.setRowData(0, subCategories);
    }

    @Override
    public void displaySuppliersDetail(UserDetail userDetail) {
        reklama.setVisible(false);
        detail.setVisible(true);

        overallRating.setText(Integer.toString(userDetail.getSupplier().getOverallRating()));
        certified.setText(Boolean.toString(userDetail.getSupplier().isCertified()));
        description.setText(userDetail.getSupplier().getDescription());
//    verification = userDetail.get
        localities.setText(userDetail.getSupplier().getLocalities().toString());
        categories.setText(userDetail.getSupplier().getCategories().toString());
//    services = userDetail.getSupplier().
//    bsuRoles = userDetail.getSupplier().
        addresses.setText(userDetail.getAddress().toString());
//    businessType = userDetail.get
        email.setText(userDetail.getEmail());
        companyName.setText(userDetail.getCompanyName());
        identificationNumber.setText(userDetail.getIdentifiacationNumber());
        firstName.setText(userDetail.getFirstName());
        lastName.setText(userDetail.getLastName());
        phone.setText(userDetail.getPhone());

    }
}

/**
 * Supplier Cell.
 */
class SupplierCell extends AbstractCell<UserDetail> {

    public SupplierCell() {
        /*
         * Let the parent class know that our cell responds to click events and
         * keydown events.
         */
//        super("click", "keydown");
    }

//    @Override
//    public void onBrowserEvent(Context context, Element parent, UserDetail value,
//            NativeEvent event, ValueUpdater<UserDetail> valueUpdater) {
//        // Check that the value is not null.
//        if (value == null) {
//            return;
//        }
//
//        // Call the super handler, which handlers the enter key.
//        super.onBrowserEvent(context, parent, value, event, valueUpdater);
//
//        // On click, perform the same action that we perform on enter.
//        if ("click".equals(event.getType())) {
//            this.onEnterKeyDown(context, parent, value, event, valueUpdater);
//        }
//    }

    @Override
    public void render(Context context, UserDetail value, SafeHtmlBuilder sb) {
        /*
         * Always do a null check on the value. Cell widgets can pass null to
         * cells if the underlying data contains a null, or if the data arrives
         * out of order.
         */
        if (value == null) {
            return;
        }

        //TODO Martin Logo??
        sb.appendHtmlConstant("<div><a href=\"http://firmy.sk/66161/ivan-genda-s-car/\"> "
                + "<img style=\"float: left; margin-right: 10px;padding: 0px; width: 50px;\" "
                + "src=\"http://mattkendrick.com/wp-content/uploads/2009/07/w3schools.jpg\"  "
                + "alt=\"http://s-car.sk\"  > </a>");


        //Company Name
        if (value.getCompanyName() != null) {
            sb.appendHtmlConstant("<a href=\"#\"><strong>1.)");
            sb.appendEscaped(value.getCompanyName());
            sb.appendHtmlConstant("</strong> </a>");
        }

        //Company description
        if (value.getSupplier().getDescription() != null) {
            sb.appendHtmlConstant("<div style=\"width:800px;\">");
            sb.appendEscaped(value.getSupplier().getDescription());
            sb.appendHtmlConstant("</div>");
        }

        //Componay Website
        if (value.getWebsite() != null) {
            sb.appendHtmlConstant("<A href=\"http://www.s-car.sk\" target=\"_blank\"  "
                    + " style=\"FONT-FAMILY: Arial, sans-serif; COLOR:green;\">");
            sb.appendEscaped(value.getWebsite());
            sb.appendHtmlConstant("</A>");
        }

        //Company address
        if (value.getAddress() != null) {
            sb.appendHtmlConstant("<span style=\"FONT-FAMILY: Arial, sans-serif; COLOR:gray;\"> "
                    + "&nbsp;&nbsp;-&nbsp;");
            sb.appendEscaped(value.getAddress().toString());
            sb.appendHtmlConstant("</span></div><br/>");
        }
    }

    /**
     * By convention, cells that respond to user events should handle the enter
     * key. This provides a consistent user experience when users use keyboard
     * navigation in the widget.
     */
//    @Override
//    protected void onEnterKeyDown(Context context, Element parent,
//            UserDetail value, NativeEvent event, ValueUpdater<UserDetail> valueUpdater) {
//        Window.alert("You clicked " + value.getSupplierId());
//    }
}

/**
 * Sub Category Cell .
 */
class SubCategoryCell extends AbstractCell<CategoryDetail> {

    @Override
    public void render(Context context, CategoryDetail value, SafeHtmlBuilder sb) {
        /*
         * Always do a null check on the value. Cell widgets can pass null to
         * cells if the underlying data contains a null, or if the data arrives
         * out of order.
         */
        if (value == null) {
            return;
        }

        StringBuilder text = new StringBuilder();

        text.append(value.getName().replaceAll("-a-", " a ").replaceAll("-", ", "));
        text.append(" (");
        text.append(value.getSuppliers());
        text.append(")");

        sb.appendEscaped(text.toString());
//        sb.appendHtmlConstant("</div>");
    }
}