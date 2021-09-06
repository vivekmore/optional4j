package popnop.test.model;

import popnop.spec.NullObjectType;
import javax.annotation.NonNull;

@NullObjectType
public class IsoCode {

    private AlphaCode2 alphaCode2;

    public IsoCode() {
    }

    public IsoCode(AlphaCode2 alphaCode2) {
        this.alphaCode2 = alphaCode2;
    }

    public void setAlphaCode2(AlphaCode2 alphaCode2) {
        this.alphaCode2 = alphaCode2;
    }

    public AlphaCode2 getAlphaCode2() {
        return this.alphaCode2;
    }

    @NonNull
    public AlphaCode2 getCodePlain() {
        return this.alphaCode2;
    }
}

