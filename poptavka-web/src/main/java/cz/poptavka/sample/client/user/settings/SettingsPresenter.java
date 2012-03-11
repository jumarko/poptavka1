package cz.poptavka.sample.client.user.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.shared.domain.settings.SettingsDetail;

@Presenter(view = SettingsView.class)
public class SettingsPresenter
        extends
        BasePresenter<SettingsPresenter.HomeSettingsViewInterface, SettingsEventBus> {

    public interface HomeSettingsViewInterface {

        Widget getWidgetView();

        TextBox getCompanyName();

        TextBox getClientRating();

        TextBox getSupplierRating();

        TextBox getStreet();

        TextBox getCity();

        TextBox getZipCode();

        TextArea getCategoriesBox();

        TextArea getLocalitiesBox();

        TextBox getWeb();

        TextBox getEmail();

        TextBox getPhone();

        TextBox getFirstName();

        TextBox getLastName();

        TextBox getIdentificationNumber();

        TextBox getTaxNumber();

        TextArea getDescriptionBox();

        TextArea getServicesBox();

        CheckBox getNewMessageButton();

        ListBox getNewMessageOptions();

        CheckBox getNewDemandButton();

        ListBox getNewDemandOptions();

        CheckBox getNewSystemMessageButton();

        ListBox getNewSystemMessageOptions();

        CheckBox getNewOperatorMessageButton();

        ListBox getNewOperatorMessageOptions();

        CheckBox getDemandStateChangeButton();

        ListBox getDemandStateChangeOptions();
    }

    public void onInitSettings() {
        Storage.setCurrentlyLoadedModule("settings");
        GWT.log("User ID for settings" + Storage.getUser().getUserId());
        // eventBus.loadingShow(MSGS.progressDemandsLayoutInit());
        view.getNewMessageOptions().addItem("immediately");
        view.getNewMessageOptions().addItem("daily");
        view.getNewMessageOptions().addItem("weekly");

        view.getNewDemandOptions().addItem("immediately");
        view.getNewDemandOptions().addItem("daily");
        view.getNewDemandOptions().addItem("weekly");

        view.getNewSystemMessageOptions().addItem("immediately");
        view.getNewSystemMessageOptions().addItem("daily");
        view.getNewSystemMessageOptions().addItem("weekly");

        view.getNewOperatorMessageOptions().addItem("immediately");
        view.getNewOperatorMessageOptions().addItem("daily");
        view.getNewOperatorMessageOptions().addItem("weekly");

        view.getDemandStateChangeOptions().addItem("immediately");
        view.getDemandStateChangeOptions().addItem("daily");
        view.getDemandStateChangeOptions().addItem("weekly");
        // eventBus.setTabSettingsWidget(view.getWidgetView()); //TODO MArtin
        // ako to ma byt????
        long userId = Storage.getUser().getUserId();
        eventBus.getLoggedUser(userId);

    }

    public void onSetSettings(SettingsDetail detail) {
        GWT.log("SettingsDetail company name" + detail.getCompanyName());
        view.getCompanyName().setText(detail.getCompanyName());
        view.getClientRating().setText(
                Integer.toString(detail.getClientRating()));
        if (detail.getSupplier() != null
                && detail.getSupplier().getOverallRating() != null) {
            view.getSupplierRating().setText(
                    Integer.toString(detail.getSupplier().getOverallRating()));
        }
        view.getStreet().setText(detail.getAddresses().get(0).getStreet());
        view.getCity().setText(detail.getAddresses().get(0).getCityName());
        view.getZipCode().setText(detail.getAddresses().get(0).getZipCode());

        StringBuilder categoryBuilder = new StringBuilder();
        if (detail.getSupplier() != null) {
            for (String category : detail.getSupplier().getCategories()) {
                categoryBuilder.append(category);
                categoryBuilder.append("\n");
            }
        }
        view.getCategoriesBox().setText(categoryBuilder.toString());

        StringBuilder localitiesBuilder = new StringBuilder();
        if (detail.getSupplier() != null) {
            for (String locality : detail.getSupplier().getLocalities()) {
                localitiesBuilder.append(locality);
                localitiesBuilder.append("\n");
            }
        }
        view.getLocalitiesBox().setText(localitiesBuilder.toString());
        view.getEmail().setText(detail.getEmail());
        view.getPhone().setText(detail.getPhone());
        view.getFirstName().setText(detail.getFirstName());
        view.getLastName().setText(detail.getLastName());
        view.getIdentificationNumber()
                .setText(detail.getIdentificationNumber());
        view.getTaxNumber().setText(detail.getTaxId());
        view.getDescriptionBox().setText(detail.getDescription());
    }
}
