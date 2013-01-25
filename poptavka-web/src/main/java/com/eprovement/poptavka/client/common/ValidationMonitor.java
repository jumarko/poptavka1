/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.IListDetailObject;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

/**
 *
 * @author Martin Slavkovsky
 */
public class ValidationMonitor<T> extends Composite
        implements HasWidgets, ProvidesValidate {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    interface ChangeMonitorUiBinder extends UiBinder<Widget, ValidationMonitor> {
    }
    private static ChangeMonitorUiBinder uiBinder = GWT.create(ChangeMonitorUiBinder.class);
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField
    SimplePanel holder;
    @UiField
    Label errorLabel;
    /** Class attributes. **/
    private ArrayList<IListDetailObject> listData = null;
    private Validator validator = null;
    private Class<T> beanType;
    private Boolean valid = null;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public ValidationMonitor(Class<T> beanType) {
        this.beanType = beanType;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        listData = new ArrayList<IListDetailObject>();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Validation                                                             */
    /**************************************************************************/
    public void validate() {
        //reset for new validation
        holder.getWidget().removeStyleName(Storage.RSCS.common().errorField());
        errorLabel.setText("");
        valid = true;
        //perform new validation
        Set<ConstraintViolation<T>> violations = validator.validateValue(beanType,
                holder.getWidget().getTitle(), getValue(), Default.class);
        for (ConstraintViolation<T> violation : violations) {
            holder.getWidget().addStyleName(Storage.RSCS.common().errorField());
            errorLabel.setText(violation.getMessage());
            valid = false;
        }
    }

    @Override
    public boolean isValid() {
        if (valid == null) {
            validate();
        }
        return valid;
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
        addChangeHandler(w);
        addBlurHandler(w);
        holder.setWidget(w);
    }

    @Override
    public void add(Widget w) {
        addChangeHandler(w);
        addBlurHandler(w);
        holder.add(w);
    }

    @Override
    public void clear() {
        holder.clear();
    }

    @Override
    public Iterator<Widget> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean remove(Widget w) {
        return holder.remove(w);
    }

    /**************************************************************************/
    /* Methods                                                                */
    /**************************************************************************/
    /** Getters. **/
    public Object getValue() {
        return getInputWidgetText();
    }

    public Widget getChangeMonitorWidget() {
        return this;
    }

    /** Setters. **/
    public void setValue(Object value) {
        setInputWidgetText(value);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void addChangeHandler(Widget w) {
        if (w instanceof CellList) {
            w.addHandler(new ValueChangeHandler<IListDetailObject>() {
                @Override
                public void onValueChange(ValueChangeEvent<IListDetailObject> event) {
                    validate();
                }
            }, ValueChangeEvent.getType());
        } else if (w instanceof DateBox) {
            w.addHandler(new ValueChangeHandler<IListDetailObject>() {
                @Override
                public void onValueChange(ValueChangeEvent<IListDetailObject> event) {
                    validate();
                }
            }, ValueChangeEvent.getType());
        }
    }

    private void addBlurHandler(Widget w) {
        if (w instanceof TextBox || w instanceof IntegerBox || w instanceof BigDecimalBox) {
            w.addDomHandler(new BlurHandler() {
                @Override
                public void onBlur(BlurEvent event) {
                    validate();
                }
            }, BlurEvent.getType());
        }
    }

    private Object getInputWidgetText() {
        if (holder.getWidget() instanceof TextBox) {
            return ((TextBox) holder.getWidget()).getText();
        } else if (holder.getWidget() instanceof IntegerBox) {
            return ((IntegerBox) holder.getWidget()).getValue();
        } else if (holder.getWidget() instanceof BigDecimalBox) {
            return ((BigDecimalBox) holder.getWidget()).getValue();
        } else if (holder.getWidget() instanceof TextArea) {
            return ((TextArea) holder.getWidget()).getText();
        } else if (holder.getWidget() instanceof DateBox) {
            return ((DateBox) holder.getWidget()).getValue();
        } else if (holder.getWidget() instanceof ListBox) {
            return ((ListBox) holder.getWidget()).getSelectedIndex();
        } else if (holder.getWidget() instanceof CellList) {
            return listData;
        } else {
            return null;
        }
    }

    private void setInputWidgetText(Object value) {
        if (holder.getWidget() instanceof TextBox) {
            ((TextBox) holder.getWidget()).setText((String) value);
        } else if (holder.getWidget() instanceof IntegerBox) {
            ((IntegerBox) holder.getWidget()).setValue((Integer) value);
        } else if (holder.getWidget() instanceof BigDecimalBox) {
            ((BigDecimalBox) holder.getWidget()).setValue((BigDecimal) value);
        } else if (holder.getWidget() instanceof TextArea) {
            ((TextArea) holder.getWidget()).setText((String) value);
        } else if (holder.getWidget() instanceof DateBox) {
            ((DateBox) holder.getWidget()).setValue((Date) value);
        } else if (holder.getWidget() instanceof ListBox) {
            ((ListBox) holder.getWidget()).setSelectedIndex((Integer) value);
        } else if (holder.getWidget() instanceof CellList) {
            ((CellList) holder.getWidget()).setRowData((List) value);
            listData = (ArrayList) value;
        }
    }
}
