/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import com.google.gwt.user.client.ui.Button;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;

/**
 *
 * @author ivan.vlcek
 */
@Presenter(view = AdminDemandInfoView.class)
public class AdminDemandInfoPresenter
        extends LazyPresenter<AdminDemandInfoPresenter.AdminDemandInfoInterface, UserEventBus> {
    public interface AdminDemandInfoInterface extends LazyView {
        Widget getWidgetView();

        void setDemandDetail(FullDemandDetail contact);

        Button getUpdateBtn();
    }

    public void onShowAdminDemandDetail(FullDemandDetail selectedObject) {
//        eventBus.displayContent(view.getWidgetView());
        view.setDemandDetail(selectedObject);
        eventBus.responseAdminDemandDetail(view.getWidgetView());

    }

    public void bindView() {
        view.getUpdateBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GWT.log("demand updated");
            }
        });
    }
}
