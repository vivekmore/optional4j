package ioptional.test.model;

import ioptional.OptionalReturn;
import ioptional.OptionalType;

@OptionalType
@OptionalReturn
 public class Node {

	private Node left;

	private Node right;

	private int value;

	public Node getLeft() {
		return this.left;
	}

	public Node getRight() {
		return this.right;
	}

	public int getValue() {
		return this.value;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public void setRight(Node right) {
		this.right = right;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
