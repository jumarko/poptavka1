/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.monitors;

import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Iterator;

/**
 * Abstract monitor, super parent for all monitors. Represents UI part of monitors.
 * @author Martin Slavkovsky
 */
public abstract class AbstractMonitor extends Composite implements HasWidgets {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    interface AbstractMonitorUiBinder extends UiBinder<Widget, AbstractMonitor> {
    }
    private static AbstractMonitorUiBinder uiBinder = GWT.create(AbstractMonitorUiBinder.class);
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField SimplePanel holder;
    @UiField HTMLPanel errorPanel, changePanel;
    @UiField IconAnchor revert;
    @UiField Label errorLabel;
    @UiField ControlGroup controlGroup;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Initialize abstract monitor.
     */
    public AbstractMonitor() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* HasWidgets                                                             */
    /**************************************************************************/
    @Override
    public Widget getWidget() {
        return holder.getWidget();
    }

    @Override
    public void setWidget(Widget w) {
        addValidationHandlers(w);
        addChangeHandlers(w);
        holder.setWidget(w);
    }

    @Override
    public void add(Widget w) {
        addValidationHandlers(w);
        addChangeHandlers(w);
        holder.add(w);
    }

    @Override
    public boolean remove(Widget w) {
        return holder.remove(w);
    }

    /**
     * Clear component that is validated.
     */
    @Override
    public void clear() {
        holder.clear();
    }

    /**
     * Not supported yet.
     * @return
     */
    @Override
    public Iterator<Widget> iterator() {
        return holder.iterator();
    }

    /**************************************************************************/
    /* Abstract methods                                                       */
    /**************************************************************************/
    /**
     * Register event handlers that fires validation process if wanted.
     *
     * @param w - input widget
     */
    abstract void addValidationHandlers(Widget w);

    /**
     * Register event handlers that fires change monitor process if wanted.
     *
     * @param w - input widget
     */
    abstract void addChangeHandlers(Widget w);

    /**
     * Get component's value that is validated.
     * @return
     */
    abstract Object getValue();

    /**
     * Set component's value.
     * @param value
     */
    abstract void setValue(Object value);
}