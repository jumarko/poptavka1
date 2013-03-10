/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.jobs;

import com.eprovement.poptavka.service.jobs.base.Job;
import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

/**
 * Wrapper for Job tasks to allow job auditing and fine grained control.
 * Job task can be executed.
 */
public class JobExecution implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobExecution.class);

    private final String jobName;
    private final Job job;

    private Future jobExecutionFuture;
    private volatile DateTime startTime;
    private volatile DateTime endTime;
    private volatile boolean finished;


    public JobExecution(String jobName, Job job) {
        Validate.notEmpty(jobName, "jobName cannot be empty!");
        Validate.notNull(job, "job cannot be null!");
        this.jobName = jobName;
        this.job = job;
    }

    /**
     * Executes jobs while logging some useful info.
     */
    @Override
    public void run() {
        LOGGER.info("action=job_execution job={} status=start", jobName);
        this.startTime = new DateTime();
        try {
            job.execute();
        } finally {
            this.endTime = new DateTime();
            this.finished = true;
            LOGGER.info("action=job_execution job={} status=finish duration={} ms", jobName,
                    new Duration(startTime, endTime).getMillis());
        }
    }

    /**
     * Tries to cancel job execution.
     * Note that if jobs is already running it might not be cancelled at all - it depends on job logic itself.
     */
    public void cancel() {
        if (jobExecutionFuture != null) {
            jobExecutionFuture.cancel(true);
        }
    }

    /**
     * @return job task start time or null if job has not been started yet
     */
    public DateTime getStartTime() {
        return startTime;
    }

    /**
     * @return job task end time or null if job has not been finished yet
     */
    public DateTime getEndTime() {
        return endTime;
    }

    /**
     * @return true if this job already finished, false otherwise
     */
    public boolean isFinished() {
        return finished;
    }

    public boolean isCanceled() {
        return jobExecutionFuture.isCancelled() && jobExecutionFuture.isCancelled();
    }


    public void setJobExecutionFuture(Future jobExecutionFuture) {
        this.jobExecutionFuture = jobExecutionFuture;
    }
}
