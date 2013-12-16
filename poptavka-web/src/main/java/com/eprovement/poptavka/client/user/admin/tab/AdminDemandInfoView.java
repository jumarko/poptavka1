/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.catLocSelector.others.CatLogSimpleCell;
import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.eprovement.poptavka.client.common.smallPopups.SimpleConfirmPopup;
import com.eprovement.poptavka.client.user.widget.grid.cell.SupplierCell;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.type.ClientDemandType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ivan.vlcek, jarko
 */
public class AdminDemandInfoView extends Composite {

    private static AdminDemandInfoViewUiBinder uiBinder = GWT.create(AdminDemandInfoViewUiBinder.class);
    private LocalizableMessages messages = GWT.create(LocalizableMessages.class);
    private NumberFormat currencyFormat = NumberFormat.getFormat(messages.formatCurrency());

    interface AdminDemandInfoViewUiBinder extends
            UiBinder<Widget, AdminDemandInfoView> {
    }
    /**************************************************************************/
    /* Attributes                                                               */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) ValidationMonitor titleMonitor, descriptionMonitor, endDateMonitor;
    @UiField(provided = true) ValidationMonitor expirationMonitor, priceMonitor, maxOffersMonitor;
    @UiField(provided = true) ValidationMonitor minRatingMonitor, demandStatusMonitor, demandTypeMonitor;
    @UiField(provided = true) CellList categoryList, localityList, excludedSupplierList;
    @UiField TextBox clientID;
    @UiField Button editCatBtn, editLocBtn, editExcludedSupplierBtn, createButton, updateButton;
    /** Class attributes. **/
    private ListDataProvider categoryProvider;
    private ListDataProvider localityProvider;
    private ListDataProvider excludedSupplierProvider;
    private SimpleConfirmPopup selectorWidgetPopup;

    /**************************************************************************/
    /* INITIALIZATON                                                          */
    /**************************************************************************/
    public AdminDemandInfoView() {
        titleMonitor = createValidationMonitor(DemandField.TITLE);
        descriptionMonitor = createValidationMonitor(DemandField.DESCRIPTION);
        endDateMonitor = createValidationMonitor(DemandField.END_DATE);
        expirationMonitor = createValidationMonitor(DemandField.VALID_TO);
        priceMonitor = createValidationMonitor(DemandField.PRICE);
        maxOffersMonitor = createValidationMonitor(DemandField.MAX_OFFERS);
        minRatingMonitor = createValidationMonitor(DemandField.MIN_RATING);
        demandStatusMonitor = createValidationMonitor(DemandField.DEMAND_STATUS);
        demandTypeMonitor = createValidationMonitor(DemandField.DEMAND_TYPE);

        categoryList = new CellList<ICatLocDetail>(new CatLogSimpleCell());
        categoryProvider = new ListDataProvider(CatLocDetail.KEY_PROVIDER);
        categoryProvider.addDataDisplay(categoryList);

        localityList = new CellList<ICatLocDetail>(new CatLogSimpleCell());
        localityProvider = new ListDataProvider(CatLocDetail.KEY_PROVIDER);
        localityProvider.addDataDisplay(localityList);

        excludedSupplierList = new CellList<FullSupplierDetail>(new SupplierCell());
        excludedSupplierProvider = new ListDataProvider(CatLocDetail.KEY_PROVIDER);
        excludedSupplierProvider.addDataDisplay(excludedSupplierList);

        initWidget(uiBinder.createAndBindUi(this));

        selectorWidgetPopup = new SimpleConfirmPopup();

        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
        ((DateBox) expirationMonitor.getWidget()).setFormat(new DateBox.DefaultFormat(dateFormat));
        ((DateBox) endDateMonitor.getWidget()).setFormat(new DateBox.DefaultFormat(dateFormat));
        clientID.setEnabled(false);
    }

    private ValidationMonitor createValidationMonitor(DemandField field) {
        return new ValidationMonitor<FullDemandDetail>(FullDemandDetail.class, field.getValue());
    }

    /**************************************************************************/
    /* setters                                                                */
    /**************************************************************************/
    public void setDemandDetail(FullDemandDetail demand) {
        resetValidationMonitors();
        if (demand != null) {
            updateButton.setEnabled(demand != null);
            if (demand != null) {
                titleMonitor.setValue(demand.getDemandTitle());
                descriptionMonitor.setValue(demand.getDescription());
                priceMonitor.setValue(currencyFormat.format(demand.getPrice()));
                endDateMonitor.setValue(demand.getEndDate());
                expirationMonitor.setValue(demand.getValidTo());
                clientID.setValue(String.valueOf(demand.getClientId()));
                maxOffersMonitor.setValue(String.valueOf(demand.getMaxSuppliers()));
                minRatingMonitor.setValue(String.valueOf(demand.getMinRating()));

                excludedSupplierProvider.setList(demand.getExcludedSuppliers());
                categoryProvider.setList(demand.getCategories());
                localityProvider.setList(demand.getLocalities());

                // demand type settings
                // Add the types to the status box.
                final ClientDemandType[] types = ClientDemandType.values();
                int i = 0;
                int j = 0;
                ((ListBox) demandTypeMonitor.getWidget()).clear();
                for (ClientDemandType type : types) {
                    ((ListBox) demandTypeMonitor.getWidget()).addItem(type.getValue());
                    if (demand.getDemandType() != null
                            && demand.getDemandType().equalsIgnoreCase(type.getValue())) {
                        j = i;
                    }
                    i++;
                }
                demandTypeMonitor.setValue(j);

                // demand status settings
                // Add the statuses to the status box.
                final DemandStatus[] statuses = DemandStatus.values();
                i = 0;
                j = 0;
                ((ListBox) demandStatusMonitor.getWidget()).clear();
                for (DemandStatus status : statuses) {
                    ((ListBox) demandStatusMonitor.getWidget()).addItem(status.getValue());
                    if (demand.getDemandStatus() != null
                            && demand.getDemandStatus() == DemandStatus.valueOf(status.getValue())) {
                        j = i;
                    }
                    i++;
                }
                demandStatusMonitor.setValue(j);
            }
        }
    }

    public FullDemandDetail updateDemandDetail(FullDemandDetail demandToUpdate) {
        demandToUpdate.setDemandTitle((String) titleMonitor.getValue());
        demandToUpdate.setPrice((BigDecimal) priceMonitor.getValue());
        demandToUpdate.setEndDate((Date) endDateMonitor.getValue());
//        demandToUpdate.setValidTo(urgencySelector.getValidTo());
        demandToUpdate.setCategories(getCategories());
        demandToUpdate.setLocalities(getLocalities());
        demandToUpdate.setMaxSuppliers((Integer) maxOffersMonitor.getValue());
        demandToUpdate.setMinRating((Integer) minRatingMonitor.getValue());
        demandToUpdate.setDescription((String) descriptionMonitor.getValue());
        return demandToUpdate;
    }

    public void resetValidationMonitors() {
        titleMonitor.resetValidation();
        descriptionMonitor.resetValidation();
        endDateMonitor.resetValidation();
        expirationMonitor.resetValidation();
        priceMonitor.resetValidation();
        maxOffersMonitor.resetValidation();
        minRatingMonitor.resetValidation();
        demandStatusMonitor.resetValidation();
        demandTypeMonitor.resetValidation();
    }

    /**************************************************************************/
    /* Getters                                                               */
    /**************************************************************************/
    public List<ICatLocDetail> getCategories() {
        return categoryProvider.getList();
    }

    public List<ICatLocDetail> getLocalities() {
        return localityProvider.getList();
    }

    public SimpleConfirmPopup getSelectorWidgetPopup() {
        return selectorWidgetPopup;
    }

    public Widget getWidgetView() {
        return this;
    }

    public Button getUpdateBtn() {
        return updateButton;
    }

    public Button getEditCatBtn() {
        return editCatBtn;
    }

    public Button getEditLocBtn() {
        return editLocBtn;
    }

    public Button getEditExcludedSupplierBtn() {
        return editExcludedSupplierBtn;
    }

    public ValidationMonitor getTitleBox() {
        return titleMonitor;
    }
}