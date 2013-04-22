package com.eprovement.poptavka.client.user.widget.detail;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.DemandRatingsDetail;
import com.github.gwtbootstrap.client.ui.Heading;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class RatingDetailView extends Composite {

    private static RatingDetailViewUiBinder uiBinder = GWT.create(RatingDetailViewUiBinder.class);

    interface RatingDetailViewUiBinder extends UiBinder<Widget, RatingDetailView> {
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField Label clientDisplayName, clientCommentDefault, clientCommentAdditional;
    @UiField Label supplierDisplayName, supplierCommentDefault, supplierCommentAdditional;
    @UiField Image clientStarImg, supplierStarImg;
    @UiField Heading clientHeading, supplierHeading;

    /**************************************************************************/
    /* INITIALIZATON                                                          */
    /**************************************************************************/
    //Constructors
    public RatingDetailView() {
        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.detailViews().ensureInjected();
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    public void setDemanRatingDetail(DemandRatingsDetail demandDetail) {
        setClientRatingDetail(demandDetail);
        setSupplierRatingDetail(demandDetail);
    }

    /**************************************************************************/
    /* HELPER METHODS                                                         */
    /**************************************************************************/
    private void setClientRatingDetail(DemandRatingsDetail demandDetail) {
        // TODO RELEASE martin - add display name (person first name + last name OR company name)
        clientDisplayName.setText("TODO name");
        clientStarImg.setVisible(demandDetail.getRatingClient() != null);
        if (demandDetail.getRatingClient() == null) {
            clientHeading.setText(Storage.MSGS.feedbackNotRated());
            clientCommentDefault.setText("");
        } else {
            setDefaultRateChoice(demandDetail.getRatingClient(),
                    clientStarImg, clientHeading, clientCommentDefault);
            clientCommentAdditional.setText(getAdditionalComment(demandDetail.getRatingClientMessage()));
        }
    }

    private void setSupplierRatingDetail(DemandRatingsDetail demandDetail) {
        // TODO RELEASE martin - add display name (person first name + last name OR company name)
        supplierDisplayName.setText("TODO name");
        supplierStarImg.setVisible(demandDetail.getRatingSupplier() != null);
        if (demandDetail.getRatingSupplier() == null) {
            supplierHeading.setText(Storage.MSGS.feedbackNotRated());
            supplierCommentDefault.setText("");
        } else {
            setDefaultRateChoice(demandDetail.getRatingSupplier(),
                    supplierStarImg, supplierHeading, supplierCommentDefault);
            supplierCommentAdditional.setText(getAdditionalComment(demandDetail.getRatingSupplierMessage()));
        }
    }

    private String getAdditionalComment(String wholeComment) {
        int idx = wholeComment.indexOf("Addition:");
        if (idx != -1) {
            return wholeComment.substring(idx + 9, wholeComment.length());
        }
        return "";
    }

    private void setDefaultRateChoice(int rating, Image image, Heading heading, Label comment) {
        image.setResource(Storage.RSCS.images().starGold());
        switch (rating) {
            case Constants.RATE_1:
                heading.setText(Storage.MSGS.feedbackHeading1());
                comment.setText(Storage.MSGS.feedbackComment1());
                break;
            case Constants.RATE_2:
                heading.setText(Storage.MSGS.feedbackHeading2());
                comment.setText(Storage.MSGS.feedbackComment2());
                break;
            case Constants.RATE_3:
                heading.setText(Storage.MSGS.feedbackHeading3());
                comment.setText(Storage.MSGS.feedbackComment3());
                break;
            case Constants.RATE_4:
                heading.setText(Storage.MSGS.feedbackHeading4());
                comment.setText(Storage.MSGS.feedbackComment4());
                break;
            case Constants.RATE_5:
                heading.setText(Storage.MSGS.feedbackHeading5());
                comment.setText(Storage.MSGS.feedbackComment5());
                break;
            default:
                break;
        }
    }
}
