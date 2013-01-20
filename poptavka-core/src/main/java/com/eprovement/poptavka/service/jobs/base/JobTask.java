package com.eprovement.poptavka.service.jobs.base;

/**
 *
 * Basic interface that all scheduled jobs should implement.
 * It contains the only one parameterless method {@link #execute()} that returns void.
 * These are requirements for usage of Spring {@link org.springframework.scheduling.annotation.Scheduled}
 * annotation.
 *
 * @author Juraj Martinka
 *         Date: 4.4.11
 */
public interface JobTask {

    String EVERY_MIDNIGHT = "0 59 23 * * ? ";
    String EVERY_HOUR = "0 0 * * * ? ";
    int HOUR = 3600000;

    void execute();
}
