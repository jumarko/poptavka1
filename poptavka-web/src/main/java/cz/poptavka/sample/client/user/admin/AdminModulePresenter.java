package cz.poptavka.sample.client.user.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.user.widget.LoadingDiv;

@Presenter(view = AdminModuleView.class, multiple = true)
public class AdminModulePresenter extends
        BasePresenter<AdminModulePresenter.AdminModuleInterface, AdminModuleEventBus> {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface AdminModuleInterface {

        Widget getWidgetView();

        void setContent(Widget contentWidget);

        Anchor getDemandsAnchor();

        Anchor getClientsAnchor();

        Anchor getOffersAnchor();

        Anchor getSuppliersAnchor();

        Anchor getAccessRoleAnchor();

        Anchor getEmailActivationAnchor();

        Anchor getInvoiceAnchor();

        Anchor getMessageAnchor();

        Anchor getPaymentMethodAnchor();

        Anchor getPermissionAnchor();

        Anchor getPreferenceAnchor();

        Anchor getProblemAnchor();

        SimplePanel getContentPanel();
    }
    private LoadingDiv loading = null;

    public void bind() {
        view.getDemandsAnchor().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initDemands();
            }
        });
        view.getClientsAnchor().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initClients();
            }
        });
        view.getSuppliersAnchor().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initSuppliers();
            }
        });
        view.getOffersAnchor().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initOffers();
            }
        });
        view.getAccessRoleAnchor().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initAccessRoles();
            }
        });
        view.getEmailActivationAnchor().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initEmailsActivation();
            }
        });
        view.getInvoiceAnchor().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initInvoices();
            }
        });
        view.getMessageAnchor().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initMessages();
            }
        });
        view.getPaymentMethodAnchor().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initPaymentMethods();
            }
        });
        view.getPermissionAnchor().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initPermissions();
            }
        });
        view.getPreferenceAnchor().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initPreferences();
            }
        });
        view.getProblemAnchor().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initProblems();
            }
        });
    }

    public void onInitAdminModule(SimplePanel panel) {
        GWT.log("onInitAdminModule - som tu");
        //eventBus.loadingShow(MSGS.progressDemandsLayoutInit());
//        eventBus.setTabAdminWidget(view.getWidgetView());
//        eventBus.fireMarkedEvent();
//        eventBus.setUserInteface((StyleInterface) view.getWidgetView());

        Storage.showLoading(Storage.MSGS.progressAdminLayoutInit());
        panel.setWidget(view.getWidgetView());
        Storage.hideLoading();
    }

    public void onToggleLoading() {
        if (loading == null) {
            GWT.log("  - loading created");
            loading = new LoadingDiv(view.getContentPanel().getParent());
        } else {
            GWT.log("  - loading removed");
            loading.getElement().removeFromParent();
            loading = null;
        }
    }

    public void onDisplayView(Widget content) {
        view.setContent(content);
    }
}