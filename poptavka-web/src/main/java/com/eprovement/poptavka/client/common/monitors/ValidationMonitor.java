/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.monitors;

import com.eprovement.poptavka.client.common.ui.WSBigDecimalBox;
import com.eprovement.poptavka.client.common.ui.WSIntegerBox;
import com.eprovement.poptavka.client.common.ui.WSPriceBox;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import java.text.ParseException;

/**
 * Main purpose of Validation monitor is to simplifies validation process through
 * hibernate validation. Works like this:
 * Unlike validation of whole object using validation Editor, validation monitor
 * validates only one attribute separately. It enable partial validation of form attributes.
 * And to do partial validation in form, we needed additional logic and components.
 * All this additional requirements are build in validation monitor.
 *
 * To use Validation monitor. Wrap component you want to validate by validation monitor.
 * Supported components are: TextBox, IntegerBox, BigDecimalBox, DateBox, ListBox, CellList.
 * Then you have to define validation monitor as (provided=true) and manually initialize it,
 * because validation monitor need to know Class<T> of validated object. And thats it. Enjoy.
 *
 * @author Martin Slavkovsky
 */
public class ValidationMonitor<T> extends AbstractValidationMonitor<T> {

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    public ValidationMonitor(Class<T> beanType, Class<?>[] beanGroups, String field) {
        super(beanType, beanGroups, field);
    }

    public ValidationMonitor(Class<T> beanType, String field) {
        super(beanType, field);
    }

    /**************************************************************************/
    /* Abstract methods implemntations                                        */
    /**************************************************************************/
    /**
     * When entering input widget and leaving making some changes - call validate methods
     * in case validation constraints are met. Validation process then displays appropriate
     * error message.
     *
     * @param w - input widget
     */
    @Override
    public void addValidationHandlers(Widget w) {
        if (w instanceof CellList || w instanceof DateBox) {
            w.addHandler(new ValueChangeHandler<Object>() {
                @Override
                public void onValueChange(ValueChangeEvent<Object> event) {
                    if (!isExternalValidation()) {
                        validate();
                    }
                }
            }, ValueChangeEvent.getType());
        }
        if (w instanceof HasValue) {
            w.addDomHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent event) {
                    if (!isExternalValidation() && preValidate()) {
                        validate();
                    }
                }
            }, ChangeEvent.getType());
            w.addDomHandler(new BlurHandler() {
                @Override
                public void onBlur(BlurEvent event) {
                    if (!isExternalValidation() && preValidate()) {
                        validate();
                    }
                }
            }, BlurEvent.getType());
        }
    }

    @Override
    void addChangeHandlers(Widget w) {
        //nothing by default
    }

    /**
     * Get component's value that is validated.
     * @return
     */
    @Override
    public Object getValue() {
        if (holder.getWidget() instanceof HasValue) {
            return ((HasValue) holder.getWidget()).getValue();
        } else if (holder.getWidget() instanceof ListBox) {
            return ((ListBox) holder.getWidget()).getSelectedIndex();
        } else {
            return null;
        }
    }

    /**
     * Set component's value.
     * @param value
     */
    @Override
    public void setValue(Object value) {
        if (holder.getWidget() instanceof HasValue) {
            ((HasValue) holder.getWidget()).setValue(value);
        } else if (holder.getWidget() instanceof ListBox) {
            ((ListBox) holder.getWidget()).setSelectedIndex((Integer) value);
        }
    }

    /**
     * Pre-Check validation. Check validating field for exceptions such as NumberFormatExcpetions, i.e.
     * If no such case is catch, standard validation can continue, otherwise field is validated immediately
     * and the rest of validation process is stopped.
     * @return true if validation can continue, false otherwise
     */
    @Override
    protected boolean preValidate() {
        try {
            if (holder.getWidget() instanceof WSBigDecimalBox) {
                ((WSBigDecimalBox) holder.getWidget()).getValueOrThrow();
            } else if (holder.getWidget() instanceof WSIntegerBox) {
                ((WSIntegerBox) holder.getWidget()).getValueOrThrow();
            } else if (holder.getWidget() instanceof WSPriceBox) {
                ((WSPriceBox) holder.getWidget()).formatAndValidate();
            }
        } catch (ParseException ex) {
            return validateNumberException();
        } catch (NumberFormatException ex) {
            return validateNumberException();
        }
        return true;
    }
}
