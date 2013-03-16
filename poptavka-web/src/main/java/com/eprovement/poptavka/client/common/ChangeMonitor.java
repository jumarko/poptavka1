/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.eprovement.poptavka.shared.domain.IListDetailObject;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
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
public class ChangeMonitor<T> extends Composite implements HasWidgets, HasChangeHandlers {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    interface ChangeMonitorUiBinder extends UiBinder<Widget, ChangeMonitor> {
    }
    private static ChangeMonitorUiBinder uiBinder = GWT.create(ChangeMonitorUiBinder.class);
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField SimplePanel holder;
    @UiField Anchor revert;
    @UiField Label errorLabel;
    /** Class attributes. **/
    private boolean enabled = true;
    private boolean valid = true;
    private Validator validator = null;
    private ChangeDetail changeDetail;
    private Class<T> beanType;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public ChangeMonitor(Class<T> beanType, ChangeDetail changeDetail) {
        this.beanType = beanType;
        this.changeDetail = changeDetail;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        initWidget(uiBinder.createAndBindUi(this));
        revert.setVisible(false);
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    @UiHandler("revert")
    public void revertClickHandler(ClickEvent e) {
        revert();
        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), getChangeMonitorWidget());
    }

    /**************************************************************************/
    /* Validation                                                             */
    /**************************************************************************/
    private boolean validate() {
        boolean valid = false;
        reset();
        //perform new validation
        Set<ConstraintViolation<T>> violations = validator.validateValue(beanType,
                changeDetail.getField(), getValue(), Default.class);
        if (violations.isEmpty()) {
            valid = true;
        }
        for (ConstraintViolation<T> violation : violations) {
            setValidationStyles(false, violation.getMessage());
        }
        this.valid = valid;
        return valid;
    }

    public boolean isValid() {
        return validate();
    }

    /**************************************************************************/
    /* HasWidgets                                                             */
    /**************************************************************************/
    public Widget getChangeMonitorWidget() {
        return this;
    }

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
    /* HasHandlers, HasChangeHandler                                          */
    /**************************************************************************/
    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        return addDomHandler(handler, ChangeEvent.getType());
    }

    /**************************************************************************/
    /* Methods                                                                */
    /**************************************************************************/
    /** Setters. **/
    public void setBothValues(Object value) {
        changeDetail.setOriginalValue(value);
        changeDetail.setValue(value);
        setInputWidgetText(value);
    }

    public void setValue(Object value) {
        changeDetail.setValue(value);
        setInputWidgetText(value);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setChangeDetail(ChangeDetail detail) {
        changeDetail = detail;
        setChangedStyles(true);
        setInputWidgetText(detail.getValue());
    }

    /** Getters. **/
    public Object getValue() {
        return getInputWidgetText();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ChangeDetail getChangeDetail() {
        return changeDetail;
    }

    public ChangeDetail getChangeDetailCopy() {
        ChangeDetail detail = new ChangeDetail(changeDetail.getField());
        detail.setOriginalValue(changeDetail.getOriginalValue());
        detail.setValue(changeDetail.getValue());
        return detail;
    }

    public boolean isModified() {
        //If DB data will be correct, this can be avoided
        if (changeDetail.getOriginalValue() == null) {
            return true;
        } else {
            return !changeDetail.getOriginalValue().equals(getInputWidgetText());
        }
    }

    /** Change operations. **/
    public void commit() {
        changeDetail.revert();
        setChangedStyles(false);
    }

    public void revert() {
        if (isModified()) {
            changeDetail.revert();
            setInputWidgetText(changeDetail.getOriginalValue());
        }
        reset();
    }

    public void reset() {
        setValidationStyles(true, "");
        setChangedStyles(false);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void addChangeHandler(Widget w) {
        if (w instanceof DateBox) {
            addChangeHandlerToDateBox(w);
        } else {
            addChangeHandlerToWidget(w);
        }
    }

    private void addChangeHandlerToDateBox(Widget w) {
        w.addHandler(new ValueChangeHandler<IListDetailObject>() {
            @Override
            public void onValueChange(ValueChangeEvent<IListDetailObject> event) {
                validate();
                if (enabled) {
                    setChangedStyles(true);
                    DomEvent.fireNativeEvent(Document.get().createChangeEvent(), getChangeMonitorWidget());
                }
            }
        }, ValueChangeEvent.getType());
    }

    private void addChangeHandlerToWidget(Widget w) {
        w.addDomHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                validate();
                if (enabled) {
                    setChangedStyles(true);
                }
            }
        }, ChangeEvent.getType());
    }

    /**
     * When entering input widget and leaving - call validate method in case no data were
     * provided (text, numbers, etc..). Validation process displays appropriate error message
     * if validation constraints are met. Each widget input widget has registered blur handler.
     *
     * Only input widgets of types TextBoxBase, IntegerBox and BigDecimalBox has registered change handler.
     *
     * @param w - input widget
     */
    private void addBlurHandler(Widget w) {
        if (w instanceof ValueBoxBase || w instanceof HasValue) {
            w.addDomHandler(new BlurHandler() {
                @Override
                public void onBlur(BlurEvent event) {
                    validate();
                }
            }, BlurEvent.getType());
        }
    }

    public void setChangedStyles(boolean isChange) {
        revert.setVisible(isChange);
        if (valid) {
            if (isChange) {
                holder.getWidget().addStyleName(Storage.RSCS.common().changed());
            } else {
                holder.getWidget().removeStyleName(Storage.RSCS.common().changed());
            }
        }
    }

    public void setValidationStyles(boolean isValid, String validationMessage) {
        errorLabel.setText(validationMessage);
        if (isValid) {
            holder.getWidget().removeStyleName(Storage.RSCS.common().errorField());
        } else {
            holder.getWidget().addStyleName(Storage.RSCS.common().errorField());
        }
    }

    private Object getInputWidgetText() {
        if (holder.getWidget() instanceof HasValue) {
            return ((HasValue) holder.getWidget()).getValue();
        } else if (holder.getWidget() instanceof ListBox) {
            return ((ListBox) holder.getWidget()).getSelectedIndex();
        } else if (holder.getWidget() instanceof CellList) {
            return changeDetail.getValue();
        } else {
            return null;
        }
    }

    private void setInputWidgetText(Object value) {
        if (holder.getWidget() instanceof HasValue) {
            ((HasValue) holder.getWidget()).setValue(value);
        } else if (holder.getWidget() instanceof ListBox) {
            ((ListBox) holder.getWidget()).setSelectedIndex((Integer) value);
        } else if (holder.getWidget() instanceof CellList) {
            ((CellList) holder.getWidget()).setRowData((List) value);
        }
    }
}
