package cz.poptavka.sample.application.logging;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aspect which is responsible for logging all uncaught exceptions that occur in any method in application.
 *
 * @author Juraj Martinka
 *         Date: 10.4.11
 */
@Aspect
public class ExceptionLoggerAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionTimeLoggerAspect.class);

    @AfterThrowing(
            pointcut = "execution(* cz.poptavka.sample..*.*(..)) "
                    + " and ! execution(* cz.poptavka.sample.application.logging.*.*(..))",
            throwing = "exception")
    public void logExceptionMethod(Exception exception) {
        LOGGER.error("An exception has been thrown.", exception);
    }

}
