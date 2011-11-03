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
import cz.poptavka.sample.shared.domain.ClientDetail;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminClientInfoView.class)
public class AdminClientInfoPresenter
        extends LazyPresenter<AdminClientInfoPresenter.AdminClientInfoInterface, UserEventBus> {

    public interface AdminClientInfoInterface extends LazyView {

        Widget getWidgetView();

        void setClientDetail(ClientDetail contact);

        ClientDetail getUpdatedClientDetail();

        Button getUpdateBtn();
    }

    public void onShowAdminClientDetail(ClientDetail selectedObject) {
//        eventBus.displayContent(view.getWidgetView());
        view.setClientDetail(selectedObject);
        eventBus.responseAdminClientDetail(view.getWidgetView());

    }

    @Override
    public void bindView() {
        view.getUpdateBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
//                eventBus.addClientToCommit(view.getUpdatedClientDetail(), "all");
                Window.alert("Client updated");
            }
        });
    }
}
