//$Id: EmailValidator.java 19796 2010-06-23 11:23:07Z hardy.ferentschik $
package com.eprovement.poptavka.client.main.common.validation;

import com.google.gwt.regexp.shared.RegExp;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Checks that a given string is a well-formed email address. <p> The
 * specification of a valid email can be found in <a
 * href="http://www.faqs.org/rfcs/rfc2822.html">RFC 2822</a> and one can come up
 * with a regular expression matching <a
 * href="http://www.ex-parrot.com/~pdw/Mail-RFC822-Address.html"> all valid
 * email addresses</a> as per specification. However, as this <a
 * href="http://www.regular-expressions.info/email.html">article</a> discusses
 * it is not necessarily practical to implement a 100% compliant email
 * validator. This implementation is a trade-off trying to match most email
 * while ignoring for example emails with double quotes or comments. </p>
 *
 * Martin: RegExp used instead of java.util.rexep - it's not supported when
 * compliled to javascript.
 *
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 * @author Martin Slavkovsky
 */
public class EmailValidator
        implements ConstraintValidator<com.eprovement.poptavka.client.main.common.validation.Email, String> {

    private static final String ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
    private static final String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)*";
    private static final String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";
    private RegExp pattern = RegExp.compile(
            "^" + ATOM + "+(\\." + ATOM + "+)*@"
            + DOMAIN
            + "|"
            + IP_DOMAIN
            + ")$", "i");

    @Override
    public void initialize(com.eprovement.poptavka.client.main.common.validation.Email annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() == 0) {
            return true;
        }
        return pattern.test(value);
    }
}
