package com.petra.ioptional.codegen.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ElementUtilTest {

	@Test
	public void extractFieldName() {

		String fieldName = ElementUtil.extractFieldName("getStreet", "get");

		assertEquals("street", fieldName);
	}
}