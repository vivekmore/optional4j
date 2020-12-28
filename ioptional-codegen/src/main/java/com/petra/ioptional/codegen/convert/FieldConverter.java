package com.petra.ioptional.codegen.convert;

import com.petra.ioptional.codegen.CodeGenContext;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;

import java.util.List;

import static com.petra.ioptional.codegen.util.ElementUtil.getFields;
import static com.petra.ioptional.codegen.util.ElementUtil.getModifiers;
import static com.petra.ioptional.codegen.util.ElementUtil.getSimpleName;
import static com.petra.ioptional.codegen.util.IOptionalUtil.getValueTypeName;
import static com.petra.ioptional.codegen.util.IOptionalUtil.isOptionalizable;
import static com.petra.ioptional.codegen.util.PoetUtil.getTypeName;
import static java.util.stream.Collectors.toList;

public class FieldConverter {

	public List<FieldSpec> fieldSpecs(Element classElement, CodeGenContext codeGenContext) {
		return fieldSpecs(getFields(classElement), codeGenContext);
	}

	private List<FieldSpec> fieldSpecs(List<? extends Element> fieldElements, CodeGenContext codeGenContext) {
		return fieldElements.stream()
				.map(aField -> fieldSpec(aField, codeGenContext))
				.collect(toList());
	}

	private FieldSpec fieldSpec(Element fieldElement, CodeGenContext codeGenContext) {
		return FieldSpec.builder(fieldTypeName(fieldElement, codeGenContext), getSimpleName(fieldElement),
				getModifiers(fieldElement))
				.build();
	}

	private TypeName fieldTypeName(Element fieldElement, CodeGenContext codeGenContext) {

		if (isOptionalizable(fieldElement, codeGenContext)) {
			return getValueTypeName(fieldElement);
		}
		return getTypeName(fieldElement);
	}
}
