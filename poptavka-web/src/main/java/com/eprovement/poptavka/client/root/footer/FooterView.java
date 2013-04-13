package com.eprovement.poptavka.client.root.footer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.root.interfaces.IFooterView;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.inject.Singleton;

@Singleton
public class FooterView extends Composite implements IFooterView {

    private static FooterViewUiBinder uiBinder = GWT
            .create(FooterViewUiBinder.class);

    interface FooterViewUiBinder extends UiBinder<Widget, FooterView> {
    }

    @UiField Button contactUs;

    public FooterView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public FooterView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * @return the contactUs button
     */
    public Button getContactUs() {
        return contactUs;
    }
}
