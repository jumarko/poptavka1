package cz.poptavka.sample.client.common.creation.widget;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class FormPersonView extends Composite implements FormPersonPresenter.FormPersonInterface {

    private static FormPersonViewUiBinder uiBinder = GWT.create(FormPersonViewUiBinder.class);
    interface FormPersonViewUiBinder extends UiBinder<Widget, FormPersonView> {  }

    private ArrayList<TextBox> widgets = new ArrayList<TextBox>();
    @UiField TextBox websiteBox;
    @UiField TextBox streetBox;
    @UiField TextBox cityBox;
    @UiField TextBox zipBox;
    @UiField TextBox nameBox;
    @UiField TextBox surnameBox;
    @UiField TextBox phoneBox;
    //login fields
    @UiField TextBox mailBox;
    @UiField PasswordTextBox passBox;
    @UiField PasswordTextBox passConfirmBox;

    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        widgets.add(nameBox);
        widgets.add(surnameBox);
        widgets.add(phoneBox);
        widgets.add(mailBox);
        widgets.add(passBox);
    }

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
