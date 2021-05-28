package info.fingo.xactus.processor.conformancesuite.parser.testcase;

import static info.fingo.xactus.processor.testutil.CollectionUtil.unmodifiableCopy;

import java.util.Collection;
import java.util.Objects;
import info.fingo.xactus.processor.testutil.Preconditions;

public class TestGroup {
    private final String name;
    private final String title;
    private final String description;
    private final Collection<TestGroup> testGroups;
    private final Collection<TestCase> testCases;
    private final boolean isEmpty;

    TestGroup(String name,
              String title,
              String description,
              Collection<TestGroup> testGroups,
              Collection<TestCase> testCases) {
        Preconditions.requireNonEmptyString(name);
        Preconditions.requireNonEmptyString(title);
        Objects.requireNonNull(description);
        Objects.requireNonNull(testGroups);
        Objects.requireNonNull(testCases);

        this.name = name;
        this.title = title;
        this.description = description;
        this.testGroups = unmodifiableCopy(testGroups);
        this.testCases = unmodifiableCopy(testCases);
        this.isEmpty = testGroups.stream().allMatch(TestGroup::isEmpty) &&
            testCases.isEmpty();
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Collection<TestGroup> getTestGroups() {
        return testGroups;
    }

    public Collection<TestCase> getTestCases() {
        return testCases;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
