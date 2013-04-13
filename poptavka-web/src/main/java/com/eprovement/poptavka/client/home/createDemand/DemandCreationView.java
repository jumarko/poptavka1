package com.eprovement.poptavka.client.home.createDemand;

import com.eprovement.poptavka.client.common.OverflowComposite;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.footer.FooterView;
import com.eprovement.poptavka.resources.StyleResource;
import com.github.gwtbootstrap.client.ui.FluidContainer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import java.util.ArrayList;
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
    @UiField(provided = true) Widget footer;
    @UiField SimplePanel contentHolder1, contentHolder2, contentHolder3, contentHolder4, contentHolder5;
    @UiField FluidContainer panel1, panel2;
    @UiField TabLayoutPanel mainPanel;
    @UiField Button loginBtn, registerBtn;
    @UiField Button nextButtonTab1, nextButtonTab2, nextButtonTab3, nextButtonTab4, nextButtonTab5;
    @UiField Button backButtonTab1, backButtonTab2, backButtonTab3, backButtonTab4, backButtonTab5;
    @UiField HTML headerLabelTab1, headerLabelTab2, headerLabelTab3, headerLabelTab4, headerLabelTab5;
    /** Class attributes. **/
    private @Inject FooterView footerView;
    private List<SimplePanel> holderPanels = new ArrayList<SimplePanel>();

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        footer = footerView;
        initWidget(uiBinder.createAndBindUi(this));
        recalculateTabNumbers();

        /** filling panels list **/
        holderPanels = Arrays.asList(contentHolder1, contentHolder2, contentHolder3, contentHolder4, contentHolder5);

        /** style implementation and overflow tweaks **/
        StyleResource.INSTANCE.common().ensureInjected();
        StyleResource.INSTANCE.createTabPanel().ensureInjected();
        for (SimplePanel panel : holderPanels) {
            setParentOverflow(panel, Overflow.AUTO);
        }
        contentHolder3.setSize("100%", "100%");
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    /** NEXT. **/
    @UiHandler(value = {"nextButtonTab1", "nextButtonTab2", "nextButtonTab3", "nextButtonTab4" })
    public void nextButtonsClickHandler(ClickEvent event) {
        selectNextTab();
    }

    /** BACK. **/
    @UiHandler(value = {"backButtonTab1", "backButtonTab2", "backButtonTab3", "backButtonTab4" })
    public void backButtonsClickHandler(ClickEvent event) {
        selectPreviousTab();
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
        backButtonTab2.setVisible(visible);
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
        return loginBtn;
    }

    @Override
    public Button getRegisterBtn() {
        return registerBtn;
    }

    /**
     * Get next button 1 - represents register client button.
     * @return button
     */
    @Override
    public Button getNextButtonTab1() {
        return nextButtonTab1;
    }

    /**
     * Get next button 5 - represents creating demand button.
     * @return button
     */
    @Override
    public Button getNextButtonTab5() {
        return nextButtonTab5;
    }

    /**
     * Get back button 1 - represents restoring first tab.
     * @return button
     */
    @Override
    public Button getBackButtonTab1() {
        return backButtonTab1;
    }

    @Override
    public Widget getWidgetView() {
        return this;
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
    private void selectNextTab() {
        mainPanel.selectTab(mainPanel.getSelectedIndex() + 1, true);
    }

    private void selectPreviousTab() {
        mainPanel.selectTab(mainPanel.getSelectedIndex() - 1, true);
    }
}
