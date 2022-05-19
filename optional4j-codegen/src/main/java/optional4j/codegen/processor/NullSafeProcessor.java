package optional4j.codegen.processor;

import optional4j.annotation.Optional4J;
import optional4j.codegen.builder.NullObjectBuilder;
import optional4j.codegen.builder.ValueTypeBuilder;
import optional4j.codegen.visitor.nullsafe.NullSafeVisitor;
import spoon.reflect.declaration.CtElement;

public class NullSafeProcessor extends BaseAnnotationProcessor<Optional4J, CtElement> {

    @Override
    public void process(Optional4J nullSafe, CtElement ctElement) {

        getEnvironment().setAutoImports(true);

        ctElement.accept(
                new NullSafeVisitor(
                        this.getClass(),
                        getEnvironment(),
                        new NullObjectBuilder(getFactory()),
                        new ValueTypeBuilder(getFactory()),
                        getProperties()));
    }
}
