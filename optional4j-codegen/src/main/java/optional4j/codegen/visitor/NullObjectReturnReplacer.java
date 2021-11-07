package optional4j.codegen.visitor;

import static optional4j.annotation.Collaborator.NULL_INSTANCE_FACTORY_METHOD_NAME;
import static optional4j.annotation.Collaborator.NULL_OBJECT_FACTORY_METHOD_NAME;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import optional4j.codegen.CodeGenUtil;
import optional4j.codegen.builder.CollaboratorBuilder;
import optional4j.codegen.processor.ProcessorProperties;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

@RequiredArgsConstructor
@Deprecated
public class NullObjectReturnReplacer {

    private final CollaboratorBuilder collaboratorBuilder;

    private final ProcessorProperties processorProperties;

    public void replace(CtMethod<?> ctMethod, String nullObjectTypeName, CtType<?> oldReturnType) {
        ctMethod.getBody()
                .setStatements(
                        replace(
                                ctMethod.getBody().getStatements(),
                                ctMethod,
                                nullObjectTypeName,
                                oldReturnType,
                                0));
    }

    private List<CtStatement> replace(
            List<CtStatement> originalStatements,
            CtMethod<?> ctMethod,
            String nullObjectTypeName,
            CtType<?> oldReturnType,
            int depth) {

        List<CtStatement> statements = new ArrayList<>();
        for (int i = 0; i < originalStatements.size(); i++) {

            CtStatement originalStatement = originalStatements.get(i);
            CtStatement newStatement;
            if (originalStatement instanceof CtReturn<?>) {
                CtReturn<?> returnStatement = (CtReturn<?>) originalStatement;
                if ("null".equals(returnStatement.getReturnedExpression().toString())) {
                    newStatement =
                            collaboratorBuilder.createCodeSnippetStatement(
                                    nullInstanceStatement(nullObjectTypeName));
                } else {
                    String localVariableName = "$nullObject" + (i + (10 * depth));
                    statements.add(
                            createLocalVariable(returnStatement, localVariableName, oldReturnType));
                    if (CodeGenUtil.isOptimisticMode(ctMethod, processorProperties)) {
                        newStatement =
                                collaboratorBuilder.createCodeSnippetStatement(
                                        ifNullStatement(localVariableName, nullObjectTypeName));
                    } else {
                        newStatement =
                                collaboratorBuilder.createCodeSnippetStatement(
                                        ofNullObjectStatement(
                                                localVariableName, nullObjectTypeName));
                    }
                }
            } else if (originalStatement instanceof CtBlock) {
                CtBlock oldBlock = (CtBlock) originalStatement;
                CtBlock newBlock = collaboratorBuilder.createBlock();
                newBlock.setStatements(
                        replace(
                                oldBlock.getStatements(),
                                ctMethod,
                                nullObjectTypeName,
                                oldReturnType,
                                ++depth));
                newStatement = newBlock;
            } else if (originalStatement instanceof CtIf) {
                CtIf originalIf = (CtIf) originalStatement;
                CodeGenUtil.printProcessing(
                        collaboratorBuilder.getEnvironment(), originalIf.toString());
                CtStatement thenStatement = originalIf.getThenStatement();
                CodeGenUtil.printProcessing(
                        collaboratorBuilder.getEnvironment(), "Then: " + thenStatement.getClass());
                CtStatement replacedThen =
                        replace(
                                        List.of(thenStatement),
                                        ctMethod,
                                        nullObjectTypeName,
                                        oldReturnType,
                                        ++depth)
                                .get(0);
                CtIf replacedIf =
                        collaboratorBuilder
                                .createIf()
                                .setCondition(originalIf.getCondition())
                                .setThenStatement(replacedThen);
                CtStatement elseStatement = originalIf.getElseStatement();
                if (elseStatement != null) {
                    CtStatement replacedElse =
                            replace(
                                            List.of(elseStatement),
                                            ctMethod,
                                            nullObjectTypeName,
                                            oldReturnType,
                                            ++depth)
                                    .get(0);
                    CodeGenUtil.printProcessing(
                            collaboratorBuilder.getEnvironment(),
                            "Else: " + elseStatement.getClass());
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

    private CtLocalVariable<?> createLocalVariable(
            CtReturn originalReturnStatement, String localVariableName, CtType<?> oldReturnType) {

        CtLocalVariable localVariable = collaboratorBuilder.createLocalVariable();

        localVariable.setSimpleName(localVariableName);
        localVariable.setType(oldReturnType.getReference());
        localVariable.setAssignment(originalReturnStatement.getReturnedExpression());

        return localVariable;
    }

    private String ofNullObjectStatement(String localVariableName, String nullObjectTypeName) {
        return "return "
                + nullObjectTypeName
                + "."
                + NULL_OBJECT_FACTORY_METHOD_NAME
                + "("
                + localVariableName
                + ")";
    }

    private String ifNullStatement(String localVariableName, String nullObjectTypeName) {
        return "return "
                + localVariableName
                + " != null? "
                + localVariableName
                + ": "
                + nullObjectTypeName
                + "."
                + NULL_INSTANCE_FACTORY_METHOD_NAME
                + "()";
    }

    private String nullInstanceStatement(String nullObjectTypeName) {
        return "return " + nullObjectTypeName + "." + NULL_INSTANCE_FACTORY_METHOD_NAME + "()";
    }
}
