package cz.poptavka.sample.client.main;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;

public class MainView extends Composite implements MainPresenter.MainViewInterface {

    private static final Binder BINDER = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, MainView> {    }

    /** Main UI containers. **/
    @UiField DockLayoutPanel layoutMaster;
    @UiField HTMLPanel headerHolder;
    @UiField SimplePanel bodyHolder;
    @UiField HTMLPanel footerHolder;

    @UiField Anchor loginButton;

    /**
     * Constructor of parent view with some basic styling.
     */
    public MainView() {
        initWidget(BINDER.createAndBindUi(this));
        //styling layout - styled in UiBinder, but this is required
        StyleResource.INSTANCE.layout().ensureInjected();
    }

    /**
     * Sets widget to Body section widget, Body section can hold one widget only.
     *
     * @param body widget to be inserted
     */
    public void setBodyWidget(Widget body) {
        bodyHolder.setWidget(body);
    }

    @Override
    public Anchor getLoginButton() {
        return loginButton;
    }

    @Override
    public void toggleMainLayout(boolean switchToUserLayout) {
        if (switchToUserLayout) {
            layoutMaster.setStyleName(StyleResource.INSTANCE.layout().layoutUser());
        } else {
            layoutMaster.setStyleName(StyleResource.INSTANCE.layout().layoutPublic());
        }
    }

}
