package org.boring.bitcoinAlerts;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.boring.bitcoinAlerts.domain.Price;
import org.boring.bitcoinAlerts.watcher.CoindeskCurrentBtcPrice;
import org.boring.bitcoinAlerts.watcher.TwentyFourHrBtcPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsoleScanner {
	private static final Logger log = LogManager.getLogger(ConsoleScanner.class);
	
	@Autowired private CoindeskCurrentBtcPrice coindeskCurrentBtcPrice;
	@Autowired private TwentyFourHrBtcPrice twentyFourHrBtcPrice;
	
//	private List<Price> pricesToWatch;
	private static final String[] commandsToEnd = new String[] {"QUIT", "BYE" , "EXIT", "END"};
	public static final String[] commandsToWatch = new String[] {"WATCH", "W"};
	public static final String[] commandsToList = new String[] {"LIST", "L"};
	
	public static final String WATCH_COMMAND = "WATCH";
	public static final String LIST_COMMAND = "LIST";
	
//	private float currentPrice = Float.valueOf(22);
	
	
	
	public void scanConsole() {
		String command = null;
		Scanner scanner = new Scanner(System.in);

		while (true) {
			String line = scanner.nextLine();
			if (!line.isBlank()) {
			
				line = line.toUpperCase();
	
				if (matches(commandsToEnd,line)) {
	//				command = QUIT_COMMAND;
					System.out.println("Goodbye!");
					break;
					
				} else if (matches(commandsToList,line)) {
					command = LIST_COMMAND;
					listPricesToWatch();
	
				} else {
					String[] tmp = line.split(";");
	
					if (tmp.length != 2) {
						System.out.println("Bad entry: COMMAND;Price");
					} else {
						command = tmp[0];
	
						// use a switch for future commands
						switch (command) {
						case WATCH_COMMAND:
							Float price = null;
	
							try {
								price = Float.parseFloat(tmp[1]);
								coindeskCurrentBtcPrice.getPricesToWatch().add(new Price(price, price));
							} catch (Exception e) {
								price = null;
							}
	
							if (price == null) {
								System.out.println("Bad price");
							} else {
	//							Price priceObj = new Price(currentPrice, price);
	//							pricesToWatch.add(priceObj);
	//							System.out.println("Watch for BTC " + priceObj.type + " = " + price);
							}
							break;
						}
					}
				}
			}
		}

		scanner.close();
		System.exit(0);
	}
	
	
	private void listPricesToWatch() {
		
		if (coindeskCurrentBtcPrice.getPricesToWatch().isEmpty()) {
			System.out.println("No prices to watch");
		} else {
			System.out.println("Prices to watch:");

			for (Price price : coindeskCurrentBtcPrice.getPricesToWatch()) {
				System.out.println("\t" + price.type + "\t" + price.target);
			}
		}
	}
	
	
//	@PostConstruct
//	public void init() {
////		pricesToWatch = new ArrayList<Price>(0);
//		scanConsole();
//	}
	
	private boolean matches(String[] commands, String inCommand) {
		
		for (String cmd : commands) {
			if (inCommand.startsWith(cmd))
				return true;
		}
		return false;
	}

}
