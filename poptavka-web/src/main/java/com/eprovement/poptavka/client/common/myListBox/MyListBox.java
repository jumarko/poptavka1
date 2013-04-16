/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.myListBox;

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
public final class MyListBox extends SuggestBox {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private MyTextBox textBox;
    private String defaultString;
    private MyListBoxData data;

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
    public static MyListBox createListBox(MyListBoxData listBoxData, int selectedIndex) {
        return new MyListBox(createSuggestOracle(listBoxData.getValues().values()),
                createReadOnlyTextBox(), createOracleDisplay(), listBoxData, selectedIndex);
    }

    private MyListBox(MultiWordSuggestOracle oracleData, MyTextBox textBox,
            DefaultSuggestionDisplay display, MyListBoxData data, int selectedIndex) {
        super(oracleData, textBox, display);
        this.textBox = textBox;
        this.data = data;
        this.defaultString = data.getValue(selectedIndex);
        setSelectedByIndex(selectedIndex);
        bind();
    }

    private static MultiWordSuggestOracle createSuggestOracle(Collection<String> values) {
        MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        oracle.setDefaultSuggestionsFromText(values);
        oracle.addAll(values);
        return oracle;
    }

    private static MyTextBox createReadOnlyTextBox() {
        MyTextBox readOnlyTextBox = new MyTextBox();
        readOnlyTextBox.setReadOnly(true);
        return readOnlyTextBox;
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
    /* Setters                                                                */
    /**************************************************************************/
    public void setDefaultString(String defaultString) {
        this.defaultString = defaultString;
    }

    public void setSelected(String text) {
        textBox.setPlaceholder(text);
        if (text.equals(defaultString)) {
            removeStyleName(Constants.ACT);
        } else {
            addStyleName(Constants.ACT);
        }
    }

    public void setSelectedByIndex(int itemValueIndex) {
        setSelected(data.getValue(itemValueIndex));
    }

    public void setSelectedByValue(String itemValue) {
        setSelected(data.getValue(itemValue));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public String getSelected() {
        return textBox.getPlaceholder();
    }

    public boolean isSelected() {
        return !textBox.getPlaceholder().contains(defaultString);
    }
}

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
