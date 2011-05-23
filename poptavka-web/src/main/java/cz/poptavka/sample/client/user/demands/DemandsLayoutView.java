package cz.poptavka.sample.client.user.demands;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class DemandsLayoutView extends Composite implements DemandsLayoutPresenter.DemandsLayoutInterface {

    private static DemandsLayoutViewUiBinder uiBinder = GWT.create(DemandsLayoutViewUiBinder.class);
    interface DemandsLayoutViewUiBinder extends UiBinder<Widget, DemandsLayoutView> {
    }


    private static final Logger LOGGER = Logger
            .getLogger(DemandsLayoutView.class.getName());

//    @UiField Button myDemandsBtn, offersBtn, createDemandBtn;
    @UiField SimplePanel contentPanel;
    @UiField Hyperlink myDemandsLink, offersLink, newDemandLink, myDemandsOperatorLink;

    public DemandsLayoutView() {
        initWidget(uiBinder.createAndBindUi(this));
    }
//
//    @Override
//    public HasClickHandlers getMyDemandsBtn() {
//        return myDemandsBtn;
//    }
//
//    @Override
//    public HasClickHandlers getOffersBtn() {
//        // TODO Auto-generated method stub
//        return offersBtn;
//    }
//
//    @Override
//    public HasClickHandlers getCreateDemandBtn() {
//        // TODO Auto-generated method stub
//        return createDemandBtn;
//    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public void setContent(Widget contentWidget) {
        contentPanel.setWidget(contentWidget);
    }

    public void setMyDemandsToken(String linkString) {
        myDemandsLink.setTargetHistoryToken(linkString);
    }

    public void setOffersToken(String linkString) {
        offersLink.setTargetHistoryToken(linkString);
    }

    public void setNewDemandToken(String linkString) {
        newDemandLink.setTargetHistoryToken(linkString);
    }

    public void setMyDemandsOperatorToken(String linkString) {
        myDemandsOperatorLink.setTargetHistoryToken(linkString);
    }

}
