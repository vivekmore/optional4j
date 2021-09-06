package popnop.codegen.processor;

import popnop.codegen.builder.NullObjectBuilder;
import popnop.codegen.visitor.NullableVisitor;
import popnop.codegen.builder.PoptionalBuilder;
import spoon.reflect.declaration.CtElement;

import javax.annotation.Nullable;

public class NullableProcessor extends BaseAnnotationProcessor<Nullable, CtElement> {

    @Override
    public void process(Nullable nullable, CtElement ctElement) {

        getEnvironment().setAutoImports(true);

        ctElement.accept(new NullableVisitor(this.getClass(), getEnvironment(), new NullObjectBuilder(getFactory()), new PoptionalBuilder(getFactory()), getProperties()));
    }
}