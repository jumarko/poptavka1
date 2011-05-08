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


}
