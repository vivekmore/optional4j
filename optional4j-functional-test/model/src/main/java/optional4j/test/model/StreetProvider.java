package optional4j.test.model;

import optional4j.annotation.Collaborator;

public abstract class StreetProvider {

    @Collaborator
    public abstract Street getStreet();
}
