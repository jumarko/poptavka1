/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.myListBox;

import com.eprovement.poptavka.client.common.session.Storage;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.base.HasPlaceholder;
import com.github.gwtbootstrap.client.ui.base.PlaceholderHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import java.util.List;

/**
 *
 *
 * @author Martin Slavkovsky
 */
public final class MyListBox extends SuggestBox {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static MyTextBox textBox;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Call this from outside when initializing MyListBox to convert List to MultiWordSuggestOracle.
     * MyListBox needs several object during initialization, therefore instead of doing this conversion
     * in each class that uses MyListBox, call this method and return values used in initialization.
     * @param data
     * @return MultiWordSuggestOracle filled with given List
     */
    public static MyListBox createListBox(List<String> data) {
        return new MyListBox(createSuggestOracle(data), createReadOnlyTextBox(), createOracleDisplay());
    }

    private MyListBox(MultiWordSuggestOracle oracleData, MyTextBox textBox, DefaultSuggestionDisplay display) {
        super(oracleData, textBox, display);

        bind();
    }

    private static MultiWordSuggestOracle createSuggestOracle(List<String> data) {
        MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        oracle.setDefaultSuggestionsFromText(data);
        oracle.addAll(data);
        return oracle;
    }

    private static MyTextBox createReadOnlyTextBox() {
        textBox = new MyTextBox();
        textBox.setReadOnly(true);
        return textBox;
    }

    private static DefaultSuggestionDisplay createOracleDisplay() {
        DefaultSuggestionDisplay display = new DefaultSuggestionDisplay();
        display.setPopupStyleName(Storage.RSCS.common().myListBox());
        return display;
    }

    /**************************************************************************/
    /* Bind handlers                                                          */
    /**************************************************************************/
    private void bind() {
        /** CLICK. **/
        addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showSuggestionList();
            }
        }, ClickEvent.getType());
        /** SELECTION. **/
        addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            @Override
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
                setText("");
                setSelected(event.getSelectedItem().getDisplayString());
            }
        });
    }

    /**************************************************************************/
    /* Getters & Setters                                                      */
    /**************************************************************************/
    public void setSelected(String text) {
        textBox.setPlaceholder(text);
    }

    public String getSelected() {
        return textBox.getPlaceholder();
    }
}

/**************************************************************************/
/* Private classes                                                        */
/**************************************************************************/
/**
 * Custom text box to access place holder text.
 * @author Martin Slavkovsky
 */
class MyTextBox extends TextBox implements HasPlaceholder {
    private PlaceholderHelper placeholderHelper = GWT.create(PlaceholderHelper.class);

    @Override
    public void setPlaceholder(String placeholder) {
        placeholderHelper.setPlaceholer(getElement(), placeholder);
    }

    @Override
    public String getPlaceholder() {
        return placeholderHelper.getPlaceholder(getElement());
    }
}
