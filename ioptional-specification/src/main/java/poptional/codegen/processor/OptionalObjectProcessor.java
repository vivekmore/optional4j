package poptional.codegen.processor;

import poptional.OptionalObject;
import poptional.Poptional;
import poptional.codegen.PoptionalFactory;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtAbstractVisitor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static spoon.reflect.declaration.ModifierKind.FINAL;

public class OptionalObjectProcessor extends AbstractAnnotationProcessor<OptionalObject, CtElement> {

	@Override
	public void process(OptionalObject optionalReturn, CtElement ctElement) {

		ctElement.accept(new CtAbstractVisitor() {

			@Override
			public <T> void visitCtClass(CtClass<T> ctClass) {
				implementSomething(ctClass);
				if (!ctClass.hasModifier(FINAL)) {
					ctClass.addModifier(FINAL);
				}
				visitCtMethods(ctClass.getMethods());
			}

			private void visitCtMethods(Set<CtMethod<?>> methods) {
				methods.forEach(this::visitCtMethod);
			}

			@Override
			public <T> void visitCtMethod(CtMethod<T> ctMethod) {

				// todo: Think about accepting if it alternately implements Poptional.
				if (!getReturnType(ctMethod).hasAnnotation(OptionalObject.class)) {
					return;
				}

				if (ctMethod.hasAnnotation(OptionalObject.NotNull.class)) {
					return;
				}

				changeMethodReturnTypeToPoptional(ctMethod);
				replaceReturnStatementsWithPoptionalOfNullable(ctMethod);
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
				"return Poptional.ofNullable(" + returnStatement.getReturnedExpression() + ")");
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

