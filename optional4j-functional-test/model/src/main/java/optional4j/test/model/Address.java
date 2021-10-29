package optional4j.test.model;

import optional4j.annotation.Collaborator;
import optional4j.annotation.OptionalReturn;
import optional4j.annotation.ValueType;
import optional4j.spec.Optional;

@ValueType
public class Address {

	private Country country;

	private Integer zipcode;

	public Address(Country country) {
		this.country = country;
	}

	public void doSomething(Optional<Country> country) {
		this.country.ifNull(new Runnable() {

			@Override
			public void run() {
				System.out.println("country is null");
			}
		});
	}

	public Address() {
	}

	@OptionalReturn
	public Country getCountry() {
		return country;
	}

	public Integer getZipcode() {
		return zipcode;
	}

	public void setCountry(@Collaborator Country country) {
		this.country = country;
	}

	public Country getCountryPlain() {
		return this.country;
	}

	public void setZipcode(Integer zipcode) {
		this.zipcode = zipcode;
	}
}
