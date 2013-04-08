package com.eprovement.poptavka.resources;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.ClassName;
/**
 *
 * @author Archo
 */
public interface ModalStyles extends CssResource {

    /** Login Modal Styles **/
    @ClassName("login-modal")
    String loginModal();

    /* Loader Modal Styles */
    @ClassName("loader-modal")
    String loaderModal();

    /* Loader Modal Styles */
    @ClassName("advanced-search-modal")
    String advancedSearchModal();

    /* Loader Modal Styles */
    @ClassName("feedback-modal")
    String feedbackModal();
}
