package optional4j.codegen.visitor.valuetype;

import static optional4j.codegen.CodeGenUtil.*;
import static optional4j.support.NullityValue.NULLABLE;

import java.util.Set;
import javax.annotation.NonNull;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import optional4j.annotation.Collaborator;
import optional4j.annotation.Mode;
import optional4j.annotation.OptionalReturn;
import optional4j.annotation.ValueType;
import optional4j.codegen.builder.NullObjectBuilder;
import optional4j.codegen.builder.ValueTypeBuilder;
import optional4j.codegen.processor.ProcessorProperties;
import optional4j.codegen.visitor.nullobject.CollaboratorVisitor;
import spoon.compiler.Environment;
import spoon.processing.AnnotationProcessor;
import spoon.reflect.declaration.*;
import spoon.reflect.visitor.CtAbstractVisitor;

@RequiredArgsConstructor
public class ValueTypeVisitor extends CtAbstractVisitor {

    private final Class<? extends AnnotationProcessor<?, ?>> processorClass;

    private final Environment environment;

    private final ValueTypeBuilder valueTypeBuilder;

    private final ProcessorProperties processorProperties;

    @Override
    public <T> void visitCtInterface(CtInterface<T> tCtInterface) {

        if (tCtInterface.hasAnnotation(Collaborator.class)) {
            return;
        }

        ValueType valueType = tCtInterface.getAnnotation((ValueType.class));
        if (valueType == null) {
            return;
        }

        printProcessing(environment, tCtInterface);

        implementSomething(tCtInterface);
        // visitJsr305Methods(tCtInterface);
        addGeneratedAnnotations(tCtInterface);
    }

    private <T> void visitJsr305Methods(CtType<T> ctType) {

        if (!processorProperties.isNullityEnabled()) {
            return;
        }

        if (NULLABLE == getNullness(ctType, processorProperties)) {
            visitCtMethods(ctType.getMethods());
            return;
        }

        visitCtMethods(getNullableMethods(ctType));
    }

    @Override
    public <T> void visitCtClass(CtClass<T> ctClass) {

        if (ctClass.hasAnnotation(Collaborator.class)) {
            return;
        }

        ValueType valueType = ctClass.getAnnotation((ValueType.class));
        if (valueType == null) {
            return;
        }

        printProcessing(environment, ctClass);

        if (processorProperties.isEnhancedSyntax()) {
            implementEnhancedOptionalType(ctClass);
        }
        implementSomething(ctClass);
        // visitJsr305Methods(ctClass);
        addGeneratedAnnotations(ctClass);
    }

    public void visitCtMethods(Set<CtMethod<?>> methods) {
        methods.forEach(this::visitCtMethod);
    }

    @Override
    public <T> void visitCtMethod(CtMethod<T> ctMethod) {

        printProcessing(environment, ctMethod);

        if (isVoidReturn(ctMethod, valueTypeBuilder.getFactory())) {
            return;
        }

        if (returnsNullObjectType(ctMethod)) {
            ctMethod.accept(
                    new CollaboratorVisitor(
                            processorClass,
                            environment,
                            new NullObjectBuilder(valueTypeBuilder.getFactory()),
                            valueTypeBuilder,
                            processorProperties));
            return;
        }

        if (isOptionalReturn(ctMethod)) { // @OptionalReturn
            removeAnnotation(ctMethod, valueTypeBuilder.getFactory(), OptionalReturn.class);
        } else { // Check nullity compatibility

            if (!processorProperties.isNullityEnabled()) {
                return;
            }

            if (hasNonNullAnnotation(ctMethod)) { // @NonNull
                return;
            }

            if (returnsOptionalType(ctMethod)) {
                removeAnnotation(ctMethod, valueTypeBuilder.getFactory(), Nullable.class);
            } else {
                if (isOptimisticMode(ctMethod, processorProperties)) {
                    return;
                }
            }
        }

        ctMethod.getDeclaringType()
                .addMethod(
                        new OptionalMethodWrapper(valueTypeBuilder, processorProperties)
                                .wrapMethod(ctMethod));
    }

    private <T> void addGeneratedAnnotations(CtType<T> tCtType) {
        addGeneratedAnnotation(tCtType, valueTypeBuilder.getFactory(), processorClass);
        removeAnnotation(tCtType, valueTypeBuilder.getFactory(), Nullable.class);
        removeAnnotation(tCtType, valueTypeBuilder.getFactory(), NonNull.class);
        removeAnnotation(tCtType, valueTypeBuilder.getFactory(), Mode.class);
    }

    private <T> void implementEnhancedOptionalType(CtClass<T> ctClass) {
        CtType<?> nType = valueTypeBuilder.createEnhancedOptionalType(ctClass);
        ctClass.addSuperInterface(nType.getReference());
    }

    private void implementSomething(CtType<?> ctType) {
        valueTypeBuilder.implementSomething(ctType);
    }
}
