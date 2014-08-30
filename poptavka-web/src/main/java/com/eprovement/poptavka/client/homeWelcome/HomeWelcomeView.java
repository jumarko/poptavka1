/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.homeWelcome;

import com.eprovement.poptavka.client.homeWelcome.interfaces.IHomeWelcomeView;
import com.eprovement.poptavka.client.homeWelcome.interfaces.IHomeWelcomeView.IHomeWelcomePresenter;
import com.eprovement.poptavka.client.common.ReverseCompositeView;
import com.eprovement.poptavka.client.user.widget.grid.cell.RootCategoryCell;
import com.eprovement.poptavka.shared.selectors.catLocSelector.LesserCatLocDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ILesserCatLocDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.ArrayList;

/**
 * Home Welcome view.
 *
 * @author Martin Slavkovsky
 */
public class HomeWelcomeView extends ReverseCompositeView<IHomeWelcomePresenter> implements IHomeWelcomeView {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static HomeWelcomeViewUiBinder uiBinder = GWT.create(HomeWelcomeViewUiBinder.class);

    interface HomeWelcomeViewUiBinder extends UiBinder<Widget, HomeWelcomeView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) CellList<ILesserCatLocDetail> categoryList;
    @UiField SimplePanel footerContainer;
    @UiField Button suppliersBtn, demandsBtn;
    @UiField Button howItWorksSupplierBtn, howItWorksDemandBtn;
    @UiField Button registerSupplierBtn, registerDemandBtn;
    /** Class attributes. **/
    private ListDataProvider dataProvider = new ListDataProvider();
    private final SingleSelectionModel<ILesserCatLocDetail> selectionRootModel =
            new SingleSelectionModel<ILesserCatLocDetail>(LesserCatLocDetail.KEY_PROVIDER);

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Comment: If want to use GIN:
     * <ul>
     * <li><br>lazy views</br> - can use <code>@Inject FooterView footerView;<code/></li>
     * <li><br>classic view</br> - need to @Inject FooterView as constructor's attribute:
     *      <code>
     *          @Inject
     *          public HomeWelcomeView(FooterView footerView) {
     *      </code>
     * </li>
     * </ul>
     */
    @Override
    public void createView() {
        categoryList = new CellList<ILesserCatLocDetail>(new RootCategoryCell());
        categoryList.setSelectionModel(selectionRootModel);
        dataProvider.addDataDisplay(categoryList);
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    /**
     * Displays retrieved categories.
     * @param rootCategories list of categories
     */
    @Override
    public void displayCategories(ArrayList<ILesserCatLocDetail> rootCategories) {
        dataProvider.setList(rootCategories);
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /**
     * @return the category selection model
     */
    @Override
    public SingleSelectionModel<ILesserCatLocDetail> getCategorySelectionModel() {
        return selectionRootModel;
    }

    /**
     * @return the category data provider
     */
    @Override
    public ListDataProvider getDataProvider() {
        return dataProvider;
    }

    /**
     * @return the footer container
     */
    @Override
    public SimplePanel getFooterContainer() {
        return footerContainer;
    }

    /**
     * @return the widget view
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }

    /** ANCHOR. **/
    /**
     * @return the supplier button
     */
    @Override
    public Button getSuppliersBtn() {
        return suppliersBtn;
    }

    /**
     * @return the demands button
     */
    @Override
    public Button getDemandsBtn() {
        return demandsBtn;
    }

    /**
     * @return the hotItWorksSupplier button
     */
    @Override
    public Button getHowItWorksSupplierBtn() {
        return howItWorksSupplierBtn;
    }

    /**
     * @return the howItWorksDemand button
     */
    @Override
    public Button getHowItWorksDemandBtn() {
        return howItWorksDemandBtn;
    }

    /** BUTTONS. **/
    /**
     * @return the register supplier button
     */
    @Override
    public Button getRegisterSupplierBtn() {
        return registerSupplierBtn;
    }

    /**
     * @return the register demand button
     */
    @Override
    public Button getRegisterDemandBtn() {
        return registerDemandBtn;
    }
}