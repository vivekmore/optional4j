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
import static com.petra.ioptional.codegen.util.IOptionalUtil.getIOptionalOfTypeName;
import static com.petra.ioptional.codegen.util.IOptionalUtil.getValueSimpleClassName;
import static com.petra.ioptional.codegen.util.IOptionalUtil.isOptionalizable;
import static com.petra.ioptional.codegen.util.PoetUtil.getTypeName;
import static java.util.stream.Collectors.toList;

public class GetterConverter {

	public List<MethodSpec> getterSpecs(Element classElement, CodeGenContext codeGenContext) {
		return getterSpecs(getGetters(classElement), codeGenContext);
	}

	private List<MethodSpec> getterSpecs(List<? extends ExecutableElement> methodElements,
			CodeGenContext codeGenContext) {
		return methodElements.stream()
				.map(getterMethod -> getterSpec(getterMethod, codeGenContext))
				.collect(toList());
	}

	private MethodSpec getterSpec(ExecutableElement methodElement, CodeGenContext codeGenContext) {
		return MethodSpec.methodBuilder(getSimpleName(methodElement))
				.addModifiers(methodElement.getModifiers())
				.returns(getGetterTypeName(methodElement, codeGenContext))
				.addCode(getGetterReturnLine(methodElement, codeGenContext))
				.build();
	}

	private TypeName getGetterTypeName(ExecutableElement methodElement, CodeGenContext codeGenContext) {

		if (isOptionalizable(methodElement, codeGenContext)) {
			return getIOptionalOfTypeName(methodElement);
		}
		return getTypeName(methodElement);
	}

	private CodeBlock getGetterReturnLine(ExecutableElement methodElement, CodeGenContext codeGenContext) {

		String fieldName = extractFieldNameForGetter(methodElement);

		if (isOptionalizable(methodElement, codeGenContext)) {
			String valueSimpleClassName = getValueSimpleClassName(codeGenContext.getTypeUtils()
					.asElement(methodElement.getReturnType()));
			// e.g., return Country$.ofNullable(country);
			return CodeBlock.builder()
					.add("return $L.ofNullable($L);\n", valueSimpleClassName, fieldName)
					.build();
		}

		// e.g., return street;
		return CodeBlock.builder()
				.add("return $L;", fieldName)
				.build();

	}
}
