package cz.poptavka.sample.client.common.creation;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;

public class DemandCreationView extends Composite implements DemandCreationPresenter.CreationViewInterface {

    private static CreationViewUiBinder uiBinder = GWT.create(CreationViewUiBinder.class);
    interface CreationViewUiBinder extends UiBinder<Widget, DemandCreationView> {    }

    private static final Logger LOGGER = Logger.getLogger("    DemandCreationView");

    protected enum TopPanel {
        SECOND, THIRD, FOURTH, FIFTH, REMOVE
    }

    @UiField AbsolutePanel absolutePanel;

    //anim constants
    private static final int START_VALUE = 465;
    private static final int END_SECOND_VALUE = 40;
    private static final int END_THIRD_VALUE = 60;
    private static final int END_FOURTH_VALUE = 80;
    private static final int END_FIFTH_VALUE = 100;
    private static final int ONE_SECOND = /** 1000; **/ 1;

    //initial topPanel
    private ArrayList<VerticalPanel> topPanel = new ArrayList<VerticalPanel>();
    private int endValue = 0;

    //step1
    @UiField VerticalPanel stepOne;
    @UiField SimplePanel basicInfoHolder;
    @UiField Button btnOneNext;
    //step2
    @UiField VerticalPanel stepTwo;
    @UiField SimplePanel categoryHolder;
    @UiField Button btnTwoBack, btnTwoNext;
    //step3
    @UiField VerticalPanel stepThree;
    @UiField SimplePanel localityHolder;
    @UiField Button btnThreeBack, btnThreeNext;
    //step4
    @UiField VerticalPanel stepFour;
    @UiField SimplePanel advInfoHolder;
    @UiField Button btnFourBack, btnFourNext;
    //step5
    @UiField VerticalPanel stepFive;
    @UiField SimplePanel userFormHolder;
    @UiField Button btnFiveBack, btnFiveRegAndCreate;



    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        StyleResource.INSTANCE.cssBase().ensureInjected();
        btnFiveRegAndCreate.setStyleName(StyleResource.INSTANCE.cssBase().elemHiddenOn());
        stepFive.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
    }

    @Override
    public Widget getWidgetView() {
        return this;
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
                topPanel.add(stepTwo);
                endValue = END_SECOND_VALUE;
                break;
            case THIRD:
                topPanel.add(stepThree);
                endValue = END_THIRD_VALUE;
                break;
            case FOURTH:
                topPanel.add(stepFour);
                endValue = END_FOURTH_VALUE;
                break;
            case FIFTH:
                topPanel.add(stepFive);
                endValue = END_FIFTH_VALUE;
                break;
            case REMOVE:
                new MoveAnimation().setAndRun(endValue, START_VALUE, topPanel.remove(topPanel.size() - 1), ONE_SECOND);
                //because it's the nominal value of space
                endValue -= END_SECOND_VALUE;
                showNext = false;
                break;
            default:
                break;
        }
        if (showNext) {
            new MoveAnimation().setAndRun(START_VALUE, endValue, topPanel.get(topPanel.size() - 1), ONE_SECOND);
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

    @Override
    public SimplePanel getBasicInfoHolder() {
        return basicInfoHolder;
    }

    @Override
    public SimplePanel getAdvInfoHolder() {
        return advInfoHolder;
    }

    @Override
    public HasClickHandlers oneNextButton() {
        return btnOneNext;
    }

    @Override
    public HasClickHandlers twoBackButton() {
        return btnTwoBack;
    }

    @Override
    public HasClickHandlers twoNextButton() {
        return btnTwoNext;
    }

    @Override
    public HasClickHandlers threeBackButton() {
        return btnThreeBack;
    }

    @Override
    public HasClickHandlers threeNextButton() {
        return btnThreeNext;
    }

    @Override
    public HasClickHandlers fourBackButton() {
        return btnFourBack;
    }

    @Override
    public HasClickHandlers fourNextButton() {
        return btnFourNext;
    }

    @Override
    public HasClickHandlers fiveBackButton() {
        return btnFiveBack;
    }

    @Override
    public HasClickHandlers fiveCreateButton() {
        return btnFiveRegAndCreate;
    }

    @Override
    public VerticalPanel getVerticalPanel() {
        return stepTwo;
    }

    @Override
    public void cascadeTogglePanel(final TopPanel second) {
        int panelOnTop = topPanel.size();
        LOGGER.fine("top panel is " + panelOnTop + " and ordinal is: " + second.ordinal());
        if ((second.ordinal() + 1) < panelOnTop) {
            toggleVisiblePanel(TopPanel.REMOVE);
            Timer timer = new Timer() {

                @Override
                public void run() {
                    cascadeTogglePanel(second);
                }
            };
            timer.schedule(100);
        }
    }

    @Override
    public SimplePanel getUserFormHolder() {
        return userFormHolder;
    }

    @Override
    public void toggleRegAndCreateButton() {
        LOGGER.fine(btnFiveRegAndCreate.getStylePrimaryName()
            + " VS " + StyleResource.INSTANCE.cssBase().elemHiddenOn());
        if (btnFiveRegAndCreate.getStylePrimaryName().equals(StyleResource.INSTANCE.cssBase().elemHiddenOn())) {
            btnFiveRegAndCreate.setStylePrimaryName(StyleResource.INSTANCE.cssBase().elemHiddenOff());
        } else {
            btnFiveRegAndCreate.setStylePrimaryName(StyleResource.INSTANCE.cssBase().elemHiddenOn());
        }
    }

//********************************************************************************************************************//
//                                            MOVE ANIMATION CLASS                                                    //
//********************************************************************************************************************//
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
