package cz.poptavka.sample.client.homeWelcome;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.homeWelcome.interfaces.IHomeWelcomeView;
import cz.poptavka.sample.client.homeWelcome.interfaces.IHomeWelcomeView.IHomeWelcomePresenter;
import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.root.ReverseCompositeView;

public class HomeWelcomeView extends
        ReverseCompositeView<IHomeWelcomePresenter> implements IHomeWelcomeView {

    private static HomeWelcomeViewUiBinder uiBinder = GWT
            .create(HomeWelcomeViewUiBinder.class);

    interface HomeWelcomeViewUiBinder extends UiBinder<Widget, HomeWelcomeView> {
    }

    public HomeWelcomeView() {
        initWidget(uiBinder.createAndBindUi(this));
                StyleResource.INSTANCE.common().ensureInjected();

        Address address = new Address();
        Person person = new Person();
        address.setOwner(person);

        this.addressEditor.edit(address);
    }

//    @UiField
//    Button button;
    @UiField
    AddressEditor addressEditor;

    public HomeWelcomeView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
//        button.setText(firstName);
        
    }

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

    @Override
    public Widget getWidgetView() {
        return this;
    }

}
