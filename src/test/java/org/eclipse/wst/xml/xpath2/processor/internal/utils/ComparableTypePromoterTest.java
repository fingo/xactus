package org.eclipse.wst.xml.xpath2.processor.internal.utils;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.eclipse.wst.xml.xpath2.api.ResultBuffer;
import org.eclipse.wst.xml.xpath2.api.ResultSequence;
import org.eclipse.wst.xml.xpath2.processor.internal.types.XSDate;
import org.eclipse.wst.xml.xpath2.processor.internal.types.XSDateTime;
import org.eclipse.wst.xml.xpath2.processor.internal.types.XSDecimal;
import org.eclipse.wst.xml.xpath2.processor.internal.types.XSInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ComparableTypePromoterTest {

    @SuppressWarnings("unused")
    static Stream<Arguments> params() {
        return Stream.of(
                arguments(dateSeq("2010-01-01"), XSDate.class, "xs:date(2010-01-01) gives: xs:date"),
                arguments(dateSeq("2010-01-01", "2010-01-02"), XSDate.class, "xs:date,xs:date gives: xs:date"),
                arguments(dateTimeSeq("2010-01-01T12:00:00", "2010-01-02T12:00:00"), XSDateTime.class,
                "xs:dateTime, xs:dateTime gives: xs:dateTime"),
                arguments(concat(dateTimeSeq("2010-01-01T12:00:00"), dateSeq("2010-01-01")), XSDateTime.class,
                "xs:dateTime, xs:date gives: xs:dateTime"),
                arguments(concat(dateSeq("2010-01-01"), dateTimeSeq("2010-01-02T12:00:00")), XSDateTime.class,
                "xs:date, xs:dateTime gives: xs:dateTime"),
                arguments(concat(dateSeq("2010-01-01"), intSeq("1")), XSDecimal.class, "xs:date, xs:integer gives: xs:decimal")
        );
    }

    private static ResultSequence concat(ResultSequence... seq) {
        ResultBuffer b = new ResultBuffer();
        for (ResultSequence rs : seq) {
            b.concat(rs);
        }
        return b.getSequence();
    }

    private static ResultSequence dateSeq(String... dates) {
        ResultBuffer buffer = new ResultBuffer();

        for (String string : dates) {
            buffer.append(XSDate.parse_date(string));
        }
        return buffer.getSequence();
    }

    private static ResultSequence dateTimeSeq(String... dates) {
        ResultBuffer buffer = new ResultBuffer();

        for (String string : dates) {
            buffer.append(XSDateTime.parseDateTime(string));
        }
        return buffer.getSequence();
    }

    private static ResultSequence intSeq(String... values) {
        ResultBuffer buffer = new ResultBuffer();

        for (String string : values) {
            buffer.append(new XSInteger(string));
        }
        return buffer.getSequence();

    }

    @SuppressWarnings("unused")
    @ParameterizedTest(name = "{2}")
    @MethodSource("params")
    void test(final ResultSequence sequenceToConsider, final Class targetType, final String desc) {
        ComparableTypePromoter promoter = new ComparableTypePromoter();
        promoter.considerSequence(sequenceToConsider);
        Class newTargetType = promoter.getTargetType();

        Assertions.assertEquals(newTargetType, targetType);
    }
}
