/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.DemandStatusDetail;
import cz.poptavka.sample.shared.domain.DemandTypeDetail;

/**
 *
 * @author ivan.vlcek, jarko
 */
public class AdminDemandInfoView extends Composite implements
        AdminDemandInfoPresenter.AdminDemandInfoInterface {
    private static AdminDemandInfoViewUiBinder uiBinder = GWT
            .create(AdminDemandInfoViewUiBinder.class);

    interface AdminDemandInfoViewUiBinder extends
            UiBinder<Widget, AdminDemandInfoView> {
    }

    // demand detail input fields
    @UiField
    TextArea titleBox;
    @UiField
    TextArea descriptionBox;
    @UiField
    TextBox priceBox;
    @UiField
    DateBox endDateBox;
    @UiField
    DateBox expirationBox;
    @UiField
    TextBox clientID;
    @UiField
    TextBox maxOffers;
    @UiField
    TextBox minRating;
    @UiField
    ListBox demandType;
    @UiField
    ListBox demandStatus;

    // demand detail button fields
    @UiField
    Button categoryButton;
    @UiField
    Button localityButton;
    @UiField
    Button createButton;
    @UiField
    Button updateButton;

    private DemandDetail demandInfo;

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public Button getUpdateBtn() {
        return updateButton;
    }

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        initDemandInfoForm();
    }

    private void initDemandInfoForm() {
        // initWidget(uiBinder.createAndBindUi(this));
        DateTimeFormat dateFormat = DateTimeFormat
                .getFormat(PredefinedFormat.DATE_LONG);
        expirationBox.setFormat(new DateBox.DefaultFormat(dateFormat));
        endDateBox.setFormat(new DateBox.DefaultFormat(dateFormat));

        // Initialize the contact to null.
        setDemandDetail(null);

        // Handle events.
        updateButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (demandInfo == null) {
                    return;
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
                demandInfo.setPrice(priceBox.getText());
                demandInfo.setEndDate(endDateBox.getValue());
                demandInfo.setExpireDate(expirationBox.getValue());
                demandInfo.setClientId(Long.valueOf(clientID.getText()));
                demandInfo.setMaxOffers(Integer.valueOf(maxOffers.getValue()));
                demandInfo.setMinRating(Integer.valueOf(minRating.getValue()));
                // demandInfo.setDemandType(demandType.getValue());

                // Update the views.
                // TODO this must be called within Presenter
                // ContactDatabase.get().refreshDisplays();
            }
        });
        createButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                demandInfo = new DemandDetail();
                demandInfo.setTitle(titleBox.getText());
                demandInfo.setDescription(descriptionBox.getText());
                demandInfo.setClientId(Long.valueOf(clientID.getText()));
                demandInfo.setExpireDate(expirationBox.getValue());
                // enter new Demand into DB if it is good feature for Admin
                // ContactDatabase.get().addContact(demandInfo);
                setDemandDetail(demandInfo);
            }
        });
    }

    public void setDemandDetail(DemandDetail contact) {
        this.demandInfo = contact;
        updateButton.setEnabled(contact != null);
        if (contact != null) {
            titleBox.setText(contact.getTitle());
            descriptionBox.setText(contact.getDescription());
            priceBox.setText(contact.getPriceString());
            endDateBox.setValue(contact.getEndDate());
            expirationBox.setValue(contact.getExpireDate());
            clientID.setText(String.valueOf(contact.getClientId()));
            maxOffers.setText(String.valueOf(contact.getMaxOffers()));
            minRating.setText(String.valueOf(contact.getMinRating()));

            // demand type settings
            // Add the types to the status box.
            final DemandTypeDetail[] types = DemandTypeDetail.values();
            for (DemandTypeDetail type : types) {
                demandType.addItem(type.getValue());
            }

            String type = contact.getDemandStatus();
            for (int i = 0; i < types.length; i++) {
                if (type == null ? types[i].getValue() == null : type
                        .equals(types[i].getValue())) {
                    demandType.setSelectedIndex(i);
                    break;
                }
            }

            // demand status settings
            // Add the statuses to the status box.
            final DemandStatusDetail[] statuses = DemandStatusDetail.values();
            for (DemandStatusDetail status : statuses) {
                demandStatus.addItem(status.getValue());
            }

            String status = contact.getDemandStatus();
            for (int i = 0; i < statuses.length; i++) {
                if (status == null ? statuses[i].getValue() == null : status
                        .equals(statuses[i].getValue())) {
                    demandStatus.setSelectedIndex(i);
                    break;
                }
            }

            demandInfo.setLocalities(contact.getLocalities());
            demandInfo.setCategories(contact.getCategories());
        }
    }
}
