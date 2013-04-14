/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import java.text.ParseException;
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
    @UiField IconAnchor revert;
    @UiField HTMLPanel errorPanel, changePanel;
    @UiField Label errorLabel;
    @UiField ControlGroup controlGroup;
    /** Class attributes. **/
    private boolean enabled = true;
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
        if (preValidate()) {
            setValidationStyles(true, "");
            //perform new validation
            Set<ConstraintViolation<T>> violations = validator.validateValue(beanType,
                    changeDetail.getField(), getValue(), Default.class);
            for (ConstraintViolation<T> violation : violations) {
                setValidationStyles(false, violation.getMessage());
            }
            return violations.isEmpty();
        }
        return false;
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
        setChangeStyles(true);
        setInputWidgetText(detail.getValue());
    }

    /** Getters. **/
    public Object getValue() {
        if (preValidate()) {
            return getInputWidgetText();
        } else {
            return null;
        }
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
            return !changeDetail.getOriginalValue().equals(getValue());
        }
    }

    /** Change operations. **/
    public void commit() {
        changeDetail.revert();
        setChangeStyles(false);
    }

    public void revert() {
        changeDetail.revert();
        setInputWidgetText(changeDetail.getOriginalValue());
        reset();
    }

    public void reset() {
        setValidationStyles(true, "");
        setChangeStyles(false);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /** Action hadlers.                                                      **/
    //--------------------------------------------------------------------------
    private void addChangeHandler(Widget w) {
        if (w instanceof HasChangeHandlers) {
            addChangeHandlerToWidget(w);
        } else {
            addChangeHandlerToDateBox(w);
        }
    }

    private void addChangeHandlerToDateBox(Widget w) {
        w.addHandler(new ValueChangeHandler<Object>() {
            @Override
            public void onValueChange(ValueChangeEvent<Object> event) {
                if (enabled) {
                    setChangeStyles(true);
                    DomEvent.fireNativeEvent(Document.get().createChangeEvent(), getChangeMonitorWidget());
                }
            }
        }, ValueChangeEvent.getType());
    }

    private void addChangeHandlerToWidget(Widget w) {
        w.addDomHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if (enabled) {
                    setChangeStyles(isModified());
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

    /** Style change mehtods.                                                **/
    //--------------------------------------------------------------------------
    public void setChangeStyles(boolean isChange) {
        changePanel.setVisible(isChange);
        if (isChange) {
            controlGroup.setType(ControlGroupType.SUCCESS);
        } else {
            controlGroup.setType(ControlGroupType.NONE);
        }
    }

    public void setValidationStyles(boolean isValid, String validationMessage) {
        errorPanel.setVisible(!isValid);
        errorLabel.setText(validationMessage);
        if (isValid) {
            if (!enabled || !isModified()) {
                controlGroup.setType(ControlGroupType.NONE);
            }
        } else {
            controlGroup.setType(ControlGroupType.ERROR);
        }
    }

    /** Input methods.                                                       **/
    //--------------------------------------------------------------------------
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

    /**
     * Pre-Check validation. Check validating field for exceptions such as ParseExcpetions, i.e.
     * If no such case is catch, standard validation can continue, otherwise field is validated immediately
     * and the rest of validation process is stopped.
     * @return true if validation can continue, false otherwise
     */
    private boolean preValidate() {
        try {
            if (holder.getWidget() instanceof BigDecimalBoxBase) {
                ((BigDecimalBox) holder.getWidget()).getValueOrThrow();
            } else if (holder.getWidget() instanceof MyIntegerBox) {
                ((MyIntegerBox) holder.getWidget()).getValueOrThrow();
            }
        } catch (ParseException ex) {
            return validateNumberException();
        } catch (NumberFormatException ex) {
            return validateNumberException();
        }
        return true;
    }

    private boolean validateNumberException() {
        setValidationStyles(false, Storage.VMSGS.commonNumberFormat());
        return false;
    }
}
