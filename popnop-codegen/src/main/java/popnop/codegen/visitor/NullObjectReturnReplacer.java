package popnop.codegen.visitor;

import lombok.RequiredArgsConstructor;
import popnop.codegen.builder.NullObjectBuilder;
import popnop.codegen.processor.ProcessorProperties;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtMethod;

import java.util.ArrayList;
import java.util.List;

import static popnop.codegen.CodeGenUtil.*;

@RequiredArgsConstructor
public class NullObjectReturnReplacer {

    private final NullObjectBuilder nullObjectBuilder;

    private final ProcessorProperties processorProperties;

    public void replace(CtMethod<?> ctMethod, String nullObjectTypeName) {
        ctMethod.getBody().setStatements(replace(ctMethod.getBody().getStatements(), ctMethod, nullObjectTypeName, 0));
    }

    private List<CtStatement> replace(List<CtStatement> originalStatements, CtMethod<?> ctMethod, String nullObjectTypeName, int depth) {

        List<CtStatement> statements = new ArrayList<>();
        for (int i = 0; i < originalStatements.size(); i++) {

            CtStatement originalStatement = originalStatements.get(i);
            CtStatement newStatement;
            if (originalStatement instanceof CtReturn<?>) {
                CtReturn<?> returnStatement = (CtReturn<?>) originalStatement;
                String localVariableName = "$nullObject" + (i + (10 * depth));
                statements.add(createLocalVariable(returnStatement, localVariableName));
                if (isStrictMode(ctMethod, processorProperties)) {
                    newStatement = nullObjectBuilder.createCodeSnippetStatement(ifNullStatement(localVariableName, nullObjectTypeName));
                } else {
                    newStatement = nullObjectBuilder.createCodeSnippetStatement(ofNullObjectStatement(localVariableName, nullObjectTypeName));
                }
            } else if (originalStatement instanceof CtBlock) {
                CtBlock oldBlock = (CtBlock) originalStatement;
                CtBlock newBlock = nullObjectBuilder.createBlock();
                newBlock.setStatements(replace(oldBlock.getStatements(), ctMethod, nullObjectTypeName, ++depth));
                newStatement = newBlock;
            } else if (originalStatement instanceof CtIf) {
                CtIf originalIf = (CtIf) originalStatement;
                printProcessing(nullObjectBuilder.getEnvironment(), originalIf.toString());
                CtStatement thenStatement = originalIf.getThenStatement();
                printProcessing(nullObjectBuilder.getEnvironment(), "Then: " + thenStatement.getClass());
                CtStatement replacedThen = replace(List.of(thenStatement), ctMethod, nullObjectTypeName, ++depth).get(0);
                CtIf replacedIf = nullObjectBuilder.createIf().setCondition(originalIf.getCondition()).setThenStatement(replacedThen);
                CtStatement elseStatement = originalIf.getElseStatement();
                if (elseStatement != null) {
                    CtStatement replacedElse = replace(List.of(elseStatement), ctMethod, nullObjectTypeName, ++depth).get(0);
                    printProcessing(nullObjectBuilder.getEnvironment(), "Else: " + elseStatement.getClass());
                    replacedIf.setElseStatement(replacedElse);
                }
                newStatement = replacedIf;
            } else {
                newStatement = originalStatement;
            }
            statements.add(newStatement);
        }
        return statements;
    }

    private CtLocalVariable<?> createLocalVariable(CtReturn originalReturnStatement, String localVariableName) {

        CtLocalVariable<?> localVariable = nullObjectBuilder.createLocalVariable();

        localVariable.setSimpleName(localVariableName);
        localVariable.setType(originalReturnStatement.getReturnedExpression().getType());
        localVariable.setAssignment(originalReturnStatement.getReturnedExpression());

        return localVariable;
    }

    private String ofNullObjectStatement(String localVariableName, String nullObjectTypeName) {
        return "return " + nullObjectTypeName + ".ofNullObject(" + localVariableName + ")";
    }

    private String ifNullStatement(String localVariableName, String nullObjectTypeName) {
        return "return " + localVariableName + " != null? " + localVariableName + ": " + nullObjectTypeName + "." + NULL_INSTANCE + "()";
    }

}
