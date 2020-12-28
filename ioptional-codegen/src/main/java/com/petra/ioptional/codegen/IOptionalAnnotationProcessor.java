package com.petra.ioptional.codegen;

import com.google.auto.service.AutoService;
import com.petra.ioptional.annotations.Optionalizable;
import com.petra.ioptional.codegen.convert.JavaFileConverter;
import com.squareup.javapoet.JavaFile;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static javax.lang.model.SourceVersion.RELEASE_12;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.petra.ioptional.annotations.Optionalizable")
@SupportedSourceVersion(RELEASE_12)
public class IOptionalAnnotationProcessor extends AbstractProcessor {

	JavaFileConverter javaFileConverter = new JavaFileConverter();

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

		try {
			Set<? extends Element> classes = getClassesToOptionalize(roundEnv);
			CodeGenContext codeGenContext = new CodeGenContext(classes, toTypes(classes), roundEnv, processingEnv);
			doProcess(codeGenContext);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void doProcess(CodeGenContext codeGenContext) {

		Set<? extends Element> classElements = codeGenContext.getClassElements();

		if (classElements.isEmpty()) {
			return;
		}
		codeGenContext.warning("Started IOptional annotation processor!!");
		classElements.forEach(element -> codeGenContext.note("Element found: " + element.getSimpleName()));

		Set<JavaFile> files = javaFileConverter.convert(codeGenContext);

		files.forEach(javaFile -> writeFile(javaFile, codeGenContext));
		codeGenContext.warning("Finished IOptional annotation processor!!");
	}

	private Set<TypeMirror> toTypes(Set<? extends Element> elements) {
		return elements.stream()
				.map(Element::asType)
				.collect(toSet());
	}

	private Set<? extends Element> getClassesToOptionalize(RoundEnvironment roundEnv) {
		return roundEnv.getElementsAnnotatedWith(Optionalizable.class)
				.stream()
				.filter(element -> element.getKind()
						.isClass())
				.collect(toSet());
	}

	private void writeFile(JavaFile javaFile, CodeGenContext codeGenContext) {
		try {
			codeGenContext.warning("Writing file: " + javaFile);
			javaFile.writeTo(processingEnv.getFiler());
		} catch (IOException e) {
			throw new RuntimeException("Failed to write file: " + javaFile.toString(), e);
		}
	}
}

