package poptional.codegen;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import poptional.Poptional;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtModifiable;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

import java.util.Set;

import static java.util.Collections.emptySet;

@RequiredArgsConstructor
public class NullObjectFactory {

	@Getter
	private final Factory factory;

	public CtInterface<?> createNullObjInterface(CtClass<?> ctClass) {

		CtInterface<?> ctInterface = declareNullObjectInterface(ctClass);
		implementPoptionalSuperInterface(ctInterface, ctClass);
		declareInterfaceMethods(ctInterface, ctClass);

		return ctInterface;
	}

	private CtInterface<?> declareNullObjectInterface(CtClass<?> ctClass) {
		return getFactory().createInterface(ctClass.getPackage(), "Nullable" + ctClass.getSimpleName());
	}

	private void implementPoptionalSuperInterface(CtInterface<?> ctInterface, CtClass<?> ctClass) {
		CtTypeReference<Poptional<?>> poptional = getFactory().createCtTypeReference(Poptional.class);
		poptional.addActualTypeArgument(ctClass.getReference());
		ctInterface.addSuperInterface(poptional);
	}

	private void declareInterfaceMethods(CtInterface<?> anInterface, CtClass<?> ctClass) {
		declareInterfaceMethods(anInterface, ctClass.getMethods());
	}

	private void declareInterfaceMethods(CtInterface<?> anInterface, Set<CtMethod<?>> methods) {
		methods.stream()
				.filter(CtModifiable::isPublic)
				.forEach(method -> declareInterfaceMethod(anInterface, method));
	}

	private void declareInterfaceMethod(CtInterface<?> anInterface, CtMethod<?> method) {
		getFactory().createMethod(anInterface, method.getModifiers(), method.getType(), method.getSimpleName(),
				method.getParameters(), emptySet());
	}
}
