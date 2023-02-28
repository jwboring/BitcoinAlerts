package org.boring.bitcoinAlerts;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.boring.bitcoinAlerts.dao.WatchDao;
import org.boring.bitcoinAlerts.domain.Watch;
import org.boring.bitcoinAlerts.smsSender.VerizonSender;
import org.boring.bitcoinAlerts.watcher.BtcNetworkMetricWatcher;
import org.boring.bitcoinAlerts.watcher.ScheduledJobs;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jeff W Boring
 * 
 * 
 * 
 * https://stackoverflow.com/questions/70225302/how-to-schedule-a-task-at-runtime-where-the-frequency-of-the-scheduler-is-read-f
 * https://stackoverflow.com/questions/46974272/how-to-add-new-schedule-job-dynamically-with-spring
 *
 */
public class Starter {
	private static final Logger log = LogManager.getLogger(Starter.class);
	
	private static ApplicationContext appContext;
	private static WatchDao watchDao;
	
	private static List<BtcNetworkMetricWatcher> watchers = new ArrayList<BtcNetworkMetricWatcher>();
	
	
	public static void main(String[] args) {
		
		Properties sysProps = System.getProperties();
        log.debug("JRE Vendor: "+ sysProps.getProperty("java.vendor"));
        log.debug("JRE Version: "+ sysProps.getProperty("java.version"));
        log.debug("Classpath:\n%s" + sysProps.getProperty("java.class.path").replaceAll(";", "\n"));
        
		appContext = new ClassPathXmlApplicationContext("appContext.xml");
		VerizonSender verizonSender = appContext.getBean("verizonSender", VerizonSender.class);
		
		WatchDao watchDao = appContext.getBean("watchDaoMySql", WatchDao.class);
		ScheduledJobs scheduledJobs = appContext.getBean("scheduledJobs",ScheduledJobs.class);

		BtcNetworkMetricWatcher addToScanner = null;

		List<Watch> watches = watchDao.getAllWatches();
		
		for (Watch watch : watches) {
			BtcNetworkMetricWatcher btcNetworkMetricWatcher = appContext.getBean("btcNetworkMetricWatcher", BtcNetworkMetricWatcher.class);
			watchers.add(btcNetworkMetricWatcher);
			btcNetworkMetricWatcher.setWatchName(watch.getName());
			btcNetworkMetricWatcher.setWatchDescription(watch.getDescription());
			btcNetworkMetricWatcher.setMessage(watch.getMessage());
			btcNetworkMetricWatcher.setHasTargets(watch.isHasTargets());
			
			boolean bool = !watch.isHasTargets();
			btcNetworkMetricWatcher.setSkipAdjustWatchlist(bool);
			
			btcNetworkMetricWatcher.addListener(verizonSender);
			
			if (watch.getUrls().size() > 0) {
				btcNetworkMetricWatcher.setUrl(watch.getUrls().get(0).getUrl());
				scheduledJobs.scheduleAtAFixedRate(btcNetworkMetricWatcher, watch.getFixedDelayMilSecs(), watch.getInitDelaySecs());

			}
		}

		ConsoleScanner scanner = appContext.getBean("consoleScanner", ConsoleScanner.class);

		scanner.setWatchers(watchers);
		scanner.setMyWatcher(watchers.get(0));

		scanner.scanConsole();
		
	}

}


















