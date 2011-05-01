package cz.poptavka.sample.client.main;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;

public class MainView extends Composite implements MainPresenter.MainViewInterface {

    private static final Binder BINDER = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, MainView> {    }

    /** Main UI containers. **/
    @UiField HTMLPanel layoutMaster;
    @UiField HTMLPanel headerHolder;
    @UiField SimplePanel bodyHolder;
//    @UiField HTMLPanel footerHolder;

    @UiField Anchor loginButton;

    /**
     * Constructor of parent view with some basic styling.
     */
    public MainView() {
        initWidget(BINDER.createAndBindUi(this));
        //styling layout - styled in UiBinder
        StyleResource.INSTANCE.cssBase().ensureInjected();
        layoutMaster.getElement().setId("page");
        Document.get().getElementById("footerContainer")
            .addClassName(StyleResource.INSTANCE.cssBase().footerContainer());
    }

    /**
     * Sets widget to Body section widget, Body section can hold one widget only.
     *
     * @param body widget to be inserted
     */
    public void setBodyWidget(Widget body) {
        bodyHolder.setWidget(body);
    }

    public void setListOfDemands(Widget demands) {
        bodyHolder.setWidget(demands);
    }

    /**
     * Sets widget to header Login Area. Designed for Login Widget only.
     *
     * @param login login widget
     */
    public void setLoginWidget(Widget login) {
        headerHolder.add(login, "loginArea");
    }

    @Override
    public Anchor getLoginButton() {
        return loginButton;
    }

    @Override
    public void toggleMainLayout(boolean switchToUserLayout) {
        if (switchToUserLayout) {
            layoutMaster.setStyleName(StyleResource.INSTANCE.cssBase().layoutContainerUser());
        } else {
            layoutMaster.setStyleName(StyleResource.INSTANCE.cssBase().layoutContainer());
        }
    }

}
