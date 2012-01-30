package cz.poptavka.sample.client.user.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.root.ReverseCompositeView;
import cz.poptavka.sample.client.user.IUserMenuView.IUserMenuPresenter;
import cz.poptavka.sample.client.user.interfaces.IUserMenuView;

public class UserMenuView extends ReverseCompositeView<IUserMenuPresenter>
        implements IUserMenuView {

    private static UserMenuViewUiBinder uiBinder = GWT
            .create(UserMenuViewUiBinder.class);

    interface UserMenuViewUiBinder extends UiBinder<Widget, UserMenuView> {
    }

    @UiField
    Button demands, messages, settings, contacts, administration;
    @UiField
    UListElement menuList;

    public UserMenuView() {
        uiBinder.createAndBindUi(this);
        menuList.addClassName(StyleResource.INSTANCE.layout().homeMenu());
    }

    @Override
    public HasClickHandlers getDemandsButton() {
        return demands;
    }

    @Override
    public HasClickHandlers getMessagesButton() {
        return messages;
    }

    @Override
    public HasClickHandlers getSettingsButton() {
        return settings;
    }

    @Override
    public HasClickHandlers getContactsButton() {
        return contacts;
    }

    @Override
    public HasClickHandlers getAdministrationButton() {
        return administration;
    }

    @Override
    public void setHomeToken(String token) {

    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

}
