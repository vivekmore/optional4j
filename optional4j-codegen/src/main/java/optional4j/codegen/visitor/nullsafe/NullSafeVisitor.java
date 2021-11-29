package optional4j.codegen.visitor.nullsafe;

import static optional4j.codegen.CodegenUtil.printProcessing;
import static optional4j.codegen.CodegenUtil.removeAnnotation;

import java.util.Random;
import lombok.RequiredArgsConstructor;
import optional4j.annotation.NullSafe;
import optional4j.codegen.CodegenProperties;
import optional4j.codegen.builder.NullObjectBuilder;
import optional4j.codegen.builder.ValueTypeBuilder;
import spoon.compiler.Environment;
import spoon.processing.AnnotationProcessor;
import spoon.reflect.code.*;
import spoon.reflect.visitor.CtAbstractVisitor;

@RequiredArgsConstructor
public class NullSafeVisitor extends CtAbstractVisitor {

    public static final Random RANDOM = new Random(System.currentTimeMillis());

    private final Class<? extends AnnotationProcessor<?, ?>> processorClass;

    private final Environment environment;

    private final NullObjectBuilder nullObjectBuilder;

    private final ValueTypeBuilder valueTypeBuilder;

    private final CodegenProperties codegenProperties;

    @Override
    public <T> void visitCtLocalVariable(CtLocalVariable<T> localVariable) {

        printProcessing(environment, "Processing var: " + localVariable.getSimpleName());

        CtExpression<T> assignment = localVariable.getAssignment();

        if (assignment instanceof CtInvocation) {

            CtInvocation<T> invocation = (CtInvocation<T>) assignment;

            String previousVar = doVisitCtInvocation(localVariable, invocation);

            if (!"".equalsIgnoreCase(previousVar)) {
                CtLocalVariable var = createLocalVar(invocation);
                CtConditional<T> conditional = createNullConditional(invocation, previousVar);
                var.setSimpleName(localVariable.getSimpleName());
                var.setAssignment(conditional);
                localVariable.replace(var);
            } else {
                removeAnnotation(localVariable, valueTypeBuilder.getFactory(), NullSafe.class);
            }
        }
    }

    public <T> String doVisitCtInvocation(
            CtLocalVariable<T> localVariable, CtInvocation<T> invocation) {

        printProcessing(environment, "Processing var invocation: " + invocation.toString());

        CtExpression<?> target = invocation.getTarget();

        //        if (target instanceof CtTypeAccess || target instanceof CtThisAccess) {
        //            return "";
        //        }

        if (target instanceof CtInvocation) {

            CtInvocation<T> targetInvocation = (CtInvocation<T>) target;
            String previousVar = doVisitCtInvocation(localVariable, targetInvocation);

            CtLocalVariable var = createLocalVar(target);

            if (!"".equalsIgnoreCase(previousVar)) {
                CtConditional<T> conditional = createNullConditional(targetInvocation, previousVar);
                var.setAssignment(conditional);
            }

            localVariable.insertBefore(var);
            return var.getSimpleName();
        } else if (target instanceof CtThisAccess) {
            return "";
        } else if (target instanceof CtTypeAccess) {
            return "";
        } else if (target != null) {

            // new Customer().getAddressPlain()
            // target // new Customer()
            CtLocalVariable<?> var = createLocalVar(target);
            localVariable.insertBefore(var);
            return var.getSimpleName();
        } else {
            printProcessing(environment, "target is null for: " + invocation);
            return "";
        }
    }

    private <T> CtConditional<T> createNullConditional(
            CtInvocation<T> targetInvocation, String previousVar) {
        CtConditional<T> conditional = valueTypeBuilder.createConditional();
        conditional.setCondition(
                valueTypeBuilder.createCodeSnippetExpression(previousVar + " == null"));
        conditional.setThenExpression(valueTypeBuilder.createCodeSnippetExpression("null"));

        CtInvocation<T> targetInvocationClone = targetInvocation.clone();
        targetInvocationClone.setTarget(valueTypeBuilder.createCodeSnippetExpression(previousVar));
        conditional.setElseExpression(targetInvocationClone);
        return conditional;
    }

    private <T, V> CtLocalVariable<V> createLocalVar(CtExpression<V> target) {

        CtLocalVariable<V> localVariable = valueTypeBuilder.createLocalVariable();
        String varName =
                target.toString()
                        .substring(target.toString().lastIndexOf(".") + 1)
                        .replace(" ", "")
                        .replace("(", "")
                        .replace(")", "")
                        .concat("$var")
                        .concat(String.valueOf(RANDOM.nextInt(1000)));

        localVariable.setSimpleName(varName);
        localVariable.setType(target.getType());
        localVariable.setAssignment(target);
        return localVariable;
    }
}
