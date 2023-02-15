package bitcoinAlertsorg.boring.bitcoinAlerts;

import org.boring.bitcoinAlerts.watcher.SampleTask;
import org.boring.bitcoinAlerts.watcher.ScheduledJobs;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

class RunnableTest {

	private static ApplicationContext appContext;
	private static SampleTask sampleTask;
	private static ScheduledJobs scheduledJobs;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		appContext = new ClassPathXmlApplicationContext("appContext.xml");
		sampleTask = appContext.getBean("sampleTask",SampleTask.class);
		scheduledJobs = appContext.getBean("scheduledJobs",ScheduledJobs.class);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() throws InterruptedException {
		scheduledJobs.scheduleAtAFixedRate(sampleTask, 2000);
		
		System.out.println();
		
	}

}
