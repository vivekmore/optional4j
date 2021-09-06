package popnop.codegen.builder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import popnop.spec.Nothing;
import popnop.spec.Poptional;
import popnop.spec.Something;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

@RequiredArgsConstructor
public class PoptionalBuilder {

	@Getter
	@Delegate
	private final Factory factory;

	public void implementSomething(CtClass<?> ctClass) {
		ctClass.setSuperclass(createSomethingOf(ctClass));
	}

	public void implementNothing(CtClass<?> ctClass, CtClass<?> aClass) {
		ctClass.setSuperclass(createNothingOf(aClass));
	}

	public CtTypeReference<Something<?>> createSomethingOf(CtClass<?> ctClass) {
		CtTypeReference<Something<?>> something = getFactory().createCtTypeReference(Something.class);
		something.addActualTypeArgument(ctClass.getReference());
		return something;
	}

	public CtTypeReference<Something<?>> createNothingOf(CtClass<?> ctClass) {
		CtTypeReference<Something<?>> something = getFactory().createCtTypeReference(Nothing.class);
		something.addActualTypeArgument(ctClass.getReference());
		return something;
	}

	public CtTypeReference<Poptional<?>> createPoptionalOf(CtTypeReference<?> ctTypeReference) {
		return getFactory().createCtTypeReference(Poptional.class)
				.addActualTypeArgument(ctTypeReference);
	}
}