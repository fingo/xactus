package org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase;

import java.util.Objects;
import org.eclipse.wst.xml.xpath2.processor.testutil.Preconditions;

public class OutputFile {
    private final String file;
    private final ComparisonType comparisonType;

    OutputFile(String file,
               ComparisonType comparisonType) {
        Preconditions.requireNonEmptyString(file);
        Objects.requireNonNull(comparisonType);

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
