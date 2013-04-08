package com.eprovement.poptavka.service.jobs.base;

/**
 *
 * Basic interface that all jobs should implement.
 *
 * @author Juraj Martinka
 *         Date: 4.4.11
 */
public interface Job {

    String EVERY_HOUR = "0 0 * * * ? ";
    String EVERY_MIDNIGHT = "0 59 23 * * ? ";
    /** Once a day early in the morning */
    String EVERY_DAY = "0 59 04 * * ? ";
    /** Each monday (once a week) early in the morning */
    String EVERY_WEEK = "0 59 04 * * MON ";


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
