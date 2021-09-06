package popnop.codegen.visitor;

import lombok.RequiredArgsConstructor;
import popnop.codegen.builder.PoptionalBuilder;
import popnop.codegen.processor.ProcessorProperties;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;

import java.util.List;
import java.util.stream.Collectors;

import static popnop.codegen.CodeGenUtil.*;

@RequiredArgsConstructor
public class PoptionalReturnReplacer {

    private final PoptionalBuilder poptionalBuilder;

    private final ProcessorProperties processorProperties;

    public void replace(CtMethod<?> ctMethod) {
        List<CtStatement> originalStatements = getMethodStatements(ctMethod);
        List<CtStatement> updatedStatements = replace(originalStatements, ctMethod);
        setMethodStatements(ctMethod, updatedStatements);
    }

    private List<CtStatement> replace(List<CtStatement> originalStatements, CtMethod<?> ctMethod) {
        return originalStatements.stream()
                .map(originalStatement -> replace(originalStatement, ctMethod))
                .collect(Collectors.toList());
    }

    private CtStatement replace(CtStatement originalStatement, CtMethod<?> ctMethod) {
        if (!(originalStatement instanceof CtReturn<?>)) {
            return originalStatement;
        }
        return createPoptionalOfNullableStatement((CtReturn<?>) originalStatement, ctMethod);
    }

    private CtCodeSnippetStatement createPoptionalOfNullableStatement(CtReturn<?> returnStatement, CtMethod<?> ctMethod) {
        return poptionalBuilder.createCodeSnippetStatement(getReturnStatement(returnStatement, ctMethod));
    }

    private String getReturnStatement(CtReturn<?> returnStatement, CtMethod<?> ctMethod) {

        if (isStrictMode(ctMethod, processorProperties)) {
            return ifNullStatement(returnStatement);
        }

        return poptionalOfNullableStatement(returnStatement);
    }

    private String poptionalOfNullableStatement(CtReturn<?> returnStatement) {
        return "return Poptional.ofNullable(" + returnStatement.getReturnedExpression() + ")";
    }

    private String ifNullStatement(CtReturn<?> returnStatement) {
        return "return (" + returnStatement.getReturnedExpression() + ") == null? Poptional.empty(): " + returnStatement.getReturnedExpression();
    }
}
