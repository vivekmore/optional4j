package com.petra.ioptional.codegen.util;

import com.petra.ioptional.codegen.CodeGenContext;
import com.petra.ioptional.lang.Empty;
import com.petra.ioptional.lang.IOptional;
import com.petra.ioptional.lang.Value;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import lombok.experimental.UtilityClass;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import java.util.Optional;

import static com.petra.ioptional.codegen.util.ElementUtil.contains;
import static com.petra.ioptional.codegen.util.ElementUtil.getParameterizedTypeName;

@UtilityClass
public class IOptionalUtil {

	static final String VALUE_CLASS_NAME_POSTFIX = "$";
	static final String VALUE_CLASS_NAME_PREFIX = "";
	private static final String EMPTY_CLASS_NAME_PREFIX = "Empty";

	public static boolean isOptionalizable(ExecutableElement executableElement, CodeGenContext codeGenContext) {
		return isOptionalizable(executableElement.getReturnType(), codeGenContext);
	}

	public static boolean isOptionalizable(Element element, CodeGenContext codeGenContext) {
		return isOptionalizable(element.asType(), codeGenContext);
	}

	public static boolean isOptionalizable(TypeMirror type, CodeGenContext codeGenContext) {
		return contains(codeGenContext.getClassTypes(), type);
	}

	/**
	 * @param element
	 * @return Value<T>
	 */
	public static ParameterizedTypeName getValueOfTypeName(Element element) {
		return getValueOfTypeName(element.asType());
	}

	/**
	 * @param element
	 * @return Value<T>
	 */
	public static ParameterizedTypeName getValueOfTypeName(ExecutableElement element) {
		return getValueOfTypeName(element.getReturnType());
	}

	/**
	 * @param typeMirror
	 * @return Value<T>
	 */
	public static ParameterizedTypeName getValueOfTypeName(TypeMirror typeMirror) {
		return getParameterizedTypeName(Value.class, getValueClassQualifiedClassName(typeMirror));
	}

	/**
	 * @param element
	 * @return Empty<T>
	 */
	public static ParameterizedTypeName getEmptyOfTypeName(Element element) {
		return getEmptyOfTypeName(element.asType());
	}

	/**
	 * @param element
	 * @return Empty<T>
	 */
	public static ParameterizedTypeName getEmptyOfTypeName(ExecutableElement element) {
		return getEmptyOfTypeName(element.getReturnType());
	}

	/**
	 * @param typeMirror
	 * @return Empty<T>
	 */
	public static ParameterizedTypeName getEmptyOfTypeName(TypeMirror typeMirror) {
		return getParameterizedTypeName(Empty.class, getValueClassQualifiedClassName(typeMirror));
	}

	/**
	 * @param element
	 * @return IOptional<T>
	 */
	public static ParameterizedTypeName getIOptionalOfTypeName(Element element) {
		return getIOptionalOfTypeName(element.asType());
	}

	/**
	 * @param element
	 * @return IOptional<T>
	 */
	public static ParameterizedTypeName getOptionalOfTypeName(Element element) {
		return getOptionalOfTypeName(element.asType());
	}

	/**
	 * @param element
	 * @return IOptional<T>
	 */
	public static ParameterizedTypeName getIOptionalOfTypeName(ExecutableElement element) {
		return getIOptionalOfTypeName(element.getReturnType());
	}
	/**
	 * @param element
	 * @return IOptional<T>
	 */
	public static ParameterizedTypeName getOptionalOfTypeName(ExecutableElement element) {
		return getOptionalOfTypeName(element.getReturnType());
	}

	/**
	 * @param typeMirror
	 * @return IOptional<T>
	 */
	public static ParameterizedTypeName getIOptionalOfTypeName(TypeMirror typeMirror) {
		return getParameterizedTypeName(IOptional.class, getValueClassQualifiedClassName(typeMirror));
	}

	/**
	 * @param typeMirror
	 * @return IOptional<T>
	 */
	public static ParameterizedTypeName getOptionalOfTypeName(TypeMirror typeMirror) {
		return getParameterizedTypeName(Optional.class, getValueClassQualifiedClassName(typeMirror));
	}

	/**
	 * @param element
	 * @return get the TypeName of the "Value" class being generated e.g., Address$
	 */
	public static TypeName getValueTypeName(Element element) {
		return getValueTypeName(element.asType());
	}

	/**
	 * @param element
	 * @return get the TypeName of the "Value" class being generated e.g., Address$
	 */
	public static TypeName getValueTypeName(ExecutableElement element) {
		return getValueTypeName(element.getReturnType());
	}

	/**
	 * @param typeMirror
	 * @return get the TypeName of the "Value" class being generated e.g., Address$
	 */
	public static TypeName getValueTypeName(TypeMirror typeMirror) {
		return ClassName.bestGuess(getValueClassQualifiedClassName(typeMirror));
	}

	/**
	 * @param element
	 * @return the short name (as a String) of the "Value" class to be generated (e.g., "Address$")
	 */
	public static String getValueSimpleClassName(Element element) {
		return decorateValueClassName(String.valueOf(element.getSimpleName()));
	}

	/**
	 * @param element
	 * @return the short name (as a String) of the "Empty" class to be generated (e.g., "EmptyAddress$")
	 */
	public static String getEmptySimpleClassName(Element element) {
		return decorateEmptyClassName(String.valueOf(element.getSimpleName()));
	}

	/**
	 * @param typeMirror
	 * @return the fully-qualified name (as a String) of the "Value" class to be generated ("com.petra.model.Address$")
	 */
	public static String getValueClassQualifiedClassName(TypeMirror typeMirror) {
		return decorateValueClassName(String.valueOf(typeMirror));
	}

	public static String decorateValueClassName(String className) {
		return VALUE_CLASS_NAME_PREFIX + className + VALUE_CLASS_NAME_POSTFIX;
	}

	public static String decorateEmptyClassName(String className) {
		return EMPTY_CLASS_NAME_PREFIX + decorateValueClassName(className);
	}
}
