package org.eclipse.wst.xml.xpath2.processor.conformancesuite;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.flattener.TestCasePath;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.flattener.TestCaseWithPath;

final class TestCaseFilter {
    private TestCaseFilter() {
    }

    private static final Set<TestCasePath> EXCLUDED_TEST_CASES =
        getExcludedTestCases();

    private static Set<TestCasePath> getExcludedTestCases() {
        try (InputStream inputStream = TestCaseFilter.class.getResourceAsStream("excluded_test_cases.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             Stream<String> lines = reader.lines()) {
            return lines
                .map(TestCasePath::parse)
                .collect(collectingAndThen(
                    toSet(), Collections::unmodifiableSet));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static boolean isTestcaseEnabled(TestCaseWithPath testCaseWithPath) {
        return !EXCLUDED_TEST_CASES.contains(testCaseWithPath.getTestCasePath());
    }
}
