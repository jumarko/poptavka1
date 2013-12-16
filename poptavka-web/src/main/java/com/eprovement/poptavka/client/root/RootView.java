package com.eprovement.poptavka.client.root;

import com.eprovement.poptavka.client.common.session.CssInjector;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.root.interfaces.IRootView;
import com.eprovement.poptavka.client.root.interfaces.IRootView.IRootPresenter;
import com.eprovement.poptavka.resources.StyleResource;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class RootView extends ReverseCompositeView<IRootPresenter> implements
        IRootView {

    private static RootViewUiBinder uiBinder = GWT.create(RootViewUiBinder.class);

    interface RootViewUiBinder extends UiBinder<Widget, RootView> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        StyleResource.INSTANCE.initialStandartStyles().ensureInjected();
        CssInjector.INSTANCE.ensureInitialStylesInjected();
        CssInjector.INSTANCE.ensureLayoutStylesInjected();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField ResizeLayoutPanel body;
    @UiField SimplePanel header, toolbar;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public RootView() {
        initWidget(uiBinder.createAndBindUi(this));
        //ResizeLayoutPanel uses strange styles, that interfere with ours. Therefore remove them.
        body.getElement().removeAttribute("style");
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    @Override
    public void setBody(IsWidget body) {
        GWT.log("Body widget view set");
        this.body.clear();
        this.body.add(body);

    }

    @Override
    public void setHeader(IsWidget header) {
        GWT.log("Header widget view set");
        this.header.add(header);

    }

    @Override
    public void setToolbar(IsWidget toolbar) {
        GWT.log("Toolbar widget view set");
        this.toolbar.add(toolbar);

    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public ResizeLayoutPanel getBody() {
        return body;
    }
}