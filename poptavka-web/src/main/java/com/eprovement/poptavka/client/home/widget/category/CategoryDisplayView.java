package com.eprovement.poptavka.client.home.widget.category;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class CategoryDisplayView extends Composite implements CategoryDisplayPresenter.CategoryDisplayInterface {

    private static CategorySelectorUiBinder uiBinder = GWT.create(CategorySelectorUiBinder.class);

    interface CategorySelectorUiBinder extends UiBinder<Widget, CategoryDisplayView> {
    }

    @UiField
    HTML categoryView;


    public CategoryDisplayView() {
        initWidget(uiBinder.createAndBindUi(this));
    }



    @Override
    public Widget getWidgetView() {
        return this;
    }


    @Override
    public HTML getCategoryView() {
        // TODO Auto-generated method stub
        return categoryView;
    }


}
