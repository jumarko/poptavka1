package cz.poptavka.sample.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource.NotStrict;

import cz.poptavka.sample.client.resources.images.Images;
import cz.poptavka.sample.client.resources.richtext.RichTextStyle;

/**
*
* Main clientBundle interface. Used to aggregate all Resources
* (Data, Text, Image, Css)
* <br /><br />
* ClientBundle si very powerful
* <a href="http://code.google.com/webtoolkit/doc/latest/DevGuideClientBundle.html">ClientBundle at com.google.com</a>
*
* @author Beho
*
*/
public interface StyleResource extends ClientBundle {

    StyleResource INSTANCE = GWT.create(StyleResource.class);

    /**
     * Return CssStyle resource. Before use, call <code>css().ensureInjected();</code>
     * to load css from defined <code>@Source</code>
     *
     * @return CssStyle style
     */
    @Source("poptavka-base.css")
    PoptavkaBaseStyle cssBase();

    Images images();

    /**
     *
     * @return CssStyle for richTextToolbar
     */
    @Source("richtext/rich-text-toolbar.css")
    @NotStrict
    RichTextStyle richTextCss();
}
