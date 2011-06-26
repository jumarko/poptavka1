package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.offer.FullOfferDetail;

public class AdminOfferInfoPresenter
                    extends LazyPresenter<AdminOfferInfoPresenter.AdminOfferInfoInterface, UserEventBus> {
    public interface AdminOfferInfoInterface extends LazyView {
        Widget getWidgetView();

        void setOfferDetail(FullOfferDetail contact);

        Button getUpdateBtn();
    }

    public void onShowAdminOfferDetail(FullOfferDetail selectedObject) {
//        eventBus.displayContent(view.getWidgetView());
        view.setOfferDetail(selectedObject);
        eventBus.responseAdminOfferDetail(view.getWidgetView());

    }

    public void bindView() {
        view.getUpdateBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GWT.log("offer updated");
            }
        });
    }
}
