package optional4j.test.model;

import optional4j.spec.Optional;

public interface AddressProvider {

    default Optional<Address> getAddress() {
        return null;
    }
}
