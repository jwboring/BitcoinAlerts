package org.boring.bitcoinAlerts.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.boring.bitcoinAlerts.domain.User;
import org.boring.bitcoinAlerts.domain.Watch;
import org.boring.bitcoinAlerts.domain.WatchUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository(value="watchDaoMySql")
public class WatchDaoMySql implements WatchDao {
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private JdbcTemplate jdbcTemplate;
	
	
	private String GETUSERBYID = "select *  from user u where u.id= :id ";
	
	private String GETALLWATCHES = "select name, hasTargets, message, fixedDelayMilSecs, initDelaySecs from watch w ";
	private String GETALLURLSFORWATCHES = "select id, watchName, url from watchurl wu where watchName = ? ";
	

	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	
	
	
	
	
	@Override
	public List<Watch> getAllWatches() {
		
		int[] argTypes = new int[] {java.sql.Types.LONGVARCHAR};
		
		List<Watch> result = jdbcTemplate.query(GETALLWATCHES, new BeanPropertyRowMapper<Watch>(Watch.class));
		
		for (Watch watch : result) {
			String[] args = new String[] {watch.getName()};
			List<WatchUrl> urls =jdbcTemplate.query(GETALLURLSFORWATCHES, args, argTypes, new BeanPropertyRowMapper<WatchUrl>(WatchUrl.class));
			
			watch.setUrls(urls);
			
			
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public User getUserById(int id) {
		
	    Map<String, Integer> namedParameters = Collections.singletonMap("id", id);
	    User user = namedParameterJdbcTemplate.queryForObject(GETUSERBYID, namedParameters,  new UserRowMapper());
	    return user;
	}


























	


























	@Override
	public User getUserAndById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// shows how to load user with 1 SQL query
	/*public User getUserAndById(int id) {
		
		int userId = 0;
		String userName = null;
		String userSmsNum = null;
		
		List<Watch> alerts = new ArrayList<Watch>();
		
		
		SqlRowSet srs = jdbcTemplate.queryForRowSet(GETUSERandBYID, id);
		while (srs.next()) {
			userId = srs.getInt("userId");
			userName = srs.getString("userName");
			userSmsNum = srs.getString("smsNum");
			
			int alertId = srs.getInt("alertId");
			String alertName = srs.getString("alertName");
			String url = srs.getString("url");
			
			alerts.add(new Watch(alertId, alertName, url));
		}
	    
		User user = new User(userId, userName, userSmsNum, alerts);
	    return user;
	}*/
	
	
	
	

}
