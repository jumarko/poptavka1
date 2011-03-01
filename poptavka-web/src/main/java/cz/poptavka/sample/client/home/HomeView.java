package cz.poptavka.sample.client.home;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;

/**
 * Main view for home/unlogged user aka public section.
 *
 * @author Beho
 *
 */
public class HomeView extends Composite implements HomePresenter.HomeInterface {

    private static HomeViewUiBinder uiBinder = GWT.create(HomeViewUiBinder.class);

    interface HomeViewUiBinder extends UiBinder<Widget, HomeView> {
    }

    private Map<AnchorEnum, Widget> widgetMap = new HashMap<AnchorEnum, Widget>();

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

    public void setContent(AnchorEnum anchor, Widget body) {
        shouldRemoveWidget(anchor);
        widgetMap.put(anchor, body);
        switch (anchor) {
            case FIRST:
                container.add(body, "first");
                break;
            case SECOND:
                container.add(body, "second");
                break;
            case THIRD:
                container.add(body, "third");
                break;
            default:
                break;
        }
    }

    /**
     * Returns this view instance.
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }

    /**
     * check and eventually remove widget from anchor.
     *
     * @param anchor
     */
    private void shouldRemoveWidget(AnchorEnum anchor) {
        if (widgetMap.containsKey(anchor)) {
            widgetMap.remove(anchor);
        }
    }

}
