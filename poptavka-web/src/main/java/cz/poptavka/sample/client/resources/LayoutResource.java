package cz.poptavka.sample.client.resources;

import com.google.gwt.resources.client.CssResource;

/**
 *
 * GWT Wrapper for base app layout.
 *
 * @author Beho
 *
 */
public interface LayoutResource extends CssResource {

    /** MainView.class **/
    @ClassName("layout-public-main")
    String layoutPublic();
    @ClassName("layout-user-main")
    String layoutUser();
    @ClassName("header-container")
    String headerContainer();
    @ClassName("footer-container")
    String footerContainer();
    @ClassName("body-container")
    String bodyContainer();
    @ClassName("login-area")
    String loginArea();

    /** UserView.class **/
    @ClassName("full-size-panel")
    String fullSize();


    @ClassName("main-menu-container")
    String mainMenuContainer();

    @ClassName("side-menu-container")
    String sideMenuContainer();



    @ClassName("max-size")
    String maxSize();

    @ClassName("closed")
    String closedWidget();



    @ClassName("creation-hidden")
    String creationHidden();

    @ClassName("creation-second")
    String displaySecondPart();

    @ClassName("creation-third")
    String displayThirdPart();

    @ClassName("creation-fourth")
    String displayFourthPart();

    @ClassName("solid-back")
    String solidBackground();



    @ClassName("error-field")
    String errorField();

    @ClassName("resizeIt")
    String resize();

}
