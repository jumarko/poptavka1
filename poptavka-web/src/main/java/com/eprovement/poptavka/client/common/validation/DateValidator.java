package com.eprovement.poptavka.client.common.validation;

import com.google.gwt.user.datepicker.client.CalendarUtil;
import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Date validator. If given date is greater or equal today's date, consider is as valid;
 *
 * @author Martin Slavkovsky
 */
public class DateValidator
        implements ConstraintValidator<com.eprovement.poptavka.client.common.validation.DateEqualOrGreater, Date> {

    @Override
    public void initialize(com.eprovement.poptavka.client.common.validation.DateEqualOrGreater annotation) {
    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return 0 <= CalendarUtil.getDaysBetween(new Date(), value);
    }
}
