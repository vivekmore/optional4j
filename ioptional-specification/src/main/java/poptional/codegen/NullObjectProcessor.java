package poptional.codegen;

import poptional.NullObject;
import poptional.Poptional;
import poptional.Something;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.processing.FileGenerator;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtModifiable;
import spoon.reflect.reference.CtTypeReference;

import java.util.Collections;

public class NullObjectProcessor extends AbstractAnnotationProcessor<NullObject, CtClass> {

	@Override
	public void process(NullObject nullObject, CtClass ctClass) {

		CtInterface<?> nullObjInterface = createNullObjInterface(ctClass);

		FileGenerator defaultFileGenerator = getEnvironment().getDefaultFileGenerator();
		defaultFileGenerator.process(nullObjInterface);

		ctClass.addSuperInterface(nullObjInterface.getReference());
		CtTypeReference<Something<?>> something = getFactory().createCtTypeReference(Something.class);
		something.addActualTypeArgument(ctClass.getReference());
		ctClass.addSuperInterface(something);

		clearConsumedAnnotationTypes();
	}

	private CtInterface<?> createNullObjInterface(CtClass<?> ctClass) {

		CtInterface<?> anInterface = getFactory().createInterface(ctClass.getPackage(),
				ctClass.getSimpleName() + "NullObject");

		ctClass.getMethods()
				.stream()
				.filter(CtModifiable::isPublic)
				.forEach(method -> {
					getFactory().createMethod(anInterface, method.getModifiers(), method.getType(),
							method.getSimpleName(), method.getParameters(), Collections.emptySet());
				});

		CtTypeReference<Poptional<?>> poptional = getFactory().createCtTypeReference(Poptional.class);
		poptional.addActualTypeArgument(ctClass.getReference());
		anInterface.addSuperInterface(poptional);
		return anInterface;
	}
}

