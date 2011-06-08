package cz.poptavka.sample.client.user.demands.tab;

import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

import cz.poptavka.sample.client.main.common.OverflowComposite;
import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.user.demands.widget.table.PotentialDemandTable;
import cz.poptavka.sample.shared.domain.message.PotentialMessageDetail;

/**
 * View representing potential demands for supplier. Supplier can list them,
 * reply to selected, cancel displaying and other magic tricks
 *
 * @author Beho
 *
 */
public class PotentialDemandsView extends OverflowComposite implements
        PotentialDemandsPresenter.IPotentialDemands {

    private static PotentialDemandsViewUiBinder uiBinder = GWT
            .create(PotentialDemandsViewUiBinder.class);

    private static final StyleResource RSCS = GWT.create(StyleResource.class);

    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    interface PotentialDemandsViewUiBinder extends
            UiBinder<Widget, PotentialDemandsView> {
    }

    @UiField(provided = true) PotentialDemandTable demandTable;
    @UiField(provided = true) SimplePager demandTablePager;
    @UiField
    Button replyBtn, deleteBtn, moreActionsBtn, refreshBtn;
    //working buttons
    @UiField Button markReadBtn, markUnreadBtn;

    @UiField
    SimplePanel detailSection;

    // TODO remove then
    @UiField
    SimplePanel develPanel;

    @Override
    public void createView() {
        demandTable = new PotentialDemandTable(MSGS, RSCS);
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        demandTablePager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        demandTablePager.setDisplay(demandTable);

        initWidget(uiBinder.createAndBindUi(this));
    }


    @Override
    public Widget getWidgetView() {
        return this;
    }
    @Override
    public SimplePanel getDetailSection() {
        return detailSection;
    }

    /** table related methods **/
    @Override
    public PotentialDemandTable getDemandTable() {
        return demandTable;
    }

    @Override
    public MultiSelectionModel<PotentialMessageDetail> getSelectionModel() {
        return (MultiSelectionModel<PotentialMessageDetail>) demandTable.getSelectionModel();
    }

    @Override
    public Set<PotentialMessageDetail> getSelectedSet() {
        return getSelectionModel().getSelectedSet();
    }

    @Override
    public ListDataProvider<PotentialMessageDetail> getDataProvider() {
        return demandTable.getDataProvider();
    }

    /** view buttons **/
    @Override
    public Button getReplyButton() {
        return replyBtn;
    }

    @Override
    public Button getDeleteButton() {
        return deleteBtn;
    }

    @Override
    public Button getActionButton() {
        return moreActionsBtn;
    }

    @Override
    public Button getRefreshButton() {
        return refreshBtn;
    }

    @Override
    public Button getMarkReadButton() {
        return markReadBtn;
    }

    @Override
    public Button getMarkUnreadButton() {
        return markUnreadBtn;
    }

}
