package poptional.test.model;
import poptional.OptionalObject;
import poptional.Something;
@OptionalObject
public final class Code extends Something<Code> {
    private Integer code;

    public Code(Integer code) {
        this.code = code;
    }

    public Code() {
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}