package com.helloingob.bluefever.data;

public class JobTO {

    public static enum JobType {
        GET_TEMPERATURE, SET_TEMPERATURE, CLEANUP;
    }

    private JobType jobType;
    private Object parameter;

    public JobTO(JobType jobType, Object parameter) {
        this.jobType = jobType;
        this.parameter = parameter;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public Object getParameter() {
        return parameter;
    }

    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }

}
