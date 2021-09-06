package popnop.codegen.visitor;

import lombok.RequiredArgsConstructor;
import popnop.codegen.builder.NullObjectBuilder;
import popnop.codegen.builder.PoptionalBuilder;
import popnop.codegen.processor.ProcessorProperties;
import popnop.spec.*;
import spoon.compiler.Environment;
import spoon.processing.AnnotationProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtAbstractVisitor;

import javax.annotation.NonNull;
import javax.annotation.Nullable;
import java.util.Set;

import static popnop.codegen.CodeGenUtil.*;


@RequiredArgsConstructor
public class OptionalTypeVisitor extends CtAbstractVisitor {

    private final Class<? extends AnnotationProcessor<?, ?>> processorClass;

    private final Environment environment;

    private final PoptionalBuilder poptionalBuilder;

    private final ProcessorProperties processorProperties;

    @Override
    public <T> void visitCtClass(CtClass<T> ctClass) {

        if (ctClass.hasAnnotation(NullObjectType.class)) {
            return;
        }

        OptionalType optionalType = ctClass.getAnnotation((OptionalType.class));
        if (optionalType == null) {
            return;
        }

        printProcessing(environment, ctClass);

        implementSomething(ctClass);

//        if (!isFinal(ctClass)) {
//            makeFinal(ctClass);
//        }


        if (NullnessValue.NULLABLE == getNullness(ctClass, processorProperties)) {
            visitCtMethods(ctClass.getMethods());
        } else {
            visitCtMethods(getNullableMethods(ctClass));
        }

        addGeneratedAnnotation(ctClass, poptionalBuilder.getFactory(), processorClass);
        removeAnnotation(ctClass, poptionalBuilder.getFactory(), Nullable.class);
        removeAnnotation(ctClass, poptionalBuilder.getFactory(), NonNull.class);
        removeAnnotation(ctClass, poptionalBuilder.getFactory(), Mode.class);
    }

    private void visitCtMethods(Set<CtMethod<?>> methods) {
        methods.forEach(this::visitCtMethod);
    }

    @Override
    public <T> void visitCtMethod(CtMethod<T> ctMethod) {

        printProcessing(environment, ctMethod);

        // todo: Think about accepting if it alternately implements Poptional.

        if (hasNonNullAnnotation(ctMethod)) {
            return;
        }

        if (ctMethod.getType().equals(poptionalBuilder.createCtTypeReference(void.class)) ||
                ctMethod.getType().equals(poptionalBuilder.createCtTypeReference(Void.class))) {
            return;
        }

        if (returnsNullObjectType(ctMethod)) {
            ctMethod.accept(new NullObjectVisitor(processorClass, environment, new NullObjectBuilder(poptionalBuilder.getFactory()), poptionalBuilder, processorProperties));
            return;
        }

        if (!returnsOptionalType(ctMethod)) {
            if (isStrictMode(ctMethod, processorProperties)) {
                return;
            }
        }

        changeMethodReturnTypeToPoptional(ctMethod);
        new PoptionalReturnReplacer(poptionalBuilder, processorProperties).replace(ctMethod);

        // Remove Nullable and use NonNull instead
        removeAnnotation(ctMethod, poptionalBuilder.getFactory(), Nullable.class);
        addNonNullAnnotation(ctMethod, poptionalBuilder.getFactory());
    }

    private <T> void makeFinal(CtClass<T> ctClass) {
        ctClass.addModifier(ModifierKind.FINAL);
    }

    private <T> boolean isFinal(CtClass<T> ctClass) {
        return ctClass.hasModifier(ModifierKind.FINAL);
    }

    /**
     * Changes the method return type to Poptional<original-return-type>
     *
     * @param ctMethod
     */
    private void changeMethodReturnTypeToPoptional(CtMethod ctMethod) {
        ctMethod.setType(poptionalOf(ctMethod));
    }

    private void implementSomething(CtClass<?> ctClass) {
        poptionalBuilder.implementSomething(ctClass);
    }



    private CtTypeReference<Poptional<?>> poptionalOf(CtMethod<?> ctMethod) {
        return poptionalBuilder.createPoptionalOf(getReturnTypeRef(ctMethod));
    }
}
