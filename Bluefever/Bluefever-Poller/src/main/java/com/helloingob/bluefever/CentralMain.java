package com.helloingob.bluefever;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.helloingob.bluefever.data.JobTO;
import com.helloingob.bluefever.data.job.CleanUpJob;
import com.helloingob.bluefever.data.job.GetTemperatureJob;
import com.helloingob.bluefever.data.job.SetTemperatureJob;
import com.helloingob.bluefever.data.to.ThermostatJobTO;
import com.helloingob.bluefever.data.watcher.CleanupWatcher;
import com.helloingob.bluefever.data.watcher.DatabaseWatcher;
import com.helloingob.bluefever.data.watcher.TemperatureWatcher;
import com.helloingob.bluefever.util.LogFileWriter;

public class CentralMain {

    private static List<JobTO> jobQueue = Collections.synchronizedList(new ArrayList<JobTO>());

    public static void main(String[] args) {
        Trigger databaseWatcherTrigger = TriggerBuilder.newTrigger().withSchedule(cronSchedule("0 0/1 * * * ?")).build(); //Every Minute
        Trigger temperatureWatcherTrigger = TriggerBuilder.newTrigger().withSchedule(cronSchedule("0 0 0/1 * * ?")).build(); //Every Hour
        Trigger cleanUpWatcherTrigger = TriggerBuilder.newTrigger().withSchedule(cronSchedule("0 0 0 * * ?")).build(); //00:00:00 Database cleanup

        Scheduler scheduler;
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();

            JobDetail databaseWatcherJob = newJob(DatabaseWatcher.class).build();
            scheduler.scheduleJob(databaseWatcherJob, databaseWatcherTrigger);

            JobDetail temperatureWatcherJob = newJob(TemperatureWatcher.class).build();
            scheduler.scheduleJob(temperatureWatcherJob, temperatureWatcherTrigger);

            JobDetail cleanUpWatcherJob = newJob(CleanupWatcher.class).build();
            scheduler.scheduleJob(cleanUpWatcherJob, cleanUpWatcherTrigger);
        } catch (SchedulerException e) {
            LogFileWriter.writeErrorLogLine(e);
        }

        while (true) {
            synchronized (jobQueue) {
                ListIterator<JobTO> iter = jobQueue.listIterator();
                while (iter.hasNext()) {
                    JobTO queuedJob = iter.next();
                    LogFileWriter.writeDebugLogLine("Jobs queued: " + jobQueue.size());

                    switch (queuedJob.getJobType()) {
                    case GET_TEMPERATURE: {
                        GetTemperatureJob.execute();
                        break;
                    }
                    case SET_TEMPERATURE: {
                        SetTemperatureJob.execute((ThermostatJobTO) queuedJob.getParameter());
                        break;
                    }
                    case CLEANUP: {
                        CleanUpJob.execute();
                        break;
                    }
                    }
                    LogFileWriter.writeDebugLogLine("Remove Job (" + queuedJob.getJobType() + ")");
                    iter.remove();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LogFileWriter.writeErrorLogLine(e);
                }
            }
        }
    }

    public static List<JobTO> getJobQueue() {
        return jobQueue;
    }

}
