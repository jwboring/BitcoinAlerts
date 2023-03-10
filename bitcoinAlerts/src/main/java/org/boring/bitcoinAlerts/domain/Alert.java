package org.boring.bitcoinAlerts.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Alert {
	
	private int watcherId;
	private String subject = "BTC Alert";
	private String message;
	
	public Alert(int watcherId, String message) {
		super();
		this.watcherId = watcherId;
		this.message = message;
	}

	
	
}
