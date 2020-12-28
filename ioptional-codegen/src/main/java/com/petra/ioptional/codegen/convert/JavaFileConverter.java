package com.petra.ioptional.codegen.convert;

import com.petra.ioptional.codegen.CodeGenContext;
import com.petra.ioptional.codegen.util.ElementUtil;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import lombok.experimental.Delegate;

import javax.lang.model.element.Element;
import java.util.Set;

import static com.petra.ioptional.codegen.util.PoetUtil.newJavaFile;
import static java.util.stream.Collectors.toSet;

public class JavaFileConverter {

	private final ClassConverter classConverter = new ClassConverter();
	public Set<JavaFile> convert(CodeGenContext codeGenContext) {
		return toJavaFiles(codeGenContext.getClassElements(), codeGenContext);
	}

	private Set<JavaFile> toJavaFiles(Set<? extends Element> elements, CodeGenContext codeGenContext) {
		return elements.stream()
				.map(element -> toJavaFile(element, codeGenContext))
				.collect(toSet());
	}

	private JavaFile toJavaFile(Element classElement, CodeGenContext codeGenContext) {
		TypeSpec typeSpec = classConverter.typeSpec(classElement, codeGenContext);
		String aPackage = getPackage(classElement, codeGenContext);
		return newJavaFile(typeSpec, aPackage); // Address$.java
	}

	private String getPackage(Element element, CodeGenContext codeGenContext) {
		return ElementUtil.getPackage(element, codeGenContext);
	}
}
