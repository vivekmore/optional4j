package optional4j.codegen.visitor;

import static optional4j.annotation.Collaborator.NULL_OBJ_INTERFACE_POSTFIX;
import static optional4j.annotation.Collaborator.NULL_OBJ_INTERFACE_PREFIX;
import static optional4j.codegen.CodeGenUtil.addNonNullAnnotation;
import static optional4j.codegen.CodeGenUtil.getNullableMethods;
import static optional4j.codegen.CodeGenUtil.getNullness;
import static optional4j.codegen.CodeGenUtil.getReturnType;
import static optional4j.codegen.CodeGenUtil.hasNonNullAnnotation;
import static optional4j.codegen.CodeGenUtil.isOptimisticMode;
import static optional4j.codegen.CodeGenUtil.printProcessing;
import static optional4j.codegen.CodeGenUtil.removeAnnotation;
import static optional4j.codegen.CodeGenUtil.returnsNullObjectType;
import static optional4j.codegen.CodeGenUtil.returnsOptionalType;
import static optional4j.support.NullityValue.NULLABLE;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import optional4j.annotation.Collaborator;
import optional4j.codegen.CodeGenUtil;
import optional4j.codegen.builder.CollaboratorBuilder;
import optional4j.codegen.builder.ValueTypeBuilder;
import optional4j.codegen.processor.ProcessorProperties;
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

    private final CollaboratorBuilder collaboratorBuilder;

    private final ValueTypeBuilder valueTypeBuilder;

    private final ProcessorProperties processorProperties;

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
        CtType<?> nullObjectType = collaboratorBuilder.createNullObjectType(ctClass);
        collaboratorBuilder.addOfNullObjectFactoryMethod(nullObjectType);
        collaboratorBuilder.addNullInstanceFactoryMethod(nullObjectType, ctClass);

        // Elevate ctClass methods from (e.g., Foo) to NullObject interface (e.g., FooNullObject)
        elevateMethods(ctClass, nullObjectType);

        // Create Null Class
        CtClass<?> nullClass = collaboratorBuilder.createNullClass(ctClass, nullObjectType);
        implementNullInterfaceMethods(nullObjectType, nullClass);
        implementNothingInterface(nullClass, ctClass);

        // Modify ctClass
        valueTypeBuilder.implementSomething(ctClass);
        ctClass.addSuperInterface(nullObjectType.getReference());
        addOverrideAnnotationToMethods(ctClass);

        // Add Generated annotations
        CodeGenUtil.addGeneratedAnnotation(
                ctClass, collaboratorBuilder.getFactory(), processorClass);
        CodeGenUtil.addGeneratedAnnotation(
                nullObjectType, collaboratorBuilder.getFactory(), processorClass);
        CodeGenUtil.addGeneratedAnnotation(
                nullClass, collaboratorBuilder.getFactory(), processorClass);

        // Generate classes
        generateFile(nullClass);
        generateFile(nullObjectType);
    }

    public <T> void visitJsr305Methods(CtClass<T> ctClass) {
        if (NULLABLE == getNullness(ctClass, processorProperties)) {
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

        if (returnsOptionalType(ctMethod) && !returnsNullObjectType(ctMethod)) {
            ctMethod.accept(
                    new ValueTypeVisitor(
                            processorClass, environment, valueTypeBuilder, processorProperties));
            return;
        }

        if (!returnsNullObjectType(ctMethod)) {
            if (isOptimisticMode(ctMethod, processorProperties)) {
                return;
            } else {
                ctMethod.accept(
                        new ValueTypeVisitor(
                                processorClass,
                                environment,
                                valueTypeBuilder,
                                processorProperties));
            }
        } else {

            CtType<?> oldReturnType = getReturnType(ctMethod);
            CtTypeReference<?> newReturnType =
                    collaboratorBuilder.getNullObjectTypeReference(oldReturnType);
            changeMethodReturnTypeToNullObject(ctMethod, newReturnType);
            new NullObjectReturnReplacer(collaboratorBuilder, processorProperties)
                    .replace(ctMethod, newReturnType.getSimpleName(), oldReturnType);
            removeAnnotation(ctMethod, collaboratorBuilder.getFactory(), Nullable.class);
            addNonNullAnnotation(ctMethod, collaboratorBuilder.getFactory());
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

        CtLocalVariable newParameter = collaboratorBuilder.createLocalVariable();
        newParameter.setSimpleName(parameter.getSimpleName());
        newParameter.setType(parameterTypeRef);

        parameter.setSimpleName("temp_" + parameter.getSimpleName());
        newParameter.setAssignment(
                collaboratorBuilder.createCodeSnippetExpression(parameter.getSimpleName()));

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

        CtMethod<Object> newMethod = collaboratorBuilder.createMethod();
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

    /**
     * Changes the method return type to FooNullObject
     *
     * @param ctMethod
     * @param newReturnType
     */
    private void changeMethodReturnTypeToNullObject(
            CtMethod ctMethod, CtTypeReference<?> newReturnType) {
        ctMethod.setType(newReturnType);
    }

    private <T> void addOverrideAnnotationToMethods(CtClass<T> ctClass) {
        ctClass.getMethods().stream()
                .filter(CtModifiable::isPublic)
                .filter(ctMethod -> !ctMethod.isStatic())
                .filter(ctMethod -> !ctMethod.hasAnnotation(Override.class))
                .forEach(
                        ctMethod ->
                                ctMethod.addAnnotation(
                                        collaboratorBuilder.createAnnotation(
                                                collaboratorBuilder.createCtTypeReference(
                                                        Override.class))));
    }

    private void implementNullInterfaceMethods(CtType<?> nullObjectType, CtClass<?> nullClass) {

        nullObjectType.getMethods().stream()
                .filter(ctMethod -> !ctMethod.isStatic())
                .forEach(
                        ctMethod -> {
                            CtBlock<?> block = collaboratorBuilder.createBlock();
                            CtTypeReference type = ctMethod.getType();

                            CtMethod<Object> newMethod = collaboratorBuilder.createMethod();
                            newMethod.setSimpleName(ctMethod.getSimpleName());
                            newMethod.setParameters(ctMethod.getParameters());
                            newMethod.setModifiers(ctMethod.getModifiers());
                            newMethod.setType(type);
                            newMethod.addAnnotation(
                                    collaboratorBuilder.createAnnotation(
                                            collaboratorBuilder.createCtTypeReference(
                                                    Override.class)));

                            if (type.equals(
                                    collaboratorBuilder.createCtTypeReference(void.class))) {
                                // do nothing
                            } else if (type.equals(
                                    collaboratorBuilder.createCtTypeReference(Void.class))) {
                                block.addStatement(
                                        collaboratorBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        collaboratorBuilder
                                                                .createCodeSnippetExpression(
                                                                        "null")));
                            } else if (type.equals(
                                            collaboratorBuilder.createCtTypeReference(
                                                    Integer.class))
                                    || type.equals(
                                            collaboratorBuilder.createCtTypeReference(int.class))) {
                                block.addStatement(
                                        collaboratorBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        collaboratorBuilder
                                                                .createCodeSnippetExpression("0")));
                            } else if (type.equals(
                                            collaboratorBuilder.createCtTypeReference(Float.class))
                                    || type.equals(
                                            collaboratorBuilder.createCtTypeReference(
                                                    float.class))) {
                                block.addStatement(
                                        collaboratorBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        collaboratorBuilder
                                                                .createCodeSnippetExpression(
                                                                        "0.0f")));
                            } else if (type.equals(
                                            collaboratorBuilder.createCtTypeReference(Double.class))
                                    || type.equals(
                                            collaboratorBuilder.createCtTypeReference(
                                                    double.class))) {
                                block.addStatement(
                                        collaboratorBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        collaboratorBuilder
                                                                .createCodeSnippetExpression(
                                                                        "0.0d")));
                            } else if (type.equals(
                                            collaboratorBuilder.createCtTypeReference(Long.class))
                                    || type.equals(
                                            collaboratorBuilder.createCtTypeReference(
                                                    long.class))) {
                                block.addStatement(
                                        collaboratorBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        collaboratorBuilder
                                                                .createCodeSnippetExpression(
                                                                        "0L")));
                            } else if (type.equals(
                                            collaboratorBuilder.createCtTypeReference(Byte.class))
                                    || type.equals(
                                            collaboratorBuilder.createCtTypeReference(
                                                    byte.class))) {
                                block.addStatement(
                                        collaboratorBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        collaboratorBuilder
                                                                .createCodeSnippetExpression("0")));
                            } else if (type.equals(
                                            collaboratorBuilder.createCtTypeReference(
                                                    Boolean.class))
                                    || type.equals(
                                            collaboratorBuilder.createCtTypeReference(
                                                    boolean.class))) {
                                block.addStatement(
                                        collaboratorBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        collaboratorBuilder
                                                                .createCodeSnippetExpression(
                                                                        "false")));
                            } else if (type.equals(
                                            collaboratorBuilder.createCtTypeReference(
                                                    Character.class))
                                    || type.equals(
                                            collaboratorBuilder.createCtTypeReference(
                                                    char.class))) {
                                block.addStatement(
                                        collaboratorBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        collaboratorBuilder
                                                                .createCodeSnippetExpression(
                                                                        "'\\u0000'")));
                            } else if (type.equals(
                                            collaboratorBuilder.createCtTypeReference(Short.class))
                                    || type.equals(
                                            collaboratorBuilder.createCtTypeReference(
                                                    short.class))) {
                                block.addStatement(
                                        collaboratorBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        collaboratorBuilder
                                                                .createCodeSnippetExpression("0")));
                            } else if (type.equals(
                                    collaboratorBuilder.createCtTypeReference(String.class))) {
                                block.addStatement(
                                        collaboratorBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        collaboratorBuilder
                                                                .createCodeSnippetExpression(
                                                                        "\"\"")));
                            } else if (type.isSubtypeOf(
                                    collaboratorBuilder.createCtTypeReference(Map.class))) {
                                block.addStatement(
                                        collaboratorBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        collaboratorBuilder
                                                                .createCodeSnippetExpression(
                                                                        "java.util.Collections.emptyMap()")));
                            } else if (type.isSubtypeOf(
                                    collaboratorBuilder.createCtTypeReference(Set.class))) {
                                block.addStatement(
                                        collaboratorBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        collaboratorBuilder
                                                                .createCodeSnippetExpression(
                                                                        "java.util.Collections.emptySet()")));
                            } else if (type.isSubtypeOf(
                                    collaboratorBuilder.createCtTypeReference(List.class))) {
                                block.addStatement(
                                        collaboratorBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        collaboratorBuilder
                                                                .createCodeSnippetExpression(
                                                                        "java.util.Collections.emptyList()")));
                            } else if (type.isSubtypeOf(
                                    collaboratorBuilder.createCtTypeReference(Collection.class))) {
                                block.addStatement(
                                        collaboratorBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        collaboratorBuilder
                                                                .createCodeSnippetExpression(
                                                                        "java.util.Collections.emptyList()")));
                            } else if (type.isSubtypeOf(
                                    collaboratorBuilder.createCtTypeReference(Iterable.class))) {
                                block.addStatement(
                                        collaboratorBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        collaboratorBuilder
                                                                .createCodeSnippetExpression(
                                                                        "java.util.Collections.emptyList()")));
                            } else if (type.getQualifiedName()
                                    .startsWith(Optional.class.getName())) {
                                block.addStatement(
                                        collaboratorBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        collaboratorBuilder
                                                                .createCodeSnippetExpression(
                                                                        Optional.class
                                                                                        .getSimpleName()
                                                                                + ".empty()")));
                            } else if (type.getSimpleName().startsWith(NULL_OBJ_INTERFACE_PREFIX)
                                    && type.getSimpleName().endsWith(NULL_OBJ_INTERFACE_POSTFIX)) {
                                // todo: use an @NullObjectReturn annotation for method returns
                                block.addStatement(
                                        collaboratorBuilder
                                                .createReturn()
                                                .setReturnedExpression(
                                                        collaboratorBuilder
                                                                .createCodeSnippetExpression(
                                                                        type.getSimpleName()
                                                                                + "."
                                                                                + Collaborator
                                                                                        .NULL_INSTANCE_FACTORY_METHOD_NAME
                                                                                + "()")));
                            } else {
                                block.addStatement(
                                        collaboratorBuilder.createCodeSnippetStatement(
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
