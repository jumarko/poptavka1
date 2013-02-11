/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common;

import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.IListDetailObject;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.github.gwtbootstrap.client.ui.constants.LabelType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
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
public class ValidationMonitor<T> extends Composite
        implements HasWidgets, ProvidesValidate {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    interface ValidationMonitorUiBinder extends UiBinder<Widget, ValidationMonitor> {
    }
    private static ValidationMonitorUiBinder uiBinder = GWT.create(ValidationMonitorUiBinder.class);
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField SimplePanel holder;
    @UiField HTMLPanel errorPanel;
    @UiField Label errorLabel;
    @UiField ControlGroup controlGroup;
    /** Class attributes. **/
    private ArrayList<IListDetailObject> listData = null;
    private Validator validator = null;
    private Class<T> beanType;
    private Boolean valid = null;
    private boolean hideErrorPanel = true;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Initialize Validation monitor. Need to manually initialize because we need
     * to know Class<T>.
     * @param beanType
     */
    public ValidationMonitor(Class<T> beanType) {
        this.beanType = beanType;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        listData = new ArrayList<IListDetailObject>();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Validation                                                             */
    /**************************************************************************/
    /**
     * Provide partial validation of component that was wrapped by validation monitor class.
     */
    public void validate() {
        //reset for new validation
        if (hideErrorPanel) {
            errorPanel.setVisible(false);
            errorLabel.setText("");
            controlGroup.setType(ControlGroupType.NONE);
        }
        valid = true;
        //perform new validation
        Set<ConstraintViolation<T>> violations = validator.validateValue(beanType,
                holder.getWidget().getTitle(), getValue(), Default.class);
        for (ConstraintViolation<T> violation : violations) {
            errorPanel.setVisible(true);
            errorLabel.setText(violation.getMessage());
            errorLabel.setType(LabelType.IMPORTANT);
            controlGroup.setType(ControlGroupType.ERROR);
            valid = false;
        }
    }

    /**
     * Tell if component is valid.
     * @return true if valid, false elsewere.
     */
    @Override
    public boolean isValid() {
        validate();
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Remove component that is validated.
     * @param w - widget to be removed.
     * @return true if successfully removed, false elsewhere.
     */
    @Override
    public boolean remove(Widget w) {
        return holder.remove(w);
    }

    /**************************************************************************/
    /* Methods                                                                */
    /**************************************************************************/
    /** Getters. **/
    /**
     * Get error panel. It contains error label.
     * @return
     */
    public HTMLPanel getErrorPanel() {
        return errorPanel;
    }

    /**
     * Get error label. Validation messages are displayed through this label.
     * @return
     */
    public Label getErrorLabel() {
        return errorLabel;
    }

    /**
     * Get control group. It contains panel for holding validating component and
     * error label.
     * @return
     */
    public ControlGroup getControlGroup() {
        return controlGroup;
    }

    /**
     * Get component's value.
     * @return
     */
    public Object getValue() {
        return getInputWidgetText();
    }

    /**
     * Get this widget.
     * @return
     */
    public Widget getChangeMonitorWidget() {
        return this;
    }

    /** Setters. **/
    /**
     * Set component's value.
     * @param value
     */
    public void setValue(Object value) {
        setInputWidgetText(value);
    }

    /**
     * Hide error panel if needed.
     * @param hideErrorPanel
     */
    public void setHideErrorPanel(boolean hideErrorPanel) {
        this.hideErrorPanel = hideErrorPanel;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * When entering input widget and leaving making some changes - call validate methods
     * in case validation constraints are met. Validation process then displays appropriate
     * error message.
     *
     * Only input widgets of types CellList and DateBox has registered change handler.
     *
     * @param w - input widget
     */
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

    /**
     * When entering input widget and leaving - call validate method in case no data were
     * provided (text, numbers, etc..). Validation process displays appropriate error message
     * if validation constraints are met. Each widget input widget has registered blur handler.
     *
     * Only input widgets of types TextBox, IntegerBox and BigDecimalBox has registered change handler.
     *
     * @param w - input widget
     */
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

    /**
     * Get component's value that is validated.
     * @return
     */
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

    /**
     * Set component's value.
     * @param value
     */
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
