/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.resources.initial;

import com.google.gwt.resources.client.CssResource;

/**
 * Defines styles that are needed at the app startup.
 *
 * @author Jaro
 */
public interface InitialStyles extends CssResource {

    /** MainView.class **/
    @ClassName("homepage")
    String homepage(); //HomeWelcome

    @ClassName("root-category-cell")
    String rootCategoryCell(); //HomeWelcome

    @ClassName("scroll-container")
    String scrollContainer(); //HomeWelcome

    /** Body **/
    /** Tool Bar Styles **/
    @ClassName("toolbar")
    String toolbar();

    /** Footer Module Styles **/
    @ClassName("footer")
    String footer();

    @ClassName("footer-toggle")
    String footerToggle();

    /* Loader Modal Styles */
    @ClassName("loader-modal")
    String loaderModal();

    /**
     * Common Responsive
     */
    @ClassName("hide-small")
    String hideOnSmall();

    @ClassName("expand-small")
    String expandOnSmall();

    @ClassName("half-expand-small")
    String expandHalfOnSmall();

}
