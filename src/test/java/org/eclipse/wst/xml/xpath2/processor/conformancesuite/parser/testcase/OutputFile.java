package org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase;

public class OutputFile {
    private final String file;
    private final ComparisonType comparisonType;

    OutputFile(String file,
               ComparisonType comparisonType) {
        this.file = file;
        this.comparisonType = comparisonType;
    }

    public String getFile() {
        return file;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    @Override
    public String toString() {
        return "OutputFile{" +
            "file='" + file + '\'' +
            ", comparisonType=" + comparisonType +
            '}';
    }
}