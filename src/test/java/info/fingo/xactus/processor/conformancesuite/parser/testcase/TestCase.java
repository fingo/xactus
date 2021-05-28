package info.fingo.xactus.processor.conformancesuite.parser.testcase;

import static info.fingo.xactus.processor.testutil.CollectionUtil.unmodifiableCopy;

import java.util.Collection;
import java.util.Objects;
import info.fingo.xactus.processor.testutil.Preconditions;

public class TestCase {
    private final String name;
    private final String description;
    private final String scenario;
    private final String xqFile;
    private final Collection<Input> inputs;
    /**
     * Quoting <a href="https://www.w3.org/XML/xquery/test-suite/Guidelines%20for%20Running%20the%20XML%20Query%20Test%20Suite.html">Guidelines for Running the XML Query Test Suite</a>:
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
             Collection<Input> inputs,
             Collection<OutputFile> outputFiles,
             Collection<String> expectedErrors) {
        Preconditions.requireNonEmptyString(name);
        Preconditions.requireNonEmptyString(description);
        Preconditions.requireNonEmptyString(scenario);
        Preconditions.requireNonEmptyString(xqFile);
        Objects.requireNonNull(inputs);
        Preconditions.requireAtLeastOneNonEmptyCollection(outputFiles, expectedErrors);

        this.name = name;
        this.description = description;
        this.scenario = scenario;
        this.xqFile = xqFile;
        this.inputs = unmodifiableCopy(inputs);
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

    public Collection<Input> getInputFiles() {
        return inputs;
    }

    public Collection<OutputFile> getOutputFiles() {
        return outputFiles;
    }

    public Collection<String> getExpectedErrors() {
        return expectedErrors;
    }
}
