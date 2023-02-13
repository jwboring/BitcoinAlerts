package org.boring.bitcoinAlerts.watcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.boring.bitcoinAlerts.domain.Alert;
import org.boring.bitcoinAlerts.smsSender.BtcNetworkMetricListener;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Component(value="twentyFourHrXacCount")
@Scope (value="singleton")
public class TwentyFourHrXacCount extends AbstractWatcherService {
	private static final Logger log = LogManager.getLogger(TwentyFourHrBtcPrice.class);
	
	private static final String url = "https://blockchain.info/q/24hrtransactioncount";
	private final String twentyFourHrBtcXacCount = "Number of transactions in the past 24 hours: @price. ";
	private final String additional = "Paypal does 1.2T, Visa does 11.6T";
	
	
	@Override
	synchronized public void monitor() {
		
		Response response = getResponse();
		int status = response.getStatus();
		if (status==200) {
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
		
		
		String message = twentyFourHrBtcXacCount.replaceFirst("@price", Float.valueOf(priceChange).toString());
		
		for (BtcNetworkMetricListener priceChangeListener : listeners) {
			priceChangeListener.alert(new Alert(1, message));
		}
		System.out.println(message);
	}
	
	
	
	
	
	
	
	

	

}
