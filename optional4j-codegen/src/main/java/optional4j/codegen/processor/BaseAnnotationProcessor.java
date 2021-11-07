package optional4j.codegen.processor;

import static optional4j.codegen.CodeGenUtil.printProcessing;
import static optional4j.support.ModeValue.OPTIMISTIC;
import static optional4j.support.NullityValue.NON_NULL;

import java.lang.annotation.Annotation;
import lombok.Setter;
import optional4j.support.ModeValue;
import optional4j.support.NullityValue;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.processing.Property;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtElement;

@Setter
public abstract class BaseAnnotationProcessor<A extends Annotation, E extends CtElement>
        extends AbstractAnnotationProcessor<A, E> {

    private static final boolean DEFAULT_NULLITY_ENABLED = true;
    private static final NullityValue DEFAULT_NULLITY = NON_NULL;
    private static final ModeValue DEFAULT_MODE = OPTIMISTIC;

    @Property Boolean nullityEnabled;

    @Property String nullity;

    @Property String mode;

    @Property Boolean enhancedSyntax;

    ProcessorProperties processorProperties;

    @Override
    public boolean shoudBeConsumed(CtAnnotation<? extends Annotation> annotation) {
        return false;
    }

    protected ProcessorProperties getProperties() {
        if (processorProperties == null) {
            initProperties();
        }
        return processorProperties;
    }

    private void initProperties() {
        setProcessorProperties(
                new ProcessorProperties(
                        getNullity(), getMode(), isNullityEnabled(), isEnhancedSyntax()));
        printProcessing(getEnvironment(), processorProperties.toString());
    }

    private NullityValue getNullity() {
        return nullity == null ? DEFAULT_NULLITY : NullityValue.valueOf(nullity.toUpperCase());
    }

    private ModeValue getMode() {
        return mode == null ? DEFAULT_MODE : ModeValue.valueOf(mode.toUpperCase());
    }

    private boolean isNullityEnabled() {
        if (nullityEnabled == null) {
            return DEFAULT_NULLITY_ENABLED;
        }
        return nullityEnabled;
    }

    private boolean isEnhancedSyntax() {
        if (enhancedSyntax == null) {
            return false;
        }
        return enhancedSyntax;
    }
}
