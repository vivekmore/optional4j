package optional4j.codegen.visitor;

import static optional4j.codegen.CodeGenUtil.hasNonNullAnnotation;
import static optional4j.codegen.CodeGenUtil.printProcessing;
import static optional4j.codegen.CodeGenUtil.returnsNullObjectType;
import static optional4j.codegen.CodeGenUtil.returnsOptionalType;
import static optional4j.support.ModeValue.PESSIMISTIC;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import optional4j.codegen.builder.CollaboratorBuilder;
import optional4j.codegen.builder.ValueTypeBuilder;
import optional4j.codegen.processor.ProcessorProperties;
import spoon.compiler.Environment;
import spoon.processing.AnnotationProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.CtAbstractVisitor;

@RequiredArgsConstructor
public class NullableVisitor extends CtAbstractVisitor {

    private final Class<? extends AnnotationProcessor<?, ?>> processorClass;

    private final Environment environment;

    private final CollaboratorBuilder collaboratorBuilder;

    private final ValueTypeBuilder valueTypeBuilder;

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

        if (!processorProperties.isNullityEnabled()) {
            return;
        }

        if (hasNonNullAnnotation(ctMethod)) {
            return;
        }

        printProcessing(environment, ctMethod);

        if (returnsNullObjectType(ctMethod)) {
            ctMethod.accept(
                    new CollaboratorVisitor(
                            processorClass,
                            environment,
                            collaboratorBuilder,
                            valueTypeBuilder,
                            processorProperties));
            return;
        }

        if (returnsOptionalType(ctMethod) || PESSIMISTIC.equals(processorProperties.getMode())) {
            ctMethod.accept(
                    new ValueTypeVisitor(
                            processorClass, environment, valueTypeBuilder, processorProperties));
            return;
        }

        // todo: what to do with nullable?
    }
}
