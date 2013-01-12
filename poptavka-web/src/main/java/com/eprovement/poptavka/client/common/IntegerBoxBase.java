/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common;

import com.google.gwt.dom.client.Element;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Parser;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.ValueBoxBase;
import java.text.ParseException;

/**
 *
 * @author mato
 */
public class IntegerBoxBase extends ValueBoxBase<Integer> implements
        SourcesChangeEvents {

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
    public static final IntegerBoxBase.TextAlignConstant ALIGN_CENTER = new IntegerBoxBase.TextAlignConstant(
            ValueBoxBase.TextAlignment.CENTER);
    /**
     * Justify the text.
     */
    public static final IntegerBoxBase.TextAlignConstant ALIGN_JUSTIFY = new IntegerBoxBase.TextAlignConstant(
            ValueBoxBase.TextAlignment.JUSTIFY);
    /**
     * Align the text to the left edge.
     */
    public static final IntegerBoxBase.TextAlignConstant ALIGN_LEFT = new IntegerBoxBase.TextAlignConstant(
            ValueBoxBase.TextAlignment.LEFT);
    /**
     * Align the text to the right.
     */
    public static final IntegerBoxBase.TextAlignConstant ALIGN_RIGHT = new IntegerBoxBase.TextAlignConstant(
            ValueBoxBase.TextAlignment.RIGHT);

    /**
     * Creates a text box that wraps the given browser element handle. This is
     * only used by subclasses.
     *
     * @param elem the browser element to wrap
     */
    protected IntegerBoxBase(Element elem) {
        super(elem, IntegerPassthroughRenderer.instance(), IntegerPassthroughParser.instance());
    }

    /**
     * @deprecated Use {@link #addChangeHandler} instead
     */
    @Deprecated
    public void addChangeListener(ChangeListener listener) {
//    addChangeHandler(new ListenerWrapper.WrappedChangeListener(listener));
    }

    /**
     * Overridden to return "" from an empty text box.
     */
    @Override
    public Integer getValue() {
        return super.getValue();
    }

    /**
     * Legacy wrapper for {@link #setAlignment(TextAlignment)}.
     *
     * @deprecated use {@link #setAlignment(TextAlignment)}
     */
    @Deprecated
    public void setTextAlignment(IntegerBoxBase.TextAlignConstant align) {
        setAlignment(align.value);
    }
}

class IntegerPassthroughRenderer extends AbstractRenderer<Integer> {

    private static final IntegerPassthroughRenderer INSTANCE = new IntegerPassthroughRenderer();

    /**
     * Returns the instance of the no-op renderer.
     */
    public static Renderer<Integer> instance() {
        return INSTANCE;
    }

    protected IntegerPassthroughRenderer() {
    }

    public String render(Integer object) {
        return object == null ? "" : object.toString();
    }
}

class IntegerPassthroughParser implements Parser<Integer> {

    private static final IntegerPassthroughParser INSTANCE = new IntegerPassthroughParser();

    /**
     * Returns the instance of the no-op renderer.
     */
    public static Parser<Integer> instance() {
        return INSTANCE;
    }

    protected IntegerPassthroughParser() {
    }

    @Override
    public Integer parse(CharSequence text) throws ParseException {
        if (text.toString().isEmpty()) {
            return null;
        } else {
            return Integer.valueOf(text.toString());
        }
    }
}
