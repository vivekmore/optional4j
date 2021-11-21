package optional4j.codegen.processor;

import optional4j.annotation.ValueType;
import optional4j.codegen.builder.ValueTypeBuilder;
import optional4j.codegen.visitor.valuetype.ValueTypeVisitor;
import spoon.reflect.declaration.CtElement;

public class ValueTypeProcessor extends BaseAnnotationProcessor<ValueType, CtElement> {

    @Override
    public void process(ValueType valueType, CtElement ctElement) {
        ctElement.accept(
                new ValueTypeVisitor(
                        this.getClass(),
                        getEnvironment(),
                        new ValueTypeBuilder(getFactory()),
                        getProperties()));
    }
}
