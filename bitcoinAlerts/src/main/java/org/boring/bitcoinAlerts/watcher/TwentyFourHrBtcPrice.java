package org.boring.bitcoinAlerts.watcher;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.boring.bitcoinAlerts.domain.Alert;
import org.boring.bitcoinAlerts.smsSender.BtcNetworkMetricListener;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Component(value="twentyFourHrBtcPrice")
@Scope (value="singleton")
public class TwentyFourHrBtcPrice {
	private static final Logger log = LogManager.getLogger(TwentyFourHrBtcPrice.class);
	
	private static final String url = "https://blockchain.info/q/24hrprice";
	private Client client = ClientBuilder.newClient();
	
	private final String twentyFourHrBtcPrice = "24 hour weighted price from the largest exchanges: @price";
	
	private List<BtcNetworkMetricListener> listeners = new ArrayList<BtcNetworkMetricListener>();
	
	
	public TwentyFourHrBtcPrice() {
		super();
	}

	public void addListener(BtcNetworkMetricListener toAdd) {
        listeners.add(toAdd);
    }
	
	synchronized public void monitor() {
		
		Response response = getResponse();
		int status = response.getStatus();
		if (status==200) {
//			log.debug("headers: " + response.getHeaders());
			float curentPrice = response.readEntity(float.class);
			log.debug("TwentyFourHrBtcPrice = {}",curentPrice);
			priceChange(curentPrice);
		}
		
	}
	
	public Response getResponse() {
		Response response = client.target(url)
				  .request(MediaType.TEXT_PLAIN_TYPE)
				  .get();
		return response;
	}
	

	
	
	
	public void priceChange(float priceChange) {
		
		
		String message = twentyFourHrBtcPrice.replaceFirst("@price", Float.valueOf(priceChange).toString());
		
		for (BtcNetworkMetricListener priceChangeListener : listeners) {
			priceChangeListener.alert(new Alert(1, message));
		}
		System.out.println(message);
	}
	
	
}
