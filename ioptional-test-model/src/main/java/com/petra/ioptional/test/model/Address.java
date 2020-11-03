package com.petra.ioptional.test.model;

import com.petra.ioptional.annotations.Optionalize;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Optionalize
 class Address {

	Country country;

	String street;

	String city;

	String zipcode;
}
