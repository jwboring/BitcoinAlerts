package org.boring.bitcoinAlerts;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.boring.bitcoinAlerts.smsSender.VerizonSender;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Starter {
	private static final Logger log = LogManager.getLogger(Starter.class);
	
	private static ApplicationContext appContext;
	
	public static void main(String[] args) {
		
		Properties sysProps = System.getProperties();
        log.debug("JRE Vendor: "+ sysProps.getProperty("java.vendor"));
        log.debug("JRE Version: "+ sysProps.getProperty("java.version"));
        log.debug("Classpath:\n%s" + sysProps.getProperty("java.class.path").replaceAll(";", "\n"));
        
		appContext = new ClassPathXmlApplicationContext("appContext.xml");
		
		appContext.getBean("verizonSender", VerizonSender.class);
		
		ConsoleScanner scanner = appContext.getBean("consoleScanner", ConsoleScanner.class);
		scanner.scanConsole();

	}

}
