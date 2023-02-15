package org.boring.bitcoinAlerts.watcher;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component("scheduledJobs")
public class ScheduledJobs {
	
	private final TaskScheduler executor;

    @Autowired
    public ScheduledJobs(TaskScheduler taskExecutor) {
        this.executor = taskExecutor;
    }
	
	
    public void scheduleAtAFixedRate(final Runnable task, int rate) {
    	executor.scheduleAtFixedRate(task, rate);
    	
    	
    }
    
    
    public void scheduleAtAFixedRate(final Runnable task, int rate, int initDelayMilSecs) {
    	
    	executor.scheduleWithFixedDelay(task, Date.from(LocalDateTime.now().plusSeconds(initDelayMilSecs)
                .atZone(ZoneId.systemDefault()).toInstant()), rate);
    }
    

}
