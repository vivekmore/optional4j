package com.petra.ioptional.test.model;

import org.junit.Test;

public class NodeTest {

	@Test
	public void node() {

		Node$ node = new Node$();

		Node$ left = node.getLeft()
				.orElse(new Node$());

		Node$ right = node.getRight()
				.orElse(new Node$());
	}
}