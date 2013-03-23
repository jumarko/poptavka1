/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.jobs;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

/**
 * Wrapper for Job tasks to allow job auditing and fine grained control.
 * Job task can be executed.
 */
public class JobExecutionDto {

    private final String jobName;
    private final DateTime startTime;
    private final DateTime endTime;
    private final boolean finished;
    private final boolean canceled;

    @JsonCreator
    public JobExecutionDto(@JsonProperty("jobName") String jobName,
                           @JsonProperty("startTime") DateTime startTime,
                           @JsonProperty("endTime") DateTime endTime,
                           @JsonProperty("finished") boolean finished,
                           @JsonProperty("canceled") boolean canceled) {
        this.jobName = jobName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.finished = finished;
        this.canceled = canceled;
    }

    public String getJobName() {
        return jobName;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
