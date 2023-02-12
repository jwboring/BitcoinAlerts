package org.boring.bitcoinAlerts.smsSender;

public interface Sender {
	
	public void sendMsg(String toPhone, String subject, String message); 
	

}
