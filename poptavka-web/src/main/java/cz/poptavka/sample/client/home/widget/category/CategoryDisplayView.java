package cz.poptavka.sample.client.home.widget.category;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.home.widget.category.item.CategoryItemView;

public class CategoryDisplayView extends Composite implements CategoryDisplayPresenter.CategorySelectorInterface {

    private static CategorySelectorUiBinder uiBinder = GWT.create(CategorySelectorUiBinder.class);

    interface CategorySelectorUiBinder extends UiBinder<Widget, CategoryDisplayView> {
    }

    @UiField
    CategoryItemView categoryItem;


    public CategoryDisplayView() {
        initWidget(uiBinder.createAndBindUi(this));
    }



    @Override
    public Widget getWidgetView() {
        return this;
    }



    @Override
    public CategoryItemView getCategoryItem() {
        // TODO Auto-generated method stub
        return categoryItem;
    }


}
