package org.boring.bitcoinAlerts.domain;


import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public enum AmountMovementType {
	
	
	
	UP() {
		@Override
		public boolean reached(float current, float target) {
			boolean result = target < current;
			log.debug("IS target({}) LT current({}) : {}", target, current, result);
			return result;
		}

		@Override
		public Map<String, String> msg(float current, float target) {
			log.debug("UP");
			String now = DT_FORMAT.format(ZonedDateTime.now());
			Map<String, String> xx = new HashMap<>();
			xx.put("now", now);
			xx.put("target", "has risen beyond ".concat(Float.valueOf(target).toString()));
			xx.put("price", Float.valueOf(current).toString());
			return xx;
		}
	},
	DOWN() {
		@Override
		public boolean reached(float current, float target) {
			return current < target;
		}

		@Override
		public Map<String, String> msg(float current, float target) {
			log.debug("DOWN");
			String now = DT_FORMAT.format(ZonedDateTime.now());
			Map<String, String> xx = new HashMap<>();
			xx.put("@now", now);
			xx.put("@target", "has fallen below ".concat(Float.valueOf(target).toString()));
			xx.put("@price", Float.valueOf(current).toString());
			return xx;
		}
	};
	
	private static final Logger log = LogManager.getLogger(AmountMovementType.class);
	public DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mm a z");
	
	public abstract boolean reached(float current, float target);

//	public abstract String msg(float current, float target);
	public abstract Map<String, String> msg(float current, float target);
	
	
}
