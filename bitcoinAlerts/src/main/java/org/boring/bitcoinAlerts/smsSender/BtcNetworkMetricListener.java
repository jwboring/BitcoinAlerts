package org.boring.bitcoinAlerts.smsSender;

import org.boring.bitcoinAlerts.domain.Alert;

public interface BtcNetworkMetricListener {
	
	void alert(Alert alert);

}
