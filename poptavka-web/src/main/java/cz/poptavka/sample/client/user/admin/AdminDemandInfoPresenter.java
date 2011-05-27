/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.DemandDetail;

/**
 *
 * @author ivan.vlcek
 */
@Presenter(view = AdminDemandInfoView.class)
public class AdminDemandInfoPresenter
        extends LazyPresenter<AdminDemandInfoPresenter.AdminDemandInfoInterface, UserEventBus> {
    public interface AdminDemandInfoInterface extends LazyView {
        Widget getWidgetView();

        void setContact(DemandDetail contact);
    }

    public void onShowAdminDemandDetail(DemandDetail selectedObject) {
//        eventBus.displayContent(view.getWidgetView());
        view.setContact(selectedObject);
        eventBus.responseAdminDemandDetail(view.getWidgetView());

    }
}
