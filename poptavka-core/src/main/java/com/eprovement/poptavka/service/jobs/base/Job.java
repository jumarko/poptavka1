package com.eprovement.poptavka.service.jobs.base;

/**
 *
 * Basic interface that all jobs should implement.
 *
 * @author Juraj Martinka
 *         Date: 4.4.11
 */
public interface Job {

    String EVERY_MIDNIGHT = "0 59 23 * * ? ";
    String EVERY_HOUR = "0 0 * * * ? ";

    /**
     * Runs a job.
     */
    void execute();

    /**
     * Provides brief description about job, its usages, constraints, etc.
     * @return job description.
     */
    String description();
}
