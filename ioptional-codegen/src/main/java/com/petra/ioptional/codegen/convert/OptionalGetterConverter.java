package com.petra.ioptional.codegen.convert;

import com.petra.ioptional.codegen.CodeGenContext;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.util.List;

import static com.petra.ioptional.codegen.util.ElementUtil.extractFieldNameForGetter;
import static com.petra.ioptional.codegen.util.ElementUtil.getGetters;
import static com.petra.ioptional.codegen.util.ElementUtil.getSimpleName;
import static com.petra.ioptional.codegen.util.IOptionalUtil.getOptionalOfTypeName;
import static com.petra.ioptional.codegen.util.IOptionalUtil.isOptionalizable;
import static java.util.stream.Collectors.toList;

public class OptionalGetterConverter {

	public List<MethodSpec> optionalGetterSpecs(Element classElement, CodeGenContext codeGenContext) {
		return getterSpecs(getGetters(classElement), codeGenContext);
	}

	private List<MethodSpec> getterSpecs(List<? extends ExecutableElement> methodElements,
			CodeGenContext codeGenContext) {
		return methodElements.stream()
				.filter(methodElement -> isOptionalizable(methodElement, codeGenContext))
				.map(getterMethod -> getterSpec(getterMethod, codeGenContext))
				.collect(toList());
	}

	private MethodSpec getterSpec(ExecutableElement methodElement, CodeGenContext codeGenContext) {
		return MethodSpec.methodBuilder(getSimpleName(methodElement).concat("Optional"))
				.addModifiers(methodElement.getModifiers())
				.returns(getGetterTypeName(methodElement, codeGenContext))
				.addCode(getGetterReturnLine(methodElement, codeGenContext))
				.build();
	}

	private TypeName getGetterTypeName(ExecutableElement methodElement, CodeGenContext codeGenContext) {
		return getOptionalOfTypeName(methodElement);
	}

	private CodeBlock getGetterReturnLine(ExecutableElement methodElement, CodeGenContext codeGenContext) {

		String fieldName = extractFieldNameForGetter(methodElement);

		if (isOptionalizable(methodElement, codeGenContext)) {
			// e.g., return Optional.ofNullable(country);
			return CodeBlock.builder()
					.add("return Optional.ofNullable($L);\n", fieldName)
					.build();
		}

		// e.g., return street;
		return CodeBlock.builder()
				.add("return $L;", fieldName)
				.build();

	}
}
