package optional4j.codegen.visitor.nullobject;

import static java.util.stream.Collectors.joining;
import static optional4j.annotation.Collaborator.NULL_INSTANCE_FACTORY_METHOD_NAME;
import static optional4j.annotation.Collaborator.NULL_OBJECT_FACTORY_METHOD_NAME;
import static optional4j.codegen.CodeGenUtil.*;
import static spoon.reflect.declaration.ModifierKind.*;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import optional4j.codegen.builder.NullObjectBuilder;
import optional4j.codegen.processor.ProcessorProperties;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtTypeReference;

@RequiredArgsConstructor
public class NullObjectMethodWrapper {

    private final NullObjectBuilder nullObjectBuilder;

    private final ProcessorProperties processorProperties;

    public <T> CtMethod<T> wrapMethod(CtMethod<T> ctMethod) {
        CtMethod<T> wrapper = doCreateNullObjectWrapperMethod(ctMethod);
        privatize(ctMethod);
        wrapper.setBody(ofNullableMethodInvocation(wrapper, ctMethod));
        return wrapper;
    }

    /**
     * Changes the method return type to FooNullObject
     *
     * @param ctMethod
     * @param newReturnType
     */
    private void changeMethodReturnTypeToNullObject(
            CtMethod ctMethod, CtTypeReference<?> newReturnType) {
        ctMethod.setType(newReturnType);
    }

    private <T> CtMethod<T> doCreateNullObjectWrapperMethod(CtMethod<T> ctMethod) {
        CtMethod<T> newMethod = ctMethod.clone(); // getAddress(){}
        CtTypeReference<?> newReturnType =
                nullObjectBuilder.getNullObjectTypeReference(getReturnType(ctMethod));

        changeMethodReturnTypeToNullObject(
                newMethod, newReturnType); // Optional<Address> getAddress(){}

        // @NonNull Optional<Address> getAddress(){}
        addNonNullAnnotation(newMethod, nullObjectBuilder.getFactory());

        return newMethod;
    }

    private <T> void privatize(CtMethod<T> ctMethod) {

        // do_getAddress
        ctMethod.setSimpleName("do_" + ctMethod.getSimpleName());

        if (ctMethod.getDeclaringType().isClass()) {
            // private do_getAddress(){}
            makeMethodPrivate(ctMethod);
        }
    }

    private <T> CtStatement ofNullableMethodInvocation(CtMethod<?> wrapper, CtMethod<T> ctMethod) {

        if (isOptimisticMode(ctMethod, processorProperties)) {
            return ifNullStatement(wrapper.getType().getSimpleName(), ctMethod);
        }

        return ofNullObjectStatement(wrapper.getType().getSimpleName(), ctMethod);
    }

    private CtReturn<?> ofNullObjectStatement(String nullObjectTypeName, CtMethod<?> ctMethod) {
        return createReturn(
                nullObjectTypeName
                        + "."
                        + NULL_OBJECT_FACTORY_METHOD_NAME
                        + "("
                        + delegateMethodName(ctMethod)
                        + ")");
    }

    private CtBlock<?> ifNullStatement(String nullObjectTypeName, CtMethod<?> ctMethod) {

        String localVariable = "toReturn$$";

        String declareLocalVariableStatement =
                ctMethod.getType().getSimpleName()
                        + " "
                        + localVariable
                        + " = "
                        + delegateMethodName(ctMethod);
        return nullObjectBuilder
                .createBlock()
                .addStatement(
                        nullObjectBuilder.createCodeSnippetStatement(declareLocalVariableStatement))
                .addStatement(
                        createReturn(
                                localVariable
                                        + " != null? "
                                        + localVariable
                                        + ": "
                                        + nullObjectTypeName
                                        + "."
                                        + NULL_INSTANCE_FACTORY_METHOD_NAME
                                        + "()"));
    }

    private CtReturn<?> createReturn(String toReturn) {
        return nullObjectBuilder
                .getFactory()
                .createReturn()
                .setReturnedExpression(
                        nullObjectBuilder.getFactory().createCodeSnippetExpression(toReturn));
    }

    private String delegateMethodName(CtMethod<?> ctMethod) {
        return ctMethod.getSimpleName() + "(" + joinParams(ctMethod.getParameters()) + ")";
    }

    private String joinParams(List<CtParameter<?>> parameters) {
        return parameters.stream().map(CtNamedElement::getSimpleName).collect(joining(","));
    }

    private <T> void makeMethodPrivate(CtMethod<T> ctMethod) {
        Set<ModifierKind> modifiers = ctMethod.getModifiers();
        modifiers.removeAll(Set.of(PRIVATE, PROTECTED, PUBLIC));
        modifiers.add(PRIVATE);
        ctMethod.setModifiers(modifiers);
    }
}
