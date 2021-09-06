package popnop.codegen.processor;

import popnop.spec.OptionalType;
import popnop.codegen.builder.PoptionalBuilder;
import popnop.codegen.visitor.OptionalTypeVisitor;
import spoon.reflect.declaration.CtElement;

public class OptionalTypeProcessor extends BaseAnnotationProcessor<OptionalType, CtElement> {

    @Override
    public void process(OptionalType optionalReturn, CtElement ctElement) {

        ctElement.accept(new OptionalTypeVisitor(this.getClass(), getEnvironment(), new PoptionalBuilder(getFactory()), getProperties()));
    }
}