/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Parser;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.ValueBoxBase;
import java.math.BigDecimal;
import java.text.ParseException;

/**
 *
 * @author mato
 */
public class BigDecimalBoxBase extends ValueBoxBase<BigDecimal> implements HasHandlers, HasChangeHandlers {
//        SourcesChangeEvents {

    /**
     * Legacy wrapper for {@link ValueBoxBase.TextAlignment}, soon to be deprecated.
     * @deprecated use {@link #setAlignment(ValueBoxBase.TextAlignment)}
     */
    @Deprecated
    public final static class TextAlignConstant {

        private ValueBoxBase.TextAlignment value;

        private TextAlignConstant(ValueBoxBase.TextAlignment value) {
            this.value = value;
        }

        ValueBoxBase.TextAlignment getTextAlignString() {
            return value;
        }
    }
    /**
     * Center the text.
     */
    public static final BigDecimalBoxBase.TextAlignConstant ALIGN_CENTER =
            new BigDecimalBoxBase.TextAlignConstant(ValueBoxBase.TextAlignment.CENTER);
    /**
     * Justify the text.
     */
    public static final BigDecimalBoxBase.TextAlignConstant ALIGN_JUSTIFY =
            new BigDecimalBoxBase.TextAlignConstant(ValueBoxBase.TextAlignment.JUSTIFY);
    /**
     * Align the text to the left edge.
     */
    public static final BigDecimalBoxBase.TextAlignConstant ALIGN_LEFT =
            new BigDecimalBoxBase.TextAlignConstant(ValueBoxBase.TextAlignment.LEFT);
    /**
     * Align the text to the right.
     */
    public static final BigDecimalBoxBase.TextAlignConstant ALIGN_RIGHT =
            new BigDecimalBoxBase.TextAlignConstant(ValueBoxBase.TextAlignment.RIGHT);

    /**
     * Creates a text box that wraps the given browser element handle. This is
     * only used by subclasses.
     *
     * @param elem the browser element to wrap
     */
    protected BigDecimalBoxBase(Element elem) {
        super(elem, BigDecimalPassthroughRenderer.instance(), BigDecimalPassthroughParser.instance());
    }

    /**
     * @deprecated Use {@link #addChangeHandler} instead
     */
//    @Deprecated
//    public void addChangeListener(ChangeListener listener) {
//        addChangeHandler(new ListenerWrapper.WrappedChangeListener(listener));
//    }

    /**
     * Overridden to return "" from an empty text box.
     */
    @Override
    public BigDecimal getValue() {
        return super.getValue();
    }

    /**
     * Legacy wrapper for {@link #setAlignment(TextAlignment)}.
     *
     * @deprecated use {@link #setAlignment(TextAlignment)}
     */
    @Deprecated
    public void setTextAlignment(BigDecimalBoxBase.TextAlignConstant align) {
        setAlignment(align.value);
    }
}

class BigDecimalPassthroughRenderer extends AbstractRenderer<BigDecimal> {

    private static final BigDecimalPassthroughRenderer INSTANCE = new BigDecimalPassthroughRenderer();

    /**
     * Returns the instance of the no-op renderer.
     */
    public static Renderer<BigDecimal> instance() {
        return INSTANCE;
    }

    protected BigDecimalPassthroughRenderer() {
    }

    public String render(BigDecimal object) {
        return object == null ? "" : object.toString();
    }
}

class BigDecimalPassthroughParser implements Parser<BigDecimal> {

    private static final BigDecimalPassthroughParser INSTANCE = new BigDecimalPassthroughParser();

    /**
     * Returns the instance of the no-op renderer.
     */
    public static Parser<BigDecimal> instance() {
        return INSTANCE;
    }

    protected BigDecimalPassthroughParser() {
    }

    @Override
    public BigDecimal parse(CharSequence text) throws ParseException {
        if (text.toString().isEmpty()) {
            return null;
        } else {
            return new BigDecimal(text.toString());
        }
    }
}
