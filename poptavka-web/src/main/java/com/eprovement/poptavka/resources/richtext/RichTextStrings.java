package com.eprovement.poptavka.resources.richtext;

import com.google.gwt.i18n.client.Messages;

/**
 * This interface is used to make the toolbar's strings
 * internationalizable.
 */

public interface RichTextStrings extends Messages {

    //colors
    String white();
    String black();
    String red();
    String green();
    String yellow();
    String blue();

    String color();
    String font();

    String linkURL();
    String imageURL();
    String switchView();
    String removeFormat();
    String image();
    String hline();
    String breakLink();
    String link();
    String indentLeft();
    String indentRight();
    String unorderList();
    String orderList();
    String alignRight();
    String alignCenter();
    String alignLeft();
    String superscript();
    String subscript();
    String stroke();
    String underline();
    String italic();
    String bold();

}
