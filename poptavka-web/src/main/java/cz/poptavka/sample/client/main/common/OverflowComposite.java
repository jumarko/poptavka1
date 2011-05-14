package cz.poptavka.sample.client.main.common;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Providing just overflow ability.
 *
 * @author Beho
 *
 */
public abstract class OverflowComposite extends ResizeComposite {

    protected void setParentOverflow(Widget widget, Overflow value) {
        widget.getElement().getParentElement().getStyle().setOverflow(value);
    }

}
