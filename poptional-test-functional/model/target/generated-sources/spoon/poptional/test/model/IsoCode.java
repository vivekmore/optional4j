package poptional.test.model;
import poptional.OptionalObject;
import poptional.Poptional;
import poptional.Something;
@OptionalObject
public final class IsoCode extends Something<IsoCode> {
    private AlphaCode2 alphaCode2;

    public IsoCode() {
    }

    public IsoCode(AlphaCode2 alphaCode2) {
        this.alphaCode2 = alphaCode2;
    }

    public void setAlphaCode2(AlphaCode2 alphaCode2) {
        this.alphaCode2 = alphaCode2;
    }

    public Poptional<AlphaCode2> getAlphaCode2() {
        return (this.alphaCode2) == null? poptional.Poptional.empty(): this.alphaCode2;
    }

    @OptionalObject.NotNull
    public AlphaCode2 getCodePlain() {
        return this.alphaCode2;
    }
}