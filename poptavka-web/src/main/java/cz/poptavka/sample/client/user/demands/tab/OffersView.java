package cz.poptavka.sample.client.user.demands.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class OffersView extends Composite implements OffersPresenter.OffersInterface {

    private static OffersViewUiBinder uiBinder = GWT.create(OffersViewUiBinder.class);
    interface OffersViewUiBinder extends UiBinder<Widget, OffersView> {
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
