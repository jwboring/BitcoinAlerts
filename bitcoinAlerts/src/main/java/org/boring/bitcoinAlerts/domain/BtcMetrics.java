package org.boring.bitcoinAlerts.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Jeff
 * 
 * cd = coindesk
 * bci = blockchainInfo
 *
 */
@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
@ToString
@Component(value = "btcMetrics")
@Scope(value="singleton")
public class BtcMetrics {
	
	private float cdCurrentPrice;
	private float cdiCurrentPrice;
	private float cdi24HrPrice;
	private float cdi24HrXacCount;
	private float cdiAvgTimeBetweenBlksSecs;
	private float cdiAvgXacsPerBlkCount;
	private float cdiHashrate;

	public BtcMetrics() {
	}

	public void setBtcMetric(String watchName, float value) {
		switch (watchName) {
			case ("cdCurrentPrice"):
				this.cdCurrentPrice = value;
				break;
			case ("cdiCurrentPrice"):
				this.cdiCurrentPrice = value;
				break;
			case ("cdi24HrPrice"):
				this.cdi24HrPrice = value;
				break;
			case ("cdiAvgTimeBetweenBlksSecs"):
				this.cdiAvgTimeBetweenBlksSecs = value;
				break;
			case ("cdiAvgXacsPerBlkCount"):
				this.cdiAvgXacsPerBlkCount = value;
				break;
			case ("cdiHashrate"):
				this.cdiHashrate = value;
				break;


		}
	}



}