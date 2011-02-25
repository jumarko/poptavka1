package cz.poptavka.sample.client.main;


import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;

public class MainView extends Composite implements MainPresenter.MainViewInterface {

    private static final Binder BINDER = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, MainView> {    }

    @UiField
    HTMLPanel layoutMaster;
    @UiField
    HTMLPanel headerHolder;
    @UiField
    HTMLPanel bodyHolder;
    @UiField
    HTMLPanel footerHolder;

    FlexTable table = new FlexTable();
    FlexTable categoryTable = new FlexTable();

    public MainView() {
        initWidget(BINDER.createAndBindUi(this));
        //styling layout
        StyleResource.INSTANCE.cssBase().ensureInjected();
        layoutMaster.setStylePrimaryName(StyleResource.INSTANCE.cssBase().layoutContainer());
        headerHolder.setStylePrimaryName(StyleResource.INSTANCE.cssBase().headerContainer());
        bodyHolder.setStylePrimaryName(StyleResource.INSTANCE.cssBase().bodyContainer());
        footerHolder.setStylePrimaryName(StyleResource.INSTANCE.cssBase().footerContainer());

        bodyHolder.add(table, "center");
        bodyHolder.add(categoryTable, "center");
        table.setText(0, 0, "District Name");
        table.setText(0, 1, "District Code");
        categoryTable.setText(0, 0, "Category name");
        categoryTable.setText(0, 1, "Category code");
    }

    @Override
    public void setData(List<Locality> data) {
        // TODO Auto-generated method stub
        for (int i = 0; i < data.size(); i++) {
            table.setText(i + 1, 0, data.get(i).getName());
            table.setText(i + 1, 1, data.get(i).getCode());
        }
    }

    @Override
    public void setCategories(List<Category> data) {
        // TODO Auto-generated method stub
        for (int i = 0; i < data.size(); i++) {
            categoryTable.setText(i + 1, 0, data.get(i).getName());
            categoryTable.setText(i + 1, 1, data.get(i).getCode());
        }
    }

}
