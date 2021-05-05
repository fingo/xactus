package org.eclipse.wst.xml.xpath2.processor.conformancesuite.assertion;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.anyOf;

import java.util.Collection;
import java.util.Objects;
import org.assertj.core.api.Condition;
import org.assertj.core.api.HamcrestCondition;
import org.eclipse.wst.xml.xpath2.api.ResultSequence;
import org.eclipse.wst.xml.xpath2.processor.testutil.legacytestsuiteadapter.PsychopathTestContext;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase.OutputFile;

public final class ConformanceSuiteMatchers {
    private ConformanceSuiteMatchers() {
    }

    public static Condition<? super ResultSequence> matchesExpected(OutputFile expected,
                                                                    ContentProvider contentProvider,
                                                                    PsychopathTestContext context) {
        Objects.requireNonNull(expected);
        Objects.requireNonNull(contentProvider);
        Objects.requireNonNull(context);

        return HamcrestCondition.matching(
            new OutputFileMatcher(expected, contentProvider, context));
    }

    public static Condition<? super ResultSequence> matchesAnyOfExpected(Collection<OutputFile> expected,
                                                                         ContentProvider contentProvider,
                                                                         PsychopathTestContext context) {
        Objects.requireNonNull(expected);
        Objects.requireNonNull(contentProvider);
        Objects.requireNonNull(context);

        return anyOf(expected.stream()
            .map(o -> matchesExpected(o, contentProvider, context))
            .collect(toList()));
    }
}
