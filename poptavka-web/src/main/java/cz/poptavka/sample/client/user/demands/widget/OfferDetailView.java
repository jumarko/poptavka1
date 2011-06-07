package cz.poptavka.sample.client.user.demands.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class OfferDetailView extends Composite {

    private static OfferDetailViewUiBinder uiBinder = GWT.create(OfferDetailViewUiBinder.class);

    interface OfferDetailViewUiBinder extends UiBinder<Widget, OfferDetailView> {
    }

    @UiField HTML message, company;
    @UiField Image image;

    public OfferDetailView() {
        initWidget(uiBinder.createAndBindUi(this));
    }





}
