/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.type.ClientDemandType;
import cz.poptavka.sample.shared.domain.type.DemandStatusType;
import java.math.BigDecimal;

/**
 *
 * @author ivan.vlcek, jarko
 */
public class AdminDemandInfoView extends Composite implements
        AdminDemandInfoPresenter.AdminDemandInfoInterface {

    private static AdminDemandInfoViewUiBinder uiBinder = GWT.create(AdminDemandInfoViewUiBinder.class);

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
    ListBox demandStatus, demandType, editCatList,
    editLocList, categoryList, localityList;
    @UiField
    Button editCatBtn, editLocBtn, createButton, updateButton,
    finnishCatBtn, finnishLocBtn, backCatBtn, backLocBtn,
    rootCatBtn, rootLocBtn;
    @UiField
    VerticalPanel editCatPanel, editLocPanel;
    @UiField
    Label catPath, locPath;
    private FullDemandDetail demandInfo;

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public Button getUpdateBtn() {
        return updateButton;
    }

    @Override
    public Button getEditCatBtn() {
        return editCatBtn;
    }

    @Override
    public Button getEditLocBtn() {
        return editLocBtn;
    }

    @Override
    public Button getFinnishCatBtn() {
        return finnishCatBtn;
    }

    @Override
    public Button getFinnishLocBtn() {
        return finnishLocBtn;
    }

    @Override
    public ListBox getEditCatList() {
        return editCatList;
    }

    @Override
    public ListBox getEditLocList() {
        return editLocList;
    }

    @Override
    public Button getBackCatBtn() {
        return backCatBtn;
    }

    @Override
    public Button getBackLocBtn() {
        return backLocBtn;
    }

    @Override
    public Button getRootCatBtn() {
        return rootCatBtn;
    }

    @Override
    public Button getRootLocBtn() {
        return rootLocBtn;
    }

    @Override
    public VerticalPanel getEditCatPanel() {
        return editCatPanel;
    }

    @Override
    public VerticalPanel getEditLocPanel() {
        return editLocPanel;
    }

    @Override
    public ListBox getCategoryList() {
        return categoryList;
    }

    @Override
    public ListBox getLocalityList() {
        return localityList;
    }

    @Override
    public Label getCatPath() {
        return catPath;
    }

    @Override
    public Label getLocPath() {
        return locPath;
    }

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        editCatPanel.setVisible(false);
        editLocPanel.setVisible(false);
        editCatList.setEnabled(false);
        editLocList.setEnabled(false);
        initDemandInfoForm();
    }

    @Override
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
        demandInfo.setPrice(BigDecimal.valueOf(Double.valueOf(priceBox.getText())));
        demandInfo.setEndDate(endDateBox.getValue());
        demandInfo.setValidToDate(expirationBox.getValue());
        demandInfo.setClientId(Long.valueOf(clientID.getText()));
        demandInfo.setMaxOffers(Integer.valueOf(maxOffers.getValue()));
        demandInfo.setMinRating(Integer.valueOf(minRating.getValue()));
        demandInfo.setDemandType(demandType.getItemText(demandType.getSelectedIndex()));
        demandInfo.setDemandStatus(demandStatus.getItemText(demandStatus.getSelectedIndex()));

        return demandInfo;
    }

    private void initDemandInfoForm() {
        // initWidget(uiBinder.createAndBindUi(this));
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG);
        expirationBox.setFormat(new DateBox.DefaultFormat(dateFormat));
        endDateBox.setFormat(new DateBox.DefaultFormat(dateFormat));

        // Initialize the contact to null.
        setDemandDetail(null);
    }

    @Override
    public void setDemandDetail(FullDemandDetail contact) {
        this.demandInfo = contact;
        updateButton.setEnabled(contact != null);
        if (contact != null) {
            titleBox.setText(contact.getTitle());
            descriptionBox.setText(contact.getDescription());
            priceBox.setText(contact.getPriceString());
            endDateBox.setValue(contact.getEndDate());
            expirationBox.setValue(contact.getValidToDate());
            clientID.setText(String.valueOf(contact.getClientId()));
            maxOffers.setText(String.valueOf(contact.getMaxOffers()));
            minRating.setText(String.valueOf(contact.getMinRating()));

            // demand type settings
            // Add the types to the status box.
            final ClientDemandType[] types = ClientDemandType.values();
            int i = 0;
            int j = 0;
            demandType.clear();
            for (ClientDemandType type : types) {
                demandType.addItem(type.getValue());
                if (contact.getDemandType() != null
                        && contact.getDemandType().equalsIgnoreCase(type.getValue())) {
                    j = i;
                }
                i++;
            }
            demandType.setSelectedIndex(j);

            // demand status settings
            // Add the statuses to the status box.
            final DemandStatusType[] statuses = DemandStatusType.values();
            i = 0;
            j = 0;
            demandStatus.clear();
            for (DemandStatusType status : statuses) {
                demandStatus.addItem(status.getValue());
                if (contact.getDemandStatus() != null
                        && contact.getDemandStatus().equalsIgnoreCase(status.getValue())) {
                    j = i;
                }
                i++;
            }
            demandStatus.setSelectedIndex(j);

            categoryList.clear();
            if (contact.getCategories() != null) {
                for (String cat : contact.getCategories().values()) {
                    categoryList.addItem(cat);
                }

            }
            localityList.clear();
            if (contact.getLocalities() != null) {
                for (String loc : contact.getLocalities().values()) {
                    localityList.addItem(loc);
                }
            }
        }
    }
}