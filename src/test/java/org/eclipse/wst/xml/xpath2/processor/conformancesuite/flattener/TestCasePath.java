package org.eclipse.wst.xml.xpath2.processor.conformancesuite.flattener;

import static java.util.Collections.emptyList;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.wst.xml.xpath2.processor.testutil.CollectionUtil;

public final class TestCasePath {
    private static final String DELIMITER = "/";
    private static final TestCasePath EMPTY = new TestCasePath(emptyList());

    public static TestCasePath parse(String testCasePathString) {
        return fromArray(testCasePathString.split(Pattern.quote(DELIMITER)));
    }

    private static TestCasePath fromArray(String... segments) {
        return new TestCasePath(Arrays.stream(segments).collect(Collectors.toList()));
    }

    public static TestCasePath empty() {
        return EMPTY;
    }

    private final List<String> path;

    private TestCasePath(List<String> path) {
        Objects.requireNonNull(path);
        this.path = CollectionUtil.unmodifiableCopy(path);
    }

    public TestCasePath append(String segment) {
        return new TestCasePath(
            Stream.concat(path.stream(), Stream.of(segment))
                .collect(Collectors.toList()));
    }

    public List<String> getPath() {
        return path;
    }

    public String pathString() {
        return String.join(DELIMITER, path);
    }

    @Override
    public String toString() {
        return "TestCasePath{" +
            "path=" + path +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestCasePath that = (TestCasePath) o;
        return path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
