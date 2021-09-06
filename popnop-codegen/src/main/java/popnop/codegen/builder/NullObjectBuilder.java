package popnop.codegen.builder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.eclipse.jdt.core.dom.FieldAccess;
import popnop.spec.NullObject;
import popnop.spec.NullObjectType;
import popnop.spec.Poptional;
import spoon.reflect.code.CtFieldRead;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.*;
import spoon.reflect.visitor.filter.FieldAccessFilter;

import javax.annotation.NonNull;
import javax.annotation.Nullable;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static popnop.codegen.CodeGenUtil.NULL_INSTANCE;
import static spoon.reflect.declaration.ModifierKind.*;

@RequiredArgsConstructor
public class NullObjectBuilder {

    @Getter
    @Delegate
    private final Factory factory;

    // interface FooNullObject extends Poptional<Foo> {
    // ... Foo method declarations
    // }
    public CtType<?> createNullObjectType(CtType<?> ctClass) {

        CtInterface<?> nullObjectType = declareNullObjectType(ctClass);
        nullObjectType.addSuperInterface(factory.createCtTypeReference(NullObject.class));
        nullObjectType.setModifiers(ctClass.getModifiers().stream().filter(modifier -> Set.of(PUBLIC, PRIVATE, PROTECTED).contains(modifier)).collect(toSet()));
        implementPoptional(nullObjectType, ctClass);

        return nullObjectType;
    }

    // Reference to FooNullObject
    public CtTypeReference<?> getNullObjectTypeReference(CtType<?> ctType) {
        return getFactory().createReference(getNullObjectTypeQualifiedName(ctType));
    }

    // Reference to FooNull
    public CtTypeReference<?> getNullClassReference(CtClass<?> ctClass) {
        return getNullClassReference(getNullClassQualifiedName(ctClass));
    }

    private CtTypeReference<?> getNullClassReference(String qualifiedName) {
        return getFactory().createReference(qualifiedName);
    }

    // class NullFoo implements FooNullObject{
    // }
    public CtClass<?> createNullClass(CtClass<?> ctClass, CtType<?> nullObjectType) {
        CtClass nullClass = declareNullClass(ctClass);
        nullClass.addField(getFactory().createCtField("NULL", nullClass.getReference(), "new " + nullClass.getSimpleName() + "()", STATIC, FINAL));
        implementsNullObjectInterface(nullClass, nullObjectType);
        return nullClass;
    }

    /**
     * @param nullObjectType Generates method ==> @NonNull FooNullObject ofNullObject(@Nullable FooNullObject popnop.nullObject) {
     *                       return popnop.nullObject != null? popnop.nullObject: FooNull.nullInstance();
     *                       }
     */
    public void addOfNullObjectFactoryMethod(CtType<?> nullObjectType) {

        CtMethod ofNullObjectMethod = createMethod();
        ofNullObjectMethod.setType(nullObjectType.getReference());

        ofNullObjectMethod.setSimpleName("ofNullObject");
        ofNullObjectMethod.setModifiers(Set.of(PUBLIC, STATIC));
        ofNullObjectMethod.addAnnotation(createAnnotation(createCtTypeReference(NonNull.class)));

        CtParameter nullObject = createParameter();
        nullObject.addAnnotation(createAnnotation(createCtTypeReference(Nullable.class)));
        nullObject.setType(nullObjectType.getReference());

        String nullObjectParamName = "nullObject";
        nullObject.setSimpleName(nullObjectParamName);

        ofNullObjectMethod.addParameter(nullObject);

        ofNullObjectMethod.setBody(createReturn().setReturnedExpression(createCodeSnippetExpression(nullObjectParamName + " != null? " + nullObjectParamName + ": " + NULL_INSTANCE + "()")));

        nullObjectType.addMethod(ofNullObjectMethod);
    }

    /**
     * @param nullObjectType
     * @param ctClass        Generates method ==> @NonNull public static FooNullObject nullInstance() {
     *                       return FooNull.NULL;
     *                       }
     */
    public void addNullInstanceFactoryMethod(CtType<?> nullObjectType, CtClass<?> ctClass) {

        CtMethod nullInstanceMethod = createMethod();
        nullInstanceMethod.setType(nullObjectType.getReference());

        nullInstanceMethod.setSimpleName(NULL_INSTANCE);
        nullInstanceMethod.setModifiers(Set.of(PUBLIC, STATIC));
        nullInstanceMethod.addAnnotation(createAnnotation(createCtTypeReference(NonNull.class)));


        nullInstanceMethod.setBody(createReturn().setReturnedExpression(nullStaticInstanceAccess(ctClass)));

        nullObjectType.addMethod(nullInstanceMethod);
    }

    private CtFieldRead nullStaticInstanceAccess(CtClass<?> ctClass) {
        return createFieldRead().setVariable(createFieldReference().setDeclaringType(getNullClassReference(ctClass)).setStatic(true).setSimpleName("NULL"));
    }

    /**
     * interface FooNullObject {}
     *
     * @param ctType
     * @return
     */
    private CtInterface<?> declareNullObjectType(CtType<?> ctType) {
        return getFactory().createInterface(getNullObjectTypeQualifiedName(ctType));
    }

    /**
     * class FooNull {}
     *
     * @param ctClass
     * @return
     */
    private CtClass<?> declareNullClass(CtClass<?> ctClass) {
        return getFactory().createClass(getNullClassQualifiedName(ctClass)).addModifier(FINAL);
    }

    /**
     * FooNull
     *
     * @param ctClass
     * @return
     */
    private String getNullClassQualifiedName(CtClass<?> ctClass) {
        NullObjectType nullObjectType = ctClass.getAnnotation(NullObjectType.class);
        return ctClass.getPackage().getQualifiedName() + "." + nullObjectType.nullPrefix() + ctClass.getSimpleName() + nullObjectType.nullPostfix();
    }

    /**
     * FooNullObject
     *
     * @param ctType
     * @return
     */
    private String getNullObjectTypeQualifiedName(CtType<?> ctType) {
        NullObjectType nullObjectType = ctType.getAnnotation(NullObjectType.class);
        return ctType.getPackage().getQualifiedName() + "." + nullObjectType.nullObjectPrefix() + ctType.getSimpleName() + nullObjectType.nullObjectPostfix();
    }

    private void implementsNullObjectInterface(CtClass<?> ctClass, CtType<?> nullObjectType) {
        ctClass.addSuperInterface(nullObjectType.getReference());
    }

    private void implementPoptional(CtType<?> ctType, CtType<?> argumentCtType) {
        CtTypeReference<Poptional<?>> poptional = getFactory().createCtTypeReference(Poptional.class);
        poptional.addActualTypeArgument(argumentCtType.getReference());
        ctType.addSuperInterface(poptional);
    }
}
