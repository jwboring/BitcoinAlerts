package org.boring.bitcoinAlerts.dao;

import java.util.List;

import org.boring.bitcoinAlerts.domain.User;
import org.boring.bitcoinAlerts.domain.Watch;

public interface WatchDao {
	
	public List<Watch> getAllWatches();
	
	public User getUserById(int id);
	
	public User getUserAndById(int id) ;
}
