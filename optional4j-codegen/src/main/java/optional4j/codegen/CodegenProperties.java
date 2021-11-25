package optional4j.codegen;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import optional4j.support.ModeValue;
import optional4j.support.NullityValue;

@RequiredArgsConstructor
@Getter
@ToString
public class CodegenProperties {

    @NonNull private final NullityValue nullity;

    @NonNull private final ModeValue mode;

    private final boolean nullityEnabled;

    private final boolean enhancedSyntax;
}
