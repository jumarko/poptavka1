package cz.poptavka.sample.client.user.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.root.RootEventBus;
import cz.poptavka.sample.client.user.IUserMenuView.IUserMenuPresenter;
import cz.poptavka.sample.client.user.interfaces.IUserMenuView;

@Presenter(view = UserMenuView.class)
public class UserMenuPresenter extends BasePresenter<IUserMenuView, RootEventBus>
        implements IUserMenuPresenter {

    public void onStart() {
        bindView();
        GWT.log("User menu module loaded");
        eventBus.setMenu(view);
    }

    public void onSetUserMenu() {
        bindView();
        GWT.log("user menu loaded");
        eventBus.setMenu(view);
    }

    public void bindView() {
        GWT.log("Binding user menu view");
        view.getDemandsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initDemandModule(null, "welcome");
            }
        });
        view.getMessagesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initMessagesModule(null, "inbox");
            }
        });
        view.getSettingsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initSettings();
            }
        });
        view.getContactsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
            }
        });
        view.getAdministrationButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initAdminModule(null, "initAdminModule");

            }
        });

    }
}
