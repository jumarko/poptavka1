package cz.poptavka.sample.client.home.widget.locality;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;

public class LocalitySelectorView extends Composite implements LocalitySelectorPresenter.LocalitySelectorInterface {

    private static LocalitySelectorUiBinder uiBinder = GWT.create(LocalitySelectorUiBinder.class);

    interface LocalitySelectorUiBinder extends UiBinder<Widget, LocalitySelectorView> {
    }

    private static final int VISIBLE_COUNT = 10;

    @UiField
    ListBox regionList;
    @UiField
    ListBox districtList;
    @UiField
    ListBox townshipList;

    @UiField
    HTML loader;

    public LocalitySelectorView() {
        initWidget(uiBinder.createAndBindUi(this));

        loader.setVisible(false);

        regionList.setVisibleItemCount(VISIBLE_COUNT);
        districtList.setVisible(false);
        districtList.setVisibleItemCount(VISIBLE_COUNT);
        townshipList.setVisible(false);
        townshipList.setVisibleItemCount(VISIBLE_COUNT);
    }

    @Override
    public void setLocalities(LocalityType type, List<Locality> list) {
        switch (type) {
            case REGION:
                setData(regionList, list);
                break;
            case DISTRICT:
                setData(districtList, list);
                toggleLoader();
                districtList.setVisible(true);
                break;
            case TOWNSHIP:
                toggleLoader();
                setData(townshipList, list);
                townshipList.setVisible(true);
            default:
                break;
        }
    }

    private void setData(ListBox box, List<Locality> list) {
        for (int i = 0; i < list.size(); i++) {
            box.addItem(list.get(i).getName(), list.get(i).getCode());
        }
        box.setVisible(true);
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public HasChangeHandlers getDistrictList() {
        return districtList;
    }

    @Override
    public HasChangeHandlers getRegionList() {
        return regionList;
    }

    @Override
    public HasChangeHandlers getTownshipList() {
        return townshipList;
    }

    @Override
    public String getSelectedItem() {
        return townshipList.getValue(townshipList.getSelectedIndex());
    }

    public void toggleLoader() {
        loader.setVisible(!loader.isVisible());
    }

}
