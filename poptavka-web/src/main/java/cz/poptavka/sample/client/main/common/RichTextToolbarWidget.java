package cz.poptavka.sample.client.main.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RichTextArea.Formatter;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.resources.richtext.RichTextStrings;

import java.util.HashMap;

public class RichTextToolbarWidget extends Composite implements HasValue {
    /** Local CONSTANTS **/
    //ImageMap and CSS related
    private static final String ICONS = "images/rich_text_icons.gif";
        //"http://blog.elitecoderz.net/wp-includes/js/tinymce/themes/advanced/img/icons.gif";
    private static RichTextStrings msgs = GWT.create(RichTextStrings.class);

    //Color and Fontlists - First Value (key) is the Name to display, Second Value (value) is the HTML-Definition
    public final static HashMap<String, String> GUI_COLORLIST = new HashMap<String, String>();
    static {
        GUI_COLORLIST.put(msgs.white(), "#FFFFFF");
        GUI_COLORLIST.put(msgs.black(), "#000000");
        GUI_COLORLIST.put(msgs.red(), "red");
        GUI_COLORLIST.put(msgs.green(), "green");
        GUI_COLORLIST.put(msgs.yellow(), "yellow");
        GUI_COLORLIST.put(msgs.blue(), "blue");
    }
    public final static HashMap<String, String> GUI_FONTLIST = new HashMap<String, String>();
    static {
        GUI_FONTLIST.put("Times New Roman", "Times New Roman");
        GUI_FONTLIST.put("Arial", "Arial");
        GUI_FONTLIST.put("Courier New", "Courier New");
        GUI_FONTLIST.put("Georgia", "Georgia");
        GUI_FONTLIST.put("Trebuchet", "Trebuchet");
        GUI_FONTLIST.put("Verdana", "Verdana");
    }

    //HTML Related (styles made by SPAN and DIV)
    private static final String HTML_STYLE_CLOSE_SPAN = "</span>";
    private static final String HTML_STYLE_CLOSE_DIV = "</div>";
    private static final String HTML_STYLE_OPEN_BOLD = "<span style=\"font-weight: bold;\">";
    private static final String HTML_STYLE_OPEN_ITALIC = "<span style=\"font-weight: italic;\">";
    private static final String HTML_STYLE_OPEN_UNDERLINE = "<span style=\"font-weight: underline;\">";
    private static final String HTML_STYLE_OPEN_LINETHROUGH = "<span style=\"font-weight: line-through;\">";
    private static final String HTML_STYLE_OPEN_ALIGNLEFT = "<div style=\"text-align: left;\">";
    private static final String HTML_STYLE_OPEN_ALIGNCENTER = "<div style=\"text-align: center;\">";
    private static final String HTML_STYLE_OPEN_ALIGNRIGHT = "<div style=\"text-align: right;\">";
    private static final String HTML_STYLE_OPEN_INDENTRIGHT = "<div style=\"margin-left: 40px;\">";

    //HTML Related (styles made by custom HTML-Tags)
    private static final String HTML_STYLE_OPEN_SUBSCRIPT = "<sub>";
    private static final String HTML_STYLE_CLOSE_SUBSCRIPT = "</sub>";
    private static final String HTML_STYLE_OPEN_SUPERSCRIPT = "<sup>";
    private static final String HTML_STYLE_CLOSE_SUPERSCRIPT = "</sup>";
    private static final String HTML_STYLE_OPEN_ORDERLIST = "<ol><li>";
    private static final String HTML_STYLE_CLOSE_ORDERLIST = "</ol></li>";
    private static final String HTML_STYLE_OPEN_UNORDERLIST = "<ul><li>";
    private static final String HTML_STYLE_CLOSE_UNORDERLIST = "</ul></li>";

    //HTML Related (styles without closing Tag)
    private static final String HTML_STYLE_HLINE = "<hr style=\"width: 100%; height: 2px;\">";

    /** Private Variables. **/
    //The main (Vertical)-Panel and the two inner (Horizontal)-Panels
    private HorizontalPanel topPanel;
    private HorizontalPanel bottomPanel;

    //The RichTextArea this Toolbar referes to and the Interfaces to access the RichTextArea
    private RichTextArea styleText;
    private Formatter styleTextFormatter;

    //We use an internal class of the ClickHandler and the KeyUpHandler to be private to others with these events
    private EventHandler evHandler;

    //The Buttons of the Menubar
    private ToggleButton bold;
    private ToggleButton italic;
    private ToggleButton underline;
    private ToggleButton stroke;
    private ToggleButton subscript;
    private ToggleButton superscript;
    private PushButton alignleft;
    private PushButton alignmiddle;
    private PushButton alignright;
    private PushButton orderlist;
    private PushButton unorderlist;
    private PushButton indentleft;
    private PushButton indentright;
    private PushButton generatelink;
    private PushButton breaklink;
    private PushButton insertline;
    private PushButton insertimage;
    private PushButton removeformatting;
    private ToggleButton texthtml;

    private ListBox fontlist;
    private ListBox colorlist;

    /** Constructor of the Toolbar. **/
    public RichTextToolbarWidget() {
        //Initialize the main-panel
        VerticalPanel outer = new VerticalPanel();

        //Initialize the two inner panels
        topPanel = new HorizontalPanel();
        bottomPanel = new HorizontalPanel();
        StyleResource.INSTANCE.richTextCss().ensureInjected();
        topPanel.setStyleName(StyleResource.INSTANCE.richTextCss().richTextToolbar());
        bottomPanel.setStyleName(StyleResource.INSTANCE.richTextCss().richTextToolbar());

        //Initalize the RichText area and get the interfaces to the stylings
        styleText = new RichTextArea();
        styleTextFormatter = styleText.getFormatter();

        //Set some graphical options, so this toolbar looks how we like it.
        topPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
        bottomPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);

        //Add the two inner panels to the main panel
        outer.add(topPanel);
        outer.add(bottomPanel);

        //Some graphical stuff to the main panel and the initialisation of the new widget
        outer.setWidth("100%");
        outer.setStyleName(StyleResource.INSTANCE.richTextCss().richTextToolbar());
        styleText.setWidth("100%");

        //Initialize container for toolbar and text area
        Grid wrapper = new Grid(2, 1);
        wrapper.setWidget(0, 0, outer);
        wrapper.setWidget(1, 0, styleText);
        initWidget(wrapper);

        //
        evHandler = new EventHandler();

        //Add KeyUp and Click-Handler to the RichText, so that we can actualize the toolbar if neccessary
        styleText.addKeyUpHandler(evHandler);
        styleText.addClickHandler(evHandler);

        //Now lets fill the new toolbar with life
        buildTools();
    }

    /** Click Handler of the Toolbar. **/
    private class EventHandler implements ClickHandler, KeyUpHandler, ChangeHandler {
        public void onClick(ClickEvent event) {
            if (event.getSource().equals(bold)) {
                setBoldStyle();
            } else if (event.getSource().equals(italic)) {
                setItalicStyle();
            } else if (event.getSource().equals(underline)) {
                setUnderlineStyle();
            } else if (event.getSource().equals(stroke)) {
                setStrokeStyle();
            } else if (event.getSource().equals(subscript)) {
                setSubscriptStyle();
            } else if (event.getSource().equals(superscript)) {
                setSuperscriptStyle();
            } else if (event.getSource().equals(alignleft)) {
                setLeftAlign();
            } else if (event.getSource().equals(alignmiddle)) {
                setMiddleAlign();
            } else if (event.getSource().equals(alignright)) {
                setRightAlign();
            } else if (event.getSource().equals(orderlist)) {
                setOrderedListStyle();
            } else if (event.getSource().equals(unorderlist)) {
                setUnorderedListStyle();
            } else if (event.getSource().equals(indentright)) {
                setRightIndent();
            } else if (event.getSource().equals(indentleft)) {
                setLeftIndent();
            } else if (event.getSource().equals(generatelink)) {
                generateLink();
            } else if (event.getSource().equals(breaklink)) {
                removeLink();
            } else if (event.getSource().equals(insertimage)) {
                insertImage();
            }  else if (event.getSource().equals(insertline)) {
                setInsertLineStyle();
            } else if (event.getSource().equals(removeformatting)) {
                removeFormatting();
            } else if (event.getSource().equals(texthtml)) {
                setTextHtmlStyle();
            } else if (event.getSource().equals(styleText)) {
                //Change invoked by the richtextArea
            }

            updateStatus();
        }

        private void setBoldStyle() {
            if (isHTMLMode()) {
                changeHtmlStyle(HTML_STYLE_OPEN_BOLD, HTML_STYLE_CLOSE_SPAN);
            } else {
                styleTextFormatter.toggleBold();
            }
        }

        private void setItalicStyle() {
            if (isHTMLMode()) {
                changeHtmlStyle(HTML_STYLE_OPEN_ITALIC, HTML_STYLE_CLOSE_SPAN);
            } else {
                styleTextFormatter.toggleItalic();
            }
        }

        private void setUnderlineStyle() {
            if (isHTMLMode()) {
                changeHtmlStyle(HTML_STYLE_OPEN_UNDERLINE, HTML_STYLE_CLOSE_SPAN);
            } else {
                styleTextFormatter.toggleUnderline();
            }
        }

        private void setStrokeStyle() {
            if (isHTMLMode()) {
                changeHtmlStyle(HTML_STYLE_OPEN_LINETHROUGH, HTML_STYLE_CLOSE_SPAN);
            } else {
                styleTextFormatter.toggleStrikethrough();
            }
        }

        private void setSubscriptStyle() {
            if (isHTMLMode()) {
                changeHtmlStyle(HTML_STYLE_OPEN_SUBSCRIPT, HTML_STYLE_CLOSE_SUBSCRIPT);
            } else {
                styleTextFormatter.toggleSubscript();
            }
        }

        private void setSuperscriptStyle() {
            if (isHTMLMode()) {
                changeHtmlStyle(HTML_STYLE_OPEN_SUPERSCRIPT, HTML_STYLE_CLOSE_SUPERSCRIPT);
            } else {
                styleTextFormatter.toggleSuperscript();
            }
        }

        private void setLeftAlign() {
            if (isHTMLMode()) {
                changeHtmlStyle(HTML_STYLE_OPEN_ALIGNLEFT, HTML_STYLE_CLOSE_DIV);
            } else {
                styleTextFormatter.setJustification(RichTextArea.Justification.LEFT);
            }
        }

        private void setMiddleAlign() {
            if (isHTMLMode()) {
                changeHtmlStyle(HTML_STYLE_OPEN_ALIGNCENTER, HTML_STYLE_CLOSE_DIV);
            } else {
                styleTextFormatter.setJustification(RichTextArea.Justification.CENTER);
            }
        }

        private void setRightAlign() {
            if (isHTMLMode()) {
                changeHtmlStyle(HTML_STYLE_OPEN_ALIGNRIGHT, HTML_STYLE_CLOSE_DIV);
            } else {
                styleTextFormatter.setJustification(RichTextArea.Justification.RIGHT);
            }
        }

        private void setOrderedListStyle() {
            if (isHTMLMode()) {
                changeHtmlStyle(HTML_STYLE_OPEN_ORDERLIST, HTML_STYLE_CLOSE_ORDERLIST);
            } else {
                styleTextFormatter.insertOrderedList();
            }
        }

        private void setUnorderedListStyle() {
            if (isHTMLMode()) {
                changeHtmlStyle(HTML_STYLE_OPEN_UNORDERLIST, HTML_STYLE_CLOSE_UNORDERLIST);
            } else {
                styleTextFormatter.insertUnorderedList();
            }
        }

        private void setRightIndent() {
            if (isHTMLMode()) {
                changeHtmlStyle(HTML_STYLE_OPEN_INDENTRIGHT, HTML_STYLE_CLOSE_DIV);
            } else {
                styleTextFormatter.rightIndent();
            }
        }

        private void setLeftIndent() {
            if (!isHTMLMode()) {
                styleTextFormatter.leftIndent();
            }
        }

        private void generateLink() {
            String url = Window.prompt(msgs.linkURL(), "http://");
            if (url != null) {
                if (isHTMLMode()) {
                    changeHtmlStyle("<a href=\"" + url + "\">", "</a>");
                } else {
                    styleTextFormatter.createLink(url);
                }
            }
        }

        private void removeLink() {
            if (!isHTMLMode()) {
                styleTextFormatter.removeLink();
            }
        }

        private void insertImage() {
            String url = Window.prompt(msgs.imageURL(), "http://");
            if (url != null) {
                if (isHTMLMode()) {
                    changeHtmlStyle("<img src=\"" + url + "\">", "");
                } else {
                    styleTextFormatter.insertImage(url);
                }
            }
        }

        private void setInsertLineStyle() {
            if (isHTMLMode()) {
                changeHtmlStyle(HTML_STYLE_HLINE, "");
            } else {
                styleTextFormatter.insertHorizontalRule();
            }
        }

        private void removeFormatting() {
            if (!isHTMLMode()) {
                styleTextFormatter.removeFormat();
            }
        }

        private void setTextHtmlStyle() {
            if (texthtml.isDown()) {
                styleText.setText(styleText.getHTML());
            } else {
                styleText.setHTML(styleText.getText());
            }
        }

        public void onKeyUp(KeyUpEvent event) {
            updateStatus();
        }

        public void onChange(ChangeEvent event) {
            if (event.getSource().equals(fontlist)) {
                if (isHTMLMode()) {
                    changeHtmlStyle("<span style=\"font-family: "
                            + fontlist.getValue(fontlist.getSelectedIndex()) + ";\">", HTML_STYLE_CLOSE_SPAN);
                } else {
                    styleTextFormatter.setFontName(fontlist.getValue(fontlist.getSelectedIndex()));
                }
            } else if (event.getSource().equals(colorlist)) {
                if (isHTMLMode()) {
                    changeHtmlStyle("<span style=\"color: "
                            + colorlist.getValue(colorlist.getSelectedIndex()) + ";\">", HTML_STYLE_CLOSE_SPAN);
                } else {
                    styleTextFormatter.setForeColor(colorlist.getValue(colorlist.getSelectedIndex()));
                }
            }
        }
    }

    /** Native JavaScript that returns the selected text and position of the start. **/
    public static native JsArrayString getSelection(Element elem)
    /*-{
        var txt = "";
        var pos = 0;
        var range;
        var parentElement;
        var container;

        if (elem.contentWindow.getSelection) {
            txt = elem.contentWindow.getSelection();
            pos = elem.contentWindow.getSelection().getRangeAt(0).startOffset;
        } else if (elem.contentWindow.document.getSelection) {
            txt = elem.contentWindow.document.getSelection();
            pos = elem.contentWindow.document.getSelection().getRangeAt(0).startOffset;
          } else if (elem.contentWindow.document.selection) {
              range = elem.contentWindow.document.selection.createRange();
            txt = range.text;
            parentElement = range.parentElement();
            container = range.duplicate();
            container.moveToElementText(parentElement);
            container.setEndPoint('EndToEnd', range);
            pos = container.text.length - range.text.length;
        }
          return [""+txt,""+pos];
    }-*/;

    /** Method called to toggle the style in HTML-Mode. **/
    private void changeHtmlStyle(String startTag, String stopTag) {
        JsArrayString tx = getSelection(styleText.getElement());
        String txbuffer = styleText.getText();
        Integer startpos = Integer.parseInt(tx.get(1));
        String selectedText = tx.get(0);
        styleText.setText(txbuffer.substring(0, startpos)
                + startTag + selectedText + stopTag + txbuffer.substring(startpos + selectedText.length()));
    }

    /** Private method with a more understandable name to get if HTML mode is on or not. **/
    private Boolean isHTMLMode() {
        return  texthtml.isDown();
    }

    /** Private method to set the toggle buttons and disable/enable buttons which do not work in html-mode. **/
    private void updateStatus() {
        if (styleTextFormatter != null) {
            bold.setDown(styleTextFormatter.isBold());
            italic.setDown(styleTextFormatter.isItalic());
            underline.setDown(styleTextFormatter.isUnderlined());
            subscript.setDown(styleTextFormatter.isSubscript());
            superscript.setDown(styleTextFormatter.isSuperscript());
            stroke.setDown(styleTextFormatter.isStrikethrough());
        }

        if (isHTMLMode()) {
            removeformatting.setEnabled(false);
            indentleft.setEnabled(false);
            breaklink.setEnabled(false);
        } else {
            removeformatting.setEnabled(true);
            indentleft.setEnabled(true);
            breaklink.setEnabled(true);
        }
    }

    /** Initialize the options on the toolbar. **/
    private void buildTools() {
        //Init the TOP Panel forst
        bold = createToggleButton(ICONS, 0, 0, 20, 20, msgs.bold());
        topPanel.add(bold);
        italic = createToggleButton(ICONS, 0, 60, 20, 20, msgs.italic());
        topPanel.add(italic);
        underline = createToggleButton(ICONS, 0, 140, 20, 20, msgs.underline());
        topPanel.add(underline);
        stroke = createToggleButton(ICONS, 0, 120, 20, 20, msgs.stroke());
        topPanel.add(stroke);
        topPanel.add(new HTML("&nbsp;"));
        subscript = createToggleButton(ICONS,  0, 600, 20, 20, msgs.subscript());
        topPanel.add(subscript);
        superscript = createToggleButton(ICONS, 0, 620, 20, 20, msgs.superscript());
        topPanel.add(superscript);
        topPanel.add(new HTML("&nbsp;"));
        alignleft = createPushButton(ICONS, 0, 460, 20, 20, msgs.alignLeft());
        topPanel.add(alignleft);
        alignmiddle = createPushButton(ICONS, 0, 420, 20, 20, msgs.alignCenter());
        topPanel.add(alignmiddle);
        alignright = createPushButton(ICONS, 0, 480, 20, 20, msgs.alignRight());
        topPanel.add(alignright);
        topPanel.add(new HTML("&nbsp;"));
        orderlist = createPushButton(ICONS, 0, 80, 20, 20, msgs.orderList());
        topPanel.add(orderlist);
        unorderlist = createPushButton(ICONS, 0, 20, 20, 20, msgs.unorderList());
        topPanel.add(unorderlist);
        indentright = createPushButton(ICONS, 0, 400, 20, 20, msgs.indentRight());
        topPanel.add(indentright);
        indentleft = createPushButton(ICONS, 0, 540, 20, 20, msgs.indentLeft());
        topPanel.add(indentleft);
        topPanel.add(new HTML("&nbsp;"));
        generatelink = createPushButton(ICONS, 0, 500, 20, 20, msgs.link());
        topPanel.add(generatelink);
        breaklink = createPushButton(ICONS, 0, 640, 20, 20, msgs.breakLink());
        topPanel.add(breaklink);
        topPanel.add(new HTML("&nbsp;"));
        insertline = createPushButton(ICONS, 0, 360, 20, 20, msgs.hline());
        topPanel.add(insertline);
        insertimage = createPushButton(ICONS, 0, 380, 20, 20, msgs.image());
        topPanel.add(insertimage);
        topPanel.add(new HTML("&nbsp;"));
        removeformatting = createPushButton(ICONS, 20, 460, 20, 20, msgs.removeFormat());
        topPanel.add(removeformatting);
        topPanel.add(new HTML("&nbsp;"));
        texthtml = createToggleButton(ICONS, 0, 260, 20, 20, msgs.switchView());
        topPanel.add(texthtml);

        //Init the BOTTOM Panel
        fontlist = createFontList();
        bottomPanel.add(fontlist);
        bottomPanel.add(new HTML("&nbsp;"));
        colorlist = createColorList();
        bottomPanel.add(colorlist);
    }

    /** Method to create a Toggle button for the toolbar. **/
    private ToggleButton createToggleButton(String url, Integer top,
            Integer left, Integer width, Integer height, String tip) {
        Image extract = new Image(url, left, top, width, height);
        ToggleButton tb = new ToggleButton(extract);
        tb.setHeight(height + "px");
        tb.setWidth(width + "px");
        tb.addClickHandler(evHandler);
        if (tip != null) {
            tb.setTitle(tip);
        }
        return tb;
    }

    /** Method to create a Push button for the toolbar. **/
    private PushButton createPushButton(String url, Integer top,
            Integer left, Integer width, Integer height, String tip) {
        Image extract = new Image(url, left, top, width, height);
        PushButton tb = new PushButton(extract);
        tb.setHeight(height + "px");
        tb.setWidth(width + "px");
        tb.addClickHandler(evHandler);
        if (tip != null) {
            tb.setTitle(tip);
        }
        return tb;
    }

    /** Method to create the fontlist for the toolbar. **/
    private ListBox createFontList() {
        ListBox mylistBox = new ListBox();
        mylistBox.addChangeHandler(evHandler);
        mylistBox.setVisibleItemCount(1);

        mylistBox.addItem(msgs.font());
        for (String name : GUI_FONTLIST.keySet()) {
            mylistBox.addItem(name, GUI_FONTLIST.get(name));
        }

        return mylistBox;
    }

    /** Method to create the colorlist for the toolbar. **/
    private ListBox createColorList() {
        ListBox mylistBox = new ListBox();
        mylistBox.addChangeHandler(evHandler);
        mylistBox.setVisibleItemCount(1);

        mylistBox.addItem(msgs.color());
        for (String name : GUI_COLORLIST.keySet()) {
            mylistBox.addItem(name, GUI_COLORLIST.get(name));
        }

        return mylistBox;
    }

    /** Method to return formatted text. **/
    public String getValue() {
        return styleText.getHTML();
    }

    /** Method to expose setSize for inner textArea. **/
    public void setAreaSize(String width, String height) {
        styleText.setSize(width, height);
    }

    public void setValue(Object text) {
        styleText.setHTML((String) text);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setValue(Object arg0, boolean arg1) {
        // TODO Auto-generated method stub

    }



}
