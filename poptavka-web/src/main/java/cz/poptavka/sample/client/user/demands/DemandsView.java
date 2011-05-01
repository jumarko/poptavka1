package cz.poptavka.sample.client.user.demands;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;

import com.google.gwt.user.client.ui.Widget;

public class DemandsView extends Composite implements DemandsPresenter.DemandsViewInterface {

    private static DemandsViewUiBinder uiBinder = GWT.create(DemandsViewUiBinder.class);
    interface DemandsViewUiBinder extends UiBinder<Widget, DemandsView> {
    }

    @UiField
    Button answerButton;

    @UiField
    Button editButton;

    @UiField
    Button closeButton;

    @UiField
    Button cancelButton;

    @UiField
    ListBox actionSelectButton;

    @UiField
    FlexTable demandsTable;

    public DemandsView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public Widget getWidgetView() {
        return this;
    }



}
