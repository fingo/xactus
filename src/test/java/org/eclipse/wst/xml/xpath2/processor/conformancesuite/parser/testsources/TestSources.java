package org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testsources;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

public class TestSources {
    private final Map<String, String> testSourcesMapping;

    TestSources(Collection<TestSource> testSources) {
        Objects.requireNonNull(testSources);

        this.testSourcesMapping = testSources.stream()
            .collect(collectingAndThen(
                toMap(TestSource::getId, TestSource::getFileName),
                Collections::unmodifiableMap));
    }

    public String getSourceFileName(String sourceFileId) {
        Objects.requireNonNull(sourceFileId);

        if (testSourcesMapping.containsKey(sourceFileId)) {
            return "/" + testSourcesMapping.get(sourceFileId);
        }

        throw new NoSuchElementException(
            "No source file with id \"" + sourceFileId + "\"");
    }
}
