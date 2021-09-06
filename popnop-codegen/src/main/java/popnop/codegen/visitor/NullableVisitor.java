package popnop.codegen.visitor;

import lombok.RequiredArgsConstructor;
import popnop.codegen.builder.NullObjectBuilder;
import popnop.codegen.builder.PoptionalBuilder;
import popnop.codegen.processor.ProcessorProperties;
import spoon.compiler.Environment;
import spoon.processing.AnnotationProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.CtAbstractVisitor;

import java.util.Set;

import static popnop.codegen.CodeGenUtil.*;

@RequiredArgsConstructor
public class NullableVisitor extends CtAbstractVisitor {

    private final Class<? extends AnnotationProcessor<?, ?>> processorClass;

    private final Environment environment;

    private final NullObjectBuilder nullObjectBuilder;

    private final PoptionalBuilder poptionalBuilder;

    private final ProcessorProperties processorProperties;

    @Override
    public <T> void visitCtClass(CtClass<T> ctClass) {
        visitMethods(ctClass.getMethods());
    }

    private void visitMethods(Set<CtMethod<?>> methods) {
        methods.forEach(this::visitCtMethod);
    }

    @Override
    public <T> void visitCtMethod(CtMethod<T> ctMethod) {

        if (hasNonNullAnnotation(ctMethod)) {
            return;
        }

        printProcessing(environment, ctMethod);

        if (returnsOptionalType(ctMethod)) {
            ctMethod.accept(new OptionalTypeVisitor(processorClass, environment, poptionalBuilder, processorProperties));
            return;
        }

        if (returnsNullObjectType(ctMethod)) {
            ctMethod.accept(new NullObjectVisitor(processorClass, environment, nullObjectBuilder, poptionalBuilder, processorProperties));
            return;
        }

        // todo: what to do with nullable?
    }
}
