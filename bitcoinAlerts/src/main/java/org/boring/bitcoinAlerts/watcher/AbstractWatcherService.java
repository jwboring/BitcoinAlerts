package org.boring.bitcoinAlerts.watcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.boring.bitcoinAlerts.domain.Price;
import org.boring.bitcoinAlerts.smsSender.BtcNetworkMetricListener;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

public abstract class AbstractWatcherService implements WatcherService  {
	
	
	protected Client client = ClientBuilder.newClient();
	protected boolean firstTime = true; 
	protected List<Price> pricesToWatch = new CopyOnWriteArrayList<Price>();
	protected float currentPrice;
	protected List<BtcNetworkMetricListener> listeners = new ArrayList<BtcNetworkMetricListener>();
	protected float getCurrentPrice() {
		return currentPrice;
	}
	
	
	
	public void addListener(BtcNetworkMetricListener toAdd) {
        listeners.add(toAdd);
    }

	public List<Price> getPricesToWatch() {
		return pricesToWatch;
	}
	
}
