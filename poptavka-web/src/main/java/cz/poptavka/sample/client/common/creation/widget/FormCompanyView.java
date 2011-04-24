package cz.poptavka.sample.client.common.creation.widget;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class FormCompanyView extends Composite implements FormCompanyPresenter.FormCompanyInterface {

    private static FormPersonViewUiBinder uiBinder = GWT.create(FormPersonViewUiBinder.class);
    interface FormPersonViewUiBinder extends UiBinder<Widget, FormCompanyView> {  }

    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        widgets.add(companyNameBox);
        widgets.add(icBox);
        widgets.add(dicBox);

        widgets.add(streetBox);
        widgets.add(cityBox);
        widgets.add(zipBox);
        widgets.add(nameBox);
        widgets.add(surnameBox);
        widgets.add(phoneBox);
        widgets.add(mailBox);
    }

    private ArrayList<TextBox> widgets = new ArrayList<TextBox>();

    @UiField TextBox companyNameBox;
    @UiField TextBox icBox;
    @UiField TextBox dicBox;
    @UiField TextBox websiteBox;
    @UiField TextBox streetBox;
    @UiField TextBox cityBox;
    @UiField TextBox zipBox;
    @UiField TextBox nameBox;
    @UiField TextBox surnameBox;
    @UiField TextBox phoneBox;
    @UiField TextBox mailBox;
    @UiField PasswordTextBox passBox, passConfirmBox;

    public boolean isValid() {
        int errorCount = 0;
        for (TextBox item : widgets) {
            if (item.getText().length() == 0) {
                //TODO mark error grafically

                errorCount++;
            }
        }
        return errorCount == 0;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

}
