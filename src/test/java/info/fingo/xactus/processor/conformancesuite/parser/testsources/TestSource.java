package info.fingo.xactus.processor.conformancesuite.parser.testsources;

import info.fingo.xactus.processor.testutil.Preconditions;

public class TestSource {
    private final String id;
    private final String fileName;

    TestSource(String id,
               String fileName) {
        Preconditions.requireNonEmptyString(id);
        Preconditions.requireNonEmptyString(fileName);

        this.id = id;
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }
}
