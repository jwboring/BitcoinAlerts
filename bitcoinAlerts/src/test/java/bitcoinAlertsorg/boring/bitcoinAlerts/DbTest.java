package bitcoinAlertsorg.boring.bitcoinAlerts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.boring.bitcoinAlerts.dao.WatchDaoMySql;
import org.boring.bitcoinAlerts.domain.Watch;
import org.boring.bitcoinAlerts.watcher.CoindeskCurrentBtcPrice;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

class DbTest {
	
	private static ApplicationContext appContext;
	private static WatchDaoMySql watchDaoMySql;
	private static CoindeskCurrentBtcPrice coindeskCurrentBtcPrice;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		appContext = new ClassPathXmlApplicationContext("appContext.xml");
		watchDaoMySql = (WatchDaoMySql)appContext.getBean("watchDaoMySql");
		coindeskCurrentBtcPrice = appContext.getBean("coindeskCurrentBtcPrice",CoindeskCurrentBtcPrice.class);
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
	void test1() {
		List<Watch> watches = watchDaoMySql.getAllWatches();
		System.out.println();
	}
	
//	@Test
	void test2() {
		coindeskCurrentBtcPrice.monitor();
	}
	
	
	
	
//	@Test
	void test3() {
		String coindeskMessage = "Coindesk Current Price ALERT :BTC @target with price : @price on @now.";
		Map<String,String> xx = new HashMap<>();
		xx.put("@now", "02/11/2023 at 01:21 PM EST");
		xx.put("@target", "has risen beyond 22715.0");
		xx.put("@price", "21715.84");
		
		String result = coindeskMessage.replaceFirst("@target",xx.get("@target")).replaceFirst("@price", xx.get("@price")).replaceFirst("@now", xx.get("@now"));
		System.out.println(result);
	}

}
