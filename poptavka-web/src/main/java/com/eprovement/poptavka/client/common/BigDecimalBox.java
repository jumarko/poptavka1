package com.eprovement.poptavka.client.common;

import com.github.gwtbootstrap.client.ui.base.HasPlaceholder;
import com.github.gwtbootstrap.client.ui.base.PlaceholderHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.RootPanel;
import java.math.BigDecimal;
import java.text.ParseException;

/**
 * IntegerBox created according to GWT TextBox to be used with integers. Better for validation.
 * Now we can validate directly integers in detail objects. No converstion/duplicity to String needed.
 *
 * @author Martin Slavkovsky
 */
public class BigDecimalBox extends BigDecimalBoxBase implements HasPlaceholder {

    private PlaceholderHelper placeholderHelper = GWT.create(PlaceholderHelper.class);

    @Override
    public void setPlaceholder(String placeholder) {
        placeholderHelper.setPlaceholer(getElement(), placeholder);
    }

    @Override
    public String getPlaceholder() {
        return placeholderHelper.getPlaceholder(getElement());
    }

    /**
     * Creates a IntegerTextBox widget that wraps an existing &lt;input type='text'&gt;
     * element.
     *
     * This element must already be attached to the document. If the element is
     * removed from the document, you must call
     * {@link RootPanel#detachNow(Widget)}.
     *
     * @param element the element to be wrapped
     */
    public static BigDecimalBox wrap(Element element) {
        // Assert that the element is attached.
        assert Document.get().getBody().isOrHasChild(element);

        BigDecimalBox textBox = new BigDecimalBox(element);

        // Mark it attached and remember it for cleanup.
        textBox.onAttach();
        RootPanel.detachOnWindowClose(textBox);

        return textBox;
    }

    /**
     * Creates an empty text box.
     */
    public BigDecimalBox() {
        this(Document.get().createTextInputElement(), "gwt-BigDecimalTextBox");
    }

    /**
     * This constructor may be used by subclasses to explicitly use an existing
     * element. This element must be an &lt;input&gt; element whose type is
     * 'text'.
     *
     * @param element the element to be used
     */
    protected BigDecimalBox(Element element) {
        super(element);
        assert InputElement.as(element).getType().equalsIgnoreCase("text");
    }

    BigDecimalBox(Element element, String styleName) {
        super(element);
        if (styleName != null) {
            setStyleName(styleName);
        }
    }

    /**
     * Gets the maximum allowable length of the text box.
     *
     * @return the maximum length, in characters
     */
    public int getMaxLength() {
        return getInputElement().getMaxLength();
    }

    /**
     * Gets the number of visible characters in the text box.
     *
     * @return the number of visible characters
     */
    public int getVisibleLength() {
        return getInputElement().getSize();
    }

    /**
     * Sets the maximum allowable length of the text box.
     *
     * @param length the maximum length, in characters
     */
    public void setMaxLength(int length) {
        getInputElement().setMaxLength(length);
    }

    /**
     * Sets the number of visible characters in the text box.
     *
     * @param length the number of visible characters
     */
    public void setVisibleLength(int length) {
        getInputElement().setSize(length);
    }

    private InputElement getInputElement() {
        return getElement().cast();
    }

    /**
     * Set Big decimal value
     * @param value
     * @param fireEvents
     */
    @Override
    public void setValue(BigDecimal value, boolean fireEvents) {
        super.setText(value.toString());
        BigDecimal oldValue = null;
        try {
            oldValue = getValueOrThrow();
        } catch (ParseException ex) {
            if (fireEvents) {
                ValueChangeEvent.fire(this, value);
            }
        }
        if (fireEvents) {
            ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
        }
    }
}