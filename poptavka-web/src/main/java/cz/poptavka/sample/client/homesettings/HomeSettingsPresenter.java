package cz.poptavka.sample.client.homesettings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.user.UserEventBus;

@Presenter(view = HomeSettingsView.class)
public class HomeSettingsPresenter
        extends
        BasePresenter<HomeSettingsPresenter.HomeSettingsViewInterface, UserEventBus> {
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

    }

    public void onInitSettings() {
        GWT.log("som tu");
        // eventBus.loadingShow(MSGS.progressDemandsLayoutInit());
        eventBus.setTabSettingsWidget(view.getWidgetView());
        // eventBus.fireMarkedEvent();
        // eventBus.setUserInteface((StyleInterface) view.getWidgetView());
    }
}
