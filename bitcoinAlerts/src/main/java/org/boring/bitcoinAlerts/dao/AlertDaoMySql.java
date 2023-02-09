package org.boring.bitcoinAlerts.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.boring.bitcoinAlerts.domain.Alert;
import org.boring.bitcoinAlerts.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class AlertDaoMySql implements AlertDao {
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private JdbcTemplate jdbcTemplate;
	
	private String GETUSERandBYID = "select u.id as userId, u.name as userName, u.smsNum, a.id as alertId, a.name as alertName, a.url   from user u "
			+ "	left join useralerts ua on(ua.userId = u.id)  "
			+ "	left join alert a on (a.id=ua.alertId) "
			+ "	where u.id= ? ";
	private String GETUSERBYID = "select *  from user u where u.id= :id ";
	

	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	public User getUserById(int id) {
		
	    Map<String, Integer> namedParameters = Collections.singletonMap("id", id);
	    User user = namedParameterJdbcTemplate.queryForObject(GETUSERBYID, namedParameters,  new UserRowMapper());
	    return user;
	}
	
	
	public User getUserAndById(int id) {
		
		int userId = 0;
		String userName = null;
		String userSmsNum = null;
		
		List<Alert> alerts = new ArrayList<Alert>();
		
		
		SqlRowSet srs = jdbcTemplate.queryForRowSet(GETUSERandBYID, id);
		while (srs.next()) {
			userId = srs.getInt("userId");
			userName = srs.getString("userName");
			userSmsNum = srs.getString("smsNum");
			
			int alertId = srs.getInt("alertId");
			String alertName = srs.getString("alertName");
			String url = srs.getString("url");
			
			alerts.add(new Alert(alertId, alertName, url));
		}
	    
		User user = new User(userId, userName, userSmsNum, alerts);
	    return user;
	}
	
	
	
	

}
