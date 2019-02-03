package com.helloingob.bluefever.data.watcher;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.helloingob.bluefever.CentralMain;
import com.helloingob.bluefever.data.JobTO;
import com.helloingob.bluefever.data.JobTO.JobType;

public class TemperatureWatcher implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        CentralMain.getJobQueue().add(new JobTO(JobType.GET_TEMPERATURE, null));
    }

}
