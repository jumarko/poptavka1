package com.eprovement.poptavka.client.root.footer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.client.root.interfaces.IFooterView;
import com.eprovement.poptavka.client.root.interfaces.IFooterView.IFooterPresenter;

public class FooterView extends ReverseCompositeView<IFooterPresenter>
        implements IFooterView {

    private static FooterViewUiBinder uiBinder = GWT
            .create(FooterViewUiBinder.class);

    interface FooterViewUiBinder extends UiBinder<Widget, FooterView> {
    }

    public FooterView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiField
    Button button;

    public FooterView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
        button.setTitle(firstName);
    }

    @UiHandler("button")
    void onClick(ClickEvent e) {
        Window.alert("Hello!");
    }

    public void setText(String text) {
        button.setText(text);
    }

    public String getText() {
        return button.getText();
    }

}
