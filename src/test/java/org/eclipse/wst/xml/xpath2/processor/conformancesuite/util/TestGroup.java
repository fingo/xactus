package org.eclipse.wst.xml.xpath2.processor.conformancesuite.util;

import static org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.CollectionUtil.unmodifiableCopy;

import java.util.Collection;

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
