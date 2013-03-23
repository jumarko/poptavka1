/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.jobs;

import com.eprovement.poptavka.rest.ResourceNotFoundException;
import com.eprovement.poptavka.rest.common.ResourceUtils;
import com.eprovement.poptavka.service.jobs.base.Job;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;

/**
 * Main resource for all jobs.
 * <ul>
 *     <li>It can list all jobs via GET "/jobs"</li>
 *     <li>It can execute any available job via POST "/jobs/jobName" - USE WITH CARE!</li>
 * </ul>
 *
 */
@Controller
@RequestMapping(JobsResource.RESOURCE_URI)
public class JobsResource {

    static final String RESOURCE_URI = "/jobs";

    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResource.class);
    private static final int JOBS_EXECUTIONS_MAX_COUNT = 100000;

    private final Map<String, Job> jobs = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, List<JobExecution>> jobsExecutions = new ConcurrentHashMap<>();
    private final SchedulingTaskExecutor taskExecutor;


    public JobsResource(@Qualifier("jobExecutor") SchedulingTaskExecutor jobExecutor, Map<String, Job> allJobs) {
        Validate.notNull(jobExecutor, "jobExecutor cannot be null!");
        Validate.notNull(allJobs, "allJobs cannot be null!");
        this.taskExecutor = jobExecutor;
        this.jobs.putAll(allJobs);

        // initialize jobs execution with empty execution lists for each job
        for (Map.Entry<String, Job> jobEntry : allJobs.entrySet()) {
            jobsExecutions.put(jobEntry.getKey(), new ArrayList<JobExecution>());
        }
    }


    /**
     * List all jobs set up in application..
     * @return list of all configured jobs.
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Collection<JobDetail> listJobs()  {
        final ArrayList<JobDetail> jobDetails = new ArrayList<>();
        for (Map.Entry<String, Job> jobEntry : jobs.entrySet()) {
            jobDetails.add(new JobDetail(jobEntry.getKey(), jobEntry.getValue().description(),
                    jobEntry.getValue().getClass()));
        }
        return jobDetails;
    }

    /**
     * Get details of one given job.
     * @param jobName name of job
     * @param response http response
     * @throws ResourceNotFoundException if no job for given {@code jobName} is found
     */
    @RequestMapping(value = "/{jobName}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody JobDetail getJob(@PathVariable String jobName)  {
        if (! jobs.containsKey(jobName)) {
            throw new ResourceNotFoundException("Job '" + jobName + " does not exist!");
        }
        final Job job = jobs.get(jobName);
        return new JobDetail(jobName, job.description(), job.getClass());
    }

    /**
     * Executes job with given name if it is not already running. Otherwise
     * @param jobName
     * @param response
     */
    @RequestMapping(value = "/{jobName}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void executeJob(@PathVariable String jobName, HttpServletResponse response)  {
        if (! jobs.containsKey(jobName)) {
            throw new ResourceNotFoundException("Job '" + jobName + " does not exist!");
        }
        submitJobForExecution(jobName);
        // redirect to the job execution
        response.setHeader("Location", ResourceUtils.generateSelfLink(RESOURCE_URI, "/" + jobName + "/executions"));
    }

    /**
     * Get execution detail of particular job
     * @param jobName
     * @return
     */
    @RequestMapping(value = "/{jobName}/executions", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<JobExecutionDto> getJobExecutions(@PathVariable String jobName)  {
        if (! jobs.containsKey(jobName)) {
            throw new ResourceNotFoundException("No job=" + jobName + " found!");
        }
        final ArrayList<JobExecutionDto> executionsDtos = new ArrayList<JobExecutionDto>();
        for (JobExecution execution : jobsExecutions.get(jobName)) {
            executionsDtos.add(new JobExecutionDto(jobName, execution.getStartTime(), execution.getEndTime(),
                    execution.isFinished(), execution.isCanceled()));
        }
        sortJobsByStartTimeDescending(executionsDtos);
        return executionsDtos;
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------

    /**
     * Submits job with given name for execution.
     * @param jobName name of job which should be executed
     */
    private void submitJobForExecution(String jobName) {
        LOGGER.info("action=schedule_job job={} status=start", jobName);
        final JobExecution jobExecutionTask = new JobExecution(jobName, jobs.get(jobName));
        final Future<?> jobExecutionFuture = taskExecutor.submit(jobExecutionTask);
        jobExecutionTask.setJobExecutionFuture(jobExecutionFuture);
        this.jobsExecutions.get(jobName).add(jobExecutionTask);
        LOGGER.info("action=schedule_job job={} status=finish. This does not been that job finished"
                + " but only that job has been submitted for execution.", jobName);
    }

    private void sortJobsByStartTimeDescending(List<JobExecutionDto> executionsDtos) {
        Collections.sort(executionsDtos, new Comparator<JobExecutionDto>() {
            @Override
            public int compare(JobExecutionDto o1, JobExecutionDto o2) {
                if (o1.getStartTime() == null && o2.getStartTime() == null) {
                    return 0;
                }
                if (o1.getStartTime() == null) {
                    return -1;
                }
                if (o1.getStartTime() != null && o2.getStartTime() != null) {
                    // the most recent start time should be the first
                    return -(o1.getStartTime().compareTo(o2.getStartTime()));
                }

                return -1;
            }
        });
    }
}
