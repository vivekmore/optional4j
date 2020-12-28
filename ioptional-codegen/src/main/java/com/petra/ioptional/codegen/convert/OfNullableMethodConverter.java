package com.petra.ioptional.codegen.convert;

import com.petra.ioptional.codegen.CodeGenContext;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;

import static com.petra.ioptional.codegen.util.IOptionalUtil.getIOptionalOfTypeName;
import static com.petra.ioptional.codegen.util.IOptionalUtil.getValueSimpleClassName;
import static com.petra.ioptional.codegen.util.IOptionalUtil.getValueTypeName;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

public class OfNullableMethodConverter {

	public MethodSpec ofNullableMethodSpec(Element classElement, CodeGenContext codeGenContext) {
		String parameterName = uncapitalize(getValueSimpleClassName(classElement));
		TypeName parameterType = getValueTypeName(classElement);
		return MethodSpec.methodBuilder("ofNullable")
				.addModifiers(STATIC, PUBLIC)
				.addParameter(ParameterSpec.builder(parameterType, uncapitalize(parameterName))
						.build())
				.returns(getClassTypeName(classElement, codeGenContext))
				.addCode("return $L == null ? com.petra.ioptional.lang.Empty.EMPTY : $L;\n", parameterName,
						parameterName)
				.build();
	}

	private TypeName getClassTypeName(Element classElement, CodeGenContext codeGenContext) {
		return getIOptionalOfTypeName(classElement);
	}
}
