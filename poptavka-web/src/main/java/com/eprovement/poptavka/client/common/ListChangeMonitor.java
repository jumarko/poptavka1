/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

/**
 * Widget validates and monitors changes of wrapped widget.
 * This widget support only CellList as inner widget.
 *
 * @author Martin Slavkovsky
 */
public class ListChangeMonitor<T> extends Composite implements HasWidgets, HasChangeHandlers {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    interface ChangeMonitorUiBinder extends UiBinder<Widget, ListChangeMonitor> {
    }
    private static ChangeMonitorUiBinder uiBinder = GWT.create(ChangeMonitorUiBinder.class);
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField
    SimplePanel holder;
    @UiField
    Anchor revert;
    @UiField
    Label errorLabel;
    /** Class attributes. **/
    private Validator validator = null;
    private ChangeDetail changeDetail;
    private Class<T> beanType;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public ListChangeMonitor(Class<T> beanType, ChangeDetail changeDetail) {
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
    public void validate() {
        reset();
        //perform new validation
        Set<ConstraintViolation<T>> violations = validator.validateValue(beanType,
                changeDetail.getField(), getValue(), Default.class);
        for (ConstraintViolation<T> violation : violations) {
            setValidationStyles(false, violation.getMessage());
        }
    }

    public boolean isValid() {
        return errorLabel.getText().isEmpty();
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
        holder.setWidget(w);
    }

    @Override
    public void add(Widget w) {
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
        if (isModified()) {
            validate();
            setChangedStyles(true);
            DomEvent.fireNativeEvent(Document.get().createChangeEvent(), getChangeMonitorWidget());
        }
    }

    /** Getters. **/
    public Object getValue() {
        return getInputWidgetText();
    }

    public boolean isModified() {
        //If DB data will be correct, this can be avoided
        if (changeDetail.getOriginalValue() == null) {
            return true;
        } else {
            return !changeDetail.getOriginalValue().equals(getInputWidgetText());
        }
    }

    public ChangeDetail getChangeDetail() {
        return changeDetail;
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
    private void setChangedStyles(boolean isChange) {
        revert.setVisible(isChange);
        if (isValid()) {
            if (isChange) {
                holder.getWidget().addStyleName(Storage.RSCS.common().changed());
            } else {
                holder.getWidget().removeStyleName(Storage.RSCS.common().changed());
            }
        }
    }

    private void setValidationStyles(boolean isValid, String validationMessage) {
        errorLabel.setText(validationMessage);
        if (isValid) {
            holder.getWidget().removeStyleName(Storage.RSCS.common().errorField());
        } else {
            holder.getWidget().addStyleName(Storage.RSCS.common().errorField());
        }
    }

    private Object getInputWidgetText() {
        if (holder.getWidget() instanceof CellList) {
            return changeDetail.getValue();
        } else {
            return null;
        }
    }

    private void setInputWidgetText(Object value) {
        if (holder.getWidget() instanceof CellList) {
            ((CellList) holder.getWidget()).setRowData((List) value);
        }
    }
}
