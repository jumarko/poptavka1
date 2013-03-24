package com.eprovement.poptavka.resources;

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
    @Source("poptavka-base.css")
    LayoutResource layout();

    @NotStrict
    @Source("common.css")
    CommonResource common();

    @Source("table.css")
    TableStyle table();

    @Source("gridTable.css")
    GridTableResources grid();

    @NotStrict
    @Source("tabLayout.css")
    TabLayout tabLayout();

    @NotStrict
    @Source("createTabPanel.css")
    CreateTabPanel createTabPanel();

    @NotStrict
    @Source("detailTabPanel.css")
    DetailTabPanel detailTabPanel();

    @NotStrict
    @Source("advancedSearchTabPanel.css")
    AdvancedSearchTabPanel advancedSearchTabPanel();

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

    @Source("mailBox.css")
    MailBox mailBox();

    @NotStrict
    @Source("detailViews.css")
    DetailViews detailViews();
}
