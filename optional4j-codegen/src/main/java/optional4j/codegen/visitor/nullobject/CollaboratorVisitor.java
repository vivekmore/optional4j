package optional4j.codegen.visitor.nullobject;

import static optional4j.annotation.Collaborator.NULL_OBJ_INTERFACE_POSTFIX;
import static optional4j.annotation.Collaborator.NULL_OBJ_INTERFACE_PREFIX;
import static optional4j.codegen.CodegenUtil.*;
import static optional4j.codegen.CodegenUtil.removeAnnotation;
import static optional4j.support.NullityValue.NULLABLE;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import optional4j.annotation.Collaborator;
import optional4j.codegen.CodegenProperties;
import optional4j.codegen.CodegenUtil;
import optional4j.codegen.builder.NullObjectBuilder;
import optional4j.codegen.builder.ValueTypeBuilder;
import optional4j.codegen.visitor.valuetype.ValueTypeVisitor;
import optional4j.spec.Optional;
import spoon.compiler.Environment;
import spoon.processing.AnnotationProcessor;
import spoon.processing.FileGenerator;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtModifiable;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtAbstractVisitor;

@RequiredArgsConstructor
public class CollaboratorVisitor extends CtAbstractVisitor {

    private final Class<? extends AnnotationProcessor<?, ?>> processorClass;

    private final Environment environment;

    private final NullObjectBuilder nullObjectBuilder;

    private final ValueTypeBuilder valueTypeBuilder;

    private final CodegenProperties codegenProperties;

    @Override
    public <T> void visitCtClass(CtClass<T> ctClass) {

        Collaborator collaboratorAnnotation = ctClass.getAnnotation((Collaborator.class));
        if (collaboratorAnnotation == null) {
            return;
        }

        if (ctClass.isSubtypeOf(valueTypeBuilder.createOptionalOf(ctClass.getReference()))) {
            return;
        }

        printProcessing(environment, ctClass);

        // in order to process any NullObject or Optional return-type methods before generating
        // other classes
        ctClass.setSimpleName(
                collaboratorAnnotation.prefix()
                        + ctClass.getSimpleName()
                        + collaboratorAnnotation.postfix());

        visitJsr305Methods(ctClass);

        // Create NullObject interface
        CtType<?> nullObjectType = nullObjectBuilder.createNullObjectType(ctClass);
        nullObjectBuilder.addOfNullObjectFactoryMethod(nullObjectType);
        nullObjectBuilder.addNullInstanceFactoryMethod(nullObjectType, ctClass);

        // Elevate ctClass methods from (e.g., Foo) to NullObject interface (e.g., FooNullObject)
        elevateMethods(ctClass, nullObjectType);

        // Create Null Class
        CtClass<?> nullClass = nullObjectBuilder.createNullClass(ctClass, nullObjectType);
        implementNullInterfaceMethods(nullObjectType, nullClass);
        implementNothingInterface(nullClass, ctClass);

        // Modify ctClass
        valueTypeBuilder.implementSomething(ctClass);
        ctClass.addSuperInterface(nullObjectType.getReference());
        addOverrideAnnotationToMethods(ctClass);

        // Add Generated annotations
        CodegenUtil.addGeneratedAnnotation(ctClass, nullObjectBuilder.getFactory(), processorClass);
        CodegenUtil.addGeneratedAnnotation(
                nullObjectType, nullObjectBuilder.getFactory(), processorClass);
        CodegenUtil.addGeneratedAnnotation(
                nullClass, nullObjectBuilder.getFactory(), processorClass);

        // Generate classes
        generateFile(nullClass);
        generateFile(nullObjectType);
    }

    public <T> void visitJsr305Methods(CtClass<T> ctClass) {
        if (NULLABLE == getNullness(ctClass, codegenProperties)) {
            Set<CtMethod<?>> methods = ctClass.getMethods();
            visitCtMethods(methods);
        } else {
            Set<CtMethod<?>> methods = getNullableMethods(ctClass);
            visitCtMethods(methods);
        }
    }

    public void visitCtMethods(Set<CtMethod<?>> methods) {
        methods.forEach(this::visitCtMethod);
    }

    @Override
    public <T> void visitCtMethod(CtMethod<T> ctMethod) {

        printProcessing(environment, ctMethod);

        if (hasNonNullAnnotation(ctMethod)) {
            return;
        }

        if (isValueType(ctMethod, nullObjectBuilder.getFactory())
                && !returnsNullObjectType(ctMethod)) {
            ctMethod.accept(
                    new ValueTypeVisitor(
                            processorClass, environment, valueTypeBuilder, codegenProperties));
            return;
        }

        if (!returnsNullObjectType(ctMethod)) {
            if (isOptimisticMode(ctMethod, codegenProperties)) {
                return;
            } else {
                ctMethod.accept(
                        new ValueTypeVisitor(
                                processorClass, environment, valueTypeBuilder, codegenProperties));
            }
        } else {

            removeAnnotation(ctMethod, nullObjectBuilder.getFactory(), Nullable.class);
            removeAnnotation(ctMethod, nullObjectBuilder.getFactory(), Collaborator.class);

            CtMethod<T> wrapperMethod =
                    new NullObjectMethodWrapper(nullObjectBuilder, codegenProperties)
                            .wrapMethod(ctMethod);

            CtType<?> declaringType = ctMethod.getDeclaringType();

            declaringType.accept(this);

            declaringType.addMethod(wrapperMethod);
        }
    }

    @Override
    public <T> void visitCtParameter(CtParameter<T> parameter) {

        Collaborator collaborator = parameter.getAnnotation(Collaborator.class);
        if (collaborator == null) {
            return;
        }

        CtExecutable<?> parent = parameter.getParent();
        if (!(parent instanceof CtMethod)) {
            return;
        }

        CtMethod method = (CtMethod) parent;
        if (method.getBody() == null) {
            return;
        }

        CtTypeReference<T> parameterTypeRef = parameter.getType();
        CtType<T> parameterType = parameterTypeRef.getTypeDeclaration();
        if (!parameterType.hasAnnotation(Collaborator.class)) {
            return;
        } else {
            parameterType.accept(this);
        }

        CtLocalVariable newParameter = nullObjectBuilder.createLocalVariable();
        newParameter.setSimpleName(parameter.getSimpleName());
        newParameter.setType(parameterTypeRef);

        parameter.setSimpleName("temp_" + parameter.getSimpleName());
        newParameter.setAssignment(
                nullObjectBuilder.createCodeSnippetExpression(parameter.getSimpleName()));

        method.getBody().insertBegin(newParameter);
    }

    public void elevateMethods(CtType<?> source, CtType<?> dest) {
        elevateMethods(dest, source.getMethods());
    }

    private void elevateMethods(CtType<?> ctType, Set<CtMethod<?>> methods) {
        methods.stream()
                .filter(CtModifiable::isPublic)
                .filter(ctMethod -> !ctMethod.isStatic())
                .filter(
                        method ->
                                !Set.of("toString", "equals", "hashCode", "clone")
                                        .contains(method.getSimpleName()))
                .forEach(method -> declareInterfaceMethod(ctType, method));
    }

    private void declareInterfaceMethod(CtType<?> ctType, CtMethod<?> method) {

        CtMethod<Object> newMethod = nullObjectBuilder.createMethod();
        CtTypeReference type = method.getType().clone();
        newMethod.setSimpleName(method.getSimpleName());
        newMethod.setModifiers(method.getModifiers());
        newMethod.setType(type);
        newMethod.setParameters(method.getParameters());
        newMethod.setAnnotations(method.getAnnotations());

        ctType.addMethod(newMethod);
    }

    private <T> void implementNullObjectInterface(CtClass<T> ctClass, CtType<?> nullObjectType) {
        ctClass.addSuperInterface(nullObjectType.getReference());
    }

    private <T> void addOverrideAnnotationToMethods(CtClass<T> ctClass) {
        ctClass.getMethods().stream()
                .filter(CtModifiable::isPublic)
                .filter(ctMethod -> !ctMethod.isStatic())
                .filter(ctMethod -> !ctMethod.hasAnnotation(Override.class))
                .forEach(
                        ctMethod ->
                                ctMethod.addAnnotation(
                                        nullObjectBuilder.createAnnotation(
                                                nullObjectBuilder.createCtTypeReference(
                                                        Override.class))));
    }

    private void implementNullInterfaceMethods(CtType<?> nullObjectType, CtClass<?> nullClass) {

        nullObjectType.getMethods().stream()
                .filter(ctMethod -> !ctMethod.isStatic())
                .forEach(
                        ctMethod -> {
                            CtBlock<?> block = nullObjectBuilder.createBlock();
                            CtTypeReference type = ctMethod.getType();

                            CtMethod<Object> newMethod = nullObjectBuilder.createMethod();
                            newMethod.setSimpleName(ctMethod.getSimpleName());
                            newMethod.setParameters(ctMethod.getParameters());
                            newMethod.setModifiers(ctMethod.getModifiers());
                            newMethod.setType(type);
                            newMethod.addAnnotation(
                                    nullObjectBuilder.createAnnotation(
                                            nullObjectBuilder.createCtTypeReference(
                                                    Override.class)));

                            if (type.equals(nullObjectBuilder.createCtTypeReference(void.class))) {
                                // do nothing
                            } else if (type.equals(
                                    nullObjectBuilder.createCtTypeReference(Void.class))) {
                                block.addStatement(
                                        nullObjectBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        nullObjectBuilder
                                                                .createCodeSnippetExpression(
                                                                        "null")));
                            } else if (type.equals(
                                            nullObjectBuilder.createCtTypeReference(Integer.class))
                                    || type.equals(
                                            nullObjectBuilder.createCtTypeReference(int.class))) {
                                block.addStatement(
                                        nullObjectBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        nullObjectBuilder
                                                                .createCodeSnippetExpression("0")));
                            } else if (type.equals(
                                            nullObjectBuilder.createCtTypeReference(Float.class))
                                    || type.equals(
                                            nullObjectBuilder.createCtTypeReference(float.class))) {
                                block.addStatement(
                                        nullObjectBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        nullObjectBuilder
                                                                .createCodeSnippetExpression(
                                                                        "0.0f")));
                            } else if (type.equals(
                                            nullObjectBuilder.createCtTypeReference(Double.class))
                                    || type.equals(
                                            nullObjectBuilder.createCtTypeReference(
                                                    double.class))) {
                                block.addStatement(
                                        nullObjectBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        nullObjectBuilder
                                                                .createCodeSnippetExpression(
                                                                        "0.0d")));
                            } else if (type.equals(
                                            nullObjectBuilder.createCtTypeReference(Long.class))
                                    || type.equals(
                                            nullObjectBuilder.createCtTypeReference(long.class))) {
                                block.addStatement(
                                        nullObjectBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        nullObjectBuilder
                                                                .createCodeSnippetExpression(
                                                                        "0L")));
                            } else if (type.equals(
                                            nullObjectBuilder.createCtTypeReference(Byte.class))
                                    || type.equals(
                                            nullObjectBuilder.createCtTypeReference(byte.class))) {
                                block.addStatement(
                                        nullObjectBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        nullObjectBuilder
                                                                .createCodeSnippetExpression("0")));
                            } else if (type.equals(
                                            nullObjectBuilder.createCtTypeReference(Boolean.class))
                                    || type.equals(
                                            nullObjectBuilder.createCtTypeReference(
                                                    boolean.class))) {
                                block.addStatement(
                                        nullObjectBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        nullObjectBuilder
                                                                .createCodeSnippetExpression(
                                                                        "false")));
                            } else if (type.equals(
                                            nullObjectBuilder.createCtTypeReference(
                                                    Character.class))
                                    || type.equals(
                                            nullObjectBuilder.createCtTypeReference(char.class))) {
                                block.addStatement(
                                        nullObjectBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        nullObjectBuilder
                                                                .createCodeSnippetExpression(
                                                                        "'\\u0000'")));
                            } else if (type.equals(
                                            nullObjectBuilder.createCtTypeReference(Short.class))
                                    || type.equals(
                                            nullObjectBuilder.createCtTypeReference(short.class))) {
                                block.addStatement(
                                        nullObjectBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        nullObjectBuilder
                                                                .createCodeSnippetExpression("0")));
                            } else if (type.equals(
                                    nullObjectBuilder.createCtTypeReference(String.class))) {
                                block.addStatement(
                                        nullObjectBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        nullObjectBuilder
                                                                .createCodeSnippetExpression(
                                                                        "\"\"")));
                            } else if (type.isSubtypeOf(
                                    nullObjectBuilder.createCtTypeReference(Map.class))) {
                                block.addStatement(
                                        nullObjectBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        nullObjectBuilder
                                                                .createCodeSnippetExpression(
                                                                        "java.util.Collections.emptyMap()")));
                            } else if (type.isSubtypeOf(
                                    nullObjectBuilder.createCtTypeReference(Set.class))) {
                                block.addStatement(
                                        nullObjectBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        nullObjectBuilder
                                                                .createCodeSnippetExpression(
                                                                        "java.util.Collections.emptySet()")));
                            } else if (type.isSubtypeOf(
                                    nullObjectBuilder.createCtTypeReference(List.class))) {
                                block.addStatement(
                                        nullObjectBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        nullObjectBuilder
                                                                .createCodeSnippetExpression(
                                                                        "java.util.Collections.emptyList()")));
                            } else if (type.isSubtypeOf(
                                    nullObjectBuilder.createCtTypeReference(Collection.class))) {
                                block.addStatement(
                                        nullObjectBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        nullObjectBuilder
                                                                .createCodeSnippetExpression(
                                                                        "java.util.Collections.emptyList()")));
                            } else if (type.isSubtypeOf(
                                    nullObjectBuilder.createCtTypeReference(Iterable.class))) {
                                block.addStatement(
                                        nullObjectBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        nullObjectBuilder
                                                                .createCodeSnippetExpression(
                                                                        "java.util.Collections.emptyList()")));
                            } else if (type.getQualifiedName()
                                    .startsWith(Optional.class.getName())) {
                                block.addStatement(
                                        nullObjectBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        nullObjectBuilder
                                                                .createCodeSnippetExpression(
                                                                        Optional.class
                                                                                        .getSimpleName()
                                                                                + ".empty()")));
                            } else if (type.getSimpleName().startsWith(NULL_OBJ_INTERFACE_PREFIX)
                                    && type.getSimpleName().endsWith(NULL_OBJ_INTERFACE_POSTFIX)
                                    && type.hasAnnotation(Collaborator.class)) {
                                // todo: use an @NullObjectReturn annotation for method returns
                                block.addStatement(
                                        nullObjectBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        nullObjectBuilder
                                                                .createCodeSnippetExpression(
                                                                        type.getSimpleName()
                                                                                + "."
                                                                                + Collaborator
                                                                                        .NULL_INSTANCE_FACTORY_METHOD_NAME
                                                                                + "()")));
                            } else {
                                block.addStatement(
                                        nullObjectBuilder.createCodeSnippetStatement(
                                                "return null"));
                            }

                            newMethod.setBody(block);
                            nullClass.addMethod(newMethod);
                        });
    }

    // NullFoo extends Nothing<Foo>
    private void implementNothingInterface(CtClass<?> ctClass, CtClass<?> aClass) {
        valueTypeBuilder.implementNothing(ctClass, aClass);
    }

    private void generateFile(CtElement ctElement) {
        getFileGenerator().process(ctElement);
    }

    private FileGenerator getFileGenerator() {
        return environment.getDefaultFileGenerator();
    }
}
