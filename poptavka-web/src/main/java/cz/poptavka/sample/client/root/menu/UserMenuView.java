package cz.poptavka.sample.client.root.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
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
    @UiField
    Button demands, messages, settings, administration;

    public UserMenuView() {
        initWidget(uiBinder.createAndBindUi(this));
        menuList.addClassName(StyleResource.INSTANCE.layout().homeMenu());
        demands.addStyleName(StyleResource.INSTANCE.layout().selected());
    }

    /**************************************************************************/
    /* UiHanders.                                                             */
    /**************************************************************************/
    @UiHandler("demands")
    public void onClickDemands(ClickEvent e) {
        presenter.goToDemands();
        clickDemandsUserMenuStyleChange();
    }

    @UiHandler("messages")
    public void onClickMessages(ClickEvent e) {
        presenter.goToMessages();
        clickMessagesUserMenuStyleChange();
    }

    @UiHandler("settings")
    public void onClickSettings(ClickEvent e) {
        presenter.goToSettings();
        clickSettingsUserMenuStyleChange();
    }

    @UiHandler("administration")
    public void onClickAdministration(ClickEvent e) {
        presenter.goToAdministration();
        clickAdministrationUserMenuStyleChange();
    }

    /**************************************************************************/
    /* Style change methods.                                                  */
    /**************************************************************************/
    @Override
    public void clickDemandsUserMenuStyleChange() {
        demands.addStyleName(StyleResource.INSTANCE.layout().selected());
        messages.removeStyleName(StyleResource.INSTANCE.layout().selected());
        settings.removeStyleName(StyleResource.INSTANCE.layout().selected());
        administration.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    @Override
    public void clickMessagesUserMenuStyleChange() {
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        messages.addStyleName(StyleResource.INSTANCE.layout().selected());
        settings.removeStyleName(StyleResource.INSTANCE.layout().selected());
        administration.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    @Override
    public void clickSettingsUserMenuStyleChange() {
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        messages.removeStyleName(StyleResource.INSTANCE.layout().selected());
        settings.addStyleName(StyleResource.INSTANCE.layout().selected());
        administration.removeStyleName(StyleResource.INSTANCE.layout().selected());
    }

    @Override
    public void clickAdministrationUserMenuStyleChange() {
        demands.removeStyleName(StyleResource.INSTANCE.layout().selected());
        messages.removeStyleName(StyleResource.INSTANCE.layout().selected());
        settings.removeStyleName(StyleResource.INSTANCE.layout().selected());
        administration.addStyleName(StyleResource.INSTANCE.layout().selected());
    }
}
