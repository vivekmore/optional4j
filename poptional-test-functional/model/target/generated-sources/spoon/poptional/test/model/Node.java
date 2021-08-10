package poptional.test.model;
import java.util.Optional;
import poptional.OptionalObject;
import poptional.Poptional;
import poptional.Something;
@OptionalObject
public final class Node extends Something<Node> {
    private Node left;

    private Node right;

    private int value;

    public Poptional<Node> getLeft() {
        return (this.left) == null? poptional.Poptional.empty(): this.left;
    }

    public Optional<Node> getLeftOptional() {
        return Optional.ofNullable(this.left);
    }

    public Poptional<Node> getRight() {
        return (this.right) == null? poptional.Poptional.empty(): this.right;
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