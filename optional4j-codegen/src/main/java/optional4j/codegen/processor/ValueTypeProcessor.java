package optional4j.codegen.processor;

import optional4j.annotation.Optional4J;
import optional4j.codegen.builder.ValueTypeBuilder;
import optional4j.codegen.visitor.valuetype.ValueTypeVisitor;
import spoon.reflect.declaration.CtElement;

public class ValueTypeProcessor extends BaseAnnotationProcessor<Optional4J, CtElement> {

    @Override
    public void process(Optional4J valueType, CtElement ctElement) {

        getEnvironment().setAutoImports(true);

        ctElement.accept(
                new ValueTypeVisitor(
                        this.getClass(),
                        getEnvironment(),
                        new ValueTypeBuilder(getFactory()),
                        getProperties()));
    }
}
