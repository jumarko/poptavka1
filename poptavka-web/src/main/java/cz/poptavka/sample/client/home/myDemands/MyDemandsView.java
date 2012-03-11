package cz.poptavka.sample.client.home.myDemands;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MyDemandsView extends Composite {

    private static MyDemandsUiBinder uiBinder = GWT
            .create(MyDemandsUiBinder.class);

    interface MyDemandsUiBinder extends UiBinder<Widget, MyDemandsView> {
    }

    public MyDemandsView() {
        initWidget(uiBinder.createAndBindUi(this));
    }
//
//    @UiField
//    Button button;
// upravicka
//    public MyDemandsView(String firstName) {
//        initWidget(uiBinder.createAndBindUi(this));
//        button.setText(firstName);
//    }
//
//    @UiHandler("button")
//    void onClick(ClickEvent e) {
//        Window.alert("Hello!");
//    }
//
//    public void setText(String text) {
//        button.setText(text);
//    }
//
//    public String getText() {
//        return button.getText();
//    }

}
