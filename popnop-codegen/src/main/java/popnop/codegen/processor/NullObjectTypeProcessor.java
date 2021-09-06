package popnop.codegen.processor;

import popnop.codegen.builder.NullObjectBuilder;
import popnop.codegen.visitor.NullObjectVisitor;
import popnop.spec.NullObjectType;
import popnop.codegen.builder.PoptionalBuilder;
import spoon.reflect.declaration.CtElement;

public class NullObjectTypeProcessor extends BaseAnnotationProcessor<NullObjectType, CtElement> {

    @Override
    public void process(NullObjectType nullObjectType, CtElement ctElement) {

        getEnvironment().setAutoImports(true);

        ctElement.accept(new NullObjectVisitor(this.getClass(), getEnvironment(), new NullObjectBuilder(getFactory()), new PoptionalBuilder(getFactory()), getProperties()));
    }
}

