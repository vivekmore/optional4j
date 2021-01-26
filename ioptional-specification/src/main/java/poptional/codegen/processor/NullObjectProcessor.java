package poptional.codegen.processor;

import poptional.NullObject;
import poptional.codegen.NullObjectFactory;
import poptional.codegen.PoptionalFactory;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.processing.FileGenerator;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtInterface;

public class NullObjectProcessor extends AbstractAnnotationProcessor<NullObject, CtClass> {

	@Override
	public void process(NullObject nullObject, CtClass ctClass) {

		CtInterface<?> nullObjectInterface = generateNullObjectInterface(ctClass);
		implementNullObjectInterface(ctClass, nullObjectInterface);
		implementSomethingInterface(ctClass);

		clearConsumedAnnotationTypes();
	}

	private CtInterface<?> generateNullObjectInterface(CtClass<?> ctClass) {
		NullObjectFactory nullObjectFactory = new NullObjectFactory(getFactory());
		CtInterface<?> nullObjectInterface = nullObjectFactory.createNullObjInterface(ctClass);
		generateFile(nullObjectInterface);
		return nullObjectInterface;
	}

	private void implementNullObjectInterface(CtClass<?> ctClass, CtInterface<?> nullObjectInterface) {
		ctClass.addSuperInterface(nullObjectInterface.getReference());
	}

	private void implementSomethingInterface(CtClass<?> ctClass) {
		new PoptionalFactory(getFactory()).implementSomething(ctClass);
	}

	private void generateFile(CtInterface<?> nullObjectInterface) {
		((FileGenerator) getEnvironment().getDefaultFileGenerator()).process(nullObjectInterface);
	}
}

