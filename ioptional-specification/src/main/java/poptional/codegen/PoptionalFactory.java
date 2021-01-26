package poptional.codegen;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import poptional.Poptional;
import poptional.Something;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

@RequiredArgsConstructor
public class PoptionalFactory {

	@Getter
	private final Factory factory;

	public void implementSomething(CtClass<?> ctClass) {
		ctClass.addSuperInterface(createSomethingOf(ctClass));
	}

	public CtTypeReference<Something<?>> createSomethingOf(CtClass<?> ctClass) {
		CtTypeReference<Something<?>> something = getFactory().createCtTypeReference(Something.class);
		something.addActualTypeArgument(ctClass.getReference());
		return something;
	}

	public CtTypeReference<Poptional<?>> createPoptionalOf(CtTypeReference<?> ctTypeReference) {
		CtTypeReference<Poptional<?>> poptional = getFactory().createCtTypeReference(Poptional.class);
		poptional.addActualTypeArgument(ctTypeReference);
		return poptional;
	}
}
