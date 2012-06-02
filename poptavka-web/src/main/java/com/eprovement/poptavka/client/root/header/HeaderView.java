package com.eprovement.poptavka.client.root.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.client.root.interfaces.IHeaderView;
import com.eprovement.poptavka.client.root.interfaces.IHeaderView.IHeaderPresenter;

public class HeaderView extends ReverseCompositeView<IHeaderPresenter>
        implements IHeaderView {

    private static HeaderViewUiBinder uiBinder = GWT.create(HeaderViewUiBinder.class);

    interface HeaderViewUiBinder extends UiBinder<Widget, HeaderView> {
    }

    public HeaderView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /** login area **/
    @UiField
    Anchor loginButton;
    @UiField
    HTMLPanel headerHolder;

    public HeaderView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Anchor getLoginLink() {
        return loginButton;
    }

}
