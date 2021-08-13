package poptional.codegen.processor;

import poptional.OptionalType;
import poptional.Poptional;
import poptional.codegen.PoptionalFactory;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtAbstractVisitor;

import javax.annotation.NonNull;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static spoon.reflect.declaration.ModifierKind.FINAL;

public class OptionalTypeProcessor extends AbstractAnnotationProcessor<OptionalType, CtElement> {

    private static final String _NonNull = "javax.annotation.NonNull";
    private static final String _Nonnull = "javax.annotation.Nonnull";
    private static final String CLASS_TAB = "\t";
    private static final String METHOD_TAB = "\t\t";

    @Override
    public void process(OptionalType optionalReturn, CtElement ctElement) {

        ctElement.accept(new CtAbstractVisitor() {

            @Override
            public <T> void visitCtClass(CtClass<T> ctClass) {

                getEnvironment().reportProgressMessage(CLASS_TAB + "Processing class: " + ctClass.getSimpleName());

                implementSomething(ctClass);
                if (!isFinal(ctClass)) {
                    makeFinal(ctClass);
                }
                visitCtMethods(ctClass.getMethods());
            }

            private <T> CtModifiable makeFinal(CtClass<T> ctClass) {
                return ctClass.addModifier(FINAL);
            }

            private <T> boolean isFinal(CtClass<T> ctClass) {
                return ctClass.hasModifier(FINAL);
            }

            private void visitCtMethods(Set<CtMethod<?>> methods) {
                methods.forEach(this::visitCtMethod);
            }

            @Override
            public <T> void visitCtMethod(CtMethod<T> ctMethod) {

                getEnvironment().reportProgressMessage(METHOD_TAB + "Processing method: " + ctMethod.getSimpleName());

                // todo: Think about accepting if it alternately implements Poptional.
                if (!returnsOptionalType(ctMethod)) {
                    return;
                }

                if (hasNotNullAnnotations(ctMethod)) {
                    return;
                }

                changeMethodReturnTypeToPoptional(ctMethod);
                replaceReturnStatementsWithPoptionalOfNullable(ctMethod);
                ctMethod.addAnnotation(getFactory().createAnnotation().setAnnotationType(getFactory().createCtTypeReference(NonNull.class)));
            }

            private <T> boolean returnsOptionalType(CtMethod<T> ctMethod) {
                return getReturnType(ctMethod).hasAnnotation(OptionalType.class);
            }

            private <T> boolean hasNotNullAnnotations(CtMethod<T> ctMethod) {
                return ctMethod.getAnnotations().stream()
                        .map(ctAnnotation -> ctAnnotation.getType().getQualifiedName())
                        .anyMatch(notNullAnnotations());
            }


            private Predicate<String> notNullAnnotations() {
                return _NonNull().or(_Nonnull());
            }

            private Predicate<String> _NonNull() {
                return _NonNull::equals;
            }

            private Predicate<String> _Nonnull() {
                return _Nonnull::equals;
            }
        });

        clearConsumedAnnotationTypes();
    }

    private void implementSomething(CtClass<?> ctClass) {
        getPoptionalFactory().implementSomething(ctClass);
    }

    private CtTypeReference<Poptional<?>> poptionalOf(CtMethod<?> ctMethod) {
        return getPoptionalFactory().createPoptionalOf(getReturnTypeRef(ctMethod));
    }

    private PoptionalFactory getPoptionalFactory() {
        return new PoptionalFactory(getFactory());
    }

    /**
     * Changes the method return type to Poptional<original-return-type>
     *
     * @param ctMethod
     */
    private void changeMethodReturnTypeToPoptional(CtMethod ctMethod) {
        ctMethod.setType(poptionalOf(ctMethod));
    }

    /**
     * @param ctMethod
     * @return the method's return type as a type reference
     */
    private CtTypeReference<?> getReturnTypeRef(CtMethod<?> ctMethod) {
        return getReturnType(ctMethod).getReference();
    }

    /**
     * @param ctMethod
     * @return the method's return type
     */
    private CtType<?> getReturnType(CtMethod<?> ctMethod) {
        return ctMethod.getType()
                .getTypeDeclaration();
    }

    private void replaceReturnStatementsWithPoptionalOfNullable(CtMethod<?> ctMethod) {
        List<CtStatement> originalStatements = getMethodStatements(ctMethod);
        List<CtStatement> updatedStatements = replaceReturnStatementsWithPoptionalOfNullable(originalStatements);
        setMethodStatements(ctMethod, updatedStatements);
    }

    private List<CtStatement> replaceReturnStatementsWithPoptionalOfNullable(List<CtStatement> originalStatements) {
        return originalStatements.stream()
                .map(this::replaceReturnStatementWithPoptionalOfNullable)
                .collect(Collectors.toList());
    }

    private CtStatement replaceReturnStatementWithPoptionalOfNullable(CtStatement originalStatement) {
        if (!(originalStatement instanceof CtReturn<?>)) {
            return originalStatement;
        }
        return createPoptionalOfNullableStatement((CtReturn<?>) originalStatement);
    }

    private CtCodeSnippetStatement createPoptionalOfNullableStatement(CtReturn<?> returnStatement) {
        return getFactory().createCodeSnippetStatement(
                poptionalOfNullableStatement(returnStatement));
    }

    private String poptionalOfNullableStatement(CtReturn<?> returnStatement) {
        return "return Poptional.ofNullable(" + returnStatement.getReturnedExpression() + ")";
    }

    private String ifNullStatement(CtReturn<?> returnStatement) {
        return "return (" + returnStatement.getReturnedExpression() + ") == null? Poptional.empty(): " + returnStatement.getReturnedExpression();
    }

    private void setMethodStatements(CtMethod<?> ctMethod, List<CtStatement> statements) {
        ctMethod.getBody()
                .setStatements(statements);
    }

    private List<CtStatement> getMethodStatements(CtMethod<?> ctMethod) {
        return ctMethod.getBody()
                .getStatements();
    }
}