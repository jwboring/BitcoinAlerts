package org.boring.bitcoinAlerts.watcher;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.boring.bitcoinAlerts.domain.Alert;
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
	private String message = "***BtcNetworkMetricWatcher*** :BTC @target with price : @price on @now.";
	private String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
	private boolean skipAdjustWatchlist = false;
	
	
	public BtcNetworkMetricWatcher() {
		super();
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
			System.out.printf(watchName + " = %f", currentPrice);
			log.debug("{} = {}",watchName, currentPrice);
			System.out.println(System.getProperty("line.separator"));
			priceChange(currentPrice);
			
		}
		
	}
	
	private Response getResponse() {
		Response response = client.target(url)
				  .request(MediaType.TEXT_PLAIN_TYPE)
				  .get();
		return response;
	}
	

	private void priceChange(float priceChange) {
		
//		String emailMsg = " | Current price = " + currentPrice + "\n";
//		log.debug(DT_FORMAT.format(ZonedDateTime.now()) + emailMsg);
		
		if (firstTime) {
			firstTime = false;
			adjustWatchlist(priceChange);
		}
		else {
			// we check if one price is reached
			
			int count=0;
			
			for (Price priceToWatch : pricesToWatch) {
				count++;
				log.debug("priceToWatch ={}.  count={}", priceToWatch, count);
				
			
				if (priceToWatch.type.reached(priceChange, priceToWatch.target)) {
					log.debug("reached target {} current price={}", priceToWatch, priceChange);
					pricesToWatch.remove(priceToWatch);
					
					Map<String, String> map = priceToWatch.type.msg(priceChange, priceToWatch.target);
					String myMessage = this.message.replaceFirst("@target",map.get("@target")).replaceFirst("@price", map.get("@price")).replaceFirst("@now", map.get("@now"));
					
					System.out.println(myMessage);
					
					for (BtcNetworkMetricListener priceChangeListener : listeners) {
						priceChangeListener.alert(new Alert(1, myMessage));
					}
					
					adjustWatchlist(priceChange);
				}
			}
		}
	}
	
	
	
	
	public void adjustWatchlist(float theCurrent) {
		
		if (!skipAdjustWatchlist) {
		
			log.debug(theCurrent);
			
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
			
			log.debug("size="+pricesToWatch.size());
			log.debug(pricesToWatch.stream().collect(Collectors.toList()).toString());
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

	
	
	
	
}
