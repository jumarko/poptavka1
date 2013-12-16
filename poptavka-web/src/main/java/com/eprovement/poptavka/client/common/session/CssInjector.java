package com.eprovement.poptavka.client.common.session;

import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.resources.datagrid.DataGridResources;
import com.eprovement.poptavka.resources.pager.UniversalPagerResources;
import com.google.gwt.dom.client.StyleInjector;

/**
 * CssInjector manages styles injection.
 * Since GWT doesn't support fully @media query in css, a workaround must be used
 * to be able to have responsive design.
 * That idea of workaround rest in injecting responsive styles at the end of default style directly in code.
 * Since more modules require common styles, we don't know which one will be called first.
 * Therefore, each view should contains ensureInjected call to ensure appropriate css will be loaded.
 * But we need to avoid multiple injecting of responsive styles at the end each time ensureInjected is called.
 * There is a lot of code that must be written, where CssInjector comes handy :)
 * @author Martin Slavkovsky
 */
public final class CssInjector {

    public static final CssInjector INSTANCE = new CssInjector();
    private boolean isInitialLoaded;
    private boolean isLayoutLoaded;
    private boolean isHeaderLoaded;
    private boolean isCommonLoaded;
    private boolean isCreateTabPanelLoaded;
    private boolean isModalLoaded;
    private boolean isGridLoaded;
    private boolean isPagerLoaded;

    private CssInjector() {
    }

    /**
     * Inject <b>initial</b> styles.
     */
    public void ensureInitialStylesInjected() {
        if (!isInitialLoaded) {
            isInitialLoaded = true;
            StyleResource.INSTANCE.initial().ensureInjected();
            injectResponsiveStyles(
                StyleResource.INSTANCE.initialMiddle().getText(),
                StyleResource.INSTANCE.initialSmall().getText(),
                StyleResource.INSTANCE.initialTiny().getText());
        }
    }

    /**
     * Inject <b>layout</b> styles.
     */
    public void ensureLayoutStylesInjected() {
        if (!isLayoutLoaded) {
            isLayoutLoaded = true;
            StyleResource.INSTANCE.layout().ensureInjected();
            injectResponsiveStyles(
                StyleResource.INSTANCE.layoutMiddle().getText(),
                StyleResource.INSTANCE.layoutSmall().getText(),
                StyleResource.INSTANCE.layoutTiny().getText());
        }
    }

    /**
     * Inject <b>header</b> styles.
     */
    public void ensureHeaderStylesInjected() {
        if (!isHeaderLoaded) {
            isHeaderLoaded = true;
            StyleResource.INSTANCE.header().ensureInjected();
            injectResponsiveStyles(
                StyleResource.INSTANCE.headerMiddle().getText(),
                StyleResource.INSTANCE.headerSmall().getText(),
                null);
        }
    }

    /**
     * Inject <b>common</b> styles.
     */
    public void ensureCommonStylesInjected() {
        if (!isCommonLoaded) {
            isCommonLoaded = true;
            StyleResource.INSTANCE.common().ensureInjected();
            injectResponsiveStyles(
                StyleResource.INSTANCE.commonMidle().getText(),
                StyleResource.INSTANCE.commonSmall().getText(),
                StyleResource.INSTANCE.commonTiny().getText());
        }
    }

    /**
     * Inject <b>create tabe panel</b> styles.
     */
    public void ensureCreateTabPanelStylesInjected() {
        if (!isCreateTabPanelLoaded) {
            isCreateTabPanelLoaded = true;
            StyleResource.INSTANCE.createTabPanel().ensureInjected();
            injectResponsiveStyles(
                StyleResource.INSTANCE.createTabPanelMiddle().getText(),
                StyleResource.INSTANCE.createTabPanelSmall().getText(),
                StyleResource.INSTANCE.createTabPanelTiny().getText());
        }
    }

    /**
     * Inject <b>modal</b> styles.
     */
    public void ensureModalStylesInjected() {
        if (!isModalLoaded) {
            isModalLoaded = true;
            StyleResource.INSTANCE.modal().ensureInjected();
            injectResponsiveStyles(
                StyleResource.INSTANCE.modalMiddle().getText(),
                StyleResource.INSTANCE.modalSmall().getText(),
                StyleResource.INSTANCE.modalTiny().getText());
        }
    }

    /**
     * Inject <b>grid</b> styles.
     */
    public void ensureGridStylesInjected(DataGridResources gridResources) {
        if (!isGridLoaded) {
            isGridLoaded = true;
            //Main DataGrid Styles (DataGridStylesLarge is injected by grid itself
            injectResponsiveStyles(
                gridResources.dataGridStyleMiddle().getText(),
                gridResources.dataGridStyleSmall().getText(),
                gridResources.dataGridStyleTiny().getText());
        }
    }

    /**
     * Inject <b>pager</b> styles.
     */
    public void ensurePagerStylesInjected(UniversalPagerResources pagerResources) {
        if (!isPagerLoaded) {
            isPagerLoaded = true;
            injectResponsiveStyle(pagerResources.simplePagerStyleTiny().getText(), 480);
        }
    }
    /**
     * Inject responsive styles.
     *
     * @param style style for @media only screen and (max-width: 1200px)
     * @param maxWidth defining border resolution neede by @media
     */
    private void injectResponsiveStyle(String style, int maxWidth) {
        if (style != null && maxWidth > 0) {
            StyleInjector.injectAtEnd("@media only screen and (max-width: " + maxWidth + "px) {" + style + "}");
        }
    }

    /**
     * Inject responsive styles.
     *
     * @param middle style for @media only screen and (max-width: 1200px)
     * @param small style for @media only screen and (max-width: 767px)
     * @param tiny style for @media only screen and (max-width: 480px)
     */
    private void injectResponsiveStyles(String middle, String small, String tiny) {
        if (middle != null) {
            StyleInjector.injectAtEnd("@media only screen and (max-width: 1200px) {" + middle + "}");
        }
        if (small != null) {
            StyleInjector.injectAtEnd("@media only screen and (max-width: 767px) {" + small + "}");
        }
        if (tiny != null) {
            StyleInjector.injectAtEnd("@media only screen and (max-width: 480px) {" + tiny + "}");
        }
    }
}