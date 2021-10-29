package optional4j.codegen.builder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import optional4j.spec.Absent;
import optional4j.spec.Optional;
import optional4j.spec.Present;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

import static spoon.reflect.declaration.ModifierKind.PUBLIC;

@RequiredArgsConstructor
public class ValueTypeBuilder {

	@Getter
	@Delegate
	private final Factory factory;

	public void implementSomething(CtType<?> ctType) {
		CtTypeReference<Present<?>> somethingOf = createSomethingOf(ctType);
		if (somethingOf.isInterface()) {
			ctType.addSuperInterface(somethingOf);
		} else if (somethingOf.isClass()) {
			ctType.setSuperclass(somethingOf);
		}
	}

	/**
	 * interface OptionalCustomer implements Optional<Customer>{}
	 *
	 * @param ctType
	 * @return
	 */
	public CtType<?> createEnhancedOptionalType(CtType<?> ctType) {

		CtType<?> nType = declareNType(ctType);
		nType.addSuperInterface(createOptionalOf(ctType.getReference()));
		nType.addModifier(PUBLIC);
		return nType;
	}

	/**
	 * interface NCustomer {}
	 *
	 * @param ctType
	 * @return
	 */
	private CtType<?> declareNType(CtType<?> ctType) {
		return getFactory().createInterface(getNTypeQualifiedName(ctType));
	}

	/**
	 * NCustomer
	 *
	 * @param ctType
	 * @return
	 */
	private String getNTypeQualifiedName(CtType<?> ctType) {
		return ctType.getPackage().getQualifiedName() + ".Optional" + ctType.getSimpleName();
	}

	public void implementNothing(CtClass<?> ctClass, CtClass<?> aClass) {
		CtTypeReference<Present<?>> nothingOf = createNothingOf(aClass);
		if (nothingOf.isInterface()) {
			ctClass.addSuperInterface(nothingOf);
		} else if (nothingOf.isClass()) {
			ctClass.setSuperclass(nothingOf);
		}
	}

	public CtTypeReference<Present<?>> createSomethingOf(CtType<?> ctType) {
		CtTypeReference<Present<?>> something = getFactory().createCtTypeReference(Present.class);
		something.addActualTypeArgument(ctType.getReference());
		return something;
	}

	public CtTypeReference<Present<?>> createNothingOf(CtClass<?> ctClass) {
		CtTypeReference<Present<?>> something = getFactory().createCtTypeReference(Absent.class);
		something.addActualTypeArgument(ctClass.getReference());
		return something;
	}

	public CtTypeReference<Optional<?>> createOptionalOf(CtTypeReference<?> ctTypeReference) {
		return getFactory().createCtTypeReference(Optional.class)
				.addActualTypeArgument(ctTypeReference);
	}
}