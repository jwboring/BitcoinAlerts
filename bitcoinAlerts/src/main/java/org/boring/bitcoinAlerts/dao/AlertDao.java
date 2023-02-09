package org.boring.bitcoinAlerts.dao;

import org.boring.bitcoinAlerts.domain.User;

public interface AlertDao {
	
	
	public User getUserById(int id);
	
	public User getUserAndById(int id) ;
}
