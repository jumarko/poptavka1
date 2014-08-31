/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.detail;

import com.eprovement.poptavka.client.catLocSelector.others.CatLogSimpleCell;
import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.eprovement.poptavka.client.common.UrgencySelectorView;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.smallPopups.SimpleConfirmPopup;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.DemandField;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * View consists of components allowing updating demand's data.
 * <b><i>Note:</i></b>
 * For updating categories and localities a CatLocSelector widget is used.
 * The widget is displayed in SimpleConfirmPopup.
 *
 * @author Martin Slavkovsky
 */
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
    @UiField(provided = true) ValidationMonitor titleMonitor, priceMonitor, endDateMonitor;
    @UiField(provided = true) ValidationMonitor maxOffersMonitor, minRatingMonitor, descriptionMonitor;
    @UiField(provided = true) CellList categories, localities;
    @UiField UrgencySelectorView urgencySelector;
    @UiField FluidRow editButtonsPanel;
    @UiField Button editCatBtn, editLocBtn, submitButton, cancelButton;
    @UiField Tooltip submitBtnTooltip;
    /** Class attributes. **/
    private ListDataProvider categoryProvider;
    private ListDataProvider localityProvider;
    private SimpleConfirmPopup selectorPopup;
    private List<ValidationMonitor> monitors;
    private long demandId;

    /**************************************************************************/
    /* INITIALIZATON                                                          */
    /**************************************************************************/
    /**
     * Creates EditableDemandDetail view's compontents.
     */
    @Override
    public void createView() {
        categories = new CellList<ICatLocDetail>(new CatLogSimpleCell());
        categoryProvider = new ListDataProvider(CatLocDetail.KEY_PROVIDER);
        categoryProvider.addDataDisplay(categories);

        localities = new CellList<ICatLocDetail>(new CatLogSimpleCell());
        localityProvider = new ListDataProvider(CatLocDetail.KEY_PROVIDER);
        localityProvider.addDataDisplay(localities);

        initValidationMonitors();

        initWidget(uiBinder.createAndBindUi(this));

        selectorPopup = new SimpleConfirmPopup();

        ((DateBox) endDateMonitor.getWidget()).setFormat(new DateBox.DefaultFormat(Storage.get().getDateTimeFormat()));

        StyleResource.INSTANCE.common().ensureInjected();
    }

    /**
     * Inits validation monitors.
     */
    private void initValidationMonitors() {
        titleMonitor = createDemandValidationMonitor(DemandField.TITLE);
        priceMonitor = createDemandValidationMonitor(DemandField.PRICE);
        endDateMonitor = createDemandValidationMonitor(DemandField.END_DATE);
        maxOffersMonitor = createDemandValidationMonitor(DemandField.MAX_OFFERS);
        minRatingMonitor = createDemandValidationMonitor(DemandField.MIN_RATING);
        descriptionMonitor = createDemandValidationMonitor(DemandField.DESCRIPTION);
        monitors = Arrays.asList(
                titleMonitor, priceMonitor, endDateMonitor, maxOffersMonitor, minRatingMonitor, descriptionMonitor);
    }

    /**
     * Creates validation monitor.
     * @param fieldField - validation field
     * @return validation monitor
     */
    private ValidationMonitor createDemandValidationMonitor(DemandField fieldField) {
        return new ValidationMonitor<FullDemandDetail>(FullDemandDetail.class, fieldField.getValue());
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    /**
     * Sets demand detail data.
     * @param demandDetail carrying data.
     */
    @Override
    public void setDemanDetail(FullDemandDetail demandDetail) {
        GWT.log("detail detail" + demandDetail.toString());
        demandId = demandDetail.getDemandId();
        titleMonitor.setValue(demandDetail.getDemandTitle());
        priceMonitor.setValue(demandDetail.getPrice());
        endDateMonitor.setValue(demandDetail.getEndDate());
        urgencySelector.setValidTo(demandDetail.getValidTo());
        maxOffersMonitor.setValue(demandDetail.getMaxSuppliers());
        minRatingMonitor.setValue(demandDetail.getMinRating());
        descriptionMonitor.setValue(demandDetail.getDescription());
        categoryProvider.setList(demandDetail.getCategories());
        localityProvider.setList(demandDetail.getLocalities());
    }

    /**
     * Updates given demand detail with current widget's data.
     * @param detail to be updated
     * @return updated detail object
     */
    @Override
    public FullDemandDetail updateDemandDetail(FullDemandDetail demandToUpdate) {
        demandToUpdate.setDemandTitle((String) titleMonitor.getValue());
        demandToUpdate.setPrice((BigDecimal) priceMonitor.getValue());
        demandToUpdate.setEndDate((Date) endDateMonitor.getValue());
        demandToUpdate.setValidTo(urgencySelector.getValidTo());
        demandToUpdate.setCategories(getCategories());
        demandToUpdate.setLocalities(getLocalities());
        demandToUpdate.setMaxSuppliers((Integer) maxOffersMonitor.getValue());
        demandToUpdate.setMinRating((Integer) minRatingMonitor.getValue());
        demandToUpdate.setDescription((String) descriptionMonitor.getValue());
        return demandToUpdate;
    }

    /**************************************************************************/
    /* GETTER                                                                 */
    /**************************************************************************/
    /** Button. **/
    /**
     * @return the edit categories button
     */
    @Override
    public Button getEditCatBtn() {
        return editCatBtn;
    }

    /**
     * @return the edit localities button
     */
    @Override
    public Button getEditLocBtn() {
        return editLocBtn;
    }

    /**
     * @return the submit button
     */
    @Override
    public Button getSubmitButton() {
        return submitButton;
    }

    /**
     * @return the cancel button
     */
    @Override
    public Button getCancelButton() {
        return cancelButton;
    }

    /** Panels. **/
    /**
     * @return the edit buttons panel
     */
    @Override
    public FluidRow getEditButtonsPanel() {
        return editButtonsPanel;
    }

    /**
     * @return the SimpleConfirmPopup
     */
    @Override
    public SimpleConfirmPopup getSelectorPopup() {
        return selectorPopup;
    }

    /**
     * @return the toolbar's submit button tooltip
     */
    @Override
    public Tooltip getSubmitBtnTooltip() {
        return submitBtnTooltip;
    }

    /** Data. **/
    /**
     * @return the demand id
     */
    @Override
    public long getDemandId() {
        return demandId;
    }

    /**
     * @return the updated categories
     */
    @Override
    public List<ICatLocDetail> getCategories() {
        return this.categoryProvider.getList();
    }

    /**
     * @return the updated localities
     */
    @Override
    public List<ICatLocDetail> getLocalities() {
        return this.localityProvider.getList();
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void reset() {
        for (ValidationMonitor monitor : monitors) {
            monitor.reset();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isValid() {
        boolean valid = true;
        for (ValidationMonitor monitor : monitors) {
            valid = monitor.isValid() && valid;
        }
        valid = !categoryProvider.getList().isEmpty() && valid;
        valid = !localityProvider.getList().isEmpty() && valid;
        return valid;
    }
}
