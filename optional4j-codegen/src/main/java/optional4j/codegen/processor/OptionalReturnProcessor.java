package optional4j.codegen.processor;

import optional4j.annotation.OptionalReturn;
import optional4j.codegen.builder.ValueTypeBuilder;
import optional4j.codegen.visitor.ValueTypeVisitor;
import spoon.reflect.declaration.CtElement;

public class OptionalReturnProcessor extends BaseAnnotationProcessor<OptionalReturn, CtElement> {

    @Override
    public void process(OptionalReturn optionalReturn, CtElement ctElement) {
        ctElement.accept(
                new ValueTypeVisitor(
                        this.getClass(),
                        getEnvironment(),
                        new ValueTypeBuilder(getFactory()),
                        getProperties()));
    }
}
