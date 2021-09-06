package popnop.codegen.processor;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class ProcessorProperties {

    @NonNull
    private final String nullness; // nullable vs nonnull

    @NonNull
    private final String mode; // strict vs full
}
