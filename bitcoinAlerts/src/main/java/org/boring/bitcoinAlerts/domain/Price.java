package org.boring.bitcoinAlerts.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//@Component(value="price")
//@Scope (value="singleton")
public class Price {
	private static final Logger log = LogManager.getLogger(Price.class);
	
	public float target;
	public AmountMovementType type;

	public Price(float current, float target) {
		this.target = target;
		type = Float.compare(current, target) < 0 ? AmountMovementType.UP : AmountMovementType.DOWN;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(target);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Price other = (Price) obj;
		if (Float.floatToIntBits(target) != Float.floatToIntBits(other.target))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Price [target=" + target + "]";
	}
	
	
	

}