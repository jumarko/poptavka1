package cz.poptavka.sample.client.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;

public class UserView extends Composite implements UserPresenter.UserViewInterface {

    private static UserViewUiBinder uiBinder = GWT.create(UserViewUiBinder.class);

    interface UserViewUiBinder extends UiBinder<Widget, UserView> {
    }
    @UiField
    TabLayoutPanel tabLayoutPanel;
    @UiField
    SimplePanel demandPanel;
    //@UiField SimplePanel oldTabPanel;
    @UiField
    SimplePanel adminPanel;
    @UiField
    SimplePanel settingsPanel;
    @UiField
    SimplePanel messagesPanel;

    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        /**
         * Doing it this way only to keep standart gwt-class on tabLayoutPanel
         */
        StyleResource.INSTANCE.layout().ensureInjected();
        tabLayoutPanel.addStyleName(StyleResource.INSTANCE.layout().fullSize());
        tabLayoutPanel.addStyleName(StyleResource.INSTANCE.layout().tabLayoutContainer());
    }

    @Override
    public void setBodyDemand(Widget demandModule) {
        demandPanel.setWidget(demandModule);
    }

    @Override
    public void setBodyAdmin(Widget body) {
        adminPanel.setWidget(body);
    }

    @Override
    public void setBodySettings(Widget body) {
        settingsPanel.setWidget(body);
    }

    @Override
    public void setBodyMessages(Widget body) {
        messagesPanel.setWidget(body);
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public TabLayoutPanel getLayoutPanel() {
        return tabLayoutPanel;
    }

    @Override
    public SimplePanel getDemandModulePanel() {
        return demandPanel;
    }

    @Override
    public SimplePanel getMessagesModulePanel() {
        return messagesPanel;
    }

    @Override
    public SimplePanel getAdminModulePanel() {
        return adminPanel;
    }

    public SimplePanel getSettingsPanel() {
        return settingsPanel;
    }

    public void setSettingsPanel(SimplePanel settingsPanel) {
        this.settingsPanel = settingsPanel;
    }
}
