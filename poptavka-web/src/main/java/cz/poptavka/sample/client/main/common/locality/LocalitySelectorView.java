package cz.poptavka.sample.client.main.common.locality;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.main.common.creation.ProvidesValidate;
import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.LocalityDetail;

public class LocalitySelectorView extends Composite
    implements LocalitySelectorPresenter.LocalitySelectorInterface, ProvidesValidate  {

    private static LocalitySelectorUiBinder uiBinder = GWT.create(LocalitySelectorUiBinder.class);

    interface LocalitySelectorUiBinder extends UiBinder<Widget, LocalitySelectorView> {
    }

    private HashSet<String> selectedListStrings = new HashSet<String>();

    @UiField
    ListBox regionList;
    @UiField
    ListBox districtList;
    @UiField
    ListBox cityList;
    @UiField
    HTML loader;

    @UiField ListBox selectedList;

    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        StyleResource.INSTANCE.layout().ensureInjected();
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
    public ListBox getRegionList() {
        return regionList;
    }

    @Override
    public ListBox getCityList() {
        return cityList;
    }

    public void toggleLoader() {
        loader.setVisible(!loader.isVisible());
    }

    @Override
    public String getSelectedItem(int localityType) {
        switch (localityType) {
            case LocalityDetail.REGION:
                return regionList.getValue(regionList.getSelectedIndex());
            case LocalityDetail.DISTRICT:
                return districtList.getValue(districtList.getSelectedIndex());
            default:
                return cityList.getValue(cityList.getSelectedIndex());
        }
    }

    public void addToSelectedList(int localityType) {
        String itemText = null;
        String itemValue = null;
        int index = -1;

        switch (localityType) {
            case LocalityDetail.DISTRICT:
                index = districtList.getSelectedIndex();
                itemText = districtList.getItemText(index);
                itemValue = districtList.getValue(index);
                break;
            case LocalityDetail.REGION:
                index = regionList.getSelectedIndex();
                itemText = regionList.getItemText(index);
                itemValue = regionList.getValue(index);
                break;
            default:
                index = cityList.getSelectedIndex();
                itemText = cityList.getItemText(index);
                itemValue = cityList.getValue(index);
                break;
        }
        if (!selectedListStrings.contains(itemText)) {
            selectedList.addItem(itemText, itemValue);
            selectedListStrings.add(itemText);
        }
    }

    @Override
    public ListBox getSelectedList() {
        return selectedList;
    }

    @Override
    public void removeFromSelectedList() {
        int index = selectedList.getSelectedIndex();
        String item = selectedList.getItemText(index);
        selectedListStrings.remove(item);
        selectedList.removeItem(index);
    }

    @Override
    public boolean isValid() {
        return selectedList.getItemCount() > 0;
    }

    public ArrayList<String> getSelectedLocalityCodes() {
        ArrayList<String> codes = new ArrayList<String>();
        for (int i = 0; i < selectedList.getItemCount(); i++) {
            codes.add(selectedList.getValue(i));
        }
        return codes;
    }
}
