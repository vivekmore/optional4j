package optional4j.test.model;

import javax.annotation.Nullable;

public interface CountryProvider {

    @Nullable
    Country getCountry();
}
