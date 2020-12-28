package com.petra.ioptional.codegen.convert;

import com.petra.ioptional.codegen.CodeGenContext;
import com.petra.ioptional.codegen.util.ElementUtil;
import com.petra.ioptional.codegen.util.IOptionalUtil;
import com.petra.ioptional.lang.IOptional;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;

import static com.petra.ioptional.codegen.util.ElementUtil.extractFieldNameForSetter;
import static com.petra.ioptional.codegen.util.ElementUtil.getSetters;
import static com.petra.ioptional.codegen.util.ElementUtil.getSimpleName;
import static com.petra.ioptional.codegen.util.IOptionalUtil.getIOptionalOfTypeName;
import static com.petra.ioptional.codegen.util.IOptionalUtil.getValueTypeName;
import static com.petra.ioptional.codegen.util.IOptionalUtil.isOptionalizable;
import static com.petra.ioptional.codegen.util.PoetUtil.getTypeName;
import static java.util.stream.Collectors.toList;

public class SetterConverter {

	public List<MethodSpec> setterSpecs(Element classElement, CodeGenContext codeGenContext) {
		return setterSpecs(getSetters(classElement), codeGenContext);
	}

	private List<MethodSpec> setterSpecs(List<? extends ExecutableElement> methodElements,
			CodeGenContext codeGenContext) {
		return methodElements.stream()
				.map(setterMethod -> setterSpec(setterMethod, codeGenContext))
				.collect(toList());
	}

	private MethodSpec setterSpec(ExecutableElement methodElement, CodeGenContext codeGenContext) {

		String fieldName = extractFieldNameForSetter(methodElement);

		return MethodSpec.methodBuilder(getSimpleName(methodElement))
				.addModifiers(methodElement.getModifiers())
				.addParameter(ParameterSpec.builder(getSetterTypeName(methodElement, codeGenContext), fieldName).build())
				.returns(void.class)
				.addCode(getSetterLine(methodElement, codeGenContext))
				.build();

	}

	private CodeBlock getSetterLine(ExecutableElement methodElement, CodeGenContext codeGenContext) {
		String fieldName = extractFieldNameForSetter(methodElement);
		return CodeBlock.builder()
				.add("this.$L = $L;", fieldName, fieldName)
				.build();
	}

	private TypeName getSetterTypeName(ExecutableElement methodElement, CodeGenContext codeGenContext) {

		VariableElement firstParameter = getFirstParameter(methodElement);

		if (isOptionalizable(firstParameter, codeGenContext)) {
			return getValueTypeName(firstParameter);
		}
		return getTypeName(firstParameter);
	}

	private VariableElement getFirstParameter(ExecutableElement methodElement) {
		return methodElement.getParameters()
				.get(0);
	}
}
