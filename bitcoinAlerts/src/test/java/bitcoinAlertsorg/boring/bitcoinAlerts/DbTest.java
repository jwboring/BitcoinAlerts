package bitcoinAlertsorg.boring.bitcoinAlerts;

import org.boring.bitcoinAlerts.dao.AlertDaoMySql;
import org.boring.bitcoinAlerts.domain.User;
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
	private static AlertDaoMySql alertDaoMySql;
	private static CoindeskCurrentBtcPrice coindeskCurrentBtcPrice;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		appContext = new ClassPathXmlApplicationContext("appContext.xml");
		alertDaoMySql = (AlertDaoMySql)appContext.getBean("alertDaoMySql");
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

//	@Test
	void test1() {
		User user = alertDaoMySql.getUserAndById(1);
		System.out.println(user);
	}
	
	@Test
	void test2() {
		coindeskCurrentBtcPrice.monitor();
	}

}
