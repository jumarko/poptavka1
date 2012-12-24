/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.type.ClientDemandType;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    @UiField
    TextArea titleBox, descriptionBox;
    @UiField
    DateBox endDateBox, expirationBox;
    @UiField
    TextBox priceBox, clientID, maxOffers, minRating;
    @UiField
    ListBox demandStatus, demandType, categoryList, localityList;
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

    public ListBox getCategoryList() {
        return categoryList;
    }

    public ListBox getLocalityList() {
        return localityList;
    }

    public AdminDemandInfoView() {
        initWidget(uiBinder.createAndBindUi(this));
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
        expirationBox.setFormat(new DateBox.DefaultFormat(dateFormat));
        endDateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
//        initDemandInfoForm();
        createSelectorWidgetPopup();
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
        boolean t = priceBox.getText() == null;
        if (t) {
            GWT.log("d" + t + "max offer");
        }
        GWT.log("d" + t + "max offer");
        GWT.log("d" + priceBox.getText().equals("") + "price ");

        // Update the contact.
        demandInfo.setTitle(titleBox.getText());
        demandInfo.setDescription(descriptionBox.getText());
        demandInfo.setPrice(BigDecimal.valueOf(Double.valueOf(currencyFormat.parse(priceBox.getText()))));
        demandInfo.setEndDate(endDateBox.getValue());
        demandInfo.setValidToDate(expirationBox.getValue());
        demandInfo.setClientId(Long.valueOf(clientID.getText()));
        demandInfo.setMaxOffers(Integer.valueOf(maxOffers.getValue()));
        demandInfo.setMinRating(Integer.valueOf(minRating.getValue()));
        demandInfo.setDemandType(demandType.getItemText(demandType.getSelectedIndex()));
        demandInfo.setDemandStatus(DemandStatus.valueOf(demandStatus.getItemText(demandStatus.getSelectedIndex())));
        demandInfo.setCategories(categories);
        demandInfo.setLocalities(localities);

        return demandInfo;
    }

//    private void initDemandInfoForm() {
//        // initWidget(uiBinder.createAndBindUi(this));
//        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG);
//        expirationBox.setFormat(new DateBox.DefaultFormat(dateFormat));
//        endDateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
//    }

    public ArrayList<CategoryDetail> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDetail> categories) {
        this.categories = new ArrayList<CategoryDetail>(categories);
        setCategoryBox(categories);
    }

    public void setCategoryBox(List<CategoryDetail> categoriesList) {
        categoryList.clear();
        if (categoriesList != null) {
            for (CategoryDetail cat : categoriesList) {
                categoryList.addItem(cat.getName());
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
        localityList.clear();
        if (localitiesList != null) {
            for (LocalityDetail loc : localitiesList) {
                localityList.addItem(loc.getName());
            }
        }
    }

    public FullDemandDetail getDemandDetail() {
        return demandInfo;
    }

    public void setDemandDetail(FullDemandDetail demand) {
        if (demand != null) {
            this.demandInfo = demand;
            this.categories = demand.getCategories();
            this.localities = demand.getLocalities();

            updateButton.setEnabled(demand != null);
            if (demand != null) {
                titleBox.setText(demand.getTitle());
                descriptionBox.setText(demand.getDescription());
                priceBox.setText(currencyFormat.format(demand.getPrice()));
                endDateBox.setValue(demand.getEndDate());
                expirationBox.setValue(demand.getValidToDate());
                clientID.setText(String.valueOf(demand.getClientId()));
                maxOffers.setText(String.valueOf(demand.getMaxOffers()));
                minRating.setText(String.valueOf(demand.getMinRating()));

                // demand type settings
                // Add the types to the status box.
                final ClientDemandType[] types = ClientDemandType.values();
                int i = 0;
                int j = 0;
                demandType.clear();
                for (ClientDemandType type : types) {
                    demandType.addItem(type.getValue());
                    if (demand.getDemandType() != null
                            && demand.getDemandType().equalsIgnoreCase(type.getValue())) {
                        j = i;
                    }
                    i++;
                }
                demandType.setSelectedIndex(j);

                // demand status settings
                // Add the statuses to the status box.
                final DemandStatus[] statuses = DemandStatus.values();
                i = 0;
                j = 0;
                demandStatus.clear();
                for (DemandStatus status : statuses) {
                    demandStatus.addItem(status.getValue());
                    if (demand.getDemandStatus() != null
                            && demand.getDemandStatus() == DemandStatus.valueOf(status.getValue())) {
                        j = i;
                    }
                    i++;
                }
                demandStatus.setSelectedIndex(j);

                setCategoryBox(demand.getCategories());
                setLocalityBox(demand.getLocalities());
            }
        }
    }
}