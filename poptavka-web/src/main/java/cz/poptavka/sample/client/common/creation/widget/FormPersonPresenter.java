package cz.poptavka.sample.client.common.creation.widget;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.common.CommonEventBus;
import cz.poptavka.sample.shared.domain.ClientDetail;

@Presenter(view = FormPersonView.class)
public class FormPersonPresenter extends LazyPresenter<FormPersonPresenter.FormPersonInterface, CommonEventBus> {

    private static final Logger LOGGER = Logger
            .getLogger(FormPersonPresenter.class.getName());

    public interface FormPersonInterface extends LazyView {

        boolean isValid();

        ClientDetail getNewClient();

        Widget getWidgetView();
    }

    public void onInitPersonForm(HasOneWidget embedToWidget) {
        LOGGER.fine("setting widget...");
        embedToWidget.setWidget(view.getWidgetView());
    }

    public void onSubmitUserForm() {
        if (view.isValid()) {
            ClientDetail client = view.getNewClient();
            eventBus.registerNewClient(client);
        } else {
            //error
        }
        LOGGER.fine("result of validation: " + view.isValid());
    }
}
