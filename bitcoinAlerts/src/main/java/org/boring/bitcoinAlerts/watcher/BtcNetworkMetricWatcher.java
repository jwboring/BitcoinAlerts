package org.boring.bitcoinAlerts.watcher;


import java.util.ArrayList;
import java.util.Iterator;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.boring.bitcoinAlerts.domain.Alert;
import org.boring.bitcoinAlerts.domain.BtcMetrics;
import org.boring.bitcoinAlerts.domain.Price;
import org.boring.bitcoinAlerts.smsSender.BtcNetworkMetricListener;
import org.json.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Component(value="btcNetworkMetricWatcher")
@Scope (value="prototype")
@Lazy (value = true)
public class BtcNetworkMetricWatcher extends AbstractWatcherService implements Runnable {
	private static final Logger log = LogManager.getLogger(BtcNetworkMetricWatcher.class);
	
	private String watchName;
	private String watchDescription;
	private boolean isHasTargets;
	private String message;
	private String url;
	private boolean skipAdjustWatchlist = false;
	private BtcMetrics btcMetrics;
	
	public BtcNetworkMetricWatcher(BtcMetrics btcMetrics) {
		super();
		this.btcMetrics = btcMetrics;
	}
	
	@Override
	public void run() {
		
		Response response = getResponse();
		int status = response.getStatus();		
		log.debug("status = {}",status);
		
		if (status==200) {
			String json = response.readEntity(String.class);
			
			if (json.endsWith("}")) {
				JSONObject jsonObject = new JSONObject(json);
				this.currentPrice = jsonObject.getJSONObject("bpi").getJSONObject("USD").getFloat("rate_float");
			}
			else {
				this.currentPrice = Float.valueOf(json);
			}
			btcMetrics.setBtcMetric(watchName, this.currentPrice);
			log.debug("{} = {}",watchName, this.currentPrice);
			priceChange(this.currentPrice);
		}
		
	}
	
	private Response getResponse() {
		Response response = client.target(url)
				  .request(MediaType.TEXT_PLAIN_TYPE)
				  .get();
		return response;
	}
	

	private void priceChange(float priceChange) {
		boolean removed=false;

		if (firstTime) {
			firstTime = false;
			adjustWatchlist(priceChange);
		}
		else {
			// we check if one price is reached
			
			boolean didRemoveAPrice = false;

			for (Iterator<Price> it = pricesToWatch.iterator(); it.hasNext();) {

				Price nextPrice = (Price) it.next();
				if (nextPrice.type.reached(priceChange, nextPrice.target)) {
					log.debug("{} size before={}",watchName, pricesToWatch.size());
					log.debug("{} reached target {} current price={}",watchName, nextPrice, priceChange);

					Map<String, String> map = nextPrice.type.msg(priceChange, nextPrice.target);
					String myMessage = completeMessage(map);

					for (BtcNetworkMetricListener priceChangeListener : listeners) {
						priceChangeListener.alert(new Alert(1, myMessage));
					}
					it.remove();
					didRemoveAPrice = true;
				}
			}
			if (didRemoveAPrice)
				adjustWatchlist(priceChange);
			log.debug("{} size after={}",watchName, pricesToWatch.size());
		}
	}
	

	private String completeMessage(Map<String, String> map){

		StringBuffer msg = new StringBuffer();
		msg.append(watchDescription);
		msg.append(" ");
		msg.append(this.message);
		String newMsg = msg.toString();
		if (this.message.contains("@target"))
			newMsg = newMsg.replaceFirst("@target",map.get("@target"));
		if (this.message.contains("@amount"))
			newMsg = newMsg.replaceFirst("@amount",map.get("@amount"));
		if (this.message.contains("@now"))
			newMsg = newMsg.replaceFirst("@now", map.get("@now"));

		return newMsg;
	}



	
	
	public void adjustWatchlist(float theCurrent) {
		
		if (!skipAdjustWatchlist) {
		
			log.debug(theCurrent);
			pricesToWatch = new ArrayList<Price>();

			float d;
			Price down1;
			float u;
			Price up1;
			
			d=Math.round( theCurrent * .95  );
			down1 = new Price(theCurrent, d);
			pricesToWatch.add(down1);
			
			u=Math.round( theCurrent * 1.05  );
			up1 = new Price(theCurrent, u);
			pricesToWatch.add(up1);
			
			log.debug("{} Prices to watch size={}. Targets: {}",watchName, pricesToWatch.size(), pricesToWatch.stream().collect(Collectors.toList()).toString());
			//log.debug(pricesToWatch.stream().collect(Collectors.toList()).toString());
		}
		
	}


	public void setSkipAdjustWatchlist(boolean skipAdjustWatchlist) {
		this.skipAdjustWatchlist = skipAdjustWatchlist;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWatchName() {
		return watchName;
	}

	public void setWatchName(String watchName) {
		this.watchName = watchName;
	}

	public void setWatchDescription(String description) {
		this.watchDescription = description;
	}

	public void setHasTargets(boolean hasTargets) {	isHasTargets = hasTargets;	}



	public boolean isHasTargets() {
		return isHasTargets;
	}
}
