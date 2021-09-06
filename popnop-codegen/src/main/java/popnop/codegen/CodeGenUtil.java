package popnop.codegen;

import popnop.spec.NullObjectType;
import popnop.codegen.processor.ProcessorProperties;
import popnop.spec.OptionalType;
import popnop.spec.Mode;
import popnop.spec.ModeValue;
import popnop.spec.NullnessValue;
import spoon.SpoonException;
import spoon.compiler.Environment;
import spoon.processing.AnnotationProcessor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

import javax.annotation.NonNull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toSet;
import static popnop.spec.ModeValue.*;
import static popnop.spec.NullnessValue.NON_NULL;
import static popnop.spec.NullnessValue.NULLABLE;

public class CodeGenUtil {

    public static final String NULL_INSTANCE = "nullInstance";
    
    private static final String _NonNull = "javax.annotation.NonNull";
    private static final String _Nonnull = "javax.annotation.Nonnull";
    private static final String METHOD_TAB = "\t\t";
    private static final String CLASS_TAB = "\t";

    public static <T> void printProcessing(Environment environment, CtClass<T> ctClass) {
        environment.reportProgressMessage(CLASS_TAB + "Processing class: " + ctClass.getSimpleName());
    }

    public static <T> void printProcessing(Environment environment, String text) {
        environment.reportProgressMessage(text);
    }

    public static <T> void printProcessing(Environment environment, CtMethod<T> ctMethod) {
        environment.reportProgressMessage(METHOD_TAB + "Processing method: " + ctMethod.getSimpleName());
    }

    public static <T> boolean hasNonNullAnnotation(CtMethod<T> ctMethod) {
        return ctMethod.getAnnotations().stream()
                .map(ctAnnotation -> ctAnnotation.getType().getQualifiedName())
                .anyMatch(notNullAnnotations());
    }

    private static Predicate<String> notNullAnnotations() {
        return _NonNull().or(_Nonnull());
    }

    private static Predicate<String> _NonNull() {
        return _NonNull::equals;
    }

    private static Predicate<String> _Nonnull() {
        return _Nonnull::equals;
    }

    public static <T> boolean returnsOptionalType(CtMethod<T> ctMethod) {
        return getReturnType(ctMethod).hasAnnotation(OptionalType.class);
    }
    
    /**
     * @param ctMethod
     * @return the method's return type as a type reference
     */
    public static CtTypeReference<?> getReturnTypeRef(CtMethod<?> ctMethod) {
        return getReturnType(ctMethod).getReference();
    }

    public static <T> boolean returnsNullObjectType(CtMethod<T> ctMethod) {
        return getReturnType(ctMethod).hasAnnotation(NullObjectType.class);
    }

    /**
     * @param ctMethod
     * @return the method's return type
     */
    public static CtType<?> getReturnType(CtMethod<?> ctMethod) {
        return ctMethod.getType()
                .getTypeDeclaration();
    }

    public static void setMethodStatements(CtMethod<?> ctMethod, List<CtStatement> statements) {
        ctMethod.getBody()
                .setStatements(statements);
    }

    public static List<CtStatement> getMethodStatements(CtMethod<?> ctMethod) {
        return ctMethod.getBody()
                .getStatements();
    }

    public static void addGeneratedAnnotation(CtElement ctElement, Factory factory, Class<? extends AnnotationProcessor<?, ?>> clazz) {

        if (ctElement.hasAnnotation(Generated.class)) {
            return;
        }

        CtAnnotation<Annotation> generatedAnnotation = factory.createAnnotation(factory.createCtTypeReference(Generated.class));

        generatedAnnotation.addValue("value", clazz.getName());

        ctElement.addAnnotation(generatedAnnotation);
    }

    public static <T> Set<CtMethod<?>> getNullableMethods(CtClass<T> ctClass) {
        return ctClass.getMethods()
                .stream()
                .filter(ctMethod -> ctMethod.hasAnnotation(Nullable.class))
                .collect(toSet());
    }

    public static void removeAnnotation(CtElement ctElement, Factory factory, Class<? extends Annotation> annotation) {
        if (!ctElement.hasAnnotation(annotation)) {
            return;
        }
        ctElement.removeAnnotation(ctElement.getAnnotation(factory.createCtTypeReference(annotation)));
    }

    public static <T> void addNonNullAnnotation(CtMethod<T> ctMethod, Factory factory) {
        if (ctMethod.hasAnnotation(NonNull.class)) {
            return;
        }
        ctMethod.addAnnotation(factory.createAnnotation().setAnnotationType(factory.createCtTypeReference(NonNull.class)));
    }

    public static NullnessValue getNullness(CtElement ctElement, ProcessorProperties processorProperties) {

        Nullable localNullable = ctElement.getAnnotation((Nullable.class));
        if (localNullable != null) {
            return NULLABLE;
        }

        NonNull localNonNull = ctElement.getAnnotation((NonNull.class));
        if (localNonNull != null) {
            return NON_NULL;
        }

        String globalNullness = processorProperties.getNullness();
        if ("nullable".equals(globalNullness.toLowerCase())) {
            return NULLABLE;
        }

        if ("nonnull".equals(globalNullness.toLowerCase())) {
            return NON_NULL;
        }

        throw new SpoonException("Unknown nullness property: " + globalNullness + ". Valid values are: [nullable, nonnull]");
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

        String globalMode = processorProperties.getMode();
        if (STRICT.name().toLowerCase().equals(globalMode.toLowerCase())) {
            return STRICT;
        }

        if (FULL.name().toLowerCase().equals(globalMode.toLowerCase())) {
            return FULL;
        }

        throw new SpoonException("Unknown nullness property: " + globalMode + ". Valid values are: " + modeValues());
    }

    public static <T> boolean isStrictMode(CtMethod<T> ctMethod, ProcessorProperties processorProperties) {
        return getMode(ctMethod, processorProperties) == STRICT;
    }
}
