package com.eprovement.poptavka.client.homeWelcome;

import com.eprovement.poptavka.client.homeWelcome.interfaces.IHomeWelcomeView;
import com.eprovement.poptavka.client.homeWelcome.interfaces.IHomeWelcomeView.IHomeWelcomePresenter;
import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.client.root.footer.FooterView;
import com.eprovement.poptavka.client.user.widget.grid.cell.RootCategoryCell;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import java.util.ArrayList;

public class HomeWelcomeView extends ReverseCompositeView<IHomeWelcomePresenter> implements IHomeWelcomeView {

    private static HomeWelcomeViewUiBinder uiBinder = GWT.create(HomeWelcomeViewUiBinder.class);

    interface HomeWelcomeViewUiBinder extends UiBinder<Widget, HomeWelcomeView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) CellList<CategoryDetail> categoryList;
    @UiField(provided = true) Widget footer;
    @UiField FlowPanel categorySection;
    @UiField Button suppliersBtn, demandsBtn;
    @UiField Button howItWorksSupplierBtn, howItWorksDemandBtn;
    @UiField Button registerSupplierBtn, registerDemandBtn;
    /** Class attributes. **/
    private @Inject FooterView footerView;
    private ListDataProvider dataProvider = new ListDataProvider();
    private final SingleSelectionModel<CategoryDetail> selectionRootModel =
            new SingleSelectionModel<CategoryDetail>(CategoryDetail.KEY_PROVIDER);

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
        footer = footerView;
        categoryList = new CellList<CategoryDetail>(new RootCategoryCell());
        categoryList.setSelectionModel(selectionRootModel);
        dataProvider.addDataDisplay(categoryList);
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/

    @Override
    public void displayCategories(ArrayList<CategoryDetail> rootCategories) {
        dataProvider.setList(rootCategories);
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public FlowPanel getCategorySection() {
        return categorySection;
    }

    @Override
    public SingleSelectionModel<CategoryDetail> getCategorySelectionModel() {
        return selectionRootModel;
    }

    @Override
    public ListDataProvider getDataProvider() {
        return dataProvider;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    /** ANCHOR. **/
    @Override
    public Button getSuppliersBtn() {
        return suppliersBtn;
    }

    @Override
    public Button getDemandsBtn() {
        return demandsBtn;
    }

    @Override
    public Button getHowItWorksSupplierBtn() {
        return howItWorksSupplierBtn;
    }

    @Override
    public Button getHowItWorksDemandBtn() {
        return howItWorksDemandBtn;
    }

    /** BUTTONS. **/
    @Override
    public Button getRegisterSupplierBtn() {
        return registerSupplierBtn;
    }

    @Override
    public Button getRegisterDemandBtn() {
        return registerDemandBtn;
    }
}
