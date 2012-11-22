package com.eprovement.poptavka.client.root.footer;

import com.google.gwt.core.client.GWT;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.root.email.EmailDialogPopupPresenter;
import com.eprovement.poptavka.client.root.interfaces.IFooterView;
import com.eprovement.poptavka.client.root.interfaces.IFooterView.IFooterPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

@Presenter(view = FooterView.class)
public class FooterPresenter extends BasePresenter<IFooterView, RootEventBus>
        implements IFooterPresenter {

    public void onStart() {
        // TODO praso toto je zrejme staticky pohlad takze ho mozeme optimalizovat podla
        // http://mvp4g.blogspot.com/2011/04/mvp-pattern-associated-with-event-bus.html
        // part 2
        GWT.log("Footer presenter loaded");
        eventBus.setFooter(view);
    }

    @Override
    public void bind() {
        view.getContactUs().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.addHandler(EmailDialogPopupPresenter.class);
            }
        });
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    public void onSendUsEmail(int subjectId, String errorId) {
        eventBus.initEmailDialogPopup();
        eventBus.fillContactUsValues(subjectId, errorId);
    }
}
