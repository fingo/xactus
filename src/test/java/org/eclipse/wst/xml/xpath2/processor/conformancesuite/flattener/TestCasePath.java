package org.eclipse.wst.xml.xpath2.processor.conformancesuite.flattener;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase.TestGroup;
import org.eclipse.wst.xml.xpath2.processor.testutil.CollectionUtil;

public class TestCasePath {
    private static final TestCasePath EMPTY = new TestCasePath(emptyList());

    public static TestCasePath empty() {
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
