package popnop.test.model;

import popnop.spec.NullObjectType;
import javax.annotation.NonNull;

@NullObjectType
public class Customer {

	private Address address;

	public Customer(Address address) {
		this.address = address;
	}
	public Customer() {
	}

	public Address getAddress() {
		return this.address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@NonNull
	public Address getAddressPlain() {
		return this.address;
	}
}
