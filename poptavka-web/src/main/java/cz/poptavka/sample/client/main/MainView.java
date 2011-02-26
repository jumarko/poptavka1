package cz.poptavka.sample.client.main;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;

public class MainView extends Composite implements MainPresenter.MainViewInterface {

    private static final Binder BINDER = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, MainView> {    }

    @UiField
    HTMLPanel layoutMaster;
    @UiField
    HTMLPanel headerHolder;
    @UiField
    SimplePanel bodyHolder;
    @UiField
    HTMLPanel footerHolder;

    /**
     * Constructor of parent view with some basic styling
     */
    public MainView() {
        initWidget(BINDER.createAndBindUi(this));
        //styling layout
        StyleResource.INSTANCE.cssBase().ensureInjected();
        layoutMaster.setStylePrimaryName(StyleResource.INSTANCE.cssBase().layoutContainer());
        headerHolder.setStylePrimaryName(StyleResource.INSTANCE.cssBase().headerContainer());
        bodyHolder.setStylePrimaryName(StyleResource.INSTANCE.cssBase().bodyContainer());
        footerHolder.setStylePrimaryName(StyleResource.INSTANCE.cssBase().footerContainer());
    }

    /**
     * Sets widget to Body section widget, Body section can hold one widget only
     *
     * @param body widget to be inserted
     */
    public void setBodyWidget(Widget body) {
        bodyHolder.setWidget(body);
    }

    /**
     * Sets widget to header Login Area. Designed for Login Widget only.
     *
     * @param login login widget
     */
    public void setLoginWidget(Widget login) {
        headerHolder.add(login, "loginArea");
    }

}
