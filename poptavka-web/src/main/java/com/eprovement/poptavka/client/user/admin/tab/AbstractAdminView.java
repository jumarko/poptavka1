package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.admin.tab.AbstractAdminPresenter.IAbstractAdminView;
import com.eprovement.poptavka.client.user.admin.toolbar.AdminToolbarView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.inject.Inject;
import java.util.Set;

public class AbstractAdminView extends Composite implements IAbstractAdminView {

    private static AbstractAdminViewUiBinder uiBinder = GWT.create(AbstractAdminViewUiBinder.class);

    interface AbstractAdminViewUiBinder extends UiBinder<Widget, AbstractAdminView> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        //for change monitors
        Storage.RSCS.common().ensureInjected();
        //for popups created of image hover in datagrid
        Storage.RSCS.modal().ensureInjected();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) UniversalAsyncGrid table;
    @UiField SimplePanel footerContainer, detailPanel;
    /** Class attributes. **/
    @Inject
    protected AdminToolbarView toolbar;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * This is called before createView because it is called from createPresenter,
     * which is called before createView (See: https://code.google.com/p/mvp4g/wiki/Mvp4gOptimization).
     * It is done this way, because tables are provided form presenter and binding table events
     * must by afterwards they are created. And View by default is initializated before presenter,
     * therefore we need to use LazyView, but in that case
     * we are not able to pass tables as arguments while creating view.
     * @param parentTable
     * @param table
     */
    @Override
    public void initTable(UniversalAsyncGrid table) {
        this.table = table;
        this.toolbar.getPager().setVisible(true);
        this.toolbar.bindPager(this.table);
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
//    @Override
//    public List<Long> getSelectedUserMessageIds() {
//        List<Long> idList = new ArrayList<Long>();
//        Set<TableDisplayUserMessage> set = getSelectedObjects();
//        Iterator<TableDisplayUserMessage> it = set.iterator();
//        while (it.hasNext()) {
//            idList.add(it.next().getUserMessageId());
//        }
//        return idList;
//    }

    @Override
    public Set getSelectedObjects() {
        MultiSelectionModel model = (MultiSelectionModel) table.getSelectionModel();
        return model.getSelectedSet();
    }

    @Override
    public UniversalAsyncGrid getTable() {
        return table;
    }

    @Override
    public SimplePanel getFooterContainer() {
        return footerContainer;
    }

    @Override
    public SimplePanel getDetailPanel() {
        return detailPanel;
    }

    @Override
    public AdminToolbarView getToolbar() {
        return toolbar;
    }

    @Override
    public Widget getWidgeView() {
        return this;
    }
}