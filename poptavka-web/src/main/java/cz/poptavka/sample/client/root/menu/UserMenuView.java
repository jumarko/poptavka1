package cz.poptavka.sample.client.root.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.root.ReverseCompositeView;
import cz.poptavka.sample.client.root.interfaces.IUserMenuView;
import cz.poptavka.sample.client.root.interfaces.IUserMenuView.IUserMenuPresenter;

public class UserMenuView extends ReverseCompositeView<IUserMenuPresenter> implements IUserMenuView {

    private static UserMenuViewUiBinder uiBinder = GWT.create(UserMenuViewUiBinder.class);

    interface UserMenuViewUiBinder extends UiBinder<Widget, UserMenuView> {
    }
    @UiField
    UListElement menuList;

    public UserMenuView() {
        initWidget(uiBinder.createAndBindUi(this));
        menuList.addClassName(StyleResource.INSTANCE.layout().homeMenu());
    }

    @UiHandler("demands")
    public void onClickDemands(ClickEvent e) {
        presenter.goToDemands();
    }

    @UiHandler("messages")
    public void onClickMessages(ClickEvent e) {
        presenter.goToMessages();
    }

    @UiHandler("settings")
    public void onClickSettings(ClickEvent e) {
        presenter.goToSettings();
    }

    @UiHandler("administration")
    public void onClickAdministration(ClickEvent e) {
        presenter.goToAdministration();
    }
//    Button demands, messages, settings, administration;
}
