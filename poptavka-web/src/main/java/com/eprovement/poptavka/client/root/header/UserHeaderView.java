package com.eprovement.poptavka.client.root.header;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.client.root.interfaces.IUserHeaderView;
import com.eprovement.poptavka.client.root.interfaces.IUserHeaderView.IUserHeaderPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class UserHeaderView extends ReverseCompositeView<IUserHeaderPresenter>
        implements IUserHeaderView {

    private static UserHeaderViewUiBinder uiBinder = GWT
            .create(UserHeaderViewUiBinder.class);

    interface UserHeaderViewUiBinder extends UiBinder<Widget, UserHeaderView> {
    }
    /** login area **/
    @UiField HTMLPanel headerHolder;
    @UiField PushButton pushButton;
    @UiField Label newMessagesCount;
    @UiField PushButton pushSystemButton;
    @UiField Label newSystemMessagesCount;
    @UiField MenuItem username, menuLogOut, menuMyProfile;
    @UiField MyMenuItem logoutMenuItemBtn;
    @UiField MenuBar logoutMenuBarBtn;

    public UserHeaderView() {
        initWidget(uiBinder.createAndBindUi(this));
        bindHandlers();
    }

    public UserHeaderView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
        bindHandlers();
    }

    private void bindHandlers() {
        logoutMenuBarBtn.addDomHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                logoutMenuItemBtn.setStyleName(Storage.RSCS.layout().logoutButtonAct());
            }
        }, ClickEvent.getType());
        logoutMenuBarBtn.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                logoutMenuItemBtn.setStyleName(Storage.RSCS.layout().logoutButton());
            }
        });
    }

    @Override
    public MenuItem getUsername() {
        return username;
    }

    @Override
    public MenuItem getMenuLogOut() {
        return menuLogOut;
    }

    @Override
    public MenuItem getMenuMyProfile() {
        return menuMyProfile;
    }

    /**
     * @return the newMessagesCount
     */
    @Override
    public Label getNewMessagesCount() {
        return newMessagesCount;
    }

    /**
     * @return the newSystemMessagesCount
     */
    @Override
    public Label getNewSystemMessagesCount() {
        return newSystemMessagesCount;
    }

    /**
     * @return the pushButton
     */
    @Override
    public PushButton getPushButton() {
        return pushButton;
    }

    /**
     * @return the pushSystemButton
     */
    @Override
    public PushButton getPushSystemButton() {
        return pushSystemButton;
    }
}
