/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.monitors;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.Extended;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
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
public abstract class AbstractValidationMonitor<T> extends AbstractMonitor implements ProvidesValidate {

    /** Class attributes. **/
    private Validator validator;
    private Class<T> beanType;
    //Define order for validation annotations by defining order to their validation groups.
    private Class<?>[] beanGroups;
    private String field;
    protected Boolean valid;
    protected boolean externalValidation;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Initialize Validation monitor. Need to manually initialize because we need
     * to know Class<T>.
     * @param beanType
     */
    public AbstractValidationMonitor(Class<T> beanType, Class<?>[] beanGroups, String field) {
        initValidationMonitor(beanType, beanGroups, field);
    }

    /**
     * Initialize Validation monitor. Need to manually initialize because we need
     * to know Class<T>.
     * For groups default values are used: Default.class, Extended.class.
     * @param beanType
     */
    public AbstractValidationMonitor(Class<T> beanType, String field) {
        Class<?>[] groups = {Default.class, Extended.class};
        initValidationMonitor(beanType, groups, field);
    }

    private void initValidationMonitor(Class<T> beanType, Class<?>[] beanGroups, String field) {
        this.beanType = beanType;
        this.beanGroups = beanGroups;
        this.field = field;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**************************************************************************/
    /* Validation                                                             */
    /**************************************************************************/
    /**
     * Provide partial validation of component that was wrapped by validation monitor class.
     */
    public void validate() {
        externalValidation = false;
        setValidationStyles(true, "");
        //perform new validation
        for (Class<?> bean : beanGroups) {
            Set<ConstraintViolation<T>> violations = validator.validateValue(beanType, field, getValue(), bean);
            if (!violations.isEmpty()) {
                setValidationStyles(false, violations.iterator().next().getMessage());
                //If validation constraint on first group class, don't continue.
                break;
            }
        }
    }

    protected void setValidationStyles(boolean isValid, String validationMessage) {
        valid = isValid;
        errorPanel.setVisible(!isValid);
        errorLabel.setText(validationMessage);
        if (isValid) {
            controlGroup.setType(ControlGroupType.NONE);
        } else {
            controlGroup.setType(ControlGroupType.ERROR);
        }
    }

    /**
     * Indicates that external validation manages monitor.
     * Deny internal validation in that case.
     * @return
     */
    public boolean isExternalValidation() {
        return externalValidation;
    }

    /**
     * Tell if component is valid.
     * @return true if valid, false elsewere.
     */
    @Override
    public boolean isValid() {
        /**
         * It can be validated each time isValid() is called.
         * But in cases when external validation is set, validate() removes it.
         * Validation seems to be OK, but it's not, because external validation is missing
         * and it can't be called from here. Therefore validate only those fields
         * that has not been validated yet.
         */
        if (valid == null) {
            validate();
        }
        return valid;
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    public void setExternalValidation(ControlGroupType controlGroupType, String validationMessage) {
        externalValidation = true;
        errorPanel.setVisible(true);
        valid = controlGroupType != ControlGroupType.ERROR;
        errorLabel.setText(validationMessage);
        errorPanel.setVisible(!validationMessage.isEmpty());
        controlGroup.setType(controlGroupType);
    }

    /**
     * Cancel validation in some cases.
     */
    public void resetValidation() {
        setValidationStyles(true, "");
        valid = null;
        externalValidation = false;
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/

    /**************************************************************************/
    /* Protected methods                                                      */
    /**************************************************************************/
    protected boolean validateNumberException() {
        valid = false;
        setValidationStyles(valid, Storage.VMSGS.numberFormat());
        return valid;
    }

    /**
     * Pre-Check validation. Check validating field for exceptions such as NumberFormatExcpetions, i.e.
     * If no such case is catch, standard validation can continue, otherwise field is validated immediately
     * and the rest of validation process is stopped.
     * @return true if validation can continue, false otherwise
     */
    abstract boolean preValidate();
}
