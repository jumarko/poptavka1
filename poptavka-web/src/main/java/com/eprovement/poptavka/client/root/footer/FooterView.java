package com.eprovement.poptavka.client.root.footer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.client.root.interfaces.IFooterView;
import com.eprovement.poptavka.client.root.interfaces.IFooterView.IFooterPresenter;

public class FooterView extends ReverseCompositeView<IFooterPresenter>
        implements IFooterView {

    private static FooterViewUiBinder uiBinder = GWT
            .create(FooterViewUiBinder.class);

    interface FooterViewUiBinder extends UiBinder<Widget, FooterView> {
    }

    public FooterView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public FooterView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
