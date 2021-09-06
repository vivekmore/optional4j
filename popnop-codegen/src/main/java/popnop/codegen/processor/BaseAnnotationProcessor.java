package popnop.codegen.processor;

import lombok.Setter;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.processing.Property;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtElement;

import java.lang.annotation.Annotation;

import static popnop.codegen.CodeGenUtil.printProcessing;

@Setter
public abstract class BaseAnnotationProcessor<A extends Annotation, E extends CtElement> extends AbstractAnnotationProcessor<A, E> {

    @Property
    String nullness;

    @Property
    String mode;

    // postfix for the non-null type name e.g., Foo
    @Property
    String nopPostfix;

    // prefix for the non-null type name e.g., Foo
    @Property
    String nopPrefix;

    // postfix for the Nullxxx type name e.g., NullFoo
    @Property
    String nopNullPostfix;

    // prefix for the Nullxxx type name e.g., NullFoo
    @Property
    String nopNullPrefix;

    // postfix for the parent xxxNullObject type name e.g., FooNullObject
    @Property
    String nopNullObjectPostfix;

    // prefix for the parent xxxNullObject type name e.g., FooNullObject
    @Property
    String nopNullObjectPrefix;

    @Override
    public boolean shoudBeConsumed(CtAnnotation<? extends Annotation> annotation) {
        return false;
    }

    protected ProcessorProperties getProperties() {
        ProcessorProperties processorProperties = new ProcessorProperties(getNullness(), getMode());
        printProcessing(getEnvironment(), processorProperties.toString());
        return processorProperties;
    }

    String getNullness() {
        return nullness == null ? "nullable" : nullness;
    }

    String getMode() {
        return mode == null ? "strict" : mode;
    }

    String getNopPostfix() {
        return nopPostfix == null ? "" : nopPostfix;
    }

    String getNopPrefix() {
        return nopPrefix == null ? "Null" : nopPrefix;
    }

    String getNopNullPostfix() {
        return nopNullPostfix == null ? "" : nopNullPostfix;
    }

    String getNopNullPrefix() {
        return nopNullPrefix == null ? "" : nopNullPrefix;
    }

    String getNopNullObjectPostfix() {
        return nopNullObjectPostfix == null ? "NullObject" : nopNullObjectPostfix;
    }

    public String getNopNullObjectPrefix() {
        return nopNullObjectPrefix == null ? "" : nopNullObjectPrefix;
    }
}
