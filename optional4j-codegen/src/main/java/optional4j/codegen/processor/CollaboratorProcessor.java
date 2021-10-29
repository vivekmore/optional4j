package optional4j.codegen.processor;

import optional4j.codegen.builder.CollaboratorBuilder;
import optional4j.codegen.builder.ValueTypeBuilder;
import optional4j.codegen.visitor.CollaboratorVisitor;
import optional4j.annotation.Collaborator;
import spoon.reflect.declaration.CtElement;

public class CollaboratorProcessor extends BaseAnnotationProcessor<Collaborator, CtElement> {

    @Override
    public void process(Collaborator nullObjectType, CtElement ctElement) {

        getEnvironment().setAutoImports(true);

        ctElement.accept(new CollaboratorVisitor(this.getClass(), getEnvironment(), new CollaboratorBuilder(getFactory()), new ValueTypeBuilder(getFactory()), getProperties()));
    }
}

