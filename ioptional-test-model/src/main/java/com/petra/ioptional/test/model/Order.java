package com.petra.ioptional.test.model;

import com.petra.ioptional.annotations.Optionalize;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Optionalize
 class Order {

	UUID orderId;

	Customer customer;
}
