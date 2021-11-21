package optional4j.codegen.visitor.valuetype;

import static java.util.stream.Collectors.joining;
import static optional4j.codegen.CodeGenUtil.addNonNullAnnotation;
import static optional4j.codegen.CodeGenUtil.isOptimisticMode;
import static spoon.reflect.declaration.ModifierKind.*;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import optional4j.codegen.CodeGenUtil;
import optional4j.codegen.builder.ValueTypeBuilder;
import optional4j.codegen.processor.ProcessorProperties;
import optional4j.spec.Optional;
import spoon.reflect.code.CtReturn;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtNamedElement;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;

@RequiredArgsConstructor
public class OptionalMethodWrapper {

    private final ValueTypeBuilder valueTypeBuilder;

    private final ProcessorProperties processorProperties;

    public <T> CtMethod<T> wrapMethod(CtMethod<T> ctMethod) {
        CtMethod<T> wrapper = doCreateOptionalWrapperMethod(ctMethod);
        privatize(ctMethod);
        wrapper.setBody(ofNullableMethodInvocation(ctMethod));
        return wrapper;
    }

    private <T> CtMethod<T> doCreateOptionalWrapperMethod(CtMethod<T> ctMethod) {

        // getAddress(){}
        CtMethod<T> newMethod = ctMethod.clone();

        // Optional<Address> getAddress(){}
        changeMethodReturnTypeToPoptional(newMethod);

        // @NonNull Optional<Address> getAddress(){}
        addNonNullAnnotation(newMethod, valueTypeBuilder.getFactory());
        return newMethod;
    }

    private <T> void privatize(CtMethod<T> ctMethod) {

        // do_getAddress(){}
        ctMethod.setSimpleName("do_" + ctMethod.getSimpleName());

        if (ctMethod.getDeclaringType().isClass()) {
            // private do_getAddress(){}
            makeMethodPrivate(ctMethod);
        }
    }

    private <T> CtReturn<?> ofNullableMethodInvocation(CtMethod<T> ctMethod) {

        return valueTypeBuilder
                .getFactory()
                .createReturn()
                // todo: fix getReturnStatement to be able to make it optimistic
                .setReturnedExpression(
                        valueTypeBuilder
                                .getFactory()
                                .createCodeSnippetExpression(
                                        ofNullableMethodInvocationExpression(ctMethod)));
    }

    private <T> String ofNullableMethodInvocationExpression(CtMethod<T> ctMethod) {
        return "Optional.ofNullable(" + delegateMethodName(ctMethod) + ")";
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

    /**
     * Changes the method return type to Optional<original-return-type>
     *
     * @param ctMethod
     */
    private void changeMethodReturnTypeToPoptional(CtMethod ctMethod) {
        ctMethod.setType(poptionalOf(ctMethod));
    }

    private CtTypeReference<Optional<?>> poptionalOf(CtMethod<?> ctMethod) {
        return valueTypeBuilder.createOptionalOf(CodeGenUtil.getReturnTypeRef(ctMethod));
    }

    private String getReturnStatement(CtReturn<?> returnStatement, CtMethod<?> ctMethod) {

        if (isOptimisticMode(ctMethod, processorProperties)) {
            return ifNullStatement(returnStatement);
        }

        return poptionalOfNullableStatement(returnStatement);
    }

    private String poptionalOfNullableStatement(CtReturn<?> returnStatement) {
        return "return Optional.ofNullable(" + returnStatement.getReturnedExpression() + ")";
    }

    private String ifNullStatement(CtReturn<?> returnStatement) {
        return "return ("
                + returnStatement.getReturnedExpression()
                + ") == null? Optional.empty(): "
                + returnStatement.getReturnedExpression();
    }
}
