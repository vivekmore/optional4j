package optional4j.codegen.visitor;

import static optional4j.codegen.CodegenUtil.hasNonNullAnnotation;
import static optional4j.codegen.CodegenUtil.isValueType;
import static optional4j.codegen.CodegenUtil.printProcessing;
import static optional4j.codegen.CodegenUtil.returnsNullObjectType;
import static optional4j.support.ModeValue.PESSIMISTIC;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import optional4j.codegen.CodegenProperties;
import optional4j.codegen.builder.NullObjectBuilder;
import optional4j.codegen.builder.ValueTypeBuilder;
import optional4j.codegen.visitor.nullobject.CollaboratorVisitor;
import optional4j.codegen.visitor.valuetype.ValueTypeVisitor;
import spoon.compiler.Environment;
import spoon.processing.AnnotationProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.CtAbstractVisitor;

@RequiredArgsConstructor
public class NullableVisitor extends CtAbstractVisitor {

    private final Class<? extends AnnotationProcessor<?, ?>> processorClass;

    private final Environment environment;

    private final NullObjectBuilder nullObjectBuilder;

    private final ValueTypeBuilder valueTypeBuilder;

    private final CodegenProperties codegenProperties;

    @Override
    public <T> void visitCtClass(CtClass<T> ctClass) {
        visitMethods(ctClass.getMethods());
    }

    private void visitMethods(Set<CtMethod<?>> methods) {
        methods.forEach(this::visitCtMethod);
    }

    @Override
    public <T> void visitCtMethod(CtMethod<T> ctMethod) {

        if (!codegenProperties.isNullityEnabled()) {
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
                            nullObjectBuilder,
                            valueTypeBuilder,
                            codegenProperties));
            return;
        }

        if (isValueType(ctMethod, valueTypeBuilder.getFactory())
                || PESSIMISTIC.equals(codegenProperties.getMode())) {
            ctMethod.accept(
                    new ValueTypeVisitor(
                            processorClass, environment, valueTypeBuilder, codegenProperties));
            return;
        }

        // todo: what to do with nullable?
    }
}
