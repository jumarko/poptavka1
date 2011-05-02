package cz.poptavka.sample.application.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;

/**
 * Aspect which is responsible for measuring and logging execution time of all methods.
 * This may have an notable impact on performance, therefore it should be used in development mode for
 * performance testing.
 * In production it should be left out.
 *
 * @author Juraj Martinka
 *         Date: 10.4.11
 */
//@Aspect
public class ExecutionTimeLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionTimeLogger.class);

//    @Around("execution(* cz.poptavka.sample..*.*(..)) "
//            + " && ! execution(* cz.poptavka.sample.application.logging.*.*(..))")
    public Object logTimeMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Object retVal = joinPoint.proceed();

        stopWatch.stop();

        final StringBuffer logMessage = new StringBuffer();
        logMessage.append(joinPoint.getSignature().getDeclaringType().getName());
        logMessage.append(".");
        logMessage.append(joinPoint.getSignature().getName());
        logMessage.append("(");
        // append args
        final Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            logMessage.append(args[i]).append(",");
        }
        if (args.length > 0) {
            logMessage.deleteCharAt(logMessage.length() - 1);
        }

        logMessage.append(")");
        logMessage.append(" execution time: ");
        logMessage.append(stopWatch.getTotalTimeMillis());
        logMessage.append(" ms");
        LOGGER.info(logMessage.toString());
        return retVal;
    }

}
