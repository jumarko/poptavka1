package cz.poptavka.sample.client.user.demands.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.user.demands.widgets.OffersFlexTable;

public class OffersView extends Composite implements OffersPresenter.OffersInterface {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    private static OffersViewUiBinder uiBinder = GWT.create(OffersViewUiBinder.class);
    interface OffersViewUiBinder extends UiBinder<Widget, OffersView> { }

    private Button answerBtn;
    private Button refuseBtn;
    private Button acceptBtn;
    private OffersFlexTable table;
    private SimplePanel messageSection;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        table = new OffersFlexTable();
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public OffersFlexTable getTable() {
        return table;
    }

    @Override
    public Button getAnswerBtn() {
        return answerBtn;
    }

    @Override
    public Button getRefuseBtn() {
        return refuseBtn;
    }

    @Override
    public Button getAcceptBtn() {
        return acceptBtn;
    }

    @Override
    public SimplePanel getDetailSection() {
        return messageSection;
    }


}
