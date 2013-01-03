/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.common.ChangeMonitor;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import com.eprovement.poptavka.shared.domain.type.ClientDemandType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author ivan.vlcek, jarko
 */
public class AdminDemandInfoView extends Composite {

    private static AdminDemandInfoViewUiBinder uiBinder = GWT.create(AdminDemandInfoViewUiBinder.class);
    private LocalizableMessages messages = GWT.create(LocalizableMessages.class);
    private NumberFormat currencyFormat = NumberFormat.getFormat(messages.currencyFormat());

    interface AdminDemandInfoViewUiBinder extends
            UiBinder<Widget, AdminDemandInfoView> {
    }
    // demand detail input fields
    @UiField(provided = true)
    ChangeMonitor titleBox, descriptionBox, endDateBox, expirationBox, priceBox,
    maxOffers, minRating, demandStatus, demandType, categoryList, localityList;
    @UiField
    TextBox clientID;
    @UiField
    Button editCatBtn, editLocBtn, createButton, updateButton;
    private FullDemandDetail demandInfo;
    private ArrayList<CategoryDetail> categories;
    private ArrayList<LocalityDetail> localities;
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
        titleBox = new ChangeMonitor(new ChangeDetail(DemandField.TITLE));
        descriptionBox = new ChangeMonitor(new ChangeDetail(DemandField.DESCRIPTION));
        endDateBox = new ChangeMonitor(new ChangeDetail(DemandField.END_DATE));
        expirationBox = new ChangeMonitor(new ChangeDetail(DemandField.VALID_TO_DATE));
        priceBox = new ChangeMonitor(new ChangeDetail(DemandField.PRICE));
        maxOffers = new ChangeMonitor(new ChangeDetail(DemandField.MAX_OFFERS));
        minRating = new ChangeMonitor(new ChangeDetail(DemandField.MIN_RATING));
        demandStatus = new ChangeMonitor(new ChangeDetail(DemandField.DEMAND_STATUS));
        demandType = new ChangeMonitor(new ChangeDetail(DemandField.DEMAND_TYPE));
        categoryList = new ChangeMonitor(new ChangeDetail(DemandField.CATEGORIES));
        localityList = new ChangeMonitor(new ChangeDetail(DemandField.LOCALITIES));
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

    public FullDemandDetail getUpdatedDemandDetail() {
        if (demandInfo == null) {
            return null;
        }
        boolean t = priceBox.getValue() == null;
        if (t) {
            GWT.log("d" + t + "max offer");
        }
        GWT.log("d" + t + "max offer");
        GWT.log("d" + priceBox.getValue().equals("") + "price ");

        // Update the contact.
        demandInfo.setTitle((String) titleBox.getValue());
        demandInfo.setDescription((String) descriptionBox.getValue());
        demandInfo.setPrice(BigDecimal.valueOf(Double.valueOf(currencyFormat.parse((String) priceBox.getValue()))));
        demandInfo.setEndDate((Date) endDateBox.getValue());
        demandInfo.setValidToDate((Date) expirationBox.getValue());
        demandInfo.setClientId(Long.valueOf((String) clientID.getValue()));
        demandInfo.setMaxOffers(Integer.valueOf((String) maxOffers.getValue()));
        demandInfo.setMinRating(Integer.valueOf((String) minRating.getValue()));
        demandInfo.setDemandType(((String) demandType.getValue()));
        demandInfo.setDemandStatus(DemandStatus.valueOf((String) demandStatus.getValue()));
        demandInfo.setCategories(categories);
        demandInfo.setLocalities(localities);

        return demandInfo;
    }

    public ArrayList<CategoryDetail> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDetail> categories) {
        this.categories = new ArrayList<CategoryDetail>(categories);
        setCategoryBox(categories);
    }

    public void setCategoryBox(List<CategoryDetail> categoriesList) {
        ((ListBox) categoryList.getWidget()).clear();
        if (categoriesList != null) {
            for (CategoryDetail cat : categoriesList) {
                ((ListBox) categoryList.getWidget()).addItem(cat.getName());
            }

        }
    }

    public ArrayList<LocalityDetail> getLocalities() {
        return localities;
    }

    public void setLocalities(List<LocalityDetail> localities) {
        this.localities = new ArrayList<LocalityDetail>(localities);
        setLocalityBox(localities);
    }

    public void setLocalityBox(List<LocalityDetail> localitiesList) {
        ((ListBox) localityList.getWidget()).clear();
        if (localitiesList != null) {
            for (LocalityDetail loc : localitiesList) {
                ((ListBox) localityList.getWidget()).addItem(loc.getName());
            }
        }
    }

    public FullDemandDetail getDemandDetail() {
        return demandInfo;
    }

    public void setDemandDetail(FullDemandDetail demand) {
        resetChangeMonitors();
        if (demand != null) {
            this.demandInfo = demand;
            this.categories = demand.getCategories();
            this.localities = demand.getLocalities();

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
                demandType.setValue(j);

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
                demandStatus.setValue(j);

                setCategoryBox(demand.getCategories());
                setLocalityBox(demand.getLocalities());
            }
        }
    }

    public void setFieldChanges(HashSet<ChangeDetail> changes) {
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
//                case MAX_OFFERS:
//                    ???
//                    break;
                case MIN_RATING:
                    minRating.setChanged(change);
                    break;
                case DEMAND_STATUS:
                    demandStatus.setChanged(change);
                    break;
//                case CREATED:
//                    break;
                case CATEGORIES:
                    //Treba zistovat ci sa kategorie zmenili? Ak ano, ako aby to nebolo narocne?
                    categoryList.setChanged(change);
                    break;
                case LOCALITIES:
                    localityList.setChanged(change);
                    break;
//                case EXCLUDE_SUPPLIER:
//                    break;
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
        demandStatus.reset();
        demandType.reset();
        categoryList.reset();
        localityList.reset();
    }
}