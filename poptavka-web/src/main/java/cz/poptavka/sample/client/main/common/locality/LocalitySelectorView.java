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
import com.google.inject.Singleton;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.domain.address.LocalityType;

@Singleton
public class LocalitySelectorView extends Composite implements LocalitySelectorPresenter.LocalitySelectorInterface {

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

    public LocalitySelectorView() {
        initWidget(uiBinder.createAndBindUi(this));
        StyleResource.INSTANCE.layout().ensureInjected();
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public ListBox getDistrictList() {
        return regionList;
    }

    @Override
    public ListBox getTownshipList() {
        return districtList;
    }

    @Override
    public ListBox getCityList() {
        return cityList;
    }

    public void toggleLoader() {
        loader.setVisible(!loader.isVisible());
    }

    @Override
    public String getSelectedItem(LocalityType type) {
        switch (type) {
            case REGION:
                return regionList.getValue(regionList.getSelectedIndex());
            case DISTRICT:
                return districtList.getValue(districtList.getSelectedIndex());
            default:
                return cityList.getValue(cityList.getSelectedIndex());
        }
    }

    public void addToSelectedList() {
        int index = cityList.getSelectedIndex();
        String itemText = cityList.getItemText(index);
        if (!selectedListStrings.contains(itemText)) {
            selectedList.addItem(cityList.getItemText(index), cityList.getValue(index));
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
