package com.eprovement.poptavka.client.home.createDemand;

import com.eprovement.poptavka.client.common.OverflowComposite;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.home.createDemand.widget.ButtonsPanel;
import com.github.gwtbootstrap.client.ui.Tooltip;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Arrays;
import java.util.List;

public class DemandCreationView extends OverflowComposite implements DemandCreationPresenter.CreationViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static CreationViewUiBinder uiBinder = GWT.create(CreationViewUiBinder.class);

    interface CreationViewUiBinder extends UiBinder<Widget, DemandCreationView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField SimplePanel contentHolder1, contentHolder2, contentHolder3, contentHolder4, contentHolder5;
    @UiField SimplePanel footerPanel;
    @UiField FlowPanel panel1, panel2;
    @UiField TabLayoutPanel mainPanel;
    @UiField ButtonsPanel buttonsPanel11, buttonsPanel12, buttonsPanel2, buttonsPanel3, buttonsPanel4, buttonsPanel5;
    @UiField HTML headerLabelTab1, headerLabelTab2, headerLabelTab3, headerLabelTab4, headerLabelTab5;
    /** Class attributes. **/
    private List<SimplePanel> holderPanels;
    private List<Tooltip> tooltips;;
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
        recalculateTabNumbers();
        bindHandlers();

        /** filling list **/
        holderPanels = Arrays.asList(contentHolder1, contentHolder2, contentHolder3, contentHolder4, contentHolder5);
        tooltips = Arrays.asList(buttonsPanel12.getNextBtnTooltip(), buttonsPanel2.getNextBtnTooltip(),
            buttonsPanel3.getNextBtnTooltip(), buttonsPanel4.getNextBtnTooltip(), buttonsPanel5.getNextBtnTooltip());

        for (SimplePanel panel : holderPanels) {
            setParentOverflow(panel, Overflow.AUTO);
        }

        /** style implementation and overflow tweaks **/
        CssInjector.INSTANCE.ensureCommonStylesInjected();
        CssInjector.INSTANCE.ensureCreateTabPanelStylesInjected();
    }

    /**************************************************************************/
    /* Other methods                                                          */
    /**************************************************************************/
    /**
     * Set first tab visibility.
     * After setting visibility, recalculating tab numbers is processed.
     * @param visible - true for visible, false elsewhere.
     */
    @Override
    public void setFirstTabVisibility(boolean visible) {
        mainPanel.getTabWidget(0).getParent().setVisible(visible);
        mainPanel.getTabWidget(0).setVisible(visible);
        recalculateTabNumbers();
    }

    @Override
    public void setLoginLayout() {
        panel2.setVisible(false);
        panel1.setVisible(true);
    }

    @Override
    public void setRegisterLayout() {
        panel1.setVisible(false);
        panel2.setVisible(true);
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

    /** HEADERS. **/
    @Override
    public HTML getHeaderLabelTab1() {
        return headerLabelTab1;
    }

    /** BUTTONS. **/
    @Override
    public Button getLoginBtn() {
        return buttonsPanel11.getBackBtn();
    }

    @Override
    public Button getRegisterBtn() {
        return buttonsPanel11.getNextBtn();
    }

    /**
     * Get next button 1 - represents register client button.
     * @return button
     */
    @Override
    public Button getNextButtonTab1() {
        return buttonsPanel12.getNextBtn();
    }

    /**
     * Get next button 5 - represents creating demand button.
     * @return button
     */
    @Override
    public Button getNextButtonTab5() {
        return buttonsPanel5.getNextBtn();
    }

    /**
     * Get back button 1 - represents restoring first tab.
     * @return button
     */
    @Override
    public Button getBackButtonTab1() {
        return buttonsPanel12.getBackBtn();
    }

    @Override
    public SimplePanel getFooterPanel() {
        return footerPanel;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public Tooltip getNextBtnTooltip(int order) {
        return tooltips.get(order);
    }

    /**************************************************************************/
    /* Other methods                                                          */
    /**************************************************************************/
    /**
     * Calculate visible tab numbers.
     */
    public void recalculateTabNumbers() {
        int count = 1;
        if (mainPanel.getTabWidget(0).getParent().isVisible()) {
            headerLabelTab1.setHTML(count++ + ". " + Storage.MSGS.demandCreationTab1());
        }
        headerLabelTab2.setHTML(count++ + ". " + Storage.MSGS.demandCreationTab2());
        headerLabelTab3.setHTML(count++ + ". " + Storage.MSGS.demandCreationTab3());
        headerLabelTab4.setHTML(count++ + ". " + Storage.MSGS.demandCreationTab4());
        headerLabelTab5.setHTML(count++ + ". " + Storage.MSGS.demandCreationTab5());
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void bindHandlers() {
        //back click handler
        buttonsPanel12.getBackBtn().addClickHandler(backClickHandler);
        buttonsPanel2.getBackBtn().addClickHandler(backClickHandler);
        buttonsPanel3.getBackBtn().addClickHandler(backClickHandler);
        buttonsPanel4.getBackBtn().addClickHandler(backClickHandler);
        buttonsPanel5.getBackBtn().addClickHandler(backClickHandler);
        //next click handler
        buttonsPanel12.getNextBtn().addClickHandler(nextClickHandler);
        buttonsPanel2.getNextBtn().addClickHandler(nextClickHandler);
        buttonsPanel3.getNextBtn().addClickHandler(nextClickHandler);
        buttonsPanel4.getNextBtn().addClickHandler(nextClickHandler);
        buttonsPanel5.getNextBtn().addClickHandler(nextClickHandler);
    }

    private void selectNextTab() {
        mainPanel.selectTab(mainPanel.getSelectedIndex() + 1, true);
    }

    private void selectPreviousTab() {
        mainPanel.selectTab(mainPanel.getSelectedIndex() - 1, true);
    }
}
