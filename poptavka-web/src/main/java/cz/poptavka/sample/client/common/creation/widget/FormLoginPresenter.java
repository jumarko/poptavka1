package cz.poptavka.sample.client.common.creation.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.common.CommonEventBus;
import cz.poptavka.sample.shared.domain.ClientDetail;

@Presenter(view = FormLoginView.class)
public class FormLoginPresenter extends LazyPresenter<FormLoginPresenter.FormLoginInterface, CommonEventBus> {

    public interface FormLoginInterface extends LazyView {

        Widget getWidgetView();

        HasClickHandlers getLoginBtn();

        HasClickHandlers getRegisterBtn();

        String getLogin();

        String getPassword();

        void displayError();
    }

    @Override
    public void bindView() {
        view.getLoginBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                processLogin();
            }
        });
        view.getRegisterBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.initNewUserForm((SimplePanel) view.getWidgetView().getParent());
            }
        });
    }

    /** Init widget. **/
    public void onInitFormLogin(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view.getWidgetView());
    }

    private void processLogin() {
        String login = view.getLogin();
        String pass = view.getPassword();
        if ((login.length() == 0) || (pass.length() == 0)) {
            view.displayError();
        }
        // TODO hash passwd

        // TODO service call to server to verify user
        eventBus.verifyExistingClient(new ClientDetail(view.getLogin(), view.getPassword()));
    }
}
