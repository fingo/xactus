package org.eclipse.wst.xml.xpath2.processor.conformancesuite.util;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestCasePath {
    private static final TestCasePath EMPTY = new TestCasePath(emptyList());

    static TestCasePath empty() {
        return EMPTY;
    }

    private final List<TestGroup> path;

    TestCasePath(List<TestGroup> path) {
        this.path = CollectionUtil.unmodifiableCopy(path);
    }

    public TestCasePath add(TestGroup testGroup) {
        return new TestCasePath(
            Stream.concat(path.stream(), Stream.of(testGroup))
                .collect(Collectors.toList()));
    }

    public List<TestGroup> getPath() {
        return path;
    }

    public String pathString() {
        return path.stream()
            .map(TestGroup::getName)
            .collect(joining("/"));
    }
}
