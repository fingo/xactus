package org.eclipse.wst.xml.xpath2.processor.conformancesuite.util;

import static org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.CollectionUtil.unmodifiableCopy;

import java.util.Collection;

public class TestCase {
    private final String name;
    private final String description;
    private final String scenario;

    private final String filePath;
    private final String xqFile;

    private final Collection<String> inputFiles;
    private final Collection<String> outputFiles;
    private final Collection<String> expectedErrors;

    TestCase(String name,
             String description,
             String scenario,
             String filePath,
             String xqFile,
             Collection<String> inputFiles,
             Collection<String> outputFiles,
             Collection<String> expectedErrors) {
        this.name = name;
        this.description = description;
        this.scenario = scenario;
        this.filePath = filePath;
        this.xqFile = xqFile;
        this.inputFiles = unmodifiableCopy(inputFiles);
        this.outputFiles = unmodifiableCopy(outputFiles);
        this.expectedErrors = unmodifiableCopy(expectedErrors);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getScenario() {
        return scenario;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getXqFile() {
        return xqFile;
    }

    public Collection<String> getInputFiles() {
        return inputFiles;
    }

    public Collection<String> getOutputFiles() {
        return outputFiles;
    }

    public Collection<String> getExpectedErrors() {
        return expectedErrors;
    }
}
