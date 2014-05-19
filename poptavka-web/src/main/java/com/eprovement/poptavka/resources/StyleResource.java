/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.resources;

import com.eprovement.poptavka.resources.details.DetailModuleStyles;
import com.eprovement.poptavka.resources.modals.ModalStyles;
import com.eprovement.poptavka.resources.tabPanel.CreateTabPanelStyles;
import com.eprovement.poptavka.resources.layout.LayoutStyles;
import com.eprovement.poptavka.resources.common.CommonStyles;
import com.eprovement.poptavka.resources.initial.InitialStyles;
import com.eprovement.poptavka.resources.header.HeaderStyles;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.TextResource;

import com.eprovement.poptavka.resources.images.Images;
import com.eprovement.poptavka.resources.richtext.RichTextStyle;

/**
 *
 * Main clientBundle interface. Used to aggregate all Resources (Data, Text,
 * Image, Css) <br />
 * <br />
 * ClientBundle si very powerful <a href=
 * "http://code.google.com/webtoolkit/doc/latest/DevGuideClientBundle.html"
 * >ClientBundle at com.google.com</a>
 *
 * @author Beho
 *
 */
public interface StyleResource extends ClientBundle {

    StyleResource INSTANCE = GWT.create(StyleResource.class);

    /**
     * Return CssStyle resource. Before use, call
     * <code>css().ensureInjected();</code> to load css from defined
     * <code>@Source</code>
     *
     * @return CssStyle style
     */
    @NotStrict
    @Source("initial/InitialStylesLarge.css")
    InitialStyles initial();
    @NotStrict
    @Source("initial/InitialStylesMiddle.css")
    InitialStyles initialMiddle();
    @NotStrict
    @Source("initial/InitialStylesSmall.css")
    InitialStyles initialSmall();
    @NotStrict
    @Source("initial/InitialStylesTiny.css")
    InitialStyles initialTiny();

    @NotStrict
    @Source("header/HeaderStylesLarge.css")
    HeaderStyles header();
    @NotStrict
    @Source("header/HeaderStylesMiddle.css")
    HeaderStyles headerMiddle();
    @NotStrict
    @Source("header/HeaderStylesSmall.css")
    HeaderStyles headerSmall();
    @NotStrict
    @Source("header/HeaderStylesTiny.css")
    HeaderStyles headerTiny();

    @NotStrict
    @Source("layout/LayoutStylesLarge.css")
    LayoutStyles layout();
    @NotStrict
    @Source("layout/LayoutStylesMiddle.css")
    LayoutStyles layoutMiddle();
    @NotStrict
    @Source("layout/LayoutStylesSmall.css")
    LayoutStyles layoutSmall();
    @NotStrict
    @Source("layout/LayoutStylesTiny.css")
    LayoutStyles layoutTiny();

    @NotStrict
    @Source("common/CommonStylesLarge.css")
    CommonStyles common();
    @NotStrict
    @Source("common/CommonStylesMiddle.css")
    CommonStyles commonMidle();
    @NotStrict
    @Source("common/CommonStylesSmall.css")
    CommonStyles commonSmall();
    @NotStrict
    @Source("common/CommonStylesTiny.css")
    CommonStyles commonTiny();

    @NotStrict
    @Source("modals/ModalStylesLarge.css")
    ModalStyles modal();
    @NotStrict
    @Source("modals/ModalStylesMiddle.css")
    ModalStyles modalMiddle();
    @NotStrict
    @Source("modals/ModalStylesSmall.css")
    ModalStyles modalSmall();
    @NotStrict
    @Source("modals/ModalStylesTiny.css")
    ModalStyles modalTiny();

    @NotStrict
    @Source("tabPanel/CreateTabPanelStylesLarge.css")
    CreateTabPanelStyles createTabPanel();
    @NotStrict
    @Source("tabPanel/CreateTabPanelStylesMiddle.css")
    CreateTabPanelStyles createTabPanelMiddle();
    @NotStrict
    @Source("tabPanel/CreateTabPanelStylesSmall.css")
    CreateTabPanelStyles createTabPanelSmall();
    @NotStrict
    @Source("tabPanel/CreateTabPanelStylesTiny.css")
    CreateTabPanelStyles createTabPanelTiny();

    @NotStrict
    @Source("details/DetailModuleStylesLarge.css")
    DetailModuleStyles details();
    @NotStrict
    @Source("details/DetailModuleStylesMiddle.css")
    DetailModuleStyles detailsMiddle();
    @NotStrict
    @Source("details/DetailModuleStylesSmall.css")
    DetailModuleStyles detailsSmall();

    @NotStrict
    @Source("standartStyles-initial.css")
    StandartStyles initialStandartStyles();

    @NotStrict
    @Source("standartStyles.css")
    StandartStyles standartStyles();

    Images images();

    /**
     *
     * @return CssStyle for richTextToolbar
     */
    @Source("richtext/rich-text-toolbar.css")
    @NotStrict
    RichTextStyle richTextCss();

    /** Text Resource **/
    @Source("text/conditions.txt")
    TextResource conditions();
}
