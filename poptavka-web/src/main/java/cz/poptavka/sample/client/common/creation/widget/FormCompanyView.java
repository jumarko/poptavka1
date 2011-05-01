package cz.poptavka.sample.client.common.creation.widget;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.AddressDetail;
import cz.poptavka.sample.shared.domain.ClientDetail;

public class FormCompanyView extends Composite implements FormCompanyPresenter.FormCompanyInterface {

    private static FormPersonViewUiBinder uiBinder = GWT.create(FormPersonViewUiBinder.class);
    interface FormPersonViewUiBinder extends UiBinder<Widget, FormCompanyView> {  }

    private ArrayList<TextBox> widgets = new ArrayList<TextBox>();

    @UiField TextBox companyNameBox;
    @UiField TextBox idNumberBox;
    @UiField TextBox taxNumberBox;
    @UiField TextBox websiteBox;
    @UiField TextBox streetBox;
    @UiField TextBox cityBox;
    @UiField TextBox zipBox;
    @UiField TextBox nameBox;
    @UiField TextBox surnameBox;
    @UiField TextBox phoneBox;
    @UiField TextBox mailBox;
    @UiField PasswordTextBox passBox, passConfirmBox;

    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        widgets.add(companyNameBox);
        widgets.add(idNumberBox);
        widgets.add(taxNumberBox);

        widgets.add(streetBox);
        widgets.add(cityBox);
        widgets.add(zipBox);
        widgets.add(nameBox);
        widgets.add(surnameBox);
        widgets.add(phoneBox);
        widgets.add(mailBox);
        widgets.add(passBox);
    }

    public boolean isValid() {
        int errorCount = 0;
        for (TextBox item : widgets) {
            ((Widget) item).removeStyleName(StyleResource.INSTANCE.cssBase().errorField());
            if (item.getText().length() == 0) {
                errorCount++;
                ((Widget) item).setStyleName(StyleResource.INSTANCE.cssBase().errorField());
            }
        }
        return errorCount == 0;
    }

    @Override
    public ClientDetail getNewClient() {
        ClientDetail client = new ClientDetail(mailBox.getText(), passBox.getText());

        client.setCompanyName(companyNameBox.getText());
        client.setIdentifiacationNumber(idNumberBox.getText());
        client.setTaxId(taxNumberBox.getText());

        client.setFirstName(nameBox.getText());
        client.setLastName(surnameBox.getText());
        client.setPhone(phoneBox.getText());
        client.setWebsite(websiteBox.getText());

        AddressDetail address = new AddressDetail();
        address.setCityName(cityBox.getText());
        address.setStreet(streetBox.getText());
        address.setZipCode(zipBox.getText());

        client.setAddress(address);

        return client;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }



}
