package optional4j.codegen.visitor;

import lombok.RequiredArgsConstructor;
import optional4j.annotation.OptionalReturn;
import optional4j.codegen.CodeGenUtil;
import optional4j.codegen.builder.CollaboratorBuilder;
import optional4j.codegen.builder.ValueTypeBuilder;
import optional4j.codegen.processor.ProcessorProperties;
import optional4j.annotation.Mode;
import optional4j.annotation.Collaborator;
import optional4j.spec.Optional;
import optional4j.annotation.ValueType;
import spoon.compiler.Environment;
import spoon.processing.AnnotationProcessor;
import spoon.reflect.code.CtReturn;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtNamedElement;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtAbstractVisitor;

import javax.annotation.NonNull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import java.util.Set;
import java.util.stream.Collectors;

import static optional4j.codegen.CodeGenUtil.addGeneratedAnnotation;
import static optional4j.codegen.CodeGenUtil.addNonNullAnnotation;
import static optional4j.codegen.CodeGenUtil.getNullableMethods;
import static optional4j.codegen.CodeGenUtil.getNullness;
import static optional4j.codegen.CodeGenUtil.hasNonNullAnnotation;
import static optional4j.codegen.CodeGenUtil.isOptimisticMode;
import static optional4j.codegen.CodeGenUtil.isOptionalReturn;
import static optional4j.codegen.CodeGenUtil.isVoidReturn;
import static optional4j.codegen.CodeGenUtil.printProcessing;
import static optional4j.codegen.CodeGenUtil.removeAnnotation;
import static optional4j.codegen.CodeGenUtil.returnsNullObjectType;
import static optional4j.codegen.CodeGenUtil.returnsOptionalType;
import static optional4j.support.NullityValue.NULLABLE;
import static spoon.reflect.declaration.ModifierKind.PRIVATE;
import static spoon.reflect.declaration.ModifierKind.PROTECTED;
import static spoon.reflect.declaration.ModifierKind.PUBLIC;

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
			ctMethod.accept(new CollaboratorVisitor(processorClass, environment,
					new CollaboratorBuilder(valueTypeBuilder.getFactory()), valueTypeBuilder, processorProperties));
			return;
		}

		if (isOptionalReturn(ctMethod)) { // @OptionalReturn
			removeAnnotation(ctMethod, valueTypeBuilder.getFactory(), OptionalReturn.class);
		} else {

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

		CtMethod<T> newMethod = ctMethod.clone(); // getAddress(){}
		changeMethodReturnTypeToPoptional(newMethod); // Optional<Address> getAddress(){}
		addNonNullAnnotation(newMethod, valueTypeBuilder.getFactory()); // @NonNull Optional<Address> getAddress(){}
		ctMethod.setSimpleName("do_" + ctMethod.getSimpleName()); // do_getAddress(){}

		if (ctMethod.getDeclaringType()
				.isClass()) {
			makePrivate(ctMethod); // private do_getAddress(){}
		}

		String parameters = ctMethod.getParameters()
				.stream()
				.map(CtNamedElement::getSimpleName)
				.collect(Collectors.joining(","));

		newMethod.setBody(valueTypeBuilder.getFactory()
				.createReturn()
				// todo: fix getReturnStatement to be able to make it optimistic
				.setReturnedExpression(valueTypeBuilder.getFactory()
						.createCodeSnippetExpression(
								"Optional.ofNullable(" + ctMethod.getSimpleName() + "(" + parameters + "))")));

		ctMethod.getDeclaringType()
				.addMethod(newMethod);
	}

	private <T> void makePrivate(CtMethod<T> ctMethod) {
		Set<ModifierKind> modifiers = ctMethod.getModifiers();
		modifiers.removeAll(Set.of(PRIVATE, PROTECTED, PUBLIC));
		modifiers.add(PRIVATE);
		ctMethod.setModifiers(modifiers);
	}

	private String getReturnStatement(CtReturn<?> returnStatement, CtMethod<?> ctMethod) {

		if (isOptimisticMode(ctMethod, processorProperties)) {
			return ifNullStatement(returnStatement);
		}

		return poptionalOfNullableStatement(returnStatement);
	}

	private String poptionalOfNullableStatement(CtReturn<?> returnStatement) {
		return "return Optional.ofNullable(" + returnStatement.getReturnedExpression() + ")";
	}

	private String ifNullStatement(CtReturn<?> returnStatement) {
		return "return (" + returnStatement.getReturnedExpression() + ") == null? Optional.empty(): "
				+ returnStatement.getReturnedExpression();
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

	/**
	 * Changes the method return type to Optional<original-return-type>
	 *
	 * @param ctMethod
	 */
	private void changeMethodReturnTypeToPoptional(CtMethod ctMethod) {
		ctMethod.setType(poptionalOf(ctMethod));
	}

	private void implementSomething(CtType<?> ctType) {
		valueTypeBuilder.implementSomething(ctType);
	}

	private CtTypeReference<Optional<?>> poptionalOf(CtMethod<?> ctMethod) {
		return valueTypeBuilder.createOptionalOf(CodeGenUtil.getReturnTypeRef(ctMethod));
	}
}
