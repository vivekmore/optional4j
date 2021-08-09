package poptional.test.model;
import poptional.OptionalObject;
import poptional.Poptional;
import poptional.Something;
@OptionalObject
public final class IsoCode extends Something<IsoCode> {
    private Code code;

    public IsoCode(Code code) {
        this.code = code;
    }

    public IsoCode() {
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public Poptional<Code> getCode() {
        return Poptional.ofNullable(this.code);
    }

    @OptionalObject.NotNull
    public Code getCodePlain() {
        return this.code;
    }
}