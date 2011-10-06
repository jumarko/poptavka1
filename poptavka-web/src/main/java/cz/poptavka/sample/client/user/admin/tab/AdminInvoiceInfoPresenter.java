/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.google.gwt.user.client.ui.Button;
import com.mvp4g.client.presenter.BasePresenter;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.InvoiceDetail;

/**
 *
 * @author Martin Slavkovky
 */
@Presenter(view = AdminInvoiceInfoView.class)
public class AdminInvoiceInfoPresenter
        extends BasePresenter<AdminInvoiceInfoPresenter.AdminInvoiceInfoInterface, UserEventBus> {

    public interface AdminInvoiceInfoInterface {

        Widget getWidgetView();

        void setInvoiceDetail(InvoiceDetail contact);

        InvoiceDetail getUpdatedInvoiceDetail();

        Button getUpdateBtn();
    }

    public void onShowAdminInvoicesDetail(InvoiceDetail selectedObject) {
//        eventBus.displayContent(view.getWidgetView());
        view.setInvoiceDetail(selectedObject);
        eventBus.responseAdminDemandDetail(view.getWidgetView());

    }

    @Override
    public void bind() {
        view.getUpdateBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
//                eventBus.addInvoiceToCommit(view.getUpdatedInvoiceDetail(), "all");
                Window.alert("Demand updated");
            }
        });
    }
}
