package optional4j.codegen.visitor.valuetype;

import static optional4j.codegen.CodegenUtil.addGeneratedAnnotation;
import static optional4j.codegen.CodegenUtil.getNullableMethods;
import static optional4j.codegen.CodegenUtil.getNullness;
import static optional4j.codegen.CodegenUtil.hasNonNullAnnotation;
import static optional4j.codegen.CodegenUtil.isOptimisticMode;
import static optional4j.codegen.CodegenUtil.isOptionalReturn;
import static optional4j.codegen.CodegenUtil.isValueType;
import static optional4j.codegen.CodegenUtil.isVoidReturn;
import static optional4j.codegen.CodegenUtil.printProcessing;
import static optional4j.codegen.CodegenUtil.removeAnnotation;
import static optional4j.codegen.CodegenUtil.returnsNullObjectType;
import static optional4j.support.NullityValue.NULLABLE;

import java.util.Set;
import javax.annotation.NonNull;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import optional4j.annotation.Collaborator;
import optional4j.annotation.Mode;
import optional4j.annotation.OptionalReturn;
import optional4j.annotation.ValueType;
import optional4j.codegen.CodegenProperties;
import optional4j.codegen.builder.NullObjectBuilder;
import optional4j.codegen.builder.ValueTypeBuilder;
import optional4j.codegen.visitor.nullobject.CollaboratorVisitor;
import optional4j.spec.Optional;
import spoon.compiler.Environment;
import spoon.processing.AnnotationProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.CtAbstractVisitor;

@RequiredArgsConstructor
public class ValueTypeVisitor extends CtAbstractVisitor {

    private final Class<? extends AnnotationProcessor<?, ?>> processorClass;

    private final Environment environment;

    private final ValueTypeBuilder valueTypeBuilder;

    private final CodegenProperties codegenProperties;

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

        if (!codegenProperties.isNullityEnabled()) {
            return;
        }

        if (NULLABLE == getNullness(ctType, codegenProperties)) {
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

        if (codegenProperties.isEnhancedSyntax()) {
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
                            codegenProperties));
            return;
        }

        if (ctMethod.getType().getQualifiedName().startsWith(Optional.class.getName())) {
            removeAnnotation(ctMethod, valueTypeBuilder.getFactory(), OptionalReturn.class);
            removeAnnotation(ctMethod, valueTypeBuilder.getFactory(), Nullable.class);
            return;
        }

        if (!isOptionalReturn(ctMethod)) { // @OptionalReturn

            if (!codegenProperties.isNullityEnabled()) {
                return;
            }

            if (hasNonNullAnnotation(ctMethod)) { // @NonNull
                return;
            }
        }

        if (!isValueType(ctMethod, valueTypeBuilder.getFactory())) {
            if (isOptimisticMode(ctMethod, codegenProperties)) {
                return;
            }
        }

        removeAnnotation(ctMethod, valueTypeBuilder.getFactory(), OptionalReturn.class);
        removeAnnotation(ctMethod, valueTypeBuilder.getFactory(), Nullable.class);

        OptionalMethodWrapper wrapper =
                new OptionalMethodWrapper(valueTypeBuilder, codegenProperties);
        if (ctMethod.getBody() == null) {
            wrapper.changeMethodReturnTypeToPoptional(ctMethod);
            return;
        }
        ctMethod.getDeclaringType().addMethod(wrapper.wrapMethod(ctMethod));
    }

    private <T> void addGeneratedAnnotations(CtType<T> tCtType) {
        addGeneratedAnnotation(tCtType, valueTypeBuilder.getFactory(), processorClass);
        removeAnnotation(tCtType, valueTypeBuilder.getFactory(), Nullable.class);
        removeAnnotation(tCtType, valueTypeBuilder.getFactory(), NonNull.class);
        removeAnnotation(tCtType, valueTypeBuilder.getFactory(), Mode.class);
        removeAnnotation(tCtType, valueTypeBuilder.getFactory(), ValueType.class);
    }

    private <T> void implementEnhancedOptionalType(CtClass<T> ctClass) {
        CtType<?> nType = valueTypeBuilder.createEnhancedOptionalType(ctClass);
        ctClass.addSuperInterface(nType.getReference());
    }

    private void implementSomething(CtType<?> ctType) {
        valueTypeBuilder.implementSomething(ctType);
    }
}
