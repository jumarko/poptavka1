package cz.poptavka.sample.client.common.creation;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.common.OverflowComposite;
import cz.poptavka.sample.client.resources.StyleResource;

public class DemandCreationView extends OverflowComposite implements DemandCreationPresenter.CreationViewInterface {

    private static CreationViewUiBinder uiBinder = GWT.create(CreationViewUiBinder.class);
    interface CreationViewUiBinder extends UiBinder<Widget, DemandCreationView> {    }

    private static final Logger LOGGER = Logger.getLogger("    DemandCreationView");

    //step1
    @UiField StackLayoutPanel mainPanel;
    @UiField SimplePanel basicInfoHolder;
    //step2
//    @UiField VerticalPanel stepTwo;
    @UiField SimplePanel categoryHolder;
    //step3
//    @UiField VerticalPanel stepThree;
    @UiField SimplePanel localityHolder;
    //step4
//    @UiField VerticalPanel stepFour;
    @UiField SimplePanel advInfoHolder;
    //step5
//    @UiField VerticalPanel stepFive;
    @UiField DockLayoutPanel userFormPanel;
    @UiField SimplePanel userFormHolder;
    @UiField Button demandCreateBtn;

    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        StyleResource.INSTANCE.common().ensureInjected();
        setParentOverflow(basicInfoHolder, Overflow.AUTO);
        setParentOverflow(categoryHolder, Overflow.AUTO);
        setParentOverflow(localityHolder, Overflow.AUTO);
        setParentOverflow(advInfoHolder, Overflow.AUTO);
        setParentOverflow(userFormPanel, Overflow.AUTO);
        demandCreateBtn.addStyleName(StyleResource.INSTANCE.common().elemHiddenOn());
    }

    @Override
    public Widget getWidgetView() {
        return this;
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
    public SimplePanel getUserFormHolder() {
        return userFormHolder;
    }

    @Override
    public void toggleRegAndCreateButton() {
        LOGGER.fine(demandCreateBtn.getStylePrimaryName()
            + " VS " + StyleResource.INSTANCE.common().elemHiddenOn());
        if (demandCreateBtn.getStylePrimaryName().equals(StyleResource.INSTANCE.common().elemHiddenOn())) {
            demandCreateBtn.setStylePrimaryName(StyleResource.INSTANCE.common().elemHiddenOff());
        } else {
            demandCreateBtn.setStylePrimaryName(StyleResource.INSTANCE.common().elemHiddenOn());
        }
    }

    @Override
    public HasClickHandlers getCreateDemandButton() {
        return demandCreateBtn;
    }

}
