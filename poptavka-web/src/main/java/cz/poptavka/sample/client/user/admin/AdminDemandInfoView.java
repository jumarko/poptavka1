/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin;

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

/**
 *
 * @author ivan.vlcek
 */
public class AdminDemandInfoView extends Composite implements
        AdminDemandInfoPresenter.AdminDemandInfoInterface {
    private static AdminDemandInfoViewUiBinder uiBinder =
            GWT.create(AdminDemandInfoViewUiBinder.class);

    interface AdminDemandInfoViewUiBinder extends UiBinder<Widget, AdminDemandInfoView> {
    }
    @UiField
    TextArea addressBox;
    @UiField
    DateBox birthdayBox;
    @UiField
    ListBox categoryBox;
    @UiField
    Button createButton;
    @UiField
    TextBox firstNameBox;
    @UiField
    TextBox lastNameBox;
    @UiField
    Button updateButton;
    private DemandDetail contactInfo;

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        initDemandInfoForm();
    }

    private void initDemandInfoForm() {
//        initWidget(uiBinder.createAndBindUi(this));
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(
                PredefinedFormat.DATE_LONG);
        birthdayBox.setFormat(new DateBox.DefaultFormat(dateFormat));

        // Add the categories to the category box.
        final DemandStatusDetail[] categories = DemandStatusDetail.values();
        for (DemandStatusDetail category : categories) {
            categoryBox.addItem(category.getValue());
        }

        // Initialize the contact to null.
        setContact(null);

        // Handle events.
        updateButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (contactInfo == null) {
                    return;
                }

                // Update the contact.
                contactInfo.setTitle(firstNameBox.getText());
                contactInfo.setDescription(lastNameBox.getText());
                contactInfo.setClientId(Long.valueOf(addressBox.getText()));
                contactInfo.setExpireDate(birthdayBox.getValue());
                int categoryIndex = categoryBox.getSelectedIndex();
                contactInfo.setDemandStatus(categories[categoryIndex].getValue());

                // Update the views.
                // TODO this must be called within Presenter
//                ContactDatabase.get().refreshDisplays();
            }
        });
        createButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int categoryIndex = categoryBox.getSelectedIndex();
                DemandStatusDetail category = categories[categoryIndex];
                contactInfo = new DemandDetail();
                contactInfo.setDemandStatus(category.getValue());
                contactInfo.setTitle(firstNameBox.getText());
                contactInfo.setDescription(lastNameBox.getText());
                contactInfo.setClientId(Long.valueOf(addressBox.getText()));
                contactInfo.setExpireDate(birthdayBox.getValue());
                // enter new Demand into DB if it is good feature for Admin
//                ContactDatabase.get().addContact(contactInfo);
                setContact(contactInfo);
            }
        });
    }

    public void setContact(DemandDetail contact) {
        this.contactInfo = contact;
        updateButton.setEnabled(contact != null);
        if (contact != null) {
            firstNameBox.setText(contact.getTitle());
            lastNameBox.setText(contact.getDescription());
            addressBox.setText(String.valueOf(contact.getClientId()));
            birthdayBox.setValue(contact.getExpireDate());
            String category = contact.getDemandStatus();
            DemandStatusDetail[] categories = DemandStatusDetail.values();
            for (int i = 0; i < categories.length; i++) {
                if (category == null ? categories[i].getValue() == null : category.equals(categories[i].getValue())) {
                    categoryBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
}
