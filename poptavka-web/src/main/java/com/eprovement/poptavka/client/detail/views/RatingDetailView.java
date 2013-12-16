/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.detail.views;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.FullRatingDetail;
import com.github.gwtbootstrap.client.ui.Heading;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Rating detail view for displaying data from RatingDemandDetail objects.
 * @author Mato
 */
public class RatingDetailView extends Composite {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static RatingDetailViewUiBinder uiBinder = GWT.create(RatingDetailViewUiBinder.class);

    interface RatingDetailViewUiBinder extends UiBinder<Widget, RatingDetailView> {
    }

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField Label clientDisplayName, clientCommentDefault, clientCommentAdditional;
    @UiField Label supplierDisplayName, supplierCommentDefault, supplierCommentAdditional;
    @UiField Label demandDescription;
    @UiField Heading clientHeading, supplierHeading;
    @UiField HTMLPanel clientStar1, clientStar2, clientStar3, clientStar4, clientStar5;
    @UiField HTMLPanel supplierStar1, supplierStar2, supplierStar3, supplierStar4, supplierStar5;
    /** Constants. **/
    private static final String STAR_GOLD = "gold-star";
    private static final String STAR_GREY = "grey-star";

    /**************************************************************************/
    /* INITIALIZATON                                                          */
    /**************************************************************************/
    /**
     * Creates rating detail view's compontents.
     */
    public RatingDetailView() {
        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.details().ensureInjected();
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    /**
     * Sets rating detail's data.
     * @param ratingDetail to be set
     */
    public void setRatingDetail(FullRatingDetail ratingDetail) {
        demandDescription.setText(ratingDetail.getDemandDescription());
        setClientFullRatingDetail(ratingDetail);
        setSupplierFullRatingDetail(ratingDetail);
    }

    /**************************************************************************/
    /* HELPER METHODS                                                         */
    /**************************************************************************/
    /**
     * Sets rating detail's data - <b>client</b>'s part
     * @param ratingDetail to be set
     */
    private void setClientFullRatingDetail(FullRatingDetail ratingDetail) {
        clientDisplayName.setText(ratingDetail.getClientName());
        if (ratingDetail.getRatingClient() == null) {
            clientHeading.setText(Storage.MSGS.feedbackNotRated());
            clientCommentDefault.setText("");
        } else {
            setClientRate(
                    ratingDetail.getRatingClient(),
                    getAdditionalComment(ratingDetail.getRatingClientMessage()));
        }
    }

    /**
     * Sets rating detail's data - <b>supplier</b>'s part
     * @param ratingDetail to be set
     */
    private void setSupplierFullRatingDetail(FullRatingDetail demandDetail) {
        supplierDisplayName.setText(demandDetail.getSupplierName());
        if (demandDetail.getRatingSupplier() == null) {
            supplierHeading.setText(Storage.MSGS.feedbackNotRated());
            supplierCommentDefault.setText("");
        } else {
            setSupplierRate(
                    demandDetail.getRatingSupplier(),
                    getAdditionalComment(demandDetail.getRatingSupplierMessage()));
        }
    }

    /**
     * Sets <b>client</b>'s rating details.
     * @param rating of client
     * @param additionalComment of rating
     */
    private void setClientRate(int rating, String additionalComment) {
        clientCommentAdditional.setText(additionalComment);
        switch (rating) {
            case Constants.RATE_1:
                clientHeading.setText(Storage.MSGS.feedbackHeading1());
                clientCommentDefault.setText(Storage.MSGS.feedbackComment1());
                setClientStarGoldStyles(true, true, true, true, true);
                break;
            case Constants.RATE_2:
                clientHeading.setText(Storage.MSGS.feedbackHeading2());
                clientCommentDefault.setText(Storage.MSGS.feedbackComment2());
                setClientStarGoldStyles(true, true, true, true, false);
                break;
            case Constants.RATE_3:
                clientHeading.setText(Storage.MSGS.feedbackHeading3());
                clientCommentDefault.setText(Storage.MSGS.feedbackComment3());
                setClientStarGoldStyles(true, true, true, false, false);
                break;
            case Constants.RATE_4:
                clientHeading.setText(Storage.MSGS.feedbackHeading4());
                clientCommentDefault.setText(Storage.MSGS.feedbackComment4());
                setClientStarGoldStyles(true, true, false, false, false);
                break;
            case Constants.RATE_5:
                clientHeading.setText(Storage.MSGS.feedbackHeading5());
                clientCommentDefault.setText(Storage.MSGS.feedbackComment5());
                setClientStarGoldStyles(true, false, false, false, false);
                break;
            default:
                break;
        }
    }

    /**
     * Sets <b>suppliers</b>'s rating details.
     * @param rating of supplier
     * @param additionalComment of rating
     */
    private void setSupplierRate(int rating, String additionalComment) {
        supplierCommentAdditional.setText(additionalComment);
        switch (rating) {
            case Constants.RATE_1:
                supplierHeading.setText(Storage.MSGS.feedbackHeading1());
                supplierCommentDefault.setText(Storage.MSGS.feedbackComment1());
                setSupplierStarGoldStyles(true, true, true, true, true);
                break;
            case Constants.RATE_2:
                supplierHeading.setText(Storage.MSGS.feedbackHeading2());
                supplierCommentDefault.setText(Storage.MSGS.feedbackComment2());
                setSupplierStarGoldStyles(true, true, true, true, false);
                break;
            case Constants.RATE_3:
                supplierHeading.setText(Storage.MSGS.feedbackHeading3());
                supplierCommentDefault.setText(Storage.MSGS.feedbackComment3());
                setSupplierStarGoldStyles(true, true, true, false, false);
                break;
            case Constants.RATE_4:
                supplierHeading.setText(Storage.MSGS.feedbackHeading4());
                supplierCommentDefault.setText(Storage.MSGS.feedbackComment4());
                setSupplierStarGoldStyles(true, true, false, false, false);
                break;
            case Constants.RATE_5:
                supplierHeading.setText(Storage.MSGS.feedbackHeading5());
                supplierCommentDefault.setText(Storage.MSGS.feedbackComment5());
                setSupplierStarGoldStyles(true, false, false, false, false);
                break;
            default:
                break;
        }
    }

    /**
     * Sets <b>client</b>'s gold stars.
     * @param star1 true if <b>first<\b> star is gold.
     * @param star2 true if <b>second<\b> star is gold.
     * @param star3 true if <b>third<\b> star is gold.
     * @param star4 true if <b>fourth<\b> star is gold.
     * @param star5 true if <b>fifth<\b> star is gold.
     */
    private void setClientStarGoldStyles(boolean star1, boolean star2,
            boolean star3, boolean star4, boolean star5) {
        clientStar1.setStyleName(star1 ? STAR_GOLD : STAR_GREY);
        clientStar2.setStyleName(star2 ? STAR_GOLD : STAR_GREY);
        clientStar3.setStyleName(star3 ? STAR_GOLD : STAR_GREY);
        clientStar4.setStyleName(star4 ? STAR_GOLD : STAR_GREY);
        clientStar5.setStyleName(star5 ? STAR_GOLD : STAR_GREY);
    }

    /**
     * Sets <b>supplier</b>'s gold stars.
     * @param star1 true if <b>first<\b> star is gold.
     * @param star2 true if <b>second<\b> star is gold.
     * @param star3 true if <b>third<\b> star is gold.
     * @param star4 true if <b>fourth<\b> star is gold.
     * @param star5 true if <b>fifth<\b> first star is gold.
     */
    private void setSupplierStarGoldStyles(boolean star1, boolean star2,
            boolean star3, boolean star4, boolean star5) {
        supplierStar1.setStyleName(star1 ? STAR_GOLD : STAR_GREY);
        supplierStar2.setStyleName(star2 ? STAR_GOLD : STAR_GREY);
        supplierStar3.setStyleName(star3 ? STAR_GOLD : STAR_GREY);
        supplierStar4.setStyleName(star4 ? STAR_GOLD : STAR_GREY);
        supplierStar5.setStyleName(star5 ? STAR_GOLD : STAR_GREY);
    }

    /**
     * Extracts additional comment from given comment.
     * @param wholeComment - given comment
     * @return additional comment part
     */
    private String getAdditionalComment(String wholeComment) {
        int idx = wholeComment.indexOf("Addition:");
        if (idx != -1) {
            return wholeComment.substring(idx + 9, wholeComment.length());
        }
        return "";
    }
}