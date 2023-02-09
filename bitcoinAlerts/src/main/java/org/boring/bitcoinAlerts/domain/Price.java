package org.boring.bitcoinAlerts.domain;

//@Component(value="price")
//@Scope (value="singleton")
public class Price {
	
	public float target;
	public AmountMovementTyoe type;

	public Price(float current, float target) {
		this.target = target;
		type = Float.compare(current, target) < 0 ? AmountMovementTyoe.UP : AmountMovementTyoe.DOWN;
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