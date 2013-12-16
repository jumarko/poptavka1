/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.ui;

import com.eprovement.poptavka.client.common.session.Constants;
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
import java.util.Collection;

/**************************************************************************/
/* Private classes                                                        */
/**************************************************************************/

/**
 * Suggest box that behaves as ListBox and can be better designed.
 *
 * @author Martin Slavkovsky
 */
public final class WSListBox extends SuggestBox {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private MyTextBox textBox;
    private String defaultString;
    private WSListBoxData data;

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
    public static WSListBox createListBox(WSListBoxData listBoxData, int selectedIndex) {
        return new WSListBox(createSuggestOracle(listBoxData.getItems().values()),
                createReadOnlyTextBox(), createOracleDisplay(), listBoxData, selectedIndex);
    }

    /**
     * Creates WSListBox
     * @param oracleData - the SuggestOracle data provider
     * @param textBox element
     * @param display - the SuggestionDisplay
     * @param data - the ListBox data provider
     * @param selectedIndex
     */
    private WSListBox(MultiWordSuggestOracle oracleData, MyTextBox textBox,
            DefaultSuggestionDisplay display, WSListBoxData data, int selectedIndex) {
        super(oracleData, textBox, display);
        this.textBox = textBox;
        this.data = data;
        this.defaultString = data.getItemByIndex(selectedIndex);
        setSelectedByIndex(selectedIndex);
        bind();
    }

    /**
     * Creates suggest oracle from data from given list.
     * @param values - list of strings
     * @return the MultiWordSuggestOracle
     */
    private static MultiWordSuggestOracle createSuggestOracle(Collection<String> values) {
        MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        oracle.setDefaultSuggestionsFromText(values);
        oracle.addAll(values);
        return oracle;
    }

    /**
     * Creates read only text box.
     * @return the MyTextBox
     */
    private static MyTextBox createReadOnlyTextBox() {
        MyTextBox readOnlyTextBox = new MyTextBox();
        readOnlyTextBox.setReadOnly(true);
        return readOnlyTextBox;
    }

    /**
     * Creates suggestion display.
     * @return the DefaultSuggestionDisplay
     */
    private static DefaultSuggestionDisplay createOracleDisplay() {
        DefaultSuggestionDisplay display = new DefaultSuggestionDisplay();
        display.setPopupStyleName(Storage.RSCS.common().myListBox());
        return display;
    }

    /**************************************************************************/
    /* Bind handlers                                                          */
    /**************************************************************************/
    /**
     * Binds events.
     */
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
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Sets default string.
     * @param defaultString
     */
    public void setDefaultString(String defaultString) {
        this.defaultString = defaultString;
    }

    /**
     * Sets selected item by its text.
     * @param text - item's text
     */
    public void setSelected(String text) {
        textBox.setPlaceholder(text);
        if (text.equals(defaultString)) {
            removeStyleName(Constants.ACT);
        } else {
            addStyleName(Constants.ACT);
        }
    }

    /**
     * Sets selected item by its index.
     * @param itemValueIndex - item's value
     */
    public void setSelectedByIndex(int itemValueIndex) {
        setSelected(data.getItemByIndex(itemValueIndex));
    }

    /**
     * Sets selected item by its key.
     * @param key - item's key
     */
    public void setSelectedByKey(int key) {
        setSelected(data.getItemByKey(key));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the selected string
     */
    public String getSelected() {
        return textBox.getPlaceholder();
    }

    /**
     * @return the default string
     */
    public String getDefaultString() {
        return defaultString;
    }

    /**
     * @return true if selected, false if no selectoin was made
     */
    public boolean isSelected() {
        return !textBox.getPlaceholder().contains(defaultString);
    }
}
/**
 * Custom text box to access place holder text.
 * TODO Martin - use bootstrap textbox - that one has already Placeholder.
 *
 * @author Martin Slavkovsky
 */
class MyTextBox extends TextBox implements HasPlaceholder {

    private PlaceholderHelper placeholderHelper = GWT.create(PlaceholderHelper.class);

    /**
     * @{inheritDoc}
     */
    @Override
    public void setPlaceholder(String placeholder) {
        placeholderHelper.setPlaceholer(getElement(), placeholder);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public String getPlaceholder() {
        return placeholderHelper.getPlaceholder(getElement());
    }
}
