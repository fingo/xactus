package org.eclipse.wst.xml.xpath2.processor.conformancesuite.util;

public class TestSource {
    private final String id;
    private final String fileName;

    TestSource(String id,
               String fileName) {
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
