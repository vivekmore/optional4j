package poptional.test.model;

import poptional.OptionalType;

import java.util.Optional;

@OptionalType
public class Node {

	private Node left;

	private Node right;

	private int value;

	public Node getLeft() {
		return this.left;
	}

	public Optional<Node> getLeftOptional() {
		return Optional.ofNullable(this.left);
	}

	public Node getRight() {
		return this.right;
	}

	public Optional<Node> getRightOptional() {
		return Optional.ofNullable(this.right);
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
