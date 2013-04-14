package com.eprovement.poptavka.resources.images;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Images extends ClientBundle {

    @Source("zoom-in.gif")
    ImageResource zoomIn();

    @Source("zoom-out.gif")
    ImageResource zoomOut();

    @Source("btn-center.png")
    ImageResource showMiddle();

    @Source("btn-center2.png")
    ImageResource showMiddleLeft();

    @Source("btn-down.png")
    ImageResource showDown();

    @Source("btn-up.png")
    ImageResource showUp();

    @Source("sort-down.png")
    ImageResource sortDown();

    @Source("sort-up.png")
    ImageResource sortUp();

    @Source("accept-icon.png")
    ImageResource acceptIcon();

    @Source("accept-icon24.png")
    ImageResource acceptIcon24();

    @Source("accept-icon16.png")
    ImageResource acceptIcon16();

    @Source("info-icon.png")
    ImageResource infoIcon();

    @Source("info-icon24.png")
    ImageResource infoIcon24();

    @Source("info-icon16.png")
    ImageResource infoIcon16();

    @Source("delete-icon.png")
    ImageResource errorIcon();

    @Source("delete-icon24.png")
    ImageResource errorIcon24();

    @Source("delete-icon16.png")
    ImageResource errorIcon16();

    @Source("loader24.gif")
    ImageResource loadIcon24();

    @Source("loader32.gif")
    ImageResource loadIcon32();

    @Source("loader-icon33.gif")
    ImageResource loaderIcon33();

    @Source("star-green.png")
    ImageResource starGreen();

    @Source("star-silver.png")
    ImageResource starSilver();

    @Source("star-header.png")
    ImageResource starHeader();

    //In displaying demands
    @Source("urgency-red.png")
    ImageResource urgencyRed();

    @Source("urgency-orange.png")
    ImageResource urgencyOrange();

    @Source("urgency-green.png")
    ImageResource urgencyGreen();

    @Source("urgency-header.png")
    ImageResource urgencyHeader();

    @Source("new.png")
    ImageResource newDemand();

    @Source("temporary.png")
    ImageResource temporary();

    @Source("time.png")
    ImageResource time();

    @Source("time-ok.png")
    ImageResource timeOk();

    @Source("time-short.png")
    ImageResource timeShort();

    @Source("toggleClosed.png")
    ImageResource toggleClosed();

    @Source("toggleOpen.png")
    ImageResource toggleOpen();

    @Source("status-work.png")
    ImageResource statusWork();

    @Source("ctrl_mouseLeft.gif")
    ImageResource ctrlMouseLeft();

    //Table buttons
    @Source("reply.png")
    ImageResource replyImage();

    @Source("accept-icon16.png")
    ImageResource acceptOfferImage();

    @Source("decline.png")
    ImageResource declineOfferImage();

    @Source("close.png")
    ImageResource closeDemandImage();

    @Source("send16.png")
    ImageResource sendOfferImage();

    @Source("edit.png")
    ImageResource editOfferImage();

    @Source("download.png")
    ImageResource downloadOfferImage();

    @Source("done.png")
    ImageResource finnishedImage();

    @Source("envelope.png")
    ImageResource envelopeImage();

    @Source("envelope-empty.png")
    ImageResource envelopeImageEmpty();

    @Source("envelope-hover.png")
    ImageResource envelopeHoverImage();

    @Source("flag.png")
    ImageResource flagImage();

    @Source("flag-hover.png")
    ImageResource flagHoverImage();

    @Source("flag-empty.png")
    ImageResource flagImageEmpty();

    @Source("contact.jpg")
    ImageResource contactImage();

    @Source("star-gold.png")
    ImageResource starGold();
}
