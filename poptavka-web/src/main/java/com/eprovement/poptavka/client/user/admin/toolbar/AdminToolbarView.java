package com.eprovement.poptavka.client.user.admin.toolbar;

import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 *
 * @author Martin Slavkovsky
 */
@Singleton
public class AdminToolbarView extends Composite implements IsWidget {

    private static AdminToolbarViewUiBinder uiBinder = GWT.create(AdminToolbarViewUiBinder.class);

    interface AdminToolbarViewUiBinder extends UiBinder<Widget, AdminToolbarView> {
    }
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    /** UiBinder attribute. **/
    @UiField Button approveBtn, createConversationBtn;
    @UiField SimplePanel actionBox;
    @UiField(provided = true) UniversalPagerWidget pager;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public AdminToolbarView() {
        pager = new UniversalPagerWidget();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    public void bindPager(UniversalAsyncGrid table) {
        pager.setDisplay(table);
    }

    /**
     * Hides items except submenu title and pager.
     */
    public void resetBasic() {
        approveBtn.setVisible(false);
        createConversationBtn.setVisible(false);
        actionBox.setVisible(false);
    }

    /**
     * Hides all items except submenu title.
     */
    public void resetFull() {
        resetBasic();
        pager.setVisible(false);
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public Button getApproveBtn() {
        return approveBtn;
    }

    public Button getCreateConversationBtn() {
        return createConversationBtn;
    }

    public SimplePanel getActionBox() {
        return actionBox;
    }

    public UniversalPagerWidget getPager() {
        return pager;
    }

    public Widget getWdigetView() {
        return this;
    }
}