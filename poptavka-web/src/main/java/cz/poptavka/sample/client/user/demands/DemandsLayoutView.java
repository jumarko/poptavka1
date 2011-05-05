package cz.poptavka.sample.client.user.demands;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class DemandsLayoutView extends Composite implements DemandsLayoutPresenter.DemandsLayoutInterface {

    private static DemandsLayoutViewUiBinder uiBinder = GWT.create(DemandsLayoutViewUiBinder.class);
    interface DemandsLayoutViewUiBinder extends UiBinder<Widget, DemandsLayoutView> {
    }


    @UiField Button myDemandsBtn, offersBtn, createDemandBtn;
    @UiField SimplePanel contentPanel;

    public DemandsLayoutView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public HasClickHandlers getMyDemandsBtn() {
        return myDemandsBtn;
    }

    @Override
    public HasClickHandlers getOffersBtn() {
        // TODO Auto-generated method stub
        return offersBtn;
    }

    @Override
    public HasClickHandlers getCreateDemandBtn() {
        // TODO Auto-generated method stub
        return createDemandBtn;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public void setContent(Widget contentWidget) {
        contentPanel.setWidget(contentWidget);
    }

}
