package cz.poptavka.sample.client.common;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Providing just overflow ability.
 *
 * @author Beho
 *
 */
public abstract class OverflowComposite extends Composite {

    protected void setParentOverflow(Widget widget, Overflow value) {
        widget.getElement().getParentElement().getStyle().setOverflow(value);
    }

}
