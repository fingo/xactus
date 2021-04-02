package org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase;

public class Input {
    private final String name;
    private final String variable;
    private final InputType inputType;

    Input(String name,
          String variable,
          InputType inputType) {
        this.name = name;
        this.variable = variable;
        this.inputType = inputType;
    }

    public String getName() {
        return name;
    }

    public String getVariable() {
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
