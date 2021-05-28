package info.fingo.xactus.processor.conformancesuite.parser.testcase;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class TestCaseRoot {
    private final Collection<TestGroup> testGroups;

    TestCaseRoot(Collection<TestGroup> testGroups) {
        Objects.requireNonNull(testGroups);

        this.testGroups = testGroups.stream().collect(
            collectingAndThen(toList(), Collections::unmodifiableList));
    }

    public Collection<TestGroup> getTestGroups() {
        return testGroups;
    }
}
