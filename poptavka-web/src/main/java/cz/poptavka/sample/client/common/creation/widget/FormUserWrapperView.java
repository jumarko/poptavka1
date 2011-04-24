package cz.poptavka.sample.client.common.creation.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class FormUserWrapperView extends Composite implements FormUserWrapperPresenter.FormWrapperInterface {

    private static FormUserWrapperUiBinder uiBinder = GWT.create(FormUserWrapperUiBinder.class);

    interface FormUserWrapperUiBinder extends UiBinder<Widget, FormUserWrapperView> {
    }

    @UiField RadioButton personRadio;
    @UiField RadioButton companyRadio;
    @UiField Button toLoginBtn;
    @UiField SimplePanel formHolder;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public RadioButton getPersonButton() {
        return personRadio;
    }

    @Override
    public RadioButton getCompanyButton() {
        return companyRadio;
    }

    @Override
    public Button getToLoginButton() {
        return toLoginBtn;
    }

    @Override
    public SimplePanel getFormHolder() {
        return formHolder;
    }



}
