package org.boring.bitcoinAlerts.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.boring.bitcoinAlerts.domain.Watch;
import org.springframework.jdbc.core.RowMapper;

public class WatchRowMapper implements RowMapper<Watch> {

	
	@Override
	public Watch mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		/*
		
		@NonNull private int id;
	@NonNull private String message;
	@NonNull private int fixedDelay;
	@NonNull private int initDelay;
	@NonNull private boolean hasTargets;
	@NonNull private List<WatchUrl> urls;
		*/
		
		
		Watch watch = new Watch();

		watch.setName(rs.getString("name"));
		watch.setMessage(rs.getString("message"));
		watch.setFixedDelayMilSecs(rs.getInt("fixedDelay"));
		watch.setInitDelaySecs(rs.getInt("initDelay"));
		watch.setHasTargets(rs.getBoolean("hasTargets"));
		
        return watch;

	}
}
