package org.boring.bitcoinAlerts.domain;

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
public class Watch {

	@NonNull private String name;
	@NonNull private String message;
	@NonNull private int fixedDelayMilSecs;
	@NonNull private int initDelaySecs;
	@NonNull private boolean hasTargets;
	@NonNull private List<WatchUrl> urls;
}
