/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.jobs;

import com.eprovement.poptavka.service.jobs.base.Job;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Represents basic details about job itself transferred to the client.
 */
public class JobDetail {
    private final String jobName;
    private final String jobDescription;
    private final Class<? extends Job> jobClass;


    @JsonCreator
    public JobDetail(@JsonProperty("jobName") String jobName, @JsonProperty("jobDescription")String jobDescription,
                     @JsonProperty("jobClass")Class<? extends Job> jobClass) {
        this.jobName = jobName;
        this.jobDescription = jobDescription;
        this.jobClass = jobClass;
    }

    public String getJobName() {
        return jobName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public Class<? extends Job> getJobClass() {
        return jobClass;
    }
}
