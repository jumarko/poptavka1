package com.eprovement.poptavka.application.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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
@Aspect
public class ExecutionTimeLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionTimeLogger.class);

    @Pointcut("execution(* com.eprovement.poptavka.service.jobs..JobTask+.execute())")
    private void jobs() { }

    @Around("jobs()")
    public Object logTimeMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Object retVal = joinPoint.proceed();

        stopWatch.stop();

        final StringBuffer logMessage = new StringBuffer();
        logMessage.append(joinPoint.getTarget());
        logMessage.append(".");
        logMessage.append(joinPoint.getSignature().getName());
        logMessage.append("(");
        // append args
        final Object[] args = joinPoint.getArgs();
        for (final Object arg : args) {
            logMessage.append(arg).append(",");
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
