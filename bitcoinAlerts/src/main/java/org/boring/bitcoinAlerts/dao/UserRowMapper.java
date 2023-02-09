package org.boring.bitcoinAlerts.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.boring.bitcoinAlerts.domain.User;
import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		
		User user = new User();

		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setSmsNum(rs.getString("smsNum"));
		
		

        return user;

	}

}
