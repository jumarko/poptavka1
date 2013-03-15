package com.eprovement.poptavka.client.user.widget.detail;

import com.eprovement.poptavka.client.common.BigDecimalBox;
import com.eprovement.poptavka.client.common.ChangeMonitor;
import com.eprovement.poptavka.client.common.IntegerBox;
import com.eprovement.poptavka.client.common.ListChangeMonitor;
import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.locality.LocalityCell;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.client.user.widget.grid.cell.SupplierCell;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditableDemandDetailView extends Composite implements
        EditableDemandDetailPresenter.IEditableDemandDetailView, ProvidesValidate {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    interface DemandDetailViewUiBinder extends UiBinder<Widget, EditableDemandDetailView> {
    }
    private static DemandDetailViewUiBinder uiBinder = GWT.create(DemandDetailViewUiBinder.class);
    /**************************************************************************/
    /* Attributes                                                               */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) ChangeMonitor titleMonitor, priceMonitor;
    @UiField(provided = true) ChangeMonitor excludedSuppliersMonitor, endDateMonitor, validToDateMonitor;
    @UiField(provided = true) ChangeMonitor maxOffersMonitor, minRatingMonitor, descriptionMonitor;
    @UiField(provided = true) ListChangeMonitor categoriesMonitor, localitiesMonitor;
    @UiField(provided = true) CellList categories, localities, excludedSuppliers;
    @UiField TextBox title;
    @UiField BigDecimalBox price;
    @UiField DateBox endDate, validToDate;
    @UiField IntegerBox maxOffers, minRating;
    @UiField TextArea description;
    @UiField HTMLPanel detail;
    @UiField Button editCatBtn, editLocBtn;
    /** Class attributes. **/
    private List<ChangeMonitor> monitors;
    private long demandId;
    private PopupPanel selectorWidgetPopup;
    /** Constants. **/
    private static final String EMPTY = "";

    /**************************************************************************/
    /* INITIALIZATON                                                          */
    /**************************************************************************/
    //Constructor
    @Override
    public void createView() {
        createSelectorWidgetPopup();
        //
        categories = new CellList<CategoryDetail>(new CategoryCell(CategoryCell.DISPLAY_COUNT_DISABLED));
        localities = new CellList<LocalityDetail>(new LocalityCell(LocalityCell.DISPLAY_COUNT_DISABLED));
        excludedSuppliers = new CellList<FullSupplierDetail>(new SupplierCell());
        //
        initValidationMonitors();

        initWidget(uiBinder.createAndBindUi(this));

        detail.setVisible(true);
        setFieldEnables(false);

        StyleResource.INSTANCE.detailViews().ensureInjected();
        StyleResource.INSTANCE.common().ensureInjected();
    }

    private void initValidationMonitors() {
        titleMonitor = createDemandChangeMonitor(DemandField.TITLE);
        categoriesMonitor = createDemandListChangeMonitor(DemandField.CATEGORIES);
        localitiesMonitor = createDemandListChangeMonitor(DemandField.LOCALITIES);
        excludedSuppliersMonitor = createDemandChangeMonitor(DemandField.EXCLUDE_SUPPLIER);
        priceMonitor = createDemandChangeMonitor(DemandField.PRICE);
        endDateMonitor = createDemandChangeMonitor(DemandField.END_DATE);
        validToDateMonitor = createDemandChangeMonitor(DemandField.VALID_TO_DATE);
        maxOffersMonitor = createDemandChangeMonitor(DemandField.MAX_OFFERS);
        minRatingMonitor = createDemandChangeMonitor(DemandField.MIN_RATING);
        descriptionMonitor = createDemandChangeMonitor(DemandField.DESCRIPTION);
        monitors = Arrays.asList(
                titleMonitor, excludedSuppliersMonitor, priceMonitor,
                endDateMonitor, validToDateMonitor, maxOffersMonitor, minRatingMonitor, descriptionMonitor);
    }

    private ChangeMonitor createDemandChangeMonitor(DemandField fieldField) {
        return new ChangeMonitor<FullDemandDetail>(
                FullDemandDetail.class, new ChangeDetail(fieldField.getValue()));
    }

    private ListChangeMonitor createDemandListChangeMonitor(DemandField fieldField) {
        return new ListChangeMonitor<FullDemandDetail>(
                FullDemandDetail.class, new ChangeDetail(fieldField.getValue()));
    }

    //Popup
    private void createSelectorWidgetPopup() {
        selectorWidgetPopup = new PopupPanel(true);
        selectorWidgetPopup.setSize("300px", "300px");
        selectorWidgetPopup.setGlassEnabled(true);
        selectorWidgetPopup.hide();
    }

    /**************************************************************************/
    /* METHODS                                                                */
    /**************************************************************************/
    @Override
    public void resetFields() {
        for (ChangeMonitor monitor : monitors) {
            monitor.reset();
        }
        categoriesMonitor.reset();
        localitiesMonitor.reset();
    }

    @Override
    public void revertFields() {
        for (ChangeMonitor monitor : monitors) {
            monitor.revert();
        }
        categoriesMonitor.revert();
        localitiesMonitor.revert();
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    public void setDemanDetail(FullDemandDetail demandDetail) {
        GWT.log("detail detail" + demandDetail.toString());
        demandId = demandDetail.getDemandId();
        titleMonitor.setBothValues(demandDetail.getTitle());
        priceMonitor.setBothValues(demandDetail.getPrice());
        endDateMonitor.setBothValues(demandDetail.getEndDate());
        validToDateMonitor.setBothValues(demandDetail.getValidTo());
        categoriesMonitor.setBothValues(demandDetail.getCategories());
        localitiesMonitor.setBothValues(demandDetail.getLocalities());
        maxOffersMonitor.setBothValues(demandDetail.getMaxSuppliers());
        minRatingMonitor.setBothValues(demandDetail.getMinRating());
        excludedSuppliersMonitor.setBothValues(demandDetail.getExcludedSuppliers());
        descriptionMonitor.setBothValues(demandDetail.getDescription());
    }

    public void clear() {
        title.setText(EMPTY);
        price.setText(EMPTY);
        endDate.getTextBox().setText(EMPTY);
        validToDate.getTextBox().setText(EMPTY);
        categories.setRowCount(0);
        localities.setRowCount(0);
        maxOffers.setText(EMPTY);
        minRating.setText(EMPTY);
        excludedSuppliers.setRowCount(0);
        description.setText(EMPTY);
    }

    @Override
    public void setFieldEnables(boolean enable) {
        title.setEnabled(enable);
        price.setEnabled(enable);
        endDate.setEnabled(enable);
        validToDate.setEnabled(enable);
        maxOffers.setEnabled(enable);
        minRating.setEnabled(enable);
        description.setEnabled(enable);
        editCatBtn.setEnabled(enable);
        editLocBtn.setEnabled(enable);
    }

    @Override
    public void setChangeHandler(ChangeHandler handler) {
        for (ChangeMonitor monitor : monitors) {
            monitor.addChangeHandler(handler);
        }
    }

    @Override
    public void setListChangeHandler(ChangeHandler handler) {
        categoriesMonitor.addChangeHandler(handler);
        localitiesMonitor.addChangeHandler(handler);
    }

    /**
     * Need for CategorySelector when closing to set newly chosen categories.
     * @param categories
     */
    @Override
    public void setCategories(List<CategoryDetail> categories) {
        categoriesMonitor.setValue(categories);
    }

    /**
     * Need for LocalitySelector when closing to set newly chosen localities.
     * @param loclaities
     */
    @Override
    public void setLocalities(List<LocalityDetail> localities) {
        localitiesMonitor.setValue(localities);
    }

    /**************************************************************************/
    /* GETTER                                                                 */
    /**************************************************************************/
    /** Button. **/
    @Override
    public Button getEditCatBtn() {
        return editCatBtn;
    }

    @Override
    public Button getEditLocBtn() {
        return editLocBtn;
    }

    /** Panels. **/
    public HTMLPanel getDetail() {
        return detail;
    }

    @Override
    public PopupPanel getSelectorWidgetPopup() {
        return selectorWidgetPopup;
    }

    /** Data. **/
    @Override
    public long getDemandId() {
        return demandId;
    }

    @Override
    public ArrayList<CategoryDetail> getCategories() {
        return (ArrayList<CategoryDetail>) categoriesMonitor.getValue();
    }

    @Override
    public ArrayList<LocalityDetail> getLocalities() {
        return (ArrayList<LocalityDetail>) localitiesMonitor.getValue();
    }

    /** Validation. **/
    @Override
    public boolean isValid() {
        boolean valid = true;
        for (ChangeMonitor monitor : monitors) {
            valid = monitor.isValid() && valid;
        }
        return valid && categoriesMonitor.isValid() && localitiesMonitor.isValid();
    }

    /** Widget view. **/
    @Override
    public Widget asWidget() {
        return this;
    }
}
