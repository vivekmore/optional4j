package optional4j.test.model;

import javax.annotation.Nullable;

public interface CustomerProvider {

    @Nullable
    default Customer getCustomer() {
        return null;
    }
}
