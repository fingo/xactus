package info.fingo.xactus.processor.conformancesuite.parser.testcase;

import java.util.Objects;
import info.fingo.xactus.processor.testutil.Preconditions;

public class Input {
    private final String name;
    private final String variable;
    private final InputType inputType;

    Input(String name,
          String variable,
          InputType inputType) {
        Objects.requireNonNull(inputType);
        Preconditions.requireNonEmptyString(name);
        if (inputType != InputType.CONTEXT_ITEM) {
            Preconditions.requireNonEmptyString(variable);
        } else {
            Preconditions.requireEmptyString(variable);
        }

        this.name = name;
        this.variable = variable;
        this.inputType = inputType;
    }

    public String getName() {
        return name;
    }

    public String getVariable() {
        if (inputType == InputType.CONTEXT_ITEM) {
            throw new IllegalStateException(
                "Variable is not available for contextItems");
        }

        return variable;
    }

    public InputType getInputFileType() {
        return inputType;
    }

    @Override
    public String toString() {
        return "InputFile{" +
            "file='" + name + '\'' +
            ", variable='" + variable + '\'' +
            ", inputFileType=" + inputType +
            '}';
    }
}
