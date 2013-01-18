/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.common.ChangeMonitor;
import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.locality.LocalityCell;
import com.eprovement.poptavka.client.user.widget.grid.cell.SupplierCell;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.type.ClientDemandType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import java.util.ArrayList;
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
    // demand detail input fields
    @UiField(provided = true) ChangeMonitor titleBox, descriptionBox, endDateBox, expirationBox, priceBox;
    @UiField(provided = true) ChangeMonitor maxOffers, minRating, demandStatus, demandType, categoryList;
    @UiField(provided = true) ChangeMonitor localityList, excludedSupplierList;
    @UiField(provided = true) CellList categoryCellList, localityCellList, excludedSupplierCellList;
    @UiField TextBox clientID;
    @UiField Button editCatBtn, editLocBtn, editExcludedSupplierBtn, createButton, updateButton;
    private FullDemandDetail demandInfo;
    private PopupPanel selectorWidgetPopup;

    public PopupPanel getSelectorWidgetPopup() {
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

    public ChangeMonitor getCategoryList() {
        return categoryList;
    }

    public ChangeMonitor getLocalityList() {
        return localityList;
    }

    public ChangeMonitor getTitleBox() {
        return titleBox;
    }

    public AdminDemandInfoView() {
        titleBox = new ChangeMonitor<FullDemandDetail>(
                FullDemandDetail.class, new ChangeDetail(DemandField.TITLE));
        descriptionBox = new ChangeMonitor<FullDemandDetail>(
                FullDemandDetail.class, new ChangeDetail(DemandField.DESCRIPTION));
        endDateBox = new ChangeMonitor<FullDemandDetail>(
                FullDemandDetail.class, new ChangeDetail(DemandField.END_DATE));
        expirationBox = new ChangeMonitor<FullDemandDetail>(
                FullDemandDetail.class, new ChangeDetail(DemandField.VALID_TO_DATE));
        priceBox = new ChangeMonitor<FullDemandDetail>(
                FullDemandDetail.class, new ChangeDetail(DemandField.PRICE));
        maxOffers = new ChangeMonitor<FullDemandDetail>(
                FullDemandDetail.class, new ChangeDetail(DemandField.MAX_OFFERS));
        minRating = new ChangeMonitor<FullDemandDetail>(
                FullDemandDetail.class, new ChangeDetail(DemandField.MIN_RATING));
        excludedSupplierCellList = new CellList<FullSupplierDetail>(new SupplierCell());
        excludedSupplierList = new ChangeMonitor<FullDemandDetail>(
                FullDemandDetail.class, new ChangeDetail(DemandField.EXCLUDE_SUPPLIER));
        demandStatus = new ChangeMonitor<FullDemandDetail>(
                FullDemandDetail.class, new ChangeDetail(DemandField.DEMAND_STATUS));
        demandType = new ChangeMonitor<FullDemandDetail>(
                FullDemandDetail.class, new ChangeDetail(DemandField.DEMAND_TYPE));
        categoryCellList = new CellList<CategoryDetail>(new CategoryCell(CategoryCell.DISPLAY_COUNT_DISABLED));
        categoryList = new ChangeMonitor<FullDemandDetail>(
                FullDemandDetail.class, new ChangeDetail(DemandField.CATEGORIES));
        localityCellList = new CellList<LocalityDetail>(new LocalityCell(LocalityCell.DISPLAY_COUNT_DISABLED));
        localityList = new ChangeMonitor<FullDemandDetail>(
                FullDemandDetail.class, new ChangeDetail(DemandField.LOCALITIES));
        //
        initWidget(uiBinder.createAndBindUi(this));
        //
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
        ((DateBox) expirationBox.getWidget()).setFormat(new DateBox.DefaultFormat(dateFormat));
        ((DateBox) endDateBox.getWidget()).setFormat(new DateBox.DefaultFormat(dateFormat));
        clientID.setEnabled(false);
        //
        createSelectorWidgetPopup();
    }

    public void setChangeHandler(ChangeHandler changeHandler) {
        titleBox.addChangeHandler(changeHandler);
        descriptionBox.addChangeHandler(changeHandler);
        endDateBox.addChangeHandler(changeHandler);
        expirationBox.addChangeHandler(changeHandler);
        priceBox.addChangeHandler(changeHandler);
        maxOffers.addChangeHandler(changeHandler);
        minRating.addChangeHandler(changeHandler);
        excludedSupplierList.addChangeHandler(changeHandler);
        demandStatus.addChangeHandler(changeHandler);
        demandType.addChangeHandler(changeHandler);
        categoryList.addChangeHandler(changeHandler);
        localityList.addChangeHandler(changeHandler);
    }

    private void createSelectorWidgetPopup() {
        selectorWidgetPopup = new PopupPanel(true);
        selectorWidgetPopup.setSize("300px", "300px");
        selectorWidgetPopup.setGlassEnabled(true);
        selectorWidgetPopup.hide();
    }

//    public FullDemandDetail getUpdatedDemandDetail() {
//        if (demandInfo == null) {
//            return null;
//        }
//        boolean t = priceBox.getValue() == null;
//        if (t) {
//            GWT.log("d" + t + "max offer");
//        }
//        GWT.log("d" + t + "max offer");
//        GWT.log("d" + priceBox.getValue().equals("") + "price ");
//
//        // Update the contact.
//        demandInfo.setTitle((String) titleBox.getValue());
//        demandInfo.setDescription((String) descriptionBox.getValue());
//        demandInfo.setPrice(BigDecimal.valueOf(Double.valueOf(currencyFormat.parse((String) priceBox.getValue()))));
//        demandInfo.setEndDate((Date) endDateBox.getValue());
//        demandInfo.setValidToDate((Date) expirationBox.getValue());
//        demandInfo.setClientId(Long.valueOf((String) clientID.getValue()));
//        demandInfo.setMaxOffers(Integer.valueOf((String) maxOffers.getValue()));
//        demandInfo.setMinRating(Integer.valueOf((String) minRating.getValue()));
//        demandInfo.setExcludedSuppliers((ArrayList<FullSupplierDetail>)excludedList.getValue());
//        demandInfo.setDemandType(((String) demandType.getValue()));
//        demandInfo.setDemandStatus(DemandStatus.valueOf((String) demandStatus.getValue()));
//        demandInfo.setCategories((ArrayList<CategoryDetail>) categoryList.getValue());
//        demandInfo.setLocalities((ArrayList<LocalityDetail>) localityList.getValue());
//
//        return demandInfo;
//    }
    public ArrayList<CategoryDetail> getCategories() {
        return (ArrayList<CategoryDetail>) categoryList.getValue();
    }

    /**
     * Need for CategorySelector when closing to set newly chosen categories.
     * @param categories
     */
    public void setCategories(List<CategoryDetail> categories) {
        categoryList.setValue(categories);
    }

    public ArrayList<LocalityDetail> getLocalities() {
        return (ArrayList<LocalityDetail>) localityList.getValue();
    }

    public void setLocalities(List<LocalityDetail> localities) {
        localityList.setValue(localities);
    }

    public ArrayList<FullSupplierDetail> getExcludedSupplier() {
        return (ArrayList<FullSupplierDetail>) excludedSupplierList.getValue();
    }

    public void setExcludedSupplier(List<FullSupplierDetail> localities) {
        excludedSupplierList.setValue(localities);
    }

    public FullDemandDetail getDemandDetail() {
        return demandInfo;
    }

    public void setDemandDetail(FullDemandDetail demand) {
        resetChangeMonitors();
        if (demand != null) {
            this.demandInfo = demand;

            updateButton.setEnabled(demand != null);
            if (demand != null) {
                titleBox.setBothValues(demand.getTitle());
                descriptionBox.setBothValues(demand.getDescription());
                priceBox.setBothValues(currencyFormat.format(demand.getPrice()));
                endDateBox.setBothValues(demand.getEndDate());
                expirationBox.setBothValues(demand.getValidToDate());
                clientID.setValue(String.valueOf(demand.getClientId()));
                maxOffers.setBothValues(String.valueOf(demand.getMaxOffers()));
                minRating.setBothValues(String.valueOf(demand.getMinRating()));
                excludedSupplierList.setBothValues(demand.getExcludedSuppliers());
                categoryList.setBothValues(demand.getCategories());
                localityList.setBothValues(demand.getLocalities());

                // demand type settings
                // Add the types to the status box.
                final ClientDemandType[] types = ClientDemandType.values();
                int i = 0;
                int j = 0;
                ((ListBox) demandType.getWidget()).clear();
                for (ClientDemandType type : types) {
                    ((ListBox) demandType.getWidget()).addItem(type.getValue());
                    if (demand.getDemandType() != null
                            && demand.getDemandType().equalsIgnoreCase(type.getValue())) {
                        j = i;
                    }
                    i++;
                }
                demandType.setBothValues(j);

                // demand status settings
                // Add the statuses to the status box.
                final DemandStatus[] statuses = DemandStatus.values();
                i = 0;
                j = 0;
                ((ListBox) demandStatus.getWidget()).clear();
                for (DemandStatus status : statuses) {
                    ((ListBox) demandStatus.getWidget()).addItem(status.getValue());
                    if (demand.getDemandStatus() != null
                            && demand.getDemandStatus() == DemandStatus.valueOf(status.getValue())) {
                        j = i;
                    }
                    i++;
                }
                demandStatus.setBothValues(j);
            }
        }
    }

    public void setFieldChanges(ArrayList<ChangeDetail> changes) {
        for (ChangeDetail change : changes) {
            switch ((DemandField) change.getField()) {
                case TITLE:
                    titleBox.setChanged(change);
                    break;
                case DESCRIPTION:
                    descriptionBox.setChanged(change);
                    break;
                case PRICE:
                    priceBox.setChanged(change);
                    break;
                case END_DATE:
                    endDateBox.setChanged(change);
                    break;
                case VALID_TO_DATE:
                    expirationBox.setChanged(change);
                    break;
                case MAX_OFFERS:
                    maxOffers.setChanged(change);
                    break;
                case MIN_RATING:
                    minRating.setChanged(change);
                    break;
                case DEMAND_STATUS:
                    demandStatus.setChanged(change);
                    break;
                case CATEGORIES:
                    categoryList.setChanged(change);
                    break;
                case LOCALITIES:
                    localityList.setChanged(change);
                    break;
                case EXCLUDE_SUPPLIER:
                    excludedSupplierList.setChanged(change);
                    break;
                default:
                    break;
            }
        }
    }

    public void resetChangeMonitors() {
        titleBox.reset();
        descriptionBox.reset();
        endDateBox.reset();
        expirationBox.reset();
        priceBox.reset();
        maxOffers.reset();
        minRating.reset();
        excludedSupplierList.reset();
        demandStatus.reset();
        demandType.reset();
        categoryList.reset();
        localityList.reset();
    }

    public void revertChangeMonitors() {
        titleBox.revert();
        descriptionBox.revert();
        endDateBox.revert();
        expirationBox.revert();
        priceBox.revert();
        maxOffers.revert();
        minRating.revert();
        excludedSupplierList.revert();
        demandStatus.revert();
        demandType.revert();
        categoryList.revert();
        localityList.revert();
    }
}