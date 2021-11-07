package optional4j.codegen.processor;

import javax.annotation.Nullable;
import optional4j.codegen.builder.CollaboratorBuilder;
import optional4j.codegen.builder.ValueTypeBuilder;
import optional4j.codegen.visitor.NullableVisitor;
import spoon.reflect.declaration.CtElement;

public class NullableProcessor extends BaseAnnotationProcessor<Nullable, CtElement> {

    @Override
    public void process(Nullable nullable, CtElement ctElement) {

        getEnvironment().setAutoImports(true);

        ctElement.accept(
                new NullableVisitor(
                        this.getClass(),
                        getEnvironment(),
                        new CollaboratorBuilder(getFactory()),
                        new ValueTypeBuilder(getFactory()),
                        getProperties()));
    }
}
