package com.eprovement.poptavka.client.common.search;

import com.eprovement.poptavka.client.common.category.CategorySelectorView;
import com.eprovement.poptavka.client.common.locality.LocalitySelectorView;
import com.eprovement.poptavka.client.common.search.SearchModulePresenter.SearchModulesViewInterface;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.homedemands.HomeDemandsSearchView;
import com.eprovement.poptavka.client.homesuppliers.HomeSuppliersSearchView;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class AdvanceSearchContentView extends Composite
        implements SearchModulePresenter.AdvanceSearchContentInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static AdvanceSearchContentViewUiBinder uiBinder = GWT.create(AdvanceSearchContentViewUiBinder.class);

    interface AdvanceSearchContentViewUiBinder extends UiBinder<Widget, AdvanceSearchContentView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField
    Label currentViewAttributesLabel;
    @UiField
    TabLayoutPanel mainPanel;
    @UiField
    SimplePanel attributeSelectorWidgetPanel, categorySelectorWidgetPanel, localitySelectorWidgetPanel;
    @UiField
    HomeDemandsSearchView demandsAttributeSelectorWidget;
    @UiField
    HomeSuppliersSearchView suppliersAttributeSelectorWidget;
    /**************************************************************************/
    /* Tab constants                                                          */
    /**************************************************************************/
    public static final int DEMANDS_SELECTOR_WIDGET = 0;
    public static final int SUPPLIER_SELECTOR_WIDGET = 1;
    public static final int CURRENT_SELECTOR_WIDGET = 2;
    public static final int CATEGORY_SELECTOR_WIDGET = 3;
    public static final int LOCALITY_SELECTOR_WIDGET = 4;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public AdvanceSearchContentView() {
        initWidget(uiBinder.createAndBindUi(this));
        mainPanel.setSize("1030px", "500px");
        //searchWhat is default set to display demands attribute selector,
        //therefore hide suppliers attribute selector
        mainPanel.getTabWidget(SUPPLIER_SELECTOR_WIDGET).getParent().setVisible(false);
        StyleResource.INSTANCE.advancedSearchTabPanel().ensureInjected();
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    @Override
    public void setCurrentViewTabName() {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.CLIENT_DEMANDS:
                currentViewAttributesLabel.setText(Storage.MSGS.searchClientDemandsTab());
                break;
            case Constants.CLIENT_DEMAND_DISCUSSIONS:
                currentViewAttributesLabel.setText(Storage.MSGS.searchClientDemandsDiscussionsTab());
                break;
            case Constants.CLIENT_OFFERED_DEMANDS:
                currentViewAttributesLabel.setText(Storage.MSGS.searchClientOfferedDemandsTab());
                break;
            case Constants.CLIENT_OFFERED_DEMAND_OFFERS:
                currentViewAttributesLabel.setText(Storage.MSGS.searchClientOfferedDemandOffersTab());
                break;
            case Constants.CLIENT_ASSIGNED_DEMANDS:
                currentViewAttributesLabel.setText(Storage.MSGS.searchClientAssignedDemandsTab());
                break;
            case Constants.SUPPLIER_POTENTIAL_DEMANDS:
                currentViewAttributesLabel.setText(Storage.MSGS.searchSuppliersPotentialDemandsTab());
                break;
            case Constants.SUPPLIER_OFFERS:
                currentViewAttributesLabel.setText(Storage.MSGS.searchSuppliersOffersTab());
                break;
            case Constants.SUPPLIER_ASSIGNED_DEMANDS:
                currentViewAttributesLabel.setText(Storage.MSGS.searchSuppliersAssignedDemandsTab());
                break;
            default:
                currentViewAttributesLabel.setText(Storage.MSGS.searchCurrentViewTab());
                break;
        }
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public HomeDemandsSearchView getDemandsAttributeSelectorWidget() {
        return demandsAttributeSelectorWidget;
    }

    @Override
    public HomeSuppliersSearchView getSuppliersAttributeSelectorWidget() {
        return suppliersAttributeSelectorWidget;
    }

    @Override
    public SimplePanel getAttributeSelectorPanel() {
        return attributeSelectorWidgetPanel;
    }

    @Override
    public SimplePanel getCategorySelectorPanel() {
        return categorySelectorWidgetPanel;
    }

    @Override
    public SimplePanel getLocalitySelectorPanel() {
        return localitySelectorWidgetPanel;
    }

    @Override
    public TabLayoutPanel getTabLayoutPanel() {
        return mainPanel;
    }

    @Override
    public SearchModuleDataHolder getSearchModuleDataHolder() {
        SearchModuleDataHolder search = new SearchModuleDataHolder();
        if (attributeSelectorWidgetPanel.getWidget() != null) {
            search.setAttributes(((SearchModulesViewInterface) attributeSelectorWidgetPanel.getWidget())
                    .getFilter());
        }
        if (categorySelectorWidgetPanel != null) {
            CategorySelectorView categorySelector = (CategorySelectorView) categorySelectorWidgetPanel.getWidget();
            if (categorySelector != null) {
                search.setCategories(categorySelector.getCellListDataProvider().getList());
            }
        }
        if (localitySelectorWidgetPanel != null) {
            LocalitySelectorView localitySelector = (LocalitySelectorView) localitySelectorWidgetPanel.getWidget();
            if (localitySelector != null) {
                search.setLocalities(localitySelector.getCellListDataProvider().getList());
            }
        }
        if (search.getAttributes().isEmpty()
                && search.getCategories().isEmpty()
                && search.getLocalities().isEmpty()) {
            return null;
        }
        return search;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
