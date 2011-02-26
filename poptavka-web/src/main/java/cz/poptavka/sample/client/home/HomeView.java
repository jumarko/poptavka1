package cz.poptavka.sample.client.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;

public class HomeView extends Composite implements HomePresenter.HomeInterface {

    private static HomeViewUiBinder uiBinder = GWT.create(HomeViewUiBinder.class);

    interface HomeViewUiBinder extends UiBinder<Widget, HomeView> {
    }

    @UiField
    HTMLPanel container;
    //menu section
    @UiField
    Button button1;
    @UiField
    Button button2;
    @UiField
    Button button3;
    @UiField
    Button button4;
    @UiField
    Button button5;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setContent(AnchorEnum anchor, Widget body) {
        switch (anchor) {
            case FIRST:
                container.add(body, "first");
                break;
            case SECOND:
                container.addAndReplaceElement(body, "second");
                break;
            case THIRD:
                container.addAndReplaceElement(body, "third");
                break;
            default:
                break;
        }
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
