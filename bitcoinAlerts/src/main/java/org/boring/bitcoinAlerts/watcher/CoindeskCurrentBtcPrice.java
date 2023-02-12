package org.boring.bitcoinAlerts.watcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.boring.bitcoinAlerts.domain.Alert;
import org.boring.bitcoinAlerts.domain.Price;
import org.boring.bitcoinAlerts.smsSender.BtcNetworkMetricListener;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Component(value="coindeskCurrentBtcPrice")
@Scope (value="singleton")
public class CoindeskCurrentBtcPrice  {
	private static final Logger log = LogManager.getLogger(CoindeskCurrentBtcPrice.class);
	
	
	
	private List<BtcNetworkMetricListener> listeners = new ArrayList<BtcNetworkMetricListener>();
	
	public void addListener(BtcNetworkMetricListener toAdd) {
        listeners.add(toAdd);
    }
	
	private final String coindeskMessage = "Coindesk Current Price ALERT :BTC @target with price : @price on @now.";
	
	
	private static final String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
	private Client client = ClientBuilder.newClient();
	private boolean firstTime = true; 
	private List<Price> pricesToWatch = new CopyOnWriteArrayList<Price>();
	private float currentPrice;
	
	
	public CoindeskCurrentBtcPrice() {
		super();
	}
	
	
	public List<Price> getPricesToWatch() {
		return pricesToWatch;
	}

	synchronized public float getCurrentPrice() {
		return currentPrice;
	}






	synchronized public void monitor() {
		
		Response response = getResponse();
		int status = response.getStatus();
		log.debug("status = {}",status);
		
		if (status==200) {
//			log.debug("headers: " + response.getHeaders());
			
			String json = response.readEntity(String.class);
			JSONObject jsonObject = new JSONObject(json);
			this.currentPrice = jsonObject.getJSONObject("bpi").getJSONObject("USD").getFloat("rate_float");
			System.out.printf("current price = %f",currentPrice);
			log.debug("current price = {}",currentPrice);
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
					String message = coindeskMessage.replaceFirst("@target",map.get("@target")).replaceFirst("@price", map.get("@price")).replaceFirst("@now", map.get("@now"));
					
					System.out.println(message);
					
					for (BtcNetworkMetricListener priceChangeListener : listeners) {
						priceChangeListener.alert(new Alert(1, message));
					}
					
					adjustWatchlist(priceChange);
				}
			}
		}
	}
	
	
	
	
	public void adjustWatchlist(float theCurrent) {
		log.debug(theCurrent);
		
		float d;
		Price down1;
		float u;
		Price up1;
		
		d=Math.round( theCurrent * .98  );
		down1 = new Price(theCurrent, d);
		pricesToWatch.add(down1);
		
		u=Math.round( theCurrent * 1.02  );
		up1 = new Price(theCurrent, u);
		pricesToWatch.add(up1);
		
		log.debug("size="+pricesToWatch.size());
		log.debug(pricesToWatch.stream().collect(Collectors.toList()).toString());
		
	}

	
	
}
