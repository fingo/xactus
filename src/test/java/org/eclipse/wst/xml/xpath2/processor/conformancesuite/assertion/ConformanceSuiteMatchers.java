package org.eclipse.wst.xml.xpath2.processor.conformancesuite.assertion;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.anyOf;

import java.util.Collection;
import org.assertj.core.api.Condition;
import org.assertj.core.api.HamcrestCondition;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase.OutputFile;

public final class ConformanceSuiteMatchers {
    private ConformanceSuiteMatchers() {
    }

    public static Condition<String> matchesExpected(OutputFile expected,
                                                    ContentProvider contentProvider) {
        return HamcrestCondition.matching(
            new OutputFileMatcher(expected, contentProvider));
    }

    public static Condition<String> matchesAnyOfExpected(Collection<OutputFile> expected,
                                                         ContentProvider contentProvider) {
        return anyOf(expected.stream()
            .map(o -> matchesExpected(o, contentProvider))
            .collect(toList()));
    }
}
