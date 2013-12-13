package com.eprovement.poptavka.client.root.header;

import com.eprovement.poptavka.client.common.session.CssInjector;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.common.ReverseCompositeView;
import com.eprovement.poptavka.client.root.header.ui.LoginElement;
import com.eprovement.poptavka.client.root.header.ui.LogoutElement;
import com.eprovement.poptavka.client.root.header.ui.MenuElement;
import com.eprovement.poptavka.client.root.header.ui.NotificationsElement;
import com.eprovement.poptavka.client.root.header.ui.SearchElement;
import com.eprovement.poptavka.client.root.interfaces.IHeaderView;
import com.eprovement.poptavka.client.root.interfaces.IHeaderView.IHeaderPresenter;
import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.google.gwt.user.client.ui.Button;

public class HeaderView extends ReverseCompositeView<IHeaderPresenter>
    implements IHeaderView {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static HeaderViewUiBinder uiBinder = GWT.create(HeaderViewUiBinder.class);

    interface HeaderViewUiBinder extends UiBinder<Widget, HeaderView> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        CssInjector.INSTANCE.ensureHeaderStylesInjected();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder Attributes. **/
    @UiField Button logo;
    @UiField MenuElement menu;
    @UiField SearchElement search;
    @UiField NotificationsElement notifications;
    @UiField LoginElement login;
    @UiField LogoutElement logout;
    @UiField IconAnchor settingsAnchor;

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    public HeaderView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public HeaderView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public Button getLogo() {
        return logo;
    }

    @Override
    public MenuElement getMenu() {
        return menu;
    }

    @Override
    public SearchElement getSearch() {
        return search;
    }

    @Override
    public NotificationsElement getNotifications() {
        return notifications;
    }

    @Override
    public LoginElement getLogin() {
        return login;
    }

    @Override
    public LogoutElement getLogout() {
        return logout;
    }

    @Override
    public IconAnchor getSettingsAnchor() {
        return settingsAnchor;
    }
}
