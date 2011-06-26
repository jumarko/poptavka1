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

import cz.poptavka.sample.shared.domain.offer.FullOfferDetail;

public class AdminOfferInfoView extends Composite implements
        AdminOfferInfoPresenter.AdminOfferInfoInterface {

    private static AdminOfferInfoViewUiBinder uiBinder = GWT
            .create(AdminOfferInfoViewUiBinder.class);

    interface AdminOfferInfoViewUiBinder extends
            UiBinder<Widget, AdminOfferInfoView> {
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

    private FullOfferDetail offerInfo;

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
        initOfferInfoForm();
    }

    private void initOfferInfoForm() {
        // initWidget(uiBinder.createAndBindUi(this));
        DateTimeFormat dateFormat = DateTimeFormat
                .getFormat(PredefinedFormat.DATE_LONG);
        expirationBox.setFormat(new DateBox.DefaultFormat(dateFormat));
        endDateBox.setFormat(new DateBox.DefaultFormat(dateFormat));

        // Initialize the contact to null.
        setOfferDetail(null);

        // Handle events.
        updateButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (offerInfo == null) {
                    return;
                }
                boolean t = priceBox.getText() == null;
                if (t) {
                    GWT.log("d" + t + "max offer");
                }
                GWT.log("d" + t + "max offer");
                GWT.log("d" + priceBox.getText().equals("") + "price ");

                // Update the contact.
                // demandInfo.setDemandType(demandType.getValue());

                // Update the views.
                // TODO this must be called within Presenter
                // ContactDatabase.get().refreshDisplays();
            }
        });
        createButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                offerInfo = new FullOfferDetail();
                // enter new Demand into DB if it is good feature for Admin
                // ContactDatabase.get().addContact(demandInfo);
                setOfferDetail(offerInfo);
            }
        });
    }

    public void setOfferDetail(FullOfferDetail contact) {
        this.offerInfo = contact;
        updateButton.setEnabled(contact != null);
        if (contact != null) {


        }
    }

}
