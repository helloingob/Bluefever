package com.helloingob.bluefever.data.watcher;

import java.time.LocalDateTime;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.helloingob.bluefever.CentralMain;
import com.helloingob.bluefever.data.JobTO;
import com.helloingob.bluefever.data.ThermostatJobDAO;
import com.helloingob.bluefever.data.JobTO.JobType;
import com.helloingob.bluefever.data.to.ThermostatJobTO;

public class DatabaseWatcher implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        for (ThermostatJobTO thermostatJob : ThermostatJobDAO.get(LocalDateTime.now())) {
            CentralMain.getJobQueue().add(new JobTO(JobType.SET_TEMPERATURE, thermostatJob));
        }
    }

}
