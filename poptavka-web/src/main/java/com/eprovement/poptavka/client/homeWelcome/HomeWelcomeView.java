package com.eprovement.poptavka.client.homeWelcome;

import com.eprovement.poptavka.client.homeWelcome.interfaces.IHomeWelcomeView;
import com.eprovement.poptavka.client.homeWelcome.interfaces.IHomeWelcomeView.IHomeWelcomePresenter;
import com.eprovement.poptavka.client.common.ReverseCompositeView;
import com.eprovement.poptavka.client.user.widget.grid.cell.RootCategoryCell;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
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
    @UiField(provided = true) CellList<ICatLocDetail> categoryList;
    @UiField SimplePanel footerContainer;
    @UiField Button suppliersBtn, demandsBtn;
    @UiField Button howItWorksSupplierBtn, howItWorksDemandBtn;
    @UiField Button registerSupplierBtn, registerDemandBtn;
    /** Class attributes. **/
    private ListDataProvider dataProvider = new ListDataProvider();
    private final SingleSelectionModel<ICatLocDetail> selectionRootModel =
            new SingleSelectionModel<ICatLocDetail>(CatLocDetail.KEY_PROVIDER);

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
        categoryList = new CellList<ICatLocDetail>(new RootCategoryCell());
        categoryList.setSelectionModel(selectionRootModel);
        dataProvider.addDataDisplay(categoryList);
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void displayCategories(ArrayList<ICatLocDetail> rootCategories) {
        dataProvider.setList(rootCategories);
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public SingleSelectionModel<ICatLocDetail> getCategorySelectionModel() {
        return selectionRootModel;
    }

    @Override
    public ListDataProvider getDataProvider() {
        return dataProvider;
    }

    @Override
    public SimplePanel getFooterContainer() {
        return footerContainer;
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