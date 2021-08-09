package poptional.test.model;
import poptional.OptionalObject;
import poptional.Poptional;
import poptional.Something;
@OptionalObject
public final class Country extends Something<Country> {
    private IsoCode isoCode;

    public Country(IsoCode isoCode) {
        this.isoCode = isoCode;
    }

    public Country() {
    }

    public Poptional<IsoCode> getIsoCode() {
        return Poptional.ofNullable(this.isoCode);
    }

    public void setIsoCode(IsoCode isoCode) {
        this.isoCode = isoCode;
    }

    @OptionalObject.NotNull
    public IsoCode getIsoCodePlain() {
        return this.isoCode;
    }
}