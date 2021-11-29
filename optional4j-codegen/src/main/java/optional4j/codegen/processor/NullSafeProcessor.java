package optional4j.codegen.processor;

import optional4j.annotation.NullSafe;
import optional4j.codegen.builder.NullObjectBuilder;
import optional4j.codegen.builder.ValueTypeBuilder;
import optional4j.codegen.visitor.nullsafe.NullSafeVisitor;
import spoon.reflect.declaration.CtElement;

public class NullSafeProcessor extends BaseAnnotationProcessor<NullSafe, CtElement> {

    @Override
    public void process(NullSafe nullSafe, CtElement ctElement) {

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
