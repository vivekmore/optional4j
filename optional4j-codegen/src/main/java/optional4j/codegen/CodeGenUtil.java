package optional4j.codegen;

import static java.util.stream.Collectors.toSet;
import static optional4j.support.ModeValue.*;
import static optional4j.support.NullityValue.NON_NULL;
import static optional4j.support.NullityValue.NULLABLE;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.NonNull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import optional4j.annotation.Collaborator;
import optional4j.annotation.Mode;
import optional4j.annotation.OptionalReturn;
import optional4j.annotation.ValueType;
import optional4j.codegen.processor.ProcessorProperties;
import optional4j.support.ModeValue;
import optional4j.support.NullityValue;
import spoon.compiler.Environment;
import spoon.processing.AnnotationProcessor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class CodeGenUtil {

    private static final String NonNull = "javax.annotation.NonNull";
    private static final String Nonnull = "javax.annotation.Nonnull";
    private static final String METHOD_TAB = "\t\t";
    private static final String CLASS_TAB = "\t";

    public static <T> void printProcessing(Environment environment, CtType<T> ctType) {
        environment.reportProgressMessage(
                CLASS_TAB + "Processing class: " + ctType.getSimpleName());
    }

    public static void printProcessing(Environment environment, String text) {
        environment.reportProgressMessage(text);
    }

    public static <T> void printProcessing(Environment environment, CtMethod<T> ctMethod) {
        environment.reportProgressMessage(
                METHOD_TAB + "Processing method: " + ctMethod.getSimpleName());
    }

    public static <T> boolean hasNonNullAnnotation(CtMethod<T> ctMethod) {
        return ctMethod.getAnnotations().stream()
                .map(ctAnnotation -> ctAnnotation.getType().getQualifiedName())
                .anyMatch(nonNullAnnotations());
    }

    private static Predicate<String> nonNullAnnotations() {
        return nonNull().or(nonnull());
    }

    private static Predicate<String> nonNull() {
        return NonNull::equals;
    }

    private static Predicate<String> nonnull() {
        return Nonnull::equals;
    }

    public static <T> boolean isValueType(CtMethod<T> ctMethod) {
        return getReturnType(ctMethod).hasAnnotation(ValueType.class);
    }

    /**
     * @param ctMethod
     * @return the method's return type as a type reference
     */
    public static CtTypeReference<?> getReturnTypeRef(CtMethod<?> ctMethod) {
        return getReturnType(ctMethod).getReference();
    }

    public static <T> boolean returnsNullObjectType(CtMethod<T> ctMethod) {
        return getReturnType(ctMethod).hasAnnotation(Collaborator.class);
    }

    public static <T> boolean isOptionalReturn(CtMethod<T> ctMethod) {
        return ctMethod.hasAnnotation(OptionalReturn.class);
    }

    /**
     * @param ctMethod
     * @return the method's return type
     */
    public static CtType<?> getReturnType(CtMethod<?> ctMethod) {
        return ctMethod.getType().getTypeDeclaration();
    }

    public static void setMethodStatements(CtMethod<?> ctMethod, List<CtStatement> statements) {
        ctMethod.getBody().setStatements(statements);
    }

    public static List<CtStatement> getMethodStatements(CtMethod<?> ctMethod) {
        return ctMethod.getBody().getStatements();
    }

    public static void addGeneratedAnnotation(
            CtElement ctElement,
            Factory factory,
            Class<? extends AnnotationProcessor<?, ?>> clazz) {

        if (ctElement.hasAnnotation(Generated.class)) {
            return;
        }

        CtAnnotation<Annotation> generatedAnnotation =
                factory.createAnnotation(factory.createCtTypeReference(Generated.class));

        generatedAnnotation.addValue("value", clazz.getName());

        ctElement.addAnnotation(generatedAnnotation);
    }

    public static <T> boolean isVoidReturn(CtMethod<T> ctMethod, Factory factory) {
        return ctMethod.getType().equals(factory.createCtTypeReference(void.class))
                || ctMethod.getType().equals(factory.createCtTypeReference(Void.class));
    }

    public static <T> Set<CtMethod<?>> getNullableMethods(CtType<T> ctType) {
        return ctType.getMethods().stream()
                .filter(ctMethod -> ctMethod.hasAnnotation(Nullable.class))
                .collect(toSet());
    }

    public static void removeAnnotation(
            CtElement ctElement, Factory factory, Class<? extends Annotation> annotation) {
        if (!ctElement.hasAnnotation(annotation)) {
            return;
        }
        ctElement.removeAnnotation(
                ctElement.getAnnotation(factory.createCtTypeReference(annotation)));
    }

    public static <T> void addNonNullAnnotation(CtMethod<T> ctMethod, Factory factory) {
        if (ctMethod.hasAnnotation(NonNull.class)) {
            return;
        }
        ctMethod.addAnnotation(
                factory.createAnnotation()
                        .setAnnotationType(factory.createCtTypeReference(NonNull.class)));
    }

    public static NullityValue getNullness(
            CtElement ctElement, ProcessorProperties processorProperties) {

        Nullable localNullable = ctElement.getAnnotation((Nullable.class));
        if (localNullable != null) {
            return NULLABLE;
        }

        NonNull localNonNull = ctElement.getAnnotation((NonNull.class));
        if (localNonNull != null) {
            return NON_NULL;
        }

        return processorProperties.getNullity();
    }

    public static ModeValue getMode(CtMethod<?> ctMethod, ProcessorProperties processorProperties) {

        Mode mode = ctMethod.getAnnotation((Mode.class));
        if (mode != null) {
            return mode.value();
        }

        mode = ctMethod.getParent().getAnnotation(Mode.class);
        if (mode != null) {
            return mode.value();
        }

        return processorProperties.getMode();
    }

    public static <T> boolean isOptimisticMode(
            CtMethod<T> ctMethod, ProcessorProperties processorProperties) {
        return getMode(ctMethod, processorProperties) == OPTIMISTIC;
    }
}
