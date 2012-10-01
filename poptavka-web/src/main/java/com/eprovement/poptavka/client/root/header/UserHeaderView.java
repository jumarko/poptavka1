package com.eprovement.poptavka.client.root.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.client.root.interfaces.IUserHeaderView;
import com.eprovement.poptavka.client.root.interfaces.IUserHeaderView.IUserHeaderPresenter;

public class UserHeaderView extends ReverseCompositeView<IUserHeaderPresenter>
        implements IUserHeaderView {

    private static UserHeaderViewUiBinder uiBinder = GWT.create(UserHeaderViewUiBinder.class);

    interface UserHeaderViewUiBinder extends UiBinder<Widget, UserHeaderView> {
    }

    public UserHeaderView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /** login area **/
    @UiField
    Anchor logoutButton;
    @UiField
    HTMLPanel headerHolder;

    public UserHeaderView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Anchor getLogoutLink() {
        return logoutButton;
    }

}
