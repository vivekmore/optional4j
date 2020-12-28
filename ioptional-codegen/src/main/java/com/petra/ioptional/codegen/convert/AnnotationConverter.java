package com.petra.ioptional.codegen.convert;

import com.petra.ioptional.annotations.Optionalizable;
import com.petra.ioptional.codegen.CodeGenContext;
import com.squareup.javapoet.AnnotationSpec;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class AnnotationConverter {

	public List<AnnotationSpec> annotationSpecs(Element classElement, CodeGenContext codeGenContext) {
		return classElement.getAnnotationMirrors()
				.stream()
				.filter(annotationMirror -> !annotationMirror.toString()
						.contains(Optionalizable.class.getSimpleName()))
				.map(annotationMirror -> annotationSpec(annotationMirror, codeGenContext))
				.collect(toList());
	}

	private AnnotationSpec annotationSpec(AnnotationMirror annotationMirror, CodeGenContext codeGenContext) {
		codeGenContext.warning("Adding annotation: " + annotationMirror.toString());
		return AnnotationSpec.get(annotationMirror);
	}
}
