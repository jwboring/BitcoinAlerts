package org.boring.bitcoinAlerts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.boring.bitcoinAlerts.domain.BtcMetrics;
import org.boring.bitcoinAlerts.domain.Price;
import org.boring.bitcoinAlerts.watcher.BtcNetworkMetricWatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
// https://stackoverflow.com/questions/33555283/why-is-not-possible-to-reopen-a-closed-standard-stream


/**
 *
 */
@Component
@Scope (value="prototype")
@Lazy (value = true)
public class ConsoleScanner {
	private static final Logger log = LogManager.getLogger(ConsoleScanner.class);

	private BtcNetworkMetricWatcher myWatcher;

	private List<BtcNetworkMetricWatcher> watchers = new ArrayList<BtcNetworkMetricWatcher>();
	private int watcherInstance = 0;

	private BtcMetrics btcMetrics;

	private final String[] commandsToEnd = new String[] {"QUIT", "BYE" , "EXIT", "END"};
	private final String[] commandsToWatch = new String[] {"WATCH", "W"};
	private final String[] commandsToListTargets = new String[] {"LISTTARGETS", "LT"};
	private final String[] commandsToListMetrics = new String[] {"LISTMETRICS", "LM"};
	private final String[] commandsToSwitchConsole = new String[] {"SWITCH"};

	private final String WATCH_COMMAND = "WATCH";
	private final String LISTTARGETS_COMMAND = "LISTTARGETS";
	private final String LISTMETRICS_COMMAND = "LISTMETRICS";
	private final String SWITCHCONSOLE_COMMAND = "SWITCH";



	@Autowired public ConsoleScanner(BtcMetrics btcMetrics) {
		this.btcMetrics = btcMetrics;
	}

	/**
	 *
	 */
	public void scanConsole() {
		String command = null;
		Scanner scanner = new Scanner(System.in);


		while (true) {
			System.out.println(this.myWatcher.getWatchName()  + " scanner active");
			String line = scanner.nextLine();

			if (!line.isBlank()) {
			
				line = line.toUpperCase();
	
				if (matches(commandsToEnd,line)) {
					System.out.println("Goodbye!");
					scanner.close(); // but do we need to stop them all?
					System.exit(0);
					break;
					
				} else if (matches(commandsToListTargets,line)) {
					command = LISTTARGETS_COMMAND;
					listPricesToWatch();

				} else if (matches(commandsToListMetrics,line)) {
					command = LISTMETRICS_COMMAND;
					System.out.println(listMetrics());
				}
				else if (matches(commandsToSwitchConsole,line)) {
					command = SWITCHCONSOLE_COMMAND;
					watcherInstance = watcherInstance +1;
					try{
						myWatcher = watchers.get(watcherInstance);
					}
					catch(Exception e) {
						watcherInstance = 0;
						myWatcher = watchers.get(watcherInstance);
					}

				}
				else {
					String[] tmp = line.split(";");
	
					if (tmp.length != 2) {
						System.out.println("Bad entry: COMMAND;Price");
					} else {
						command = tmp[0];
	
						// use a switch for future commands
						switch (command) {
							case WATCH_COMMAND:
								Float newTarget = null;

								try {
									newTarget = Float.parseFloat(tmp[1]);
									myWatcher.getPricesToWatch().add(new Price(myWatcher.getCurrentPrice(), newTarget));
								} catch (Exception e) {
									newTarget = null;
								}

								if (newTarget == null) {
									System.out.println("Bad price");
								}
								break;
						}
					}
				}
			}
		}
		scanner.close();
	}
	

	private void listPricesToWatch() {
		
		if (this.myWatcher.getPricesToWatch().isEmpty()) {
			System.out.println("No prices to watch");
		} else {
			System.out.println(this.myWatcher.getWatchName() + " Prices to watch:" );

			for (Price price : myWatcher.getPricesToWatch()) {
				System.out.println("\t" + price.type + "\t" + price.target);
			}
		}
	}

	public String listMetrics() {
		return String.format("{btcMetrics=%s}", btcMetrics.toString());
	}

	/**
	 * @param commands
	 * @param inCommand
	 * @return boolean
	 */
	private boolean matches(String[] commands, String inCommand) {
		
		for (String cmd : commands) {
			if (inCommand.startsWith(cmd))
				return true;
		}
		return false;
	}

	public void setMyWatcher(BtcNetworkMetricWatcher myWatcher) {
		this.myWatcher = myWatcher;
	}

	public void setWatchers(List<BtcNetworkMetricWatcher> watchers) {
		this.watchers = watchers;
	}
}
