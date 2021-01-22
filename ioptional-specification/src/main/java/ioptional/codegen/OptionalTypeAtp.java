package ioptional.codegen;

import ioptional.OptionalType;
import ioptional.Something;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.declaration.CtAnnotationType;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.reference.CtTypeReference;

public class OptionalTypeAtp extends AbstractAnnotationProcessor<OptionalType, CtClass> {

	@Override
	public void process(OptionalType optionalType, CtClass ctClass) {
		CtTypeReference<Something<?>> something = getFactory().createCtTypeReference(Something.class);
		something.addActualTypeArgument(ctClass.getReference());
		ctClass.addSuperInterface(something);

		clearConsumedAnnotationTypes();
	}
}

