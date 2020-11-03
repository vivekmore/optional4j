package com.petra.ioptional.codegen;

import com.google.auto.service.AutoService;
import com.petra.ioptional.annotations.Optionalize;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static javax.lang.model.SourceVersion.RELEASE_12;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.petra.ioptional.annotations.Optionalize")
@SupportedSourceVersion(RELEASE_12)
public class IOptionalAnnotationProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

		CodeGenContext codeGenContext = new CodeGenContext(roundEnv, processingEnv);
		try {

			doProcess(roundEnv.getRootElements(), codeGenContext);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void doProcess(Set<? extends Element> elements, CodeGenContext codeGenContext) {

		if (elements.isEmpty()) {
			return;
		}
		codeGenContext.note("Running IOptional annotation processor");
		elements.forEach(element -> codeGenContext.note("Element found: " + element.getSimpleName()));
	}
}

