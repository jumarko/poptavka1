package cz.poptavka.sample.application.logging;

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
public class ExceptionLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionLogger.class);

    public void logExceptionMethod(Exception exception) {
        LOGGER.error("An exception has been thrown.", exception);
    }

}
