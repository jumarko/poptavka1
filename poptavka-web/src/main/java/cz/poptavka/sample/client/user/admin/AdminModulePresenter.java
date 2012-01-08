package cz.poptavka.sample.client.user.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.Button;
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

        Button getDemandsButton();

        Button getClientsButton();

        Button getOffersButton();

        Button getSuppliersButton();

        Button getAccessRoleButton();

        Button getEmailActivationButton();

        Button getInvoiceButton();

        Button getMessageButton();

        Button getPaymentMethodButton();

        Button getPermissionButton();

        Button getPreferenceButton();

        Button getProblemButton();

        SimplePanel getContentPanel();
    }
    private LoadingDiv loading = null;

    public void bind() {
        view.getDemandsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initDemands(null);
            }
        });
        view.getClientsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initClients(null);
            }
        });
        view.getSuppliersButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initSuppliers(null);
            }
        });
        view.getOffersButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initOffers(null);
            }
        });
        view.getAccessRoleButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initAccessRoles(null);
            }
        });
        view.getEmailActivationButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initEmailsActivation(null);
            }
        });
        view.getInvoiceButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initInvoices(null);
            }
        });
        view.getMessageButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initMessages(null);
            }
        });
        view.getPaymentMethodButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initPaymentMethods(null);
            }
        });
        view.getPermissionButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initPermissions(null);
            }
        });
        view.getPreferenceButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initPreferences(null);
            }
        });
        view.getProblemButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initProblems(null);
            }
        });
    }

    public void onInitAdminModule() {
        GWT.log("onInitAdminModule - som tu");
        //eventBus.loadingShow(MSGS.progressDemandsLayoutInit());
//        eventBus.setTabAdminWidget(view.getWidgetView());
//        eventBus.fireMarkedEvent();
//        eventBus.setUserInteface((StyleInterface) view.getWidgetView());

        Storage.showLoading(Storage.MSGS.progressAdminLayoutInit());
//        panel.setWidget(view.getWidgetView());
        view.getWidgetView().setStyleName(Storage.RSCS.common().user());
        eventBus.setBodyHolderWidget(view.getWidgetView());
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