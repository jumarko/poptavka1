package com.eprovement.poptavka.client.common.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.eprovement.poptavka.client.common.category.CategorySelectorView;
import com.eprovement.poptavka.client.common.locality.LocalitySelectorView;
import com.eprovement.poptavka.client.common.search.SearchModulePresenter.SearchModulesViewInterface;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

public class AdvanceSearchContentView extends Composite
        implements AdvanceSearchContentPresenter.AdvanceSearchContentInterface {

    private static AdvanceSearchContentViewUiBinder uiBinder = GWT.create(AdvanceSearchContentViewUiBinder.class);

    interface AdvanceSearchContentViewUiBinder extends UiBinder<Widget, AdvanceSearchContentView> {
    }
    @UiField
    TabLayoutPanel mainPanel;
    @UiField
    SimplePanel attributeSelectorWidgetPanel, categorySelectorWidgetPanel, localitySelectorWidgetPanel;

    public AdvanceSearchContentView() {
        initWidget(uiBinder.createAndBindUi(this));
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
            search.setCategories(((CategorySelectorView) categorySelectorWidgetPanel.getWidget())
                    .getCellListDataProvider().getList());
        }
        if (localitySelectorWidgetPanel != null) {
            search.setLocalities(((LocalitySelectorView) localitySelectorWidgetPanel.getWidget())
                    .getCellListDataProvider().getList());
        }
        return search;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
