package cz.poptavka.sample.client.homeWelcome;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.homeWelcome.interfaces.IHomeWelcomeView;
import cz.poptavka.sample.client.homeWelcome.interfaces.IHomeWelcomeView.IHomeWelcomePresenter;
import cz.poptavka.sample.client.root.ReverseCompositeView;

public class HomeWelcomeView extends
        ReverseCompositeView<IHomeWelcomePresenter> implements IHomeWelcomeView {

    private static HomeWelcomeViewUiBinder uiBinder = GWT
            .create(HomeWelcomeViewUiBinder.class);

    interface HomeWelcomeViewUiBinder extends UiBinder<Widget, HomeWelcomeView> {
    }

    public HomeWelcomeView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiField
    Button button;

    public HomeWelcomeView(String firstName) {
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
    public Widget getWidgetView() {
        return this;
    }

}
