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

	@NonNull private List<Watch> watches = new ArrayList<Watch>();
	
	// pa 2023026064
	// Shawn 8433436587
	private String[] userPhoneNumbers = new String[] {"8632559777", "8636481586"};
	
	
	
	public User(@NonNull int id, @NonNull String name, String smsNum, @NonNull List<Watch> watches) {
		super();
		this.id = id;
		this.name = name;
		this.smsNum = smsNum;
		this.watches = watches;
	}
	
	

	public String[] getUserPhoneNumbers() {
		return userPhoneNumbers;
	}







	
}
