package poptional.codegen;

import poptional.OptionalObject;
import poptional.Poptional;
import poptional.Something;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;

import java.util.ArrayList;
import java.util.List;

public class OptionalObjectProcessor extends AbstractAnnotationProcessor<OptionalObject, CtElement> {

	@Override
	public void process(OptionalObject optionalReturn, CtElement ctElement) {

		if (ctElement instanceof CtMethod) {
			doProcess((CtMethod) ctElement);
		}

		if (ctElement instanceof CtClass) {

			CtClass ctClass = (CtClass) ctElement;
			CtTypeReference<Something<?>> something = getFactory().createCtTypeReference(Something.class);
			something.addActualTypeArgument(ctClass.getReference());
			ctClass.addSuperInterface(something);

			// todo: exclude methods which are either: 1- excluded or 2- specify a @NotNull annotation
			ctClass.getMethods()
					.forEach(ctMethod -> doProcess((CtMethod) ctMethod));
		}

		clearConsumedAnnotationTypes();
	}

	void doProcess(CtMethod ctMethod) {

		CtTypeReference<?> returnType = ctMethod.getType();

		CtType<?> returnTypeDeclaration = returnType.getTypeDeclaration();

		// todo: accept if it alternately implements Poptional.

		if (!returnTypeDeclaration.hasAnnotation(OptionalObject.class)) {
			return;
		}

		CtTypeReference<Poptional<?>> iOptional = getFactory().createCtTypeReference(Poptional.class);
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
						"return Poptional.ofNullable(" + returnExpression + ")");
				updatedStatements.add(codeSnippetStatement);
			} else {
				updatedStatements.add(statement);
			}
		}

		methodBody.setStatements(updatedStatements);
	}
}

