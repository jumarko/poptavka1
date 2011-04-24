package cz.poptavka.sample.client.common.creation.widget;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.common.CommonEventBus;
import cz.poptavka.sample.shared.domain.ClientDetail;

@Presenter(view = FormCompanyView.class)
public class FormCompanyPresenter extends LazyPresenter<FormCompanyPresenter.FormCompanyInterface, CommonEventBus> {


    private static final Logger LOGGER = Logger
            .getLogger(FormCompanyPresenter.class.getName());

    public interface FormCompanyInterface extends LazyView {

        boolean isValid();

        ClientDetail getNewClient();

        Widget getWidgetView();
    }

    public void onInitCompanyForm(HasOneWidget embedToWidget) {
        LOGGER.fine("setting widget...");
        embedToWidget.setWidget(view.getWidgetView());
    }

    public void onSubmitUserForm() {
        if (view.isValid()) {
            //eventBus.sendData
            ClientDetail client = view.getNewClient();
            eventBus.registerNewClient(client);
        } else {
            //error
        }
        LOGGER.fine("result of validation: " + view.isValid());
    }
}
