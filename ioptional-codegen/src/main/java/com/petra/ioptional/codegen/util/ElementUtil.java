package com.petra.ioptional.codegen.util;

import com.petra.ioptional.codegen.CodeGenContext;
import com.squareup.javapoet.ParameterizedTypeName;
import lombok.experimental.UtilityClass;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.squareup.javapoet.ClassName.bestGuess;
import static com.squareup.javapoet.ClassName.get;
import static java.util.stream.Collectors.toList;
import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.ElementKind.METHOD;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

@UtilityClass
public class ElementUtil {

	private static final String SET = "set";
	private static final String GET = "get";

	/**
	 * @param typeMirrors
	 * @param typeMirror
	 * @return whether a type mirror is contained within a collection of type mirrors
	 */
	public static boolean contains(Collection<? extends TypeMirror> typeMirrors, TypeMirror typeMirror) {
		return typeMirrors.contains(typeMirror);
	}

	public static String getPackage(Element element, CodeGenContext codeGenContext) {
		return codeGenContext.getElementUtils()
				.getPackageOf(element)
				.getQualifiedName()
				.toString();
	}

	public static String getSimpleName(Element element) {
		return element.getSimpleName()
				.toString();
	}

	public static Modifier[] getModifiers(Element element) {
		return element.getModifiers()
				.toArray(new Modifier[0]);
	}

	public static ParameterizedTypeName getParameterizedTypeName(Class<?> clazz, String typeName) {
		return ParameterizedTypeName.get(get(clazz), bestGuess(typeName));
	}

	public static List<? extends Element> getFields(Element classElement) {
		return classElement.getEnclosedElements()
				.stream()
				.filter(anElement -> anElement.getKind() == FIELD)
				.collect(toList());
	}

	public static List<? extends ExecutableElement> getGetters(Element classElement) {
		Set<String> fieldNames = getFieldNames(classElement);
		return getMethods(classElement).filter(methodElement -> isGetter(methodElement, fieldNames))
				.collect(toList());
	}

	public static List<? extends ExecutableElement> getSetters(Element classElement) {
		Set<String> fieldNames = getFieldNames(classElement);
		return getMethods(classElement).filter(anElement -> isSetter(anElement, fieldNames))
				.collect(toList());
	}

	private static Stream<ExecutableElement> getMethods(Element classElement) {
		return classElement.getEnclosedElements()
				.stream()
				.filter(anElement -> anElement.getKind() == METHOD)
				.map(methodElement -> (ExecutableElement) methodElement);
	}

	private static Set<String> getFieldNames(Element classElement) {
		return getFields(classElement).stream()
				.map(ElementUtil::getSimpleName)
				.collect(Collectors.toSet());
	}

	private static boolean isGetter(ExecutableElement methodElement, Set<String> fieldNames) {
		String methodName = getSimpleName(methodElement);
		String fieldName = extractFieldNameForGetter(methodName);
		return methodName.startsWith(GET) && fieldNames.contains(fieldName);
	}

	public static String extractFieldNameForGetter(ExecutableElement executableElement) {
		return extractFieldName(getSimpleName(executableElement), GET);
	}

	static String extractFieldNameForGetter(String methodName) {
		return extractFieldName(methodName, GET);
	}

	static boolean isSetter(ExecutableElement methodElement, Set<String> fieldNames) {
		String methodName = getSimpleName(methodElement);
		return methodName.startsWith(SET) && fieldNames.contains(extractFieldNameForSetter(methodName));
	}

	public static String extractFieldNameForSetter(ExecutableElement executableElement) {
		return extractFieldName(getSimpleName(executableElement), SET);
	}

	static String extractFieldNameForSetter(String methodName) {
		return extractFieldName(methodName, SET);
	}

	static String extractFieldName(String methodName, String prefix) {
		return uncapitalize(methodName.substring(methodName.indexOf(prefix) + prefix.length()));
	}
}
