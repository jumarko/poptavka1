package cz.poptavka.sample.client.common.creation.widget;

import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.common.CommonEventBus;

@Presenter(view = FormDemandAdvView.class)
public class FormDemandAdvPresenter extends
    LazyPresenter<FormDemandAdvPresenter.FormDemandAdvViewInterface, CommonEventBus> {

    private static final Logger LOGGER = Logger
            .getLogger(FormDemandAdvPresenter.class.getName());

    public interface FormDemandAdvViewInterface extends LazyView {

        Widget getWidgetView();

        boolean isValid();

        HashMap<String, Object> getValues();
    }

    /** Injecting widget. **/
    public void onInitDemandAdvForm(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view.getWidgetView());
    }

}
