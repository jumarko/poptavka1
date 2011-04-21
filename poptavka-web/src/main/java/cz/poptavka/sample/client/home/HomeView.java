package cz.poptavka.sample.client.home;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
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
    @UiField Hyperlink linkHome;
    @UiField Hyperlink linkCreateDemand;
    @UiField
    Button btnCreateDemand;
    @UiField
    Button displayDemands;
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

    public void setContent(AnchorEnum anchor, Widget body, boolean clearOthers) {
        if (clearOthers) {
            for (AnchorEnum anchorToClear : AnchorEnum.values()) {
                shouldRemoveWidget(anchorToClear);
            }
        } else {
            shouldRemoveWidget(anchor);
        }
        widgetMap.put(anchor, body);
        switch (anchor) {
            case FIRST:
                container.add(body, "first");
                widgetMap.put(anchor, body);
                break;
            case SECOND:
                container.add(body, "second");
                widgetMap.put(anchor, body);
                break;
            case THIRD:
                container.add(body, "third");
                widgetMap.put(anchor, body);
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
            container.remove(widgetMap.get(anchor));
            widgetMap.remove(anchor);
        }
    }

    @Override
    public HasClickHandlers getCreateDemandBtn() {
        return btnCreateDemand;
    }

    @Override
    public HasClickHandlers getDisplayDemandsBtn() {
        return displayDemands;
    }

    @Override
    public HasClickHandlers getButton3Btn() {
        return button3;
    }

    @Override
    public void setCreateDemandToken(String token) {
        linkCreateDemand.setTargetHistoryToken(token);
    }

    public void setHomeToken(String token) {
        linkHome.setTargetHistoryToken(token);
    }
}
