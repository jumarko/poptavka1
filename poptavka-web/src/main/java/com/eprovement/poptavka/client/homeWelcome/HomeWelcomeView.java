package com.eprovement.poptavka.client.homeWelcome;

import com.eprovement.poptavka.client.homeWelcome.interfaces.IHomeWelcomeView;
import com.eprovement.poptavka.client.homeWelcome.interfaces.IHomeWelcomeView.IHomeWelcomePresenter;
import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.client.user.widget.grid.cell.RootCategoryCell;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.ArrayList;

/**************************************************************************/
/* Class: Root Category Cell                                                     */
public class HomeWelcomeView extends ReverseCompositeView<IHomeWelcomePresenter> implements IHomeWelcomeView {

    private static HomeWelcomeViewUiBinder uiBinder = GWT.create(HomeWelcomeViewUiBinder.class);

    interface HomeWelcomeViewUiBinder extends UiBinder<Widget, HomeWelcomeView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField(provided = true) CellList<CategoryDetail> categoryList;
    @UiField FlowPanel categorySection;
    @UiField Button suppliersBtn, demandsBtn;
    @UiField Button howItWorksSupplierBtn, howItWorksDemandBtn;
    @UiField Button registerSupplierBtn, registerDemandBtn;
    //TODO 25.2.13 Martin - initialize manually because UiBinder is commented
    Button demandCreateBtn = new Button();
    Button securedButton = new Button();
    Button sendUsEmailButton = new Button();
    //
    private final SingleSelectionModel<CategoryDetail> selectionRootModel =
            new SingleSelectionModel<CategoryDetail>(CategoryDetail.KEY_PROVIDER);

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public HomeWelcomeView() {
        categoryList = new CellList<CategoryDetail>(new RootCategoryCell());
        categoryList.setSelectionModel(selectionRootModel);
        initWidget(uiBinder.createAndBindUi(this));
    }

    public HomeWelcomeView(String firstName) {
        categoryList = new CellList<CategoryDetail>(new RootCategoryCell());
        categoryList.setSelectionModel(selectionRootModel);
        initWidget(uiBinder.createAndBindUi(this));
    }

    /* SETTERS                                                                */
    /**************************************************************************/

    @Override
    public void displayCategories(ArrayList<CategoryDetail> rootCategories) {
        categoryList.setRowData(rootCategories);
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

    @Override
    public HasClickHandlers getCreateDemandButton() {
        return demandCreateBtn;
    }

    @Override
    public HasClickHandlers getSecuredButton() {
        return securedButton;
    }

    @Override
    public HasClickHandlers getSendUsEmailButton() {
        return sendUsEmailButton;
    }
}
