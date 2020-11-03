package com.petra.ioptional.codegen;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;
import static javax.tools.Diagnostic.Kind.WARNING;

@RequiredArgsConstructor
@Getter

public class CodeGenContext {

	private final RoundEnvironment roundEnvironment;

	private final Set<TypeMirror> typeContext = new HashSet<>();

	@Delegate
	private final ProcessingEnvironment processingEnvironment;

	public void note(String note) {
		getMessager().printMessage(NOTE, note);
	}

	public void note(String note, Element element) {
		getMessager().printMessage(NOTE, note, element);
	}

	public void error(String text) {
		getMessager().printMessage(ERROR, text);
	}

	public void error(String text, Element element) {
		getMessager().printMessage(ERROR, text, element);
	}

	public void add(TypeMirror typeMirror) {
		typeContext.add(typeMirror);
	}

	public void remove(TypeMirror typeMirror) {
		typeContext.remove(typeMirror);
	}

	public Set<? extends Element> getElementsAnnotatedWithAny(Set<Class<? extends Annotation>> supportedAnnotations) {
		return roundEnvironment.getElementsAnnotatedWithAny(supportedAnnotations);
	}

	public boolean has(TypeMirror typeMirror) {
		return typeContext.stream()
				.anyMatch(aTypeMirror -> aTypeMirror.equals(typeMirror));
	}

	public void warning(String text) {
		getMessager().printMessage(WARNING, text);
	}
}

