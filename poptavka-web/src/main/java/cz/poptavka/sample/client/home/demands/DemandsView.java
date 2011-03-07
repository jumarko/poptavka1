package cz.poptavka.sample.client.home.demands;

import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.domain.demand.Demand;

public class DemandsView extends Composite implements DemandsPresenter.DemandsViewInterface {

    private static DemandsUiBinder uiBinder = GWT.create(DemandsUiBinder.class);

    interface DemandsUiBinder extends UiBinder<Widget, DemandsView> { }

    @UiField VerticalPanel verticalContent;

    public DemandsView() {
        initWidget(uiBinder.createAndBindUi(this));
        //styling layout - styled in UiBinder
        StyleResource.INSTANCE.cssBase().ensureInjected();
    }

    public void displayDemands(List<Demand> result) {
        DemandView demandView;

        for (Demand demand : result) {
            demandView = new DemandView();
            demandView.setDemand(demand);
            verticalContent.add(demandView);
        }
    }
}
