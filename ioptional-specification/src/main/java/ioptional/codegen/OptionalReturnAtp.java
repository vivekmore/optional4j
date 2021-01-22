package ioptional.codegen;

import ioptional.IOptional;
import ioptional.OptionalReturn;
import ioptional.OptionalType;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class OptionalReturnAtp extends AbstractAnnotationProcessor<OptionalReturn, CtElement> {

	@Override
	public void process(OptionalReturn optionalReturn, CtElement ctElement) {

		if (ctElement instanceof CtMethod) {
			doProcess((CtMethod) ctElement);
		}

		if (ctElement instanceof CtClass) {
			CtClass ctClass = (CtClass) ctElement;
			ctClass.getMethods()
					.forEach(ctMethod -> doProcess((CtMethod) ctMethod));
		}

		clearConsumedAnnotationTypes();
	}

	void doProcess(CtMethod ctMethod) {

		CtTypeReference<?> returnType = ctMethod.getType();

		CtType<?> returnTypeDeclaration = returnType.getTypeDeclaration();

		// todo: accept if it alternately implements IOptional .

		if (!returnTypeDeclaration.hasAnnotation(OptionalType.class)) {
			return;
		}

		CtTypeReference<IOptional<?>> iOptional = getFactory().createCtTypeReference(IOptional.class);
		iOptional.addActualTypeArgument(returnType);
		ctMethod.setType(iOptional);

		CtBlock<?> methodBody = ctMethod.getBody();

		List<CtStatement> statements = methodBody.getStatements();

		List<CtStatement> updatedStatements = new ArrayList<>();

		for (CtStatement statement : statements) {
			if (statement instanceof CtReturn) {
				CtReturn returnStatement = (CtReturn) statement;
				CtExpression returnExpression = returnStatement.getReturnedExpression();
				CtCodeSnippetStatement codeSnippetStatement = getFactory().createCodeSnippetStatement(
						"return IOptional.ofNullable(" + returnExpression + ")");
				updatedStatements.add(codeSnippetStatement);
			} else {
				updatedStatements.add(statement);
			}
		}

		methodBody.setStatements(updatedStatements);
	}
}

