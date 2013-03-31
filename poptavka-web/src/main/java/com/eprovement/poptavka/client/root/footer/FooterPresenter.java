package com.eprovement.poptavka.client.root.footer;

import com.eprovement.poptavka.client.common.session.Constants;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.root.interfaces.IFooterView;
import com.eprovement.poptavka.client.root.interfaces.IFooterView.IFooterPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import java.util.Date;

@Presenter(view = FooterView.class)
public class FooterPresenter extends BasePresenter<IFooterView, RootEventBus>
        implements IFooterPresenter {

    @Override
    public void bind() {
        view.getContactUs().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.sendUsEmail(Constants.SUBJECT_REPORT_ISSUE, (new Date()).toString());
            }
        });
    }
}
