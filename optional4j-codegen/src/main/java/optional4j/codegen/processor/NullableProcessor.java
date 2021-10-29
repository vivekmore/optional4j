package optional4j.codegen.processor;

import optional4j.codegen.builder.CollaboratorBuilder;
import optional4j.codegen.builder.ValueTypeBuilder;
import optional4j.codegen.visitor.NullableVisitor;
import spoon.reflect.declaration.CtElement;

import javax.annotation.Nullable;

public class NullableProcessor extends BaseAnnotationProcessor<Nullable, CtElement> {

    @Override
    public void process(Nullable nullable, CtElement ctElement) {

        getEnvironment().setAutoImports(true);

        ctElement.accept(new NullableVisitor(this.getClass(), getEnvironment(), new CollaboratorBuilder(getFactory()), new ValueTypeBuilder(getFactory()), getProperties()));
    }
}