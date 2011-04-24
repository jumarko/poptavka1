package cz.poptavka.sample.client.common.creation.widget;

import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.common.CommonEventBus;

@Presenter(view = FormDemandBasicView.class)
public class FormDemandBasicPresenter
    extends LazyPresenter<FormDemandBasicPresenter.FormDemandBasicInterface, CommonEventBus> {

    private static final Logger LOGGER = Logger
            .getLogger(FormDemandBasicPresenter.class.getName());

    public interface FormDemandBasicInterface extends LazyView {

        Widget getWidgetView();

        boolean isValid();

        HashMap<String, Object> getValues();

    }

    @Override
    public void bindView() {
    }

    /** Injecting widget. **/
    public void onInitDemandBasicForm(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view.getWidgetView());
    }

    /** toValidate() **/
    public void onValidateDemandBasicForm() {
        LOGGER.fine("validating basic form .....");
        if (view.isValid()) {
            eventBus.formNextStep();
        } else {
            Window.alert("Fill the form first");
        }
    }

    public void onGetBasicInfoValues() {
        LOGGER.info("Getting/Pushing basic values");
        HashMap<String, Object> map = view.getValues();
        eventBus.pushBasicInfoValues(map);
        LOGGER.info("Getting/Pushing basic values");
    }
}
