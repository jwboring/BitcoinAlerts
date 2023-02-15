package org.boring.bitcoinAlerts.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
public class BtcMetrics {
	
	private float cdCurrentPrice;
	private float cdiCurrentPrice;
	private float cdi24HrPrice;
	private float cdi24HrXacCount;
	private float cdiAvgTimeBetweenBlksSecs;
	private float cdiAvgXacsPerBlkCount;

}
