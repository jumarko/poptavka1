package cz.poptavka.sample.client.user.demands.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class NewDemandView extends Composite implements NewDemandPresenter.NewDemandInterface {

    private static NewDemandViewUiBinder uiBinder = GWT.create(NewDemandViewUiBinder.class);
    interface NewDemandViewUiBinder extends UiBinder<Widget, NewDemandView> {
    }

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }


}
