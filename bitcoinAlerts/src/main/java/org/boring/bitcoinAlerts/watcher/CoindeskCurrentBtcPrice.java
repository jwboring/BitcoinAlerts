package org.boring.bitcoinAlerts.watcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.boring.bitcoinAlerts.domain.Price;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Component(value="coindeskCurrentBtcPrice")
@Scope (value="singleton")
public class CoindeskCurrentBtcPrice {
	
	private static final String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
	private Client client = ClientBuilder.newClient();
	private boolean firstTime = true; 
	private List<Price> pricesToWatch;
	
	
	
	
	public CoindeskCurrentBtcPrice() {
		super();
	}


//	/** In Milliseconds 60,000 = 60 sec or 1 min */
//	@Value("${TwentyFourHrBtcPrice}") private long TwentyFourHrBtcPrice;
	
	
	
	
	synchronized public void monitor() {
		
		Response response = getResponse();
		int status = response.getStatus();
		
		if (status==200) {
			System.out.println("headers: " + response.getHeaders());
			
			String json = response.readEntity(String.class);
			JSONObject jsonObject = new JSONObject(json);
			float currentPrice = jsonObject.getJSONObject("bpi").getJSONObject("USD").getFloat("rate_float");
			System.out.printf("current price = %f",currentPrice);
			priceChange(currentPrice);
		}
		
	}
	
	public Response getResponse() {
		Response response = client.target(url)
				  .request(MediaType.TEXT_PLAIN_TYPE)
				  .get();
		return response;
	}
	

	
	
	
	public void priceChange(float priceChange) {
		
//		String emailMsg = " | Current price = " + currentPrice + "\n";
//		System.out.println(DT_FORMAT.format(ZonedDateTime.now()) + emailMsg);
		
		if (firstTime) {
			firstTime = false;
			adjustWatchlist(priceChange);
		}
		else {
			// we check if one price is reached
			for (Iterator<Price> it = pricesToWatch.iterator(); it.hasNext();) {
				Price priceToWatch = it.next();
	
				if (priceToWatch.type.reached(priceChange, priceToWatch.target)) {
					String message = priceToWatch.type.msg(priceChange, priceToWatch.target);
					System.out.printf("Bitcoin Watcher %s", message);
	//				displayNotification("Bitcoin Watcher", message);
					it.remove(); // remove from list to watch
					adjustWatchlist(priceChange);
				}
			}
		}
	}
	
	private void adjustWatchlist(float theCurrent) {
		System.out.println(theCurrent);
		pricesToWatch = new ArrayList<Price>(4);
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
		
		
//		/** Down 1000, rounded to 1000 */
//		d = Math.round((theCurrent - 1000)/1000)*1000;
//		down1 = new Price(theCurrent, d);
//		pricesToWatch.add(down1);
//		
//		/** Down 500, rounded to 100 */
//		d = Math.round((theCurrent - 500)/100)*100;
//		down1 = new Price(theCurrent, d);
//		pricesToWatch.add(down1);
//		
//		
//		/** up 1000, rounded to 1000 */
//		u = Math.round((theCurrent + 1000)/1000)*1000;
//		up1 = new Price(theCurrent, u);
//		pricesToWatch.add(up1);
//		
//		/** up 500, rounded to 100 */
//		u = Math.round((theCurrent + 500)/100)*100;
//		up1 = new Price(theCurrent, u);
//		pricesToWatch.add(up1);
		
		System.out.println("size="+pricesToWatch.size());
		System.out.println(pricesToWatch.stream().collect(Collectors.toList()).toString());
		
	}
	
}
