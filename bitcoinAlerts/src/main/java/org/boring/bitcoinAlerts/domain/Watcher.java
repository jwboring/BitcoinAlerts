package org.boring.bitcoinAlerts.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Watcher {

	@NonNull private int id;
	@NonNull private String name;
	@NonNull private String url;
}
