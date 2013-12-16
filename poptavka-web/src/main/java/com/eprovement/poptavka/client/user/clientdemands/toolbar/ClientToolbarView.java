package com.eprovement.poptavka.client.user.clientdemands.toolbar;

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
 * @author Mato
 */
@Singleton
public class ClientToolbarView extends Composite implements IsWidget {

    private static ClientToolbarViewUiBinder uiBinder = GWT.create(ClientToolbarViewUiBinder.class);

    interface ClientToolbarViewUiBinder extends UiBinder<Widget, ClientToolbarView> {
    }

    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    /** UiBinder attribute. **/
    @UiField Button backBtn, editBtn, deleteBtn, acceptBtn, closeBtn;
    @UiField SimplePanel actionBox;
    @UiField(provided = true) UniversalPagerWidget pager;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public ClientToolbarView() {
        pager = new UniversalPagerWidget();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    public void bindPager(UniversalAsyncGrid table) {
        pager.setDisplay(table);
    }

    public void setEditDemandBtnsVisibility(boolean visibility) {
        editBtn.setVisible(visibility);
        deleteBtn.setVisible(visibility);
    }

    /**
     * Hides items except submenu title and pager.
     */
    public void resetBasic() {
        backBtn.setVisible(false);
        editBtn.setVisible(false);
        deleteBtn.setVisible(false);
        acceptBtn.setVisible(false);
        closeBtn.setVisible(false);
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
    public Button getBackBtn() {
        return backBtn;
    }

    public Button getEditBtn() {
        return editBtn;
    }

    public Button getDeleteBtn() {
        return deleteBtn;
    }

    public Button getAcceptBtn() {
        return acceptBtn;
    }

    public Button getCloseBtn() {
        return closeBtn;
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
