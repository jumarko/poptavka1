package cz.poptavka.sample.client.main;


import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;

public class MainView extends Composite implements MainPresenter.MainViewInterface {

    private static final Binder BINDER = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, MainView> {    }

    private static final Logger LOGGER = Logger.getLogger(MainView.class.getName());

    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    private boolean compactDisplay = false;

    /** Main UI containers. **/
    @UiField DockLayoutPanel layoutMaster;
    @UiField HTMLPanel headerHolder;
    @UiField SimplePanel bodyHolder;
    @UiField HTMLPanel footerHolder;

    /** login area **/
    @UiField Anchor loginButton;

    @UiField HTML footController;

    /**
     * Constructor of parent view with some basic styling.
     */
    public MainView() {
        initWidget(BINDER.createAndBindUi(this));
        //styling layout - styled in UiBinder, but this is required
        StyleResource.INSTANCE.layout().ensureInjected();
        initFooterToggle();

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
    public void toggleMainLayout(boolean switchToUserLayout) {
        if (switchToUserLayout) {
            layoutMaster.setStyleName(StyleResource.INSTANCE.layout().layoutUser());
        } else {
            layoutMaster.setStyleName(StyleResource.INSTANCE.layout().layoutPublic());
        }
    }

    private void initFooterToggle() {
        footController.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                toggleFooter();
            }
        });
    }

    private static final int FOOT_HEIGHT = 50;

    private void toggleFooter() {
        Style footerParentStyle = footerHolder.getElement().getParentElement().getStyle();
        Style centerParentStyle = bodyHolder.getElement().getParentElement().getStyle();
        if (footerParentStyle.getHeight().equals("0px")) {
            footerParentStyle.setHeight(FOOT_HEIGHT, Unit.PX);
            centerParentStyle.setBottom(FOOT_HEIGHT, Unit.PX);
            footController.setText(MSGS.footerHide());
        } else {
            footerParentStyle.setHeight(0, Unit.PX);
            centerParentStyle.setBottom(0, Unit.PX);
            footController.setText(MSGS.footerDisplay());
        }
    }

    @Override
    public HTMLPanel getHeaderHolder() {
        return headerHolder;
    }

    @Override
    public Anchor getLoginLink() {
        return loginButton;
    }


}
