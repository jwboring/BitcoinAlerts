package org.boring.bitcoinAlerts.watcher;

import java.util.List;

import org.boring.bitcoinAlerts.domain.Price;
import org.boring.bitcoinAlerts.smsSender.BtcNetworkMetricListener;

public interface WatcherService  {
	
	public void addListener(BtcNetworkMetricListener toAdd);
	public void monitor();
	public List<Price> getPricesToWatch();
}
