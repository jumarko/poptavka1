/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.resources.tabPanel;

import com.google.gwt.resources.client.CssResource;

/**
 * Defines TabLayout styles.
 *
 * @author Jaro
 */
public interface CreateTabPanelStyles extends CssResource {

    @ClassName("height-registration-extended")
    String heightRegistrationExtended();

    @ClassName("height-registration")
    String heightRegistration();

    @ClassName("height-selector")
    String heightSelector();

    @ClassName("height-advanced")
    String heightAdvanced();

    @ClassName("height-basic")
    String heightBasic();

    @ClassName("createTabPanel")
    String createTabPanel();

    @ClassName("treeStepTabPanel")
    String treeStepTabPanel();

    @ClassName("fourStepTabPanel")
    String fourStepTabPanel();

    @ClassName("buttons-panel")
    String buttonsPanel();

    @ClassName("required-label")
    String requiredLabel();

    @ClassName("condition-panel")
    String conditionPanel();

}
