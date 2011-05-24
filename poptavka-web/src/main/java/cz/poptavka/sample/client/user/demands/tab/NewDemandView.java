package cz.poptavka.sample.client.user.demands.tab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.main.common.OverflowComposite;
import cz.poptavka.sample.client.main.common.StatusIconLabel;
import cz.poptavka.sample.client.resources.StyleResource;

public class NewDemandView extends OverflowComposite implements NewDemandPresenter.NewDemandInterface {

    private static NewDemandViewUiBinder uiBinder = GWT.create(NewDemandViewUiBinder.class);
    interface NewDemandViewUiBinder extends UiBinder<Widget, NewDemandView> {
    }

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    private List<StatusIconLabel> statusLabels = new ArrayList<StatusIconLabel>();

    private List<SimplePanel> holderPanels = new ArrayList<SimplePanel>();

    @UiField StackLayoutPanel mainPanel;

    @UiField SimplePanel basicInfoHolder, categoryHolder, localityHolder, advInfoHolder;
    @UiField StatusIconLabel basicStatus, categoryStatus, localityStatus, advStatus;

    @UiField Button createBtn;

    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        /** filling status list **/
        StatusIconLabel[] array = {basicStatus, categoryStatus, localityStatus, advStatus};
        statusLabels = Arrays.asList(array);

        SimplePanel[] panels = {basicInfoHolder, categoryHolder, localityHolder, advInfoHolder};
        holderPanels  = Arrays.asList(panels);

        /** style implementation and overflow tweaks **/
        StyleResource.INSTANCE.common().ensureInjected();
        for (SimplePanel panel : holderPanels) {
            setParentOverflow(panel, Overflow.AUTO);
        }
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public HasClickHandlers getCreateDemandButton() {
        return createBtn;
    }

    @Override
    public StackLayoutPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public StatusIconLabel getStatusLabel(int order) {
        return statusLabels.get(order - 1);
    }

    @Override
    public SimplePanel getHolderPanel(int order) {
        return holderPanels.get(order - 1);
    }

}
