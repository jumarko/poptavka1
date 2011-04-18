package cz.poptavka.sample.client.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabBar;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.user.listOfDemands.ListOfDemandsView;

public class UserView extends Composite implements UserPresenter.UserViewInterface {

    private static UserViewUiBinder uiBinder = GWT.create(UserViewUiBinder.class);
    interface UserViewUiBinder extends UiBinder<Widget, UserView> { }

    @UiField
    DecoratedTabBar tabBar;
    @UiField
    ListOfDemandsView demandsList;
    @UiField
    SimplePanel tabPanel;
    //tabs
    Anchor demandsTab = new Anchor("Poptavky");
    Anchor messagesTab = new Anchor("Spravy");
    Anchor settingsTab = new Anchor("Nastavenia");
    Anchor contactsTab = new Anchor("Kontakty");
    Anchor categoriesTab = new Anchor("Kategorie");

    public UserView() {

        initWidget(uiBinder.createAndBindUi(this));

        demandsList.getListOfDemandsView();

        tabBar.addTab(demandsTab);
        tabBar.addTab(messagesTab);
        tabBar.addTab(settingsTab);
        tabBar.addTab(contactsTab);
        tabBar.addTab(categoriesTab);
    }

    public void setBody(Widget body) {
        tabPanel.setWidget(body);
    }

    public Widget getWidgetView() {
        return this;
    }

}
