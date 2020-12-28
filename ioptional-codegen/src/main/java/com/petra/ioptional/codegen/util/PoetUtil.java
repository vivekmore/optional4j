package com.petra.ioptional.codegen.util;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.experimental.UtilityClass;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import static java.util.Arrays.asList;

@UtilityClass
public class PoetUtil {

	public static JavaFile newJavaFile(TypeSpec typeSpec, String packageName) {
		return JavaFile.builder(packageName, typeSpec)
				.build();
	}

	public static TypeSpec.Builder newTypeSpec(String className) {
		return TypeSpec.classBuilder(className);
	}

	public static void addAnnotations(TypeSpec.Builder typeSpecBuilder, List<AnnotationSpec> annotationSpecs) {
		typeSpecBuilder.addAnnotations(annotationSpecs);
	}

	public static void addModifiers(TypeSpec.Builder typeSpecBuilder, Modifier... modifiers) {
		typeSpecBuilder.addModifiers(modifiers);
	}

	public static void addSuperClass(TypeSpec.Builder classBuilder, ParameterizedTypeName superClass) {
		classBuilder.superclass(superClass);
	}

	public static void addFields(TypeSpec.Builder classBuilder, List<FieldSpec> fieldSpecs) {
		classBuilder.addFields(fieldSpecs);
	}

	public static void addMethods(TypeSpec.Builder classBuilder, List<MethodSpec> methodSpecs) {
		classBuilder.addMethods(methodSpecs);
	}
	public static void addMethods(TypeSpec.Builder classBuilder, MethodSpec... methodSpecs) {
		classBuilder.addMethods(asList(methodSpecs));
	}

	public static void addTypes(TypeSpec.Builder typeSpecBuilder, TypeSpec... typeSpecs) {
		typeSpecBuilder.addTypes(asList(typeSpecs.clone()));
	}

	public static TypeName getTypeName(ExecutableElement executableElement) {
		return getTypeName(executableElement.getReturnType());
	}

	public static TypeName getTypeName(Element element) {
		return getTypeName(element.asType());
	}

	public static TypeName getTypeName(TypeMirror type) {
		return TypeName.get(type);
	}
}
