package cz.poptavka.sample.client.main.common.creation;

import java.util.HashMap;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.main.MainEventBus;

@Presenter(view = FormDemandAdvView.class, multiple = true)
public class FormDemandAdvPresenter extends
    LazyPresenter<FormDemandAdvPresenter.FormDemandAdvViewInterface, MainEventBus> {

    public interface FormDemandAdvViewInterface extends LazyView {

        Widget getWidgetView();

        boolean isValid();

        HashMap<String, Object> getValues();
    }

    /** Injecting widget. **/
    public void initDemandAdvForm(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view.getWidgetView());
    }

}
