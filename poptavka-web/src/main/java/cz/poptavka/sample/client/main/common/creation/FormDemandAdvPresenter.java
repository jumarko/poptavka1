package cz.poptavka.sample.client.main.common.creation;

import java.util.HashMap;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import cz.poptavka.sample.client.home.creation.DemandCreationEventBus;

//import cz.poptavka.sample.client.main.MainEventBus;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail.DemandField;

@Presenter(view = FormDemandAdvView.class, multiple = true)
public class FormDemandAdvPresenter extends
    LazyPresenter<FormDemandAdvPresenter.FormDemandAdvViewInterface, DemandCreationEventBus> {

    public interface FormDemandAdvViewInterface extends LazyView {

        Widget getWidgetView();

        boolean isValid();

        HashMap<DemandField, Object> getValues();
    }

    /** Injecting widget. **/
    public void initDemandAdvForm(SimplePanel embedToWidget) {
        embedToWidget.setWidget(view.getWidgetView());
    }

}
