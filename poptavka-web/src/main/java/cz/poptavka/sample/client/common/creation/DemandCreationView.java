package cz.poptavka.sample.client.common.creation;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DemandCreationView extends Composite implements DemandCreationPresenter.CreationViewInterface {



    private static CreationViewUiBinder uiBinder = GWT.create(CreationViewUiBinder.class);
    interface CreationViewUiBinder extends UiBinder<Widget, DemandCreationView> {    }

    private static final Logger LOGGER = Logger.getLogger("    DemandCreationView");

    protected enum TopPanel {
        SECOND, THIRD, FOURTH, REMOVE
    }

    @UiField AbsolutePanel absolutePanel;

    //anim constants
    private static final int START_VALUE = 465;
    private static final int END_SECOND_VALUE = 20;
    private static final int END_THIRD_VALUE = 40;
    private static final int END_FOURTH_VALUE = 60;
    private static final int ONE_SECOND = 1000;

    private MoveAnimation anim;

    //initial topPanel
    private ArrayList<VerticalPanel> topPanel = new ArrayList<VerticalPanel>();
    private int endValue = 0;

    @UiField VerticalPanel part1;
    @UiField TextBox titleBox;
    @UiField TextBox paramOne;
    @UiField TextBox paramTwo;
    @UiField TextBox paramThree;
    @UiField(provided = true) RichTextToolbarWidget richText;
    //place for uploadFiles button
    //place for addNextAttachment button
    @UiField Button btnToSecond;

    @UiField VerticalPanel part2;
    @UiField SimplePanel localityHolder;
    @UiField Button btnBackFirst;
    @UiField Button btnToThird;

    @UiField VerticalPanel part3;
    @UiField SimplePanel categoryHolder;
    @UiField Button btnBackSecond;
    @UiField Button btnToFourth;

    @UiField VerticalPanel part4;
    @UiField TextBox userNameBox;
    @UiField TextBox userOtherBox;
    @UiField Button btnBackThird;
    @UiField Button btnCreate;

    public void createView() {
        LOGGER.info("initializing part: Rich Text Toolbar ... ");
        richText = new RichTextToolbarWidget();
        LOGGER.info("initializing part: COMPLETE widget ... ");
        initWidget(uiBinder.createAndBindUi(this));

        anim = new MoveAnimation();
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    public HasClickHandlers firstNextButton() {
        return btnToSecond;
    }

    public HasClickHandlers secondNextButton() {
        return btnToThird;
    }

    public HasClickHandlers thirdNextButton() {
        return btnToFourth;
    }

    public HasClickHandlers secondBackButton() {
        return btnBackFirst;
    }

    public HasClickHandlers thirdBackButton() {
        return btnBackSecond;
    }

    public HasClickHandlers fourthBackButton() {
        return btnBackThird;
    }

    public HasClickHandlers createDemandButton() {
        return btnCreate;
    }

    /**
     * Handling method for toggle display of current block.
     *
     * @param newPanel block to toggle display
     */
    public void toggleVisiblePanel(TopPanel newPanel) {
        boolean showNext = true;
        switch (newPanel) {
            case SECOND:
                topPanel.add(part2);
                endValue = END_SECOND_VALUE;
                break;
            case THIRD:
                topPanel.add(part3);
                endValue = END_THIRD_VALUE;
                break;
            case FOURTH:
                topPanel.add(part4);
                endValue = END_FOURTH_VALUE;
                break;
            case REMOVE:
                anim.setAndRun(endValue, START_VALUE, topPanel.remove(topPanel.size() - 1), ONE_SECOND);
                //because it's the nominal value of space
                endValue -= END_SECOND_VALUE;
                showNext = false;
                break;
            default:
                break;
        }
        if (showNext) {
            anim.setAndRun(START_VALUE, endValue, topPanel.get(topPanel.size() - 1), ONE_SECOND);
        }
    }

    @Override
    public SimplePanel getLocalityHolder() {
        return localityHolder;
    }

    @Override
    public SimplePanel getCategoryHolder() {
        return categoryHolder;
    }

    /**
     * For block animation (show/hide) purpose.
     *
     * @author Beho
     */
    private class MoveAnimation extends Animation {
        private int startY;
        private int distance;
        private VerticalPanel object = null;

        public MoveAnimation() {
        }

        //Initialize currently animated object
        public void setAndRun(int start, int end, VerticalPanel obj, int duration) {
            object = obj;
            startY = start;
            distance = start - end;

            //run it
            this.run(duration);
        }

        @Override
        protected void onUpdate(double progress) {
            double value = startY - (progress * distance);
            absolutePanel.setWidgetPosition(object, 0, (int) value);
        }
    }

}
