package com.eprovement.poptavka.client.root.footer;

import com.eprovement.poptavka.client.common.session.Storage;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.root.interfaces.IFooterView;
import com.eprovement.poptavka.resources.StyleResource;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Singleton;

@Singleton
public class FooterView extends Composite implements IFooterView {

    private static FooterViewUiBinder uiBinder = GWT
            .create(FooterViewUiBinder.class);

    interface FooterViewUiBinder extends UiBinder<Widget, FooterView> {
    }

    @UiField Button contactUs, aboutUs, faq, privacyPolicy, termsAndConditions;

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

    /**
     * @return the aboutUs button
     */
    public Button getAboutUs() {
        return aboutUs;
    }

    /**
     * @return the faq button
     */
    public Button getFAQ() {
        return faq;
    }

    /**
     * @return the privacy policy button
     */
    public Button getPrivacyPolicy() {
        return privacyPolicy;
    }

    /**
     * Show Terms and Conditions popup.
     * @param e
     */
    @UiHandler("termsAndConditions")
    public void termsAndConditionsHandler(ClickEvent e) {
        this.showConditions();
    }


    public void showConditions() {
        final PopupPanel panel = new PopupPanel(true, false);
        HTMLPanel contentPanel =
                new HTMLPanel("<div id='text' style='overflow: auto; height: 500px;'>"
                + "</div><hr /><div style='text-align: center' id='button'></div>");
        HTML content = new HTML(StyleResource.INSTANCE.conditions().getText());
        Button closeButton = new Button(Storage.MSGS.commonBtnClose());
        closeButton.addStyleName(StyleResource.INSTANCE.common().buttonGrey());
        contentPanel.add(content, "text");
        contentPanel.add(closeButton, "button");
        panel.setWidget(contentPanel);
        panel.setWidth("580px");
        panel.setAnimationEnabled(true);
        panel.setAutoHideEnabled(true);
        panel.setGlassEnabled(true);

        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                panel.hide();
            }
        });

        int x = (Window.getClientWidth() / 2) - 290;
        int y = (Window.getClientHeight() / 2) - 250;
        panel.setPopupPosition(x, y);

        panel.show();
    }

}
