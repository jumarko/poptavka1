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
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import com.google.gwt.user.client.ui.Button;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;

/**
 *
 * @author ivan.vlcek
 */
@Presenter(view = AdminSupplierInfoView.class)
public class AdminSupplierInfoPresenter
        extends LazyPresenter<AdminSupplierInfoPresenter.AdminSupplierInfoInterface, UserEventBus> {
    public interface AdminSupplierInfoInterface extends LazyView {
        Widget getWidgetView();

        void setSupplierDetail(FullSupplierDetail contact);

        FullSupplierDetail getUpdatedSupplierDetail();

        Button getUpdateBtn();
    }

    public void onShowAdminSupplierDetail(FullSupplierDetail selectedObject) {
//        eventBus.displayContent(view.getWidgetView());
        view.setSupplierDetail(selectedObject);
        eventBus.responseAdminSupplierDetail(view.getWidgetView());

    }

    @Override
    public void bindView() {
        view.getUpdateBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.addSuppliersToCommit(view.getUpdatedSupplierDetail(), "all");
                Window.alert("Demand updated");
            }
        });
    }
}
