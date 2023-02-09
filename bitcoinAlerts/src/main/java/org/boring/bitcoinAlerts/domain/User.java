package org.boring.bitcoinAlerts.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
	
	@NonNull private int id;
	@NonNull private String name;
	private String smsNum;

	@NonNull private List<Alert> alerts = new ArrayList<Alert>();
}
