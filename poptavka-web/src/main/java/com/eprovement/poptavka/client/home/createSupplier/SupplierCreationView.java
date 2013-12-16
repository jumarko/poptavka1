package com.eprovement.poptavka.client.home.createSupplier;

import com.eprovement.poptavka.client.common.OverflowComposite;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.home.createDemand.widget.ButtonsPanel;
import com.eprovement.poptavka.resources.StyleResource;
import com.github.gwtbootstrap.client.ui.Tooltip;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Arrays;
import java.util.List;


public class SupplierCreationView extends OverflowComposite
        implements SupplierCreationPresenter.CreationViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static CreationViewUiBinder uiBinder = GWT.create(CreationViewUiBinder.class);

    interface CreationViewUiBinder extends UiBinder<Widget, SupplierCreationView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField SimplePanel contentHolder1, contentHolder2, contentHolder3, contentHolder4;
    @UiField SimplePanel footerPanel;
    @UiField TabLayoutPanel mainPanel;
    @UiField HorizontalPanel agreementPanel;
    @UiField CheckBox agreedCheck;
    @UiField ButtonsPanel buttonsPanel1, buttonsPanel2, buttonsPanel3, buttonsPanel4;
    @UiField Anchor conditionLink;
    /** Class attributes. **/
    private List<SimplePanel> holderPanels;
    private List<Tooltip> tooltips;
    private ClickHandler nextClickHandler = new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
            selectNextTab();
        }
    };
    private ClickHandler backClickHandler = new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
            selectPreviousTab();
        }
    };


    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        buttonsPanel1.getBackBtn().setVisible(false);
        bindHandlers();

        /** filling panels list **/
        holderPanels = Arrays.asList(contentHolder1, contentHolder2, contentHolder3, contentHolder4);
        tooltips = Arrays.asList(buttonsPanel1.getNextBtnTooltip(), buttonsPanel2.getNextBtnTooltip(),
            buttonsPanel3.getNextBtnTooltip(), buttonsPanel4.getNextBtnTooltip());

        for (SimplePanel panel : holderPanels) {
            setParentOverflow(panel, Overflow.AUTO);
        }

        /** style implementation and overflow tweaks **/
        CssInjector.INSTANCE.ensureCommonStylesInjected();
        CssInjector.INSTANCE.ensureCreateTabPanelStylesInjected();
    }

    /**************************************************************************/
    /* UiHandler                                                              */
    /**************************************************************************/
    @UiHandler("agreedCheck")
    public void agreedCheckChanged(ClickEvent event) {
        agreementPanel.setStyleName(StyleResource.INSTANCE.common().emptyStyle());
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /** PANELS. **/
    @Override
    public TabLayoutPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public SimplePanel getHolderPanel(int order) {
        return holderPanels.get(order);
    }

    /** BUTTONS. **/
    @Override
    public Button getRegisterButton() {
        return buttonsPanel4.getNextBtn();
    }

    /** OTHERS. **/
    @Override
    public Tooltip getNextBtnTooltip(int order) {
        return tooltips.get(order);
    }

    @Override
    public Anchor getConditionLink() {
        return conditionLink;
    }

    @Override
    public CheckBox getAgreedCheck() {
        return agreedCheck;
    }

    @Override
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
        contentPanel.addStyleName("container-fluid");
        panel.setWidget(contentPanel);
        panel.setWidth("580px");
        panel.setAnimationEnabled(true);
        panel.setAutoHideEnabled(true);
        panel.setGlassEnabled(true);
        panel.addStyleName(StyleResource.INSTANCE.modal().commonModalStyle());


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

    @Override
    public boolean isAgreementChecked() {
        if (agreedCheck.getValue()) {
            return agreedCheck.getValue();
        } else {
            agreementPanel.setStyleName(StyleResource.INSTANCE.common().errorField());
            return false;
        }
    }

    @Override
    public SimplePanel getFooterPanel() {
        return footerPanel;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void bindHandlers() {
        //back click handler
        buttonsPanel1.getBackBtn().addClickHandler(backClickHandler);
        buttonsPanel2.getBackBtn().addClickHandler(backClickHandler);
        buttonsPanel3.getBackBtn().addClickHandler(backClickHandler);
        buttonsPanel4.getBackBtn().addClickHandler(backClickHandler);
        //next click handler
        buttonsPanel1.getNextBtn().addClickHandler(nextClickHandler);
        buttonsPanel2.getNextBtn().addClickHandler(nextClickHandler);
        buttonsPanel3.getNextBtn().addClickHandler(nextClickHandler);
        buttonsPanel4.getNextBtn().addClickHandler(nextClickHandler);
    }

    private void selectNextTab() {
        mainPanel.selectTab(mainPanel.getSelectedIndex() + 1, true);
    }

    private void selectPreviousTab() {
        mainPanel.selectTab(mainPanel.getSelectedIndex() - 1, true);
    }
}