package org.eclipse.wst.xml.xpath2.processor.conformancesuite.util;

public class InputFile {
    private final String file;
    private final String variable;

    InputFile(String file,
              String variable) {
        this.file = file;
        this.variable = variable;
    }

    public String getFile() {
        return file;
    }

    public String getVariable() {
        return variable;
    }
}
