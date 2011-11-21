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
    interface UserViewUiBinder extends UiBinder<Widget, UserView> { }

    @UiField TabLayoutPanel tabLayoutPanel;
    @UiField SimplePanel demandPanel;
    @UiField SimplePanel oldTabPanel;
    @UiField SimplePanel adminPanel;
    @UiField SimplePanel settingsPanel;

    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        /**
         * Doing it this way only to keep standart gwt-class on tabLayoutPanel
         */
        StyleResource.INSTANCE.layout().ensureInjected();
        tabLayoutPanel.addStyleName(StyleResource.INSTANCE.layout().fullSize());
    }

    public void setBody(Widget body) {
        oldTabPanel.setWidget(body);
    }

    public void setBodyAdmin(Widget body) {
        adminPanel.setWidget(body);
    }

    public Widget getWidgetView() {
        return this;
    }

    @Override
    public TabLayoutPanel getLayoutPanel() {
        return tabLayoutPanel;
    }

    //TODO
    //Beho devel section, when completed. Put into normal code section
    @Override
    public void setBodyDemand(Widget demandModule) {
        demandPanel.setWidget(demandModule);
    }

    @Override
    public SimplePanel getDemandModulePanel() {
        return demandPanel;
    }

    public SimplePanel getSettingsPanel() {
        return settingsPanel;
    }

    public void setSettingsPanel(SimplePanel settingsPanel) {
        this.settingsPanel = settingsPanel;
    }

    public void setBodySettings(Widget body) {
        settingsPanel.setWidget(body);
    }

}
