package org.eclipse.wst.xml.xpath2.processor.conformancesuite.util;

import static org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.CollectionUtil.unmodifiableCopy;

import java.util.Collection;

public class TestCase {
    private final String name;
    private final String description;
    private final String scenario;

    private final String xqFile;

    private final Collection<InputFile> inputFiles;
    /**
     * After <a href="https://www.w3.org/XML/xquery/test-suite/Guidelines%20for%20Running%20the%20XML%20Query%20Test%20Suite.html">Guidelines for Running the XML Query Test Suite</a>:
     * It is possible that a test case provides multiple expected results.
     * In this case, successfully comparing the actual result to one of the
     * provided expected results is a "pass".
     */
    private final Collection<OutputFile> outputFiles;
    private final Collection<String> expectedErrors;

    TestCase(String name,
             String description,
             String scenario,
             String xqFile,
             Collection<InputFile> inputFiles,
             Collection<OutputFile> outputFiles,
             Collection<String> expectedErrors) {
        this.name = name;
        this.description = description;
        this.scenario = scenario;
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

    public String getXqFile() {
        return xqFile;
    }

    public Collection<InputFile> getInputFiles() {
        return inputFiles;
    }

    public Collection<OutputFile> getOutputFiles() {
        return outputFiles;
    }

    public Collection<String> getExpectedErrors() {
        return expectedErrors;
    }
}
