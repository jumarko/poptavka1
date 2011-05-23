package cz.poptavka.sample.client.user.demands.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MyDemandsOperatorView extends Composite
    implements MyDemandsOperatorPresenter.MyDemandsOperatorViewInterface {

    private static MyDemandsOperatorUiBinder uiBinder = GWT
            .create(MyDemandsOperatorUiBinder.class);

    interface MyDemandsOperatorUiBinder extends
            UiBinder<Widget, MyDemandsOperatorView> {
    }

    public MyDemandsOperatorView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiField
    Button button;

    public MyDemandsOperatorView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
        button.setText(firstName);
    }

    @UiHandler("button")
    void onClick(ClickEvent e) {
        Window.alert("Hello!");
    }

    public void setText(String text) {
        button.setText(text);
    }

    public String getText() {
        return button.getText();
    }

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        // TODO Auto-generated method stub
        return this;
    }

}
