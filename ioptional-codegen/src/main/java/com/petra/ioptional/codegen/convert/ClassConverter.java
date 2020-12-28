package com.petra.ioptional.codegen.convert;

import com.petra.ioptional.codegen.CodeGenContext;
import com.petra.ioptional.codegen.util.IOptionalUtil;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import static com.petra.ioptional.codegen.util.IOptionalUtil.getValueSimpleClassName;
import static com.petra.ioptional.codegen.util.PoetUtil.addAnnotations;
import static com.petra.ioptional.codegen.util.PoetUtil.addFields;
import static com.petra.ioptional.codegen.util.PoetUtil.addMethods;
import static com.petra.ioptional.codegen.util.PoetUtil.addModifiers;
import static com.petra.ioptional.codegen.util.PoetUtil.addSuperClass;
import static com.petra.ioptional.codegen.util.PoetUtil.newTypeSpec;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

@RequiredArgsConstructor
public class ClassConverter {

	@Delegate
	private final SetterConverter setterConverter = new SetterConverter();

	@Delegate
	private final GetterConverter getterConverter = new GetterConverter();

	@Delegate
	private final FieldConverter fieldConverter = new FieldConverter();

	@Delegate
	private final AnnotationConverter annotationConverter = new AnnotationConverter();

	@Delegate
	private final OfNullableMethodConverter ofNullableMethodConverter = new OfNullableMethodConverter();

	public TypeSpec typeSpec(Element classElement, CodeGenContext codeGenContext) {
		/*
			public final class Address$ extends Value<Address$>{

			}
		 */
		// Class Name: Address$
		TypeSpec.Builder typeSpecBuilder = newTypeSpec(className(classElement));

		// Annotations: @Getter, @Setter ....etc
		addAnnotations(typeSpecBuilder, annotationSpecs(classElement, codeGenContext));

		// Modifiers: public final...
		addModifiers(typeSpecBuilder, modifiers(classElement, codeGenContext));

		// Super class: extends Value<Address$>
		addSuperClass(typeSpecBuilder, superClassTypeName(classElement, codeGenContext));

		/*
		public static IOptional<Country$> ofNullable(Country$ country) {
			return country == null ? emptyCountry() : country;
		}
		* */
		addMethods(typeSpecBuilder, ofNullableMethodSpec(classElement, codeGenContext));

		/*
		Fields:
			private Country$ country; // user types

			private String street; // native type
		* */
		addFields(typeSpecBuilder, fieldSpecs(classElement, codeGenContext));

		/*
		Getters:
			public IOptional<Country$> getCountry() {
				return Country$.ofNullable(country);
			}
		* */
		addMethods(typeSpecBuilder, getterSpecs(classElement, codeGenContext));

		/*
		Setters:
			public void setCountry(Country$ country) {
				return this.country = country
			}
		* */
		addMethods(typeSpecBuilder, setterSpecs(classElement, codeGenContext));

		return typeSpecBuilder.build();
	}

	private Modifier[] modifiers(Element classElement, CodeGenContext codeGenContext) {
		return new Modifier[] { PUBLIC, FINAL };
	}

	private String className(Element classElement) {
		return getValueSimpleClassName(classElement);
	}

	private ParameterizedTypeName superClassTypeName(Element classElement, CodeGenContext codeGenContext) {
		return IOptionalUtil.getValueOfTypeName(classElement);
	}
}
