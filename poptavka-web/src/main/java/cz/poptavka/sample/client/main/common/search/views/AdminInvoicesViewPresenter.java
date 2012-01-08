package cz.poptavka.sample.client.main.common.search.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.SearchModuleEventBus;
import cz.poptavka.sample.shared.domain.PaymentMethodDetail;
import java.util.List;

@Presenter(view = AdminInvoicesViewView.class)
public class AdminInvoicesViewPresenter
        extends BasePresenter<AdminInvoicesViewPresenter.AdminInvoicesViewInterface, SearchModuleEventBus> {

    public interface AdminInvoicesViewInterface {

        SearchModuleDataHolder getFilter();

        Widget getWidgetView();

        ListBox getPaymentMethodList();

        void displayAdvSearchDataInfo(SearchModuleDataHolder data, TextBox infoHolder);
    }

    @Override
    public void bind() {
    }

    public void onInitAdminInvoicesView(PopupPanel popupPanel) {
        eventBus.requestPaymentMethods();
        popupPanel.setWidget(view.getWidgetView());
    }

    public void onResponsePaymentMethods(final List<PaymentMethodDetail> list) {
        final ListBox box = view.getPaymentMethodList();
        box.clear();
        box.setVisible(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                box.addItem("select method...");
                for (int i = 0; i < list.size(); i++) {
                    box.addItem(list.get(i).getName(), list.get(i).getName());
                }
                box.setSelectedIndex(0);
                GWT.log("PaymentMethodList filled");
            }
        });
    }
}