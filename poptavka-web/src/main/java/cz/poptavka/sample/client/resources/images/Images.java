package cz.poptavka.sample.client.resources.images;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;


public interface Images extends ClientBundle {

    @Source("star.png")
    ImageResource star();

    @Source("zoom-in.gif")
    ImageResource zoomIn();

    @Source("zoom-out.gif")
    ImageResource zoomOut();

    @Source("offers-open.png")
    ImageResource offerOpen();

    @Source("offers-opened.png")
    ImageResource offerOpened();

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

}
