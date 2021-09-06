package popnop.codegen.visitor;

import lombok.RequiredArgsConstructor;
import popnop.codegen.builder.NullObjectBuilder;
import popnop.codegen.builder.PoptionalBuilder;
import popnop.codegen.processor.ProcessorProperties;
import popnop.spec.NullObjectType;
import popnop.spec.Poptional;
import spoon.compiler.Environment;
import spoon.processing.AnnotationProcessor;
import spoon.processing.FileGenerator;
import spoon.reflect.code.CtBlock;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtAbstractVisitor;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static popnop.codegen.CodeGenUtil.*;
import static popnop.spec.NullnessValue.NULLABLE;

@RequiredArgsConstructor
public class NullObjectVisitor extends CtAbstractVisitor {

    private final Class<? extends AnnotationProcessor<?, ?>> processorClass;

    private final Environment environment;

    private final NullObjectBuilder nullObjectBuilder;

    private final PoptionalBuilder poptionalBuilder;

    private final ProcessorProperties processorProperties;

    @Override
    public <T> void visitCtClass(CtClass<T> ctClass) { // Foo

        NullObjectType nullObjectTypeAnnotation = ctClass.getAnnotation((NullObjectType.class));
        if (nullObjectTypeAnnotation == null) {
            return;
        }

        if (ctClass.isSubtypeOf(poptionalBuilder.createPoptionalOf(ctClass.getReference()))) {
            return;
        }

        printProcessing(environment, ctClass);

        // in order to process any NullOjbect or Poptional return-type methods before generating other classes
        ctClass.setSimpleName(nullObjectTypeAnnotation.prefix() + ctClass.getSimpleName() + nullObjectTypeAnnotation.postfix());

        if (NULLABLE == getNullness(ctClass, processorProperties)) {
            Set<CtMethod<?>> methods = ctClass.getMethods();
            visitCtMethods(methods);
        } else {
            Set<CtMethod<?>> methods = getNullableMethods(ctClass);
            visitCtMethods(methods);
        }

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
        poptionalBuilder.implementSomething(ctClass);
        ctClass.addSuperInterface(nullObjectType.getReference());
        addOverrideAnnotationToMethods(ctClass);

        // Add Generated annotations
        addGeneratedAnnotation(ctClass, nullObjectBuilder.getFactory(), processorClass);
        addGeneratedAnnotation(nullObjectType, nullObjectBuilder.getFactory(), processorClass);
        addGeneratedAnnotation(nullClass, nullObjectBuilder.getFactory(), processorClass);

        // Generate classes
        generateFile(nullClass);
        generateFile(nullObjectType);
    }

    @Override
    public <T> void visitCtMethod(CtMethod<T> ctMethod) {

        printProcessing(environment, ctMethod);

        if (hasNonNullAnnotation(ctMethod)) {
            return;
        }

        if (returnsOptionalType(ctMethod) && !returnsNullObjectType(ctMethod)) {
            ctMethod.accept(new OptionalTypeVisitor(processorClass, environment, poptionalBuilder, processorProperties));
            return;
        }

        if (!returnsNullObjectType(ctMethod)) {
            if (isStrictMode(ctMethod, processorProperties)) {
                return;
            } else {
                ctMethod.accept(new OptionalTypeVisitor(processorClass, environment, poptionalBuilder, processorProperties));
            }
        } else {

            CtType<?> oldReturnType = getReturnType(ctMethod);
            CtTypeReference<?> newReturnType = nullObjectBuilder.getNullObjectTypeReference(oldReturnType);
            changeMethodReturnTypeToNullObject(ctMethod, newReturnType);
            new NullObjectReturnReplacer(nullObjectBuilder, processorProperties).replace(ctMethod, newReturnType.getSimpleName());
            removeAnnotation(ctMethod, nullObjectBuilder.getFactory(), Nullable.class);
            addNonNullAnnotation(ctMethod, nullObjectBuilder.getFactory());
        }
    }

    private void visitCtMethods(Set<CtMethod<?>> methods) {
        methods.forEach(this::visitCtMethod);
    }

    public void elevateMethods(CtType<?> source, CtType<?> dest) {
        elevateMethods(dest, source.getMethods());
    }

    private void elevateMethods(CtType<?> ctType, Set<CtMethod<?>> methods) {
        methods.stream()
                .filter(CtModifiable::isPublic)
                .filter(ctMethod -> !ctMethod.isStatic())
                .filter(method -> !Set.of("toString", "equals", "hashCode", "clone").contains(method.getSimpleName()))
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

    /**
     * Changes the method return type to FooNullObject
     *
     * @param ctMethod
     * @param newReturnType
     */
    private void changeMethodReturnTypeToNullObject(CtMethod ctMethod, CtTypeReference<?> newReturnType) {
        ctMethod.setType(newReturnType);
    }

    private <T> void addOverrideAnnotationToMethods(CtClass<T> ctClass) {
        ctClass.getMethods().stream().filter(CtModifiable::isPublic)
                .filter(ctMethod -> !ctMethod.isStatic())
                .filter(ctMethod -> !ctMethod.hasAnnotation(Override.class))
                .forEach(ctMethod -> ctMethod.addAnnotation(nullObjectBuilder.createAnnotation(nullObjectBuilder.createCtTypeReference(Override.class))));
    }

    private void implementNullInterfaceMethods(CtType<?> nullObjectType, CtClass<?> nullClass) {

        nullObjectType.getMethods()
                .stream()
                .filter(ctMethod -> !ctMethod.isStatic())
                .forEach(ctMethod -> {

                    CtBlock<?> block = nullObjectBuilder.createBlock();
                    CtTypeReference type = ctMethod.getType();

                    CtMethod<Object> newMethod = nullObjectBuilder.createMethod();
                    newMethod.setSimpleName(ctMethod.getSimpleName());
                    newMethod.setParameters(ctMethod.getParameters());
                    newMethod.setModifiers(ctMethod.getModifiers());
                    newMethod.setType(type);
                    newMethod.addAnnotation(nullObjectBuilder.createAnnotation(nullObjectBuilder.createCtTypeReference(Override.class)));

                    if (type.equals(nullObjectBuilder.createCtTypeReference(void.class))) {
                        // do nothing
                    } else if (type.equals(nullObjectBuilder.createCtTypeReference(Void.class))) {
                        block.addStatement(nullObjectBuilder.createReturn().setReturnedExpression(nullObjectBuilder.createCodeSnippetExpression("null")));
                    } else if (type.equals(nullObjectBuilder.createCtTypeReference(Integer.class)) || type.equals(nullObjectBuilder.createCtTypeReference(int.class))) {
                        block.addStatement(nullObjectBuilder.createReturn().setReturnedExpression(nullObjectBuilder.createCodeSnippetExpression("0")));
                    } else if (type.equals(nullObjectBuilder.createCtTypeReference(Float.class)) || type.equals(nullObjectBuilder.createCtTypeReference(float.class))) {
                        block.addStatement(nullObjectBuilder.createReturn().setReturnedExpression(nullObjectBuilder.createCodeSnippetExpression("0.0f")));
                    } else if (type.equals(nullObjectBuilder.createCtTypeReference(Double.class)) || type.equals(nullObjectBuilder.createCtTypeReference(double.class))) {
                        block.addStatement(nullObjectBuilder.createReturn().setReturnedExpression(nullObjectBuilder.createCodeSnippetExpression("0.0d")));
                    } else if (type.equals(nullObjectBuilder.createCtTypeReference(Long.class)) || type.equals(nullObjectBuilder.createCtTypeReference(long.class))) {
                        block.addStatement(nullObjectBuilder.createReturn().setReturnedExpression(nullObjectBuilder.createCodeSnippetExpression("0L")));
                    } else if (type.equals(nullObjectBuilder.createCtTypeReference(Byte.class)) || type.equals(nullObjectBuilder.createCtTypeReference(byte.class))) {
                        block.addStatement(nullObjectBuilder.createReturn().setReturnedExpression(nullObjectBuilder.createCodeSnippetExpression("0")));
                    } else if (type.equals(nullObjectBuilder.createCtTypeReference(Boolean.class)) || type.equals(nullObjectBuilder.createCtTypeReference(boolean.class))) {
                        block.addStatement(nullObjectBuilder.createReturn().setReturnedExpression(nullObjectBuilder.createCodeSnippetExpression("false")));
                    } else if (type.equals(nullObjectBuilder.createCtTypeReference(Character.class)) || type.equals(nullObjectBuilder.createCtTypeReference(char.class))) {
                        block.addStatement(nullObjectBuilder.createReturn().setReturnedExpression(nullObjectBuilder.createCodeSnippetExpression("'\\u0000'")));
                    } else if (type.equals(nullObjectBuilder.createCtTypeReference(Short.class)) || type.equals(nullObjectBuilder.createCtTypeReference(short.class))) {
                        block.addStatement(nullObjectBuilder.createReturn().setReturnedExpression(nullObjectBuilder.createCodeSnippetExpression("0")));
                    } else if (type.equals(nullObjectBuilder.createCtTypeReference(String.class))) {
                        block.addStatement(nullObjectBuilder.createReturn().setReturnedExpression(nullObjectBuilder.createCodeSnippetExpression("\"\"")));
                    } else if (type.isSubtypeOf(nullObjectBuilder.createCtTypeReference(Map.class))) {
                        block.addStatement(nullObjectBuilder.createReturn().setReturnedExpression(nullObjectBuilder.createCodeSnippetExpression("java.util.Collections.emptyMap()")));
                    } else if (type.isSubtypeOf(nullObjectBuilder.createCtTypeReference(Set.class))) {
                        block.addStatement(nullObjectBuilder.createReturn().setReturnedExpression(nullObjectBuilder.createCodeSnippetExpression("java.util.Collections.emptySet()")));
                    } else if (type.isSubtypeOf(nullObjectBuilder.createCtTypeReference(List.class))) {
                        block.addStatement(nullObjectBuilder.createReturn().setReturnedExpression(nullObjectBuilder.createCodeSnippetExpression("java.util.Collections.emptyList()")));
                    } else if (type.isSubtypeOf(nullObjectBuilder.createCtTypeReference(Collection.class))) {
                        block.addStatement(nullObjectBuilder.createReturn().setReturnedExpression(nullObjectBuilder.createCodeSnippetExpression("java.util.Collections.emptyList()")));
                    } else if (type.isSubtypeOf(nullObjectBuilder.createCtTypeReference(Iterable.class))) {
                        block.addStatement(nullObjectBuilder.createReturn().setReturnedExpression(nullObjectBuilder.createCodeSnippetExpression("java.util.Collections.emptyList()")));
                    } else if (type.getQualifiedName().startsWith(Poptional.class.getName())) {
                        block.addStatement(nullObjectBuilder.createReturn().setReturnedExpression(nullObjectBuilder.createCodeSnippetExpression(Poptional.class.getSimpleName() + ".empty()")));
                    } else if (type.getSimpleName().endsWith("NullObject")) {
                        // todo: use an @NullObjectReturn annotation for method returns
                        block.addStatement(nullObjectBuilder.createReturn().setReturnedExpression(nullObjectBuilder.createCodeSnippetExpression(type.getSimpleName() + "." + NULL_INSTANCE + "()")));
                    } else {
                        block.addStatement(nullObjectBuilder.createCodeSnippetStatement("return null"));
                    }

                    newMethod.setBody(block);
                    nullClass.addMethod(newMethod);
                });
    }

    // NullFoo extends Nothing<Foo>
    private void implementNothingInterface(CtClass<?> ctClass, CtClass<?> aClass) {
        poptionalBuilder.implementNothing(ctClass, aClass);
    }

    private void generateFile(CtElement ctElement) {
        getFileGenerator().process(ctElement);
    }

    private FileGenerator getFileGenerator() {
        return environment.getDefaultFileGenerator();
    }
}
