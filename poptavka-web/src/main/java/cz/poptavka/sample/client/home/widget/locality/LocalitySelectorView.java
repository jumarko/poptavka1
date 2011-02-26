package cz.poptavka.sample.client.home.widget.locality;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.shared.domain.LocalityDetail;

public class LocalitySelectorView extends Composite implements LocalitySelectorPresenter.LocalitySelectorInterface {

    private static LocalitySelectorUiBinder uiBinder = GWT.create(LocalitySelectorUiBinder.class);

    interface LocalitySelectorUiBinder extends UiBinder<Widget, LocalitySelectorView> {
    }

    private static final int VISIBLE_COUNT = 10;

    @UiField
    ListBox districtList;
    @UiField
    ListBox townshipList;
    @UiField
    ListBox cityList;
    @UiField
    HTML loader;
    @UiField
    Label selected;

    public LocalitySelectorView() {
        initWidget(uiBinder.createAndBindUi(this));

        //loader.setVisible(false);

        districtList.setVisibleItemCount(VISIBLE_COUNT);
        townshipList.setVisible(false);
        townshipList.setVisibleItemCount(VISIBLE_COUNT);
        cityList.setVisible(false);
        cityList.setVisibleItemCount(VISIBLE_COUNT);
    }

    @Override
    public void setLocalities(LocalityType type, List<LocalityDetail> list) {
        switch (type) {
            case DISTRICT:
                setData(districtList, list);
                break;
            case TOWNSHIP:
                setData(townshipList, list);
                break;
            case CITY:
                setData(cityList, list);
                break;
            default:
                break;
        }
    }

    private void setData(ListBox box, List<LocalityDetail> list) {
        box.clear();
        for (int i = 0; i < list.size(); i++) {
            box.addItem(list.get(i).getName(), list.get(i).getCode());
        }
        toggleLoader();
        box.setVisible(true);
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public ListBox getDistrictList() {
        return districtList;
    }

    @Override
    public ListBox getTownshipList() {
        return townshipList;
    }

    @Override
    public ListBox getCityList() {
        return cityList;
    }

    @Override
    public String getSelectedItem(LocalityType type) {
        switch (type) {
            case DISTRICT:
                return districtList.getValue(districtList.getSelectedIndex());
            case TOWNSHIP:
                return townshipList.getValue(townshipList.getSelectedIndex());
            default:
                return cityList.getValue(cityList.getSelectedIndex());
        }
    }

    public void toggleLoader() {
        loader.setVisible(!loader.isVisible());
    }

    public void setSelectedItem() {
        selected.setText(cityList.getValue(cityList.getSelectedIndex()));
    }

}
